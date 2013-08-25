//
// TODO: Banner
//
// in the name of goodnes this code is created by
// rebcabin and sealemar
//

package com.rebsea.jalint;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

/**
 * @brief from <gmp.h>
 */
public class JNAGmp {
    static {
        Native.register("gmp");
    }

    /**
     * @fn __GMP_DECLSPEC void mpz_set_ui (mpz_ptr, unsigned long int);
     */

    public static native void mpz_set_ui(Pointer p, long l);
}
