//
// developed by Sergey Markelov (09-2013)
//

#include <flint/fmpz.h>
#include <flint/fmpz_factor.h>
#include "logger.h"

//
// defined in logger.h
//
FILE *errStream;
FILE *outStream;

static int test_fmpz_factor()
{
    int res;
    fmpz_t  x, y, z;

    fmpz_init(x);
    fmpz_init(y);
    fmpz_init(z);

    res = fmpz_set_str(x, "6235575397", 10);
    if(res != 0) OriginateErrorEx(res, "0x%08x", "fmpz_set_str() returned 0x%08x", res);

    res = fmpz_set_str(y, "2902958701", 10);
    if(res != 0) OriginateErrorEx(res, "0x%08x", "fmpz_set_str() returned 0x%08x", res);

    //
    // calculate
    //

    fmpz_mul(z, x, y);

    fmpz_factor_t fac;
    fmpz_factor_init(fac);

    fmpz_factor(fac, z);

    //
    // print
    //

    res = fmpz_print(x);
    if(res < 0) OriginateErrorEx(res, "0x%08x", "fmpz_print() returned 0x%08x", res);

    printf(" * ");

    res = fmpz_print(y);
    if(res < 0) OriginateErrorEx(res, "0x%08x", "fmpz_print() returned 0x%08x", res);

    printf(" = ");

    res = fmpz_print(z);
    if(res < 0) OriginateErrorEx(res, "0x%08x", "fmpz_print() returned 0x%08x", res);

    printf(" --> factor = ");

    fmpz_factor_print(fac);
    printf("\n");

    fmpz_factor_clear(fac);

    fmpz_clear(x);
    fmpz_clear(y);
    fmpz_clear(z);

    return 0;
}

int main(void)
{
    int res;

    errStream = stderr;
    outStream = stdout;

    res = test_fmpz_factor();
    if(res != 0) ContinueError(res, "0x%08x");

    return 0;
}
