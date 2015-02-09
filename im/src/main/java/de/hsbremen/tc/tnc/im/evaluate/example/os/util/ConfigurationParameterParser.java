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
package de.hsbremen.tc.tnc.im.evaluate.example.os.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration parameter utility class.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class ConfigurationParameterParser {

    /**
     * Private constructor should never be invoked.
     */
    private ConfigurationParameterParser() {
        throw new AssertionError();
    }

    /**
     * Loads a java properties file with reference values for an
     * evaluation unit.
     *
     * @param evaluationValuesFile the path to the file
     * @return the file as property object
     * @throws IOException if file access fails
     */
    public static Properties loadProperties(final String evaluationValuesFile)
            throws IOException {

        Properties p = new Properties();

        BufferedInputStream stream = new BufferedInputStream(
                evaluationValuesFile.getClass().getResourceAsStream(
                        evaluationValuesFile));

        p.load(stream);

        stream.close();

        return p;
    }

}
