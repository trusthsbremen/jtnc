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
public abstract class ImComponentFactory {

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
     * @param collectorId the referred IMC
     * @param validatorId the referred IMV
     * @param attributes the measurement attributes
     * @return the component object
     */
    public static ImObjectComponent createObjectComponent(final byte imFlags,
            final long vendorId, final long type,
            final long collectorId, final long validatorId,
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

        return new ImObjectComponent(flags, vendorId, type, collectorId,
                validatorId, attributes);
    }

    /**
     * Creates a component holding integrity measurement attributes
     * as parsed objects as well as the necessary objects for error handling.
     *
     * @param imFlags the message flags as composed byte
     * @param vendorId the vendor ID describing the component
     * @param type the type ID describing the component
     * @param collectorId the referred IMC
     * @param validatorId the referred IMV
     * @param attributes the measurement attributes
     * @param exceptions the list of minor exception
     * @param messageHeader the raw message header
     * @return the component object
     */
    public static ImFaultyObjectComponent createFaultyObjectComponent(
            final byte imFlags, final long vendorId, final long type,
            final long collectorId, final long validatorId,
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

        return new ImFaultyObjectComponent(flags, vendorId, type, collectorId,
                validatorId, attributes, exceptions, messageHeader);
    }

    /**
     * Creates a component holding the message as raw byte array.
     * @param imFlags the message flags as composed byte
     * @param vendorId the vendor ID describing the component
     * @param type the type ID describing the component
     * @param collectorId the referred IMC
     * @param validatorId the referred IMV
     * @param message the raw message
     * @return the component object
     */
    public static ImRawComponent createRawComponent(final byte imFlags,
            final long vendorId, final long type, final long collectorId,
            final long validatorId, final byte[] message) {

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

        return new ImRawComponent(imFlags, vendorId, type, collectorId,
                validatorId, message);
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
                        component.getType(), component.getCollectorId(),
                        component.getValidatorId(), attributes);
    }

}
