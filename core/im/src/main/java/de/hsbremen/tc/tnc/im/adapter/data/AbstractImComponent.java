/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
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
package de.hsbremen.tc.tnc.im.adapter.data;

/**
 * Generic base, that holds a message concerning an integrity measurement
 * component. It defines the minimum attributes needed for the
 * addressing of the component to/from an IM(C/V). Especially important
 * for inheritance.
 *
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
    protected AbstractImComponent(final long vendorId, final long type,
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
