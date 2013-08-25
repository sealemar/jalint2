//
// TODO: Banner
//
// in the name of goodnes this code is created by
// rebcabin and sealemar
//

#include <flint/fmpz.h>

#include "libJalint.h"

#define UNUSED(x) (void)(x)
#define FMPZ_GET_STR_DEFAULT_BASE 10

/*
 * Class:     com_rebsea_jalint_jni_Jalint
 * Method:    simpleExample_flintPow2
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_rebsea_jalint_jni_Jalint_simpleExample_1flintPow2
  (JNIEnv *env, jclass obj, jlong var)
{
    UNUSED(obj);

    fmpz_t  x, y;

    fmpz_init(x);
    fmpz_init(y);

    fmpz_set_ui(x, (mp_limb_t)var);

    fmpz_mul(y, x, x);

    // Returns the representation of f in base b, which can vary between 2 and 62, inclusive.
    // If str is NULL, the result string is allocated by the function. Otherwise, it is up to the
    // caller to ensure that the allocated block of memory is sufficiently large.
    //
    // @warning flint::fmpz_get_str() gmp::mpz_get_str() which allocates a buffer, but doesn't do
    // a NULL check before using the buffer. The process will crash with SIGSEGV in this situation
    // I can do nothing to help with that here, but I will do a NULL check just in case
    // the implimentation of _gmp_ library changes

    char *fmpzStr = fmpz_get_str(NULL, FMPZ_GET_STR_DEFAULT_BASE, y);

    fmpz_clear(x);
    fmpz_clear(y);

    jstring result = NULL;
    if(fmpzStr != NULL)
    {
        // @note result may be NULL after calling this method indicating error.
        // I don't know what to do with it right now, so just returning it back
        // @see https://publib.boulder.ibm.com/infocenter/iseries/v5r4/index.jsp?topic=%2Frzaha%2Fjniex.htm

        result = (*env)->NewStringUTF(env, fmpzStr);
        free(fmpzStr);
    }

    return result;
}
