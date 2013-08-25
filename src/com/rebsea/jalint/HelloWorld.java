/*************************************************************************
 *  Compilation:  javac HelloWorld.java
 *  Execution:    java HelloWorld
 *
 *  Prints "Hello, World". By tradition, this is everyone's first program.
 *
 *  % java HelloWorld
 *  Hello, World
 *
 *  These 17 lines of text are comments. They are not part of the program;
 *  they serve to remind us about its properties. The first two lines tell
 *  us what to type to compile and test the program. The next line describes
 *  the purpose of the program. The next few lines give a sample execution
 *  of the program and the resulting output. We will always include such
 *  lines in our programs and encourage you to do the same.
 *
 *************************************************************************/

package com.rebsea.jalint;

import edu.princeton.cs.introcs.StdIn;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;

import java.io.FileInputStream;
import java.util.Properties;

import com.rebsea.jalint.JNAFlint;
import com.rebsea.jalint.jni.Jalint;

import com.rebsea.jalint.misc.Computer;
import com.rebsea.jalint.misc.SameDir;

public class HelloWorld {

    public static final long SIMPLE_EXAMPLE_VAR = 7;

    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)
            Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"),
                               CLibrary.class);

        void printf(String format, Object... args);
    }

    public static String jnaFlintSimpleExample(final long var) {
        Memory x = JNAFlint.create_fmpz_t();
        Memory y = JNAFlint.create_fmpz_t();

        try {
            JNAFlint.fmpz_init(x);
            JNAFlint.fmpz_init(y);
            JNAFlint.fmpz_set_ui(x, var);
            JNAFlint.fmpz_mul(y, x, x);

            final String strX = JNAFlint.fmpz_get_str(10, x);
            final String strY = JNAFlint.fmpz_get_str(10, y);

            return (strX + "^2 = " + strY);
        } finally {
            JNAFlint.fmpz_clear(x);
            JNAFlint.fmpz_clear(y);
        }
    }

    public static String jniFlintSimpleExample(final long x) {
        return (x + "^2 = " + Jalint.simpleExample_flintPow2(x));
    }

    public static long stressTestJNA(final long iterations) {
        long timeMillis = System.currentTimeMillis();

        for(long i = 0; i < iterations; ++i) {
            jnaFlintSimpleExample(SIMPLE_EXAMPLE_VAR);
        }

        timeMillis = System.currentTimeMillis() - timeMillis;
        return timeMillis;
    }

    public static long stressTestJNI(final long iterations) {
        long timeMillis = System.currentTimeMillis();

        for(long i = 0; i < iterations; ++i) {
            jniFlintSimpleExample(SIMPLE_EXAMPLE_VAR);
        }

        timeMillis = System.currentTimeMillis() - timeMillis;
        return timeMillis;
    }

    public static void main(String[] args) {

        // Using some of my own classes; in packages and not in packages.
        Computer computer = new Computer();
        SameDir  samedir  = new SameDir();
        System.out.println("Hello, World");

        // Using Princeton's utility classes.
        System.out.println("Type a string: ");
        String s = StdIn.readString();
        System.out.println("Your string was: " + s);
        System.out.println();

        // Basic use without any property setting.
        CLibrary.INSTANCE.printf("Hello, World from the C runtime!\n");
        for (int i=0;i < args.length;i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }

        System.setProperty("jna.library.path", "../flint2");

        Properties lib_path = System.getProperties();
        lib_path.list(System.out);


        Pointer t = JNAFlint.flint_malloc(42);
        System.out.println("JNAFlint.flint_malloc(42).getLong(0) = " + t.getLong(0));
        JNAFlint.flint_free(t);
        System.out.println("Pointer after flint_free = " + t.getLong(0));

        System.out.println();
        System.out.println("---------------------------------------");
        System.out.println("  and here is libflint simple example");
        System.out.println();
        System.out.println("JNA call");

        System.out.println(jnaFlintSimpleExample(SIMPLE_EXAMPLE_VAR));

        System.out.println();
        System.out.println("JNI call");
        System.out.println(jniFlintSimpleExample(SIMPLE_EXAMPLE_VAR));

        //
        // ------ stress tests -------
        //

        final long iters = 100000;

        System.out.println();
        System.out.println("stress testing JNA call with " + iters + " iterations");
        System.out.println("it took " + stressTestJNA(iters) + " milliseconds");

        System.out.println();
        System.out.println("stress testing JNI call with " + iters + " iterations");
        System.out.println("it took " + stressTestJNI(iters) + " milliseconds");
    }
}
