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
package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.attribute.Attributed;

/**
 * Common attributes for every TNC(C/S) session. Attributes are accessible
 * thru getters as well as thru Attributed.
 *
 * @author Carl-Heinz Genzel
 */
public interface SessionAttributes extends Attributed {

    /**
     * Returns the IF-TNCCS protocol type (e.g. IF-TNCCS).
     * @return the IF-TNCCS protocol type
     */
    String getTnccsProtocolType();

    /**
     * Returns the IF-TNCCS protocol version (e.g. 2.0).
     * @return the IF-TNCCS version
     */
    String getTnccsProtocolVersion();

    /**
     * Returns the current message round trip count of
     * the session.
     * @return the current round trips
     */
    long getCurrentRoundTrips();

    /**
     * Returns the preferred language for human readable
     * strings contained in messages.
     *
     * @return the preferred language
     */
    String getPreferredLanguage();

    /**
     * Sets the current message round trip count of the
     * session. If the round trips exceed the maximum value of
     * long the current round trips will be set to zero.
     *
     * @param roundtrips the current round trips
     */
    void setCurrentRoundTrips(long roundtrips);

    /**
     * Sets the preferred language for human readable strings
     * contained in messages.
     *
     * @param preferredLanguage the preferred language
     */
    void setPreferredLanguage(String preferredLanguage);

}
