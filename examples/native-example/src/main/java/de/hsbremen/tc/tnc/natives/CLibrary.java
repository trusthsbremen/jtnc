/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 *
 */
public interface CLibrary extends Library {

    CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);

    /**
     * Structure containing the attributes of the uname() c-function.
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
