//
// developed by Sergey Markelov (09-2013)
//

#include <flint/fmpz.h>
#include <flint/fmpz_factor.h>
#include "logger.h"

//
// defined in logger.h
//
extern FILE *errStream;
extern FILE *outStream;

static int test_fmpz_factor()
{
    int res;
    fmpz_t  x, y;

    fmpz_init(x);
    fmpz_init(y);

    res = fmpz_set_str(x, "99999937", 10);
    if(res != 0) OriginateErrorEx(res, "0x%08x", "fmpz_set_str() returned 0x%08x", res);

    fmpz_mul(y, x, x);

    fmpz_factor_t fac;
    fmpz_factor_init(fac);

    fmpz_factor(fac, x);

    fmpz_factor_clear(fac);

    fmpz_clear(x);
    fmpz_clear(y);

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
