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
package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import java.util.Map;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;

/**
 * Generic IMV connection context.
 *
 *
 */
public interface ImvConnectionContext extends ImConnectionContext {

    /**
     * Adds a recommendation of an IMV with the given ID to the context for
     * the overall result of an integrity check handshake.
     *
     * @param id the IMV ID
     * @param recommendationPair the recommendation pair
     * @throws TncException if recommendation cannot be added to the context
     */
    void addRecommendation(long id,
            ImvRecommendationPair recommendationPair) throws TncException;

    /**
     * Returns all recommendations, which were added to the context and
     * clears the context recommendation buffer.
     *
     * @return a map of recommendations (key:IMV ID, value:recommendation)
     */
    Map<Long, ImvRecommendationPair> clearRecommendations();
}
