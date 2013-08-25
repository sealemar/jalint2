//
// TODO: Banner
//
// in the name of goodnes this code is created by
// rebcabin and sealemar
//

package com.rebsea.jalint.jni;

public class Jalint {
    /**
     * @brief
     * Implements:
     * flint-2.3 document. Section "9.2 Simple example"
     * @return String represenation of x^2
     *         or
     *         null if error occured
     */
    public static native String simpleExample_flintPow2(long x);

    static {
        // The runtime system executes a class's static initializer
        // when it loads the class.
        System.loadLibrary("jalint");
    }
}
