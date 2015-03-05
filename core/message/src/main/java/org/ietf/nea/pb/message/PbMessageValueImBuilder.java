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
package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValueBuilder;

/**
 * Generic builder to build a TNCCS integrity measurement component message
 * value compliant to RFC 5793. It can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface PbMessageValueImBuilder extends TnccsMessageValueBuilder {

    /**
     * Sets the integrity measurement component flags encoded as byte.
     *
     * @param imFlags the integrity measurement component flags
     * @return this builder
     */
    PbMessageValueImBuilder setImFlags(byte imFlags);

    /**
     * Sets the the integrity measurement component vendor ID.
     *
     * @param subVendorId the component vendor ID
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbMessageValueImBuilder setSubVendorId(long subVendorId)
            throws RuleException;

    /**
     * Sets the type describing the integrity measurement component.
     *
     * @param subType the component type ID
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbMessageValueImBuilder setSubType(long subType) throws RuleException;

    /**
     * Sets the the IMC ID receiving/sending the measurements.
     *
     * @param collectorId the IMC ID
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbMessageValueImBuilder setCollectorId(long collectorId)
            throws RuleException;

    /**
     * Sets the the IMV ID receiving/sending the measurements.
     *
     * @param validatorId the IMV ID
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbMessageValueImBuilder setValidatorId(long validatorId)
            throws RuleException;

    /**
     * Sets the integrity measurement component message.
     *
     * @param message the message
     * @return this builder
     */
    PbMessageValueImBuilder setMessage(byte[] message);

}
