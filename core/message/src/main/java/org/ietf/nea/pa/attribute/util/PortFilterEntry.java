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
package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.enums.PaAttributePortFilterStatus;

/**
 * Entry object describing an existing port filter rule.
 *
 *
 */
public class PortFilterEntry {

    private final PaAttributePortFilterStatus blocked; // 1 bit(s)
    private final short protocolNumber; // 8 bit(s)
    private final int portNumber; // 16 bit(s)

    /**
     * Creates an entry object for a protocol identified by the protocol ID and
     * port number with a given filter status.
     *
     * @param blocked the filter status
     * @param protocolIdentifier the protocol identifier
     * @param portNumber the port number
     */
    public PortFilterEntry(final PaAttributePortFilterStatus blocked,
            final short protocolIdentifier, final int portNumber) {

        this.blocked = blocked;
        this.protocolNumber = protocolIdentifier;
        this.portNumber = portNumber;
    }

    /**
     * Returns the filter status.
     *
     * @return the filter status
     */
    public PaAttributePortFilterStatus getFilterStatus() {
        return this.blocked;
    }

    /**
     * Returns the protocol ID.
     *
     * @return the protocol ID
     */
    public short getProtocolNumber() {
        return this.protocolNumber;
    }

    /**
     * Returns the port number.
     *
     * @return the port number
     */
    public int getPortNumber() {
        return this.portNumber;
    }

    @Override
    public String toString() {
        return "PortFilterEntry [blocked=" + this.blocked.toString()
                + ", protocolNumber=" + this.protocolNumber + ", portNumber="
                + this.portNumber + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.blocked == null) ? 0 : this.blocked.hashCode());
        result = prime * result + this.portNumber;
        result = prime * result + this.protocolNumber;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PortFilterEntry other = (PortFilterEntry) obj;
        if (!this.blocked.equals(other.blocked)) {
            return false;
        }
        if (this.portNumber != other.portNumber) {
            return false;
        }
        if (this.protocolNumber != other.protocolNumber) {
            return false;
        }
        return true;
    }

}
