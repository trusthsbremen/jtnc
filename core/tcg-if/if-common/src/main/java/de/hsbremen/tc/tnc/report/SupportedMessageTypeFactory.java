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
package de.hsbremen.tc.tnc.report;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.hsbremen.tc.tnc.IETFConstants;

/**
 * Factory utility to create a SupportedMessageType object.
 *
 *
 */
public abstract class SupportedMessageTypeFactory {

    private static final int VENDOR_ID_SHIFT = 8;
    private static final int MESSAGE_TYPE_MASK = 0xFF;

    /**
     * Private constructor should never be invoked.
     */
    private SupportedMessageTypeFactory() {
        throw new AssertionError();
    }

    /**
     * Creates a message type with the given vendor ID and type ID.
     *
     * @param vendorId the vendor ID
     * @param typeId the type ID
     * @return the message type
     */
    public static SupportedMessageType createSupportedMessageType(
            final long vendorId, final long typeId) {

        if (vendorId > IETFConstants.IETF_MAX_VENDOR_ID) {
            throw new IllegalArgumentException(
                    "Vendor ID exceeds its maximum size of "
                            + IETFConstants.IETF_MAX_VENDOR_ID + ".");
        }
        if (typeId > IETFConstants.IETF_MAX_TYPE) {
            throw new IllegalArgumentException(
                    "Message type exceeds its maximum size of "
                            + IETFConstants.IETF_MAX_TYPE + ".");
        }

        return new SupportedMessageType(vendorId, typeId);
    }

    /**
     * Creates a list of message types with the given vendor ID array
     * and type ID array. The mapping is done per index line
     * message type = vendorIds[n] and messageTypeIds[n]. Therefore the
     * following condition must meet:
     * <ul><li>vendorIds.length == messageTypeIds.length</li></ul>
     *
     * @param vendorIds the array of vendor IDs
     * @param messageTypeIds the array of message type IDs
     * @return a list of message types
     */
    public static List<SupportedMessageType> createSupportedMessageTypes(
            final long[] vendorIds, final long[] messageTypeIds) {
        List<SupportedMessageType> sTypes = new LinkedList<>();

        if (vendorIds != null && messageTypeIds != null) {

            if (vendorIds.length != messageTypeIds.length) {
                throw new IllegalArgumentException(
                        "The supplied arrays have a different length ("
                        + vendorIds.length + ":" + messageTypeIds + ").");
            }

            for (int i = 0; i < vendorIds.length; i++) {
                SupportedMessageType mType = createSupportedMessageType(
                        vendorIds[i], messageTypeIds[i]);
                sTypes.add(mType);
            }
        }

        return sTypes;
    }

    /**
     * Creates a message type with the given combined vendor ID and type ID.
     * This message type ID is a 32 bit unsigned number and consists of
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
     *
     * @param messageType the combined legacy message type ID
     * @return the message type
     */
    public static SupportedMessageType createSupportedMessageTypeLegacy(
           final long messageType) {

        long vendorId = messageType >>> VENDOR_ID_SHIFT;
        long type = messageType & MESSAGE_TYPE_MASK;

        return createSupportedMessageType(vendorId, type);
    }

    /**
     * Creates a list of message types with the given array of
     * combined message type IDs.
     *
     * @param messageTypes the array of combined legacy message type IDs
     * @return a list of message types
     */
    public static List<SupportedMessageType> createSupportedMessageTypesLegacy(
            final long[] messageTypes) {

        List<SupportedMessageType> types = new LinkedList<>();
        if (messageTypes != null) {
            for (long l : messageTypes) {
                types.add(createSupportedMessageTypeLegacy(l));
            }
        }

        return types;
    }

    /**
     * Creates an array of combined legacy message type IDs
     * from a set of message type objects.
     *
     * @param supportedMessageTypes the message type objects
     * @return an array of combined legacy message type IDs
     */
    public static long[] createSupportedMessageTypeArrayLegacy(
            final Set<SupportedMessageType> supportedMessageTypes) {

        if (supportedMessageTypes == null
                || supportedMessageTypes.size() <= 0) {
            return new long[0];
        }

        long[] types = new long[supportedMessageTypes.size()];
        int i = 0;
        for (SupportedMessageType supportedMessageType
                : supportedMessageTypes) {
            long messageType =
                    (supportedMessageType.getVendorId() << VENDOR_ID_SHIFT)
                    | (supportedMessageType.getType() & MESSAGE_TYPE_MASK);
            types[i] = messageType;
        }

        return types;
    }

    /**
     * Creates a two dimensional array of message types with a
     * vendor ID sub array and type ID sub array from a set of
     * message type objects. An objects vendor ID and its
     * message type ID are placed in one row of this array:
     *
     * <ul>
     * <li>array[0][n] = vendor ID</li>
     * <li>array[1][n] = message type ID</li>
     * </ul>
     *
     * @param supportedMessageTypes the message type objects
     * @return a two dimensional array of vendor IDs and message type IDs
     */
    public static long[][] createSupportedMessageTypeArray(
            final Set<SupportedMessageType> supportedMessageTypes) {

        if (supportedMessageTypes == null
                || supportedMessageTypes.size() <= 0) {
            return new long[2][0];
        }

        long[][] types = new long[2][supportedMessageTypes.size()];
        int i = 0;
        for (SupportedMessageType supportedMessageType
                : supportedMessageTypes) {
            types[0][i] = supportedMessageType.getVendorId();
            types[1][i] = supportedMessageType.getType();
        }

        return types;
    }

}
