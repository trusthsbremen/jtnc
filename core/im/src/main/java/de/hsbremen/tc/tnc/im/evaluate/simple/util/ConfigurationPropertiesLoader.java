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
package de.hsbremen.tc.tnc.im.evaluate.simple.util;

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
public abstract class ConfigurationPropertiesLoader {

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
