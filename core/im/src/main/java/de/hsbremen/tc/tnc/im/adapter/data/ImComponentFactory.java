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
package de.hsbremen.tc.tnc.im.adapter.data;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.adapter.data.enums.PaComponentFlagsEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Factory utility to create a certain component object holding an integrity
 * message for a component.
 *
 *
 */
public final class ImComponentFactory {

    private static final int VENDOR_ID_SHIFT = 8;
    private static final int MESSAGE_TYPE_MASK = 0xFF;

    /**
     * Private constructor should never be invoked.
     */
    private ImComponentFactory() {
        throw new AssertionError();
    }

    /**
     * Creates a component holding integrity measurement
     * attributes as parsed objects.
     *
     * @param imFlags the message flags as composed byte
     * @param vendorId the vendor ID describing the component
     * @param type the type ID describing the component
     * @param sourceId the referred sender IMC/IMV
     * @param destinationId the receiving IMC/IMV
     * @param attributes the measurement attributes
     * @return the component object
     */
    public static ImObjectComponent createObjectComponent(final byte imFlags,
            final long vendorId, final long type,
            final long sourceId, final long destinationId,
            final List<? extends ImAttribute> attributes) {

        if (vendorId >= IETFConstants.IETF_MAX_VENDOR_ID) {
            throw new IllegalArgumentException(
                    "Vendor ID exceeds its maximum size of "
                            + IETFConstants.IETF_MAX_VENDOR_ID + ".");
        }

        if (type >= IETFConstants.IETF_MAX_TYPE) {
            throw new IllegalArgumentException(
                    "Message type exceeds its maximum size of "
                            + IETFConstants.IETF_MAX_TYPE + ".");
        }

        PaComponentFlagsEnum[] flags = new PaComponentFlagsEnum[0];

        if ((byte) (imFlags & PaComponentFlagsEnum.EXCL.bit())
                == PaComponentFlagsEnum.EXCL.bit()) {

            flags = new PaComponentFlagsEnum[] {PaComponentFlagsEnum.EXCL};
        }

        return new ImObjectComponent(flags, vendorId, type, sourceId,
                destinationId, attributes);
    }

    /**
     * Creates a component holding integrity measurement attributes
     * as parsed objects as well as the necessary objects for error handling.
     *
     * @param imFlags the message flags as composed byte
     * @param vendorId the vendor ID describing the component
     * @param type the type ID describing the component
     * @param sourceId the referred sender IMC/IMV
     * @param destinationId the receiving IMC/IMV
     * @param attributes the measurement attributes
     * @param exceptions the list of minor exception
     * @param messageHeader the raw message header
     * @return the component object
     */
    public static ImFaultyObjectComponent createFaultyObjectComponent(
            final byte imFlags, final long vendorId, final long type,
            final long sourceId, final long destinationId,
            final List<? extends ImAttribute> attributes,
            final List<ValidationException> exceptions,
            final byte[] messageHeader) {

        if (vendorId >= IETFConstants.IETF_MAX_VENDOR_ID) {
            throw new IllegalArgumentException(
                    "Vendor ID exceeds its maximum size of "
                            + IETFConstants.IETF_MAX_VENDOR_ID + ".");
        }
        if (type >= IETFConstants.IETF_MAX_TYPE) {
            throw new IllegalArgumentException(
                    "Message type exceeds its maximum size of "
                            + IETFConstants.IETF_MAX_TYPE + ".");
        }

        PaComponentFlagsEnum[] flags = new PaComponentFlagsEnum[0];

        if ((byte) (imFlags & PaComponentFlagsEnum.EXCL.bit())
                == PaComponentFlagsEnum.EXCL.bit()) {

            flags = new PaComponentFlagsEnum[] {PaComponentFlagsEnum.EXCL};
        }

        return new ImFaultyObjectComponent(flags, vendorId, type, sourceId,
                destinationId, attributes, exceptions, messageHeader);
    }

    /**
     * Creates a component holding the message as raw byte array.
     * @param imFlags the message flags as composed byte
     * @param vendorId the vendor ID describing the component
     * @param type the type ID describing the component
     * @param sourceId the referred sender IMC/IMV
     * @param destinationId the receiving IMC/IMV
     * @param message the raw message
     * @return the component object
     */
    public static ImRawComponent createRawComponent(final byte imFlags,
            final long vendorId, final long type, final long sourceId,
            final long destinationId, final byte[] message) {

        if (vendorId >= IETFConstants.IETF_MAX_VENDOR_ID) {
            throw new IllegalArgumentException(
                    "Vendor ID exceeds its maximum size of "
                            + IETFConstants.IETF_MAX_VENDOR_ID + ".");
        }
        if (type >= IETFConstants.IETF_MAX_TYPE) {
            throw new IllegalArgumentException(
                    "Message type exceeds its maximum size of "
                            + IETFConstants.IETF_MAX_TYPE + ".");
        }

        return new ImRawComponent(imFlags, vendorId, type, sourceId,
                destinationId, message);
    }

    /**
     * Creates a component holding the message as raw byte array
     * using legacy addressing.
     * <br/>
     * The message type is a 32 bit unsigned number and consists of
     * a 24 bit number in the higher order bits for the vendor ID and
     * a 8 bit number in the lower order bits for the message type ID
     * <p>
     * For example combined message type ID = 0x00559701:
     * <table border="1">
     * <tr>
     * <td align="center">Radix</td><td align="center">Vendor ID</td>
     * <td align="center">Message <br/> type ID</td>
     * </tr>
     * <tr>
     * <td align="center">16</td><td align="center">005597</td>
     * <td align="center">01</td>
     * </tr>
     * <tr>
     * <td align="center">10</td><td align="center">21911</td>
     * <td align="center">1</td>
     * </tr>
     * </table>
     * </p>
     *
     * @param messageType the combined message type
     * @param message the raw message
     * @return the component object
     */
    public static ImRawComponent createLegacyRawComponent(
            final long messageType, final byte[] message) {
        long vendorId = messageType >>> VENDOR_ID_SHIFT;
        long type = messageType & MESSAGE_TYPE_MASK;

        return createRawComponent((byte) 0, vendorId, type,
                HSBConstants.HSB_IM_ID_UNKNOWN, HSBConstants.HSB_IM_ID_UNKNOWN,
                message);
    }

    /**
     * Changes the attributes of a component by making a shallow copy of
     * the object and replacing the attributes, because a component object
     * is immutable.
     *
     * @param component the component to change
     * @param attributes the attributes to insert
     * @return the component object
     */
    public static ImObjectComponent changeAttributesFromComponent(
            final ImObjectComponent component,
            final List<? extends ImAttribute> attributes) {
        return new ImObjectComponent(
                new ArrayList<PaComponentFlagsEnum>(
                        component.getImFlags()).toArray(
                        new PaComponentFlagsEnum[component
                        .getImFlags().size()]), component.getVendorId(),
                        component.getType(), component.getSourceId(),
                        component.getDestinationId(), attributes);
    }

}
