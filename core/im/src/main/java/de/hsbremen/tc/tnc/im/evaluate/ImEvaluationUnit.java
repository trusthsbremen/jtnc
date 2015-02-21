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
package de.hsbremen.tc.tnc.im.evaluate;

import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Generic evaluation unit base. The evaluation unit is responsible to
 * measure/evaluate a certain aspect of a component (e.g. the version of
 * the anti-virus software for the anti-virus component).
 *
 * @author Carl-Heinz Genzel
 *
 */
interface ImEvaluationUnit extends ImEvaluationComponent<ImAttribute> {

    /**
     * Returns the vendor ID that specifies the measured aspect (e.g. an aspect
     * specified by the IETF).
     *
     * @return the vendor ID
     */
    long getVendorId();

    /**
     * Returns the type ID that specifies the measured aspect (e.g. the product
     * information aspect type from the IETF).
     *
     * @return the type ID
     */
    long getType();
}
