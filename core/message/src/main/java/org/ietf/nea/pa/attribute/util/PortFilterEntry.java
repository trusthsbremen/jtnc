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
package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.enums.PaAttributePortFilterStatus;

/**
 * Entry object describing an existing port filter rule.
 *
 * @author Carl-Heinz Genzel
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
