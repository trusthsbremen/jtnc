/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
package de.hsbremen.tc.tnc.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration parameter utility class.
 *
 *
 */
public final class ConfigurationPropertiesLoader {

    /**
     * Private constructor should never be invoked.
     */
    private ConfigurationPropertiesLoader() {
        throw new AssertionError();
    }

    /**
     * Loads a java properties file/resource with reference values for an
     * evaluation unit.
     *
     * @param evaluationValuesFile the path to the file/resource
     * @param resourceHook hook to determine the correct resource
     * path if resource should be loaded
     * @return the file as property object
     * @throws IOException if file access fails
     */
    public static Properties loadProperties(final String evaluationValuesFile,
            final Class<?> resourceHook) throws IOException {

        Properties p = new Properties();
        BufferedInputStream stream = null;

        // first try to read as resource
        try {
             Class<?> hook = (resourceHook != null) ? resourceHook
                     : evaluationValuesFile.getClass();

             stream = new BufferedInputStream(hook.getResourceAsStream(
                            evaluationValuesFile));
             p.load(stream);
        } catch (IOException e) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    // ignore
                }
            }

            // than try to read as file
            try {
                File f = new File(evaluationValuesFile);
                stream = new BufferedInputStream(new FileInputStream(f));
                p.load(stream);
            } catch (IOException e2) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e1) {
                        // ignore
                    }
                }
                throw new IOException("The given file path "
                        + evaluationValuesFile
                        + " is neither a resource nor an existing file."
                        + " Cannot load IMV properties");
            }
        }

        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e1) {
                // ignore
            }
        }

        return p;
    }

}
