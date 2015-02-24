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
package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.util.Set;

import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;

/**
 * Dispatches configuration entries to registered configuration handlers.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface ConfigurationEntryDispatcher {

    /**
     * Adds a handler as observer for configuration lines with the given
     * configuration line classifiers. Configuration line classifiers, that
     * are not supported by the dispatcher are ignored.
     *
     * @param classifiers the configuration line classifiers
     * @param handler the handler to add
     */
    void addHandler(Set<ConfigurationLineClassifier> classifiers,
            ConfigurationEntryHandler handler);

    /**
     * Removes a handler as observer for configuration lines with the given
     * configuration line classifiers. Configuration line classifiers, that
     * are not supported by the dispatcher are ignored.
     *
     * @param classifiers the configuration line classifiers
     * @param handler the handler to remove
     */
    void removeHandler(Set<ConfigurationLineClassifier> classifiers,
            ConfigurationEntryHandler handler);

}
