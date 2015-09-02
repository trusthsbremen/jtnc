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
package de.hsbremen.tc.tnc.tnccs.im.loader.enums;

/**
 * Enumeration of known standard configuration line classifiers.
 *
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
