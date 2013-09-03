//
// developed by Sergey Markelov (09-2013)
//
#ifndef GENERIC_LOGGER
#define GENERIC_LOGGER

#include <stdio.h>

extern FILE *errStream;
extern FILE *outStream;

#define ERR_STREAM              errStream
#define OUT_STREAM              outStream

#define ERROR_PREFIX            "Error: "
#define STANDARD_PREFIX         ""
#define ERROR_CONTINUE_PREFIX   " ----> "

#define _LOG(stream, prefix, format, ...) \
    { fprintf(stream, "%s"##format##"\n", prefix##__FILE__##":"##__LINE__##" ("##__func__##") - ", ##__VA_ARGS__); }

#define LogError(format, ...) \
    { _LOG(ERR_STREAM, ERROR_PREFIX, format, ##__VA_ARGS__); }

#define Log(format, ...) \
    { _LOG(OUT_STREAM, STANDARD_PREFIX, format, ##__VA_ARGS__); }

//
// @brief this macro originates the error. Functions down the stack should use
//        ContinueErrorEx() or ContinueError() to propagate the error behavior.
//
//        In the log it will look like:
//
//        Error: example.c:35 (my_function) - [0000001b] custom error
//         ----> someFile.c:50 (process) - [0000001b]
//         ----> main.c:163 (main) - [00000001]
//
// @param result the value which needs to be logged
// @param resultSpecifier printf() specifier for the result. Ex. "%d" means @c result is of type @c int
//        you can also use "0x%08x" to make it Hex
// @param format, ... - custom formatted message
//
#define OriginateErrorEx(result, resultSpecifier, format, ...) \
    { LogError("["##resultSpecifier##"]"##format, result, ##__VA_ARGS__); return result; }

//
// @see OriginateErrorEx
//
#define OriginateError(result, resultSpecifier) \
    { LogError("["##resultSpecifier##"]", result); return result; }

//
// @see OriginateErrorEx
//
#define ContinueErrorEx(result, resultSpecifier, format, ...) \
    { _LOG(ERR_STREAM, ERROR_CONTINUE_PREFIX, \
           "["##resultSpecifier##"]"##format, result, ##__VA_ARGS__); return result; }

//
// @see OriginateErrorEx
//
#define ContinueError(result, resultSpecifier) \
    { _LOG(ERR_STREAM, ERROR_CONTINUE_PREFIX, \
           "["##resultSpecifier##"]"##format, result); return result; }

#endif
