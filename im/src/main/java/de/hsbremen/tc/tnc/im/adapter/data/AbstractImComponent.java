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
package de.hsbremen.tc.tnc.im.adapter.data;

/**
 * Generic base that holds a message concerning an integrity measurement
 * component. It defines the minimum attributes needed for the
 * addressing of the component to/from an IM(C/V).
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class AbstractImComponent {

    private final long vendorId;
    private final long type;
    private final long collectorId;
    private final long validatorId;

    /**
     * Creates a base with the necessary address attributes.
     * @param vendorId the vendor ID describing the component
     * @param type the type ID describing the component
     * @param collectorId the referred IMC
     * @param validatorId the referred IMV
     */
    AbstractImComponent(final long vendorId, final long type,
            final long collectorId, final long validatorId) {

        this.vendorId = vendorId;
        this.type = type;
        this.collectorId = collectorId;
        this.validatorId = validatorId;

    }

    /**
     * Returns the vendor ID.
     * @return the vendor ID
     */
    public long getVendorId() {
        return vendorId;
    }

    /**
     * Returns the type ID.
     * @return the type ID
     */
    public long getType() {
        return type;
    }

    /**
     * Returns the IMC ID.
     * @return the collector ID
     */
    public long getCollectorId() {
        return collectorId;
    }

    /**
     * Returns the IMV ID.
     * @return the validator ID
     */
    public long getValidatorId() {
        return validatorId;
    }

}
