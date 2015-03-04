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
package de.hsbremen.tc.tnc.tnccs.im.loader.enums;

/**
 * Enumeration of known standard configuration line classifiers.
 *
 * @author Carl-Heinz Genzel
 *
 */
public enum DefaultConfigurationLineClassifierEnum implements
        ConfigurationLineClassifier {
    /**
     * A configuration line to load a Java based IMC.
     */
    JAVA_IMC("JAVA-IMC"),
    /**
     * A configuration line to load a Java based IMV.
     */
    JAVA_IMV("JAVA-IMV"),
    /**
     * A configuration line containing a comment.
     */
    COMMENT("#");

    private String prefix;

    /**
     * Creates a configuration line classifier enumeration
     * value with the given line prefix.
     *
     * @param prefix the line prefix
     */
    private DefaultConfigurationLineClassifierEnum(final String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String linePrefix() {
        return this.prefix;
    }

    /**
     * Returns a configuration line classifier enumeration for the
     * given line prefix.
     *
     * @param classifier the line prefix
     * @return the configuration line classifier enumeration or null
     */
    public static DefaultConfigurationLineClassifierEnum fromPrefix(
            final String classifier) {

        if (classifier.trim().equals(JAVA_IMC.prefix)) {
            return JAVA_IMC;
        }

        if (classifier.trim().equals(JAVA_IMV.prefix)) {
            return JAVA_IMV;
        }

        if (classifier.trim().equals("#")) {
            return COMMENT;
        }

        return null;
    }

}
