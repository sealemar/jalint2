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

import computer_package.Computer;

public class HelloWorld {

    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)
            Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"),
                               CLibrary.class);

        void printf(String format, Object... args);
    }

    public interface Flint extends Library {
        Flint INSTANCE = (Flint)
            Native.loadLibrary("flint", Flint.class);
        // Optional: synchronizeLibrary wraps each entry point in a
        // synch block, limiting thread entries to one at a time.

        // NativeLibrary.getInstance(String)
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
    }
}
