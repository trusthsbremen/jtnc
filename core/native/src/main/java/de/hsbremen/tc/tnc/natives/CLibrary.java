/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package de.hsbremen.tc.tnc.natives;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;

/**
 * JNI example to connect to the libc library of a UNIX/Linux system and get
 * information about the system.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface CLibrary extends Library {

    CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);

    /**
     * Structure containing the attributes of the uname() c-function.
     * @author Carl-Heinz Genzel
     *
     */
    public static class UTSNAME extends Structure {
        /** C type : char[64 + 1]. */
        public byte[] sysname = new byte[64 + 1];
        /** C type : char[64 + 1]. */
        public byte[] nodename = new byte[64 + 1];
        /** C type : char[64 + 1]. */
        public byte[] release = new byte[64 + 1];
        /** C type : char[64 + 1]. */
        public byte[] version = new byte[64 + 1];
        /** C type : char[64 + 1]. */
        public byte[] machine = new byte[64 + 1];
        /** C type : char[64 + 1]. */
        public byte[] domainname = new byte[64 + 1];

        /**
         * Returns the filed order.
         * @return the ordered list of fields
         */
        protected List<?> getFieldOrder() {
            return Arrays.asList("sysname", "nodename", "release", "version",
                    "machine", "domainname");
        }

        /**
         * Creates the structure.
         */
        public UTSNAME() {
        }
    }

    /**
     * Adapter for the uname c-function.
     * @param utsname the structure for the attributes.
     * @return the filled structure
     */
    int uname(UTSNAME utsname);

}
