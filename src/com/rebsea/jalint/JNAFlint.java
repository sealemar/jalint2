//
// TODO: Banner
//
// in the name of goodnes this code is created by
// rebcabin and sealemar
//

//
// TODO: rewrite with preprocessor so that ABI is always of a target platform
//       Add a recipe to Makefile to configure the interface first, so that all
//       Java source files which provide an interface to a native library are first
//       preprocessed.
//
// TODO: investigate:
//       from fmpz/fmpz.c
//       used in many places (may be all) where fmpz_t is required
//       is that a @c memoryLeak?
//
// __mpz_struct * _fmpz_promote(fmpz_t f)
// {
//     if (!COEFF_IS_MPZ(*f)) /* f is small so promote it first */
//     {
//         __mpz_struct * mpz_ptr = _fmpz_new_mpz();
//         (*f) = PTR_TO_COEFF(mpz_ptr);
//         return mpz_ptr;
//     }
//     else /* f is large already, just return the pointer */
//         return COEFF_TO_PTR(*f);
// }
//

package com.rebsea.jalint;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import com.rebsea.jalint.JNAGmp;

//
// All native converstions are done based on
// http://jna.java.net/javadoc/overview-summary.html#marshalling
//
// class Memory
//     A Pointer to memory obtained from the native heap via a call to malloc.
//     The finalize() method will free allocated memory when this object is no longer referenced.
//
public class JNAFlint {
    static {
        Native.register("flint");
    }

    //
    // @par private helpers to static __inline__ functions of the library
    //

    static final long FLINT_BITS = NativeLong.SIZE * 8;

    // maximum positive value a small coefficient can have
    // #define COEFF_MAX ((1L << (FLINT_BITS - 2)) - 1L)
    static final long COEFF_MAX = ((1L << (FLINT_BITS - 2)) - 1L);

    // minimum negative value a small coefficient can have
    // #define COEFF_MIN (-((1L << (FLINT_BITS - 2)) - 1L))
    static final long COEFF_MIN = (-((1L << (FLINT_BITS - 2)) - 1L));

    /**
     * @fn #define COEFF_IS_MPZ(x) (((x) >> (FLINT_BITS - 2)) == 1L)  /* is x a pointer not an integer
     */
    private static boolean COEFF_IS_MPZ(Pointer x) {
        final NativeLong nl = x.getNativeLong(0);

        return (((FLINT_BITS == 64 ?
                  nl.longValue() :
                  nl.intValue()) >> (NativeLong.SIZE - 2)) == 1);
    }

    /**
     * @fn static __inline__ void _fmpz_demote(fmpz_t f)
     *
     * @code from <flint/fmpz.h>
     *    static __inline__
     *    void _fmpz_demote(fmpz_t f)
     *    {
     *        if (COEFF_IS_MPZ(*f))
     *        {
     *            _fmpz_clear_mpz(*f);
     *            (*f) = 0L;
     *        }
     *    }
     * @endcode
     */
    private static void _fmpz_demote(Pointer f) {
        if(COEFF_IS_MPZ(f)) {
            _fmpz_clear_mpz(f.getNativeLong(0));
            f.setNativeLong(0, new NativeLong(0));
        }
    }

    //
    // @par Native interface to the library
    //

    // typedef fmpz fmpz_t[1];
    // ...
    // fmpz = long int (64 bit on Mac OS of 2013 on MacBook Air)

    /**
     * @fn char * fmpz_get_str(char * str, int b, const fmpz_t f)
     *
     * @returns the representation of f in base b, which can vary between 2 and 62, inclusive.
     *
     * @note
     * If str is NULL, the result string is allocated by the function. Otherwise, it is up to the
     * caller to ensure that the allocated block of memory is sufficiently large.
     *
     * @warning
     * the returned Pointer has to be manually freed later
     */
    private static native Pointer fmpz_get_str(Pointer str, int b, final Pointer f);

    /**
     * @fn void _fmpz_clear_mpz(fmpz f);
     */
    private static native void _fmpz_clear_mpz(NativeLong f);

    /**
     * @fn void * flint_malloc(size_t size);
     */
    private static native Pointer flint_malloc(NativeLong sz);

    /**
     * @fn __mpz_struct * _fmpz_promote(fmpz_t f);
     */
    private static native Pointer _fmpz_promote(Pointer f);

    //
    // @par Public API
    //

    /**
     * @fn void fmpz_mul(fmpz_t f, const fmpz_t g, const fmpz_t h);
     */
    public static native void fmpz_mul(Pointer f, final Pointer g, final Pointer h);

    /**
     * @fn void flint_free(void * ptr);
     */
    public static native void flint_free(Pointer ptr);

    /**
     * @fn void * flint_malloc(size_t size);
     */
    public static Pointer flint_malloc(long sz) {
        return flint_malloc(new NativeLong(sz));
    }

    /**
     * @fn
     * static __inline__
     * void fmpz_clear(fmpz_t f)
     *
     * @description
     * Clears the given fmpz_t, releasing any memory associated with it, either back to
     * the stack or the OS, depending on whether the reentrant or non-reentrant version of FLINT is built.
     *
     * @code
     *    static __inline__
     *    void fmpz_clear(fmpz_t f)
     *    {
     *        _fmpz_demote(f);
     *    }
     * @endcode
     */
    public static void fmpz_clear(Pointer f) {
        _fmpz_demote(f);
    }

    /**
     * @fn static __inline__ void fmpz_set_ui(fmpz_t f, ulong val)
     *
     * @code from <flint/fmpz.h>
     *    static __inline__ void
     *    fmpz_set_ui(fmpz_t f, ulong val)
     *    {
     *        if (val > COEFF_MAX)        // val is large
     *        {
     *            __mpz_struct *mpz_coeff = _fmpz_promote(f);
     *            mpz_set_ui(mpz_coeff, val);
     *        }
     *        else
     *        {
     *            _fmpz_demote(f);
     *            *f = val;               // val is small
     *        }
     *    }
     * @endcode
     */
    public static void fmpz_set_ui(Pointer f, long val) {
        if(val > COEFF_MAX) {             // val is large
            Pointer mpz_coeff = _fmpz_promote(f);
            JNAGmp.mpz_set_ui(mpz_coeff, val);
        } else {                          // val is small
            _fmpz_demote(f);
            f.setNativeLong(0, new NativeLong(val));
        }
    }

    /**
     * @fn static __inline__ void fmpz_init(fmpz_t f);
     *
     * @code from <flint/fmpz.h>
     *    static __inline__
     *    void fmpz_init(fmpz_t f)
     *    {
     *        (*f) = 0L;
     *    }
     * @endcode
     */
    public static void fmpz_init(Pointer f) {
        f.setNativeLong(0, new NativeLong(0));
    }

    /**
     * @fn char * fmpz_get_str(char * str, int b, const fmpz_t f)
     *
     * @returns the representation of f in base b, which can vary between 2 and 62, inclusive.
     */
    public static String fmpz_get_str(int b, final Pointer f) {
        final Pointer p = fmpz_get_str(Pointer.NULL, b, f);

        String s;
        try {
            s = p.getString(0);
        } finally {
            flint_free(p);
        }

        return s;
    }

    /**
     * @returns a Memory object which represents fmpz_t
     */
    public static Memory create_fmpz_t() {
        // Memory --- Allocate space in the native heap via a call to C's malloc.
        return new Memory(NativeLong.SIZE);
    }
}
