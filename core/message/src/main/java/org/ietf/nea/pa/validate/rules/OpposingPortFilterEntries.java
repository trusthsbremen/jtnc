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
package org.ietf.nea.pa.validate.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributePortFilterStatus;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
/**
 * Rule, that checks if the list contains valid port filter entries.
 * @author Carl-Heinz Genzel
 *
 */
public abstract class OpposingPortFilterEntries {
    /**
     * Private constructor should never be invoked.
     */
    private OpposingPortFilterEntries() {
        throw new AssertionError();
    }

    /**
     * Checks if the list contains valid port filter entries.
     * @param entries the list of port filter entries
     * @throws RuleException if check fails
     */
    public static void check(final List<PortFilterEntry> entries)
            throws RuleException {

        if (entries != null) {

            Map<Short, Map<Integer, PaAttributePortFilterStatus>> sorted =
                    new HashMap<>();

            for (PortFilterEntry entry : entries) {

                if (sorted.containsKey(entry.getProtocolNumber())) {
                    if (sorted.get(entry.getProtocolNumber()).containsKey(
                            entry.getPortNumber())) {
                        if (sorted.get(entry.getProtocolNumber()).values()
                                .iterator().next() != entry.getFilterStatus()) {

                            throw new RuleException(
                                    "The port filter contains"
                                            + " entries for the protocol "
                                            + entry.getProtocolNumber()
                                            + " with differnt blocking status.",
                                    false,
                                    PaAttributeErrorCodeEnum
                                        .IETF_INVALID_PARAMETER.code(),
                                    PaErrorCauseEnum.MIXED_PROTOCOL_BLOCKING
                                            .id(), entry.getProtocolNumber(),
                                    entry.getPortNumber(), entry
                                            .getFilterStatus().toString(),
                                    entries.indexOf(entry));

                        }
                        if (sorted.get(entry.getProtocolNumber()).get(
                                entry.getPortNumber()) != entry
                                .getFilterStatus()) {

                            throw new RuleException(
                                    "The port filter contains"
                                           + " duplicate entries for the tupel "
                                           + entry.getProtocolNumber() + ":"
                                           + entry.getPortNumber()
                                           + " with differnt blocking status.",
                                    false,
                                    PaAttributeErrorCodeEnum
                                        .IETF_INVALID_PARAMETER.code(),
                                    PaErrorCauseEnum.DUPLICATE_PORT_ENTRY.id(),
                                    entry.getProtocolNumber(),
                                    entry.getPortNumber(),
                                    entry.getFilterStatus().toString(),

                                    sorted.get(entry.getProtocolNumber())
                                            .get(entry.getPortNumber())
                                            .toString(),
                                            entries.indexOf(entry));
                        } else {
                            throw new RuleException(
                                    "The port filter contains duplicate"
                                            + " entries for the tupel "
                                            + entry.getProtocolNumber() + ":"
                                            + entry.getPortNumber() + ".",
                                    false,
                                    PaAttributeErrorCodeEnum
                                        .IETF_INVALID_PARAMETER.code(),
                                    PaErrorCauseEnum.DUPLICATE_PORT_ENTRY.id(),
                                    entry.getProtocolNumber(), entry
                                            .getPortNumber(), entry
                                            .getFilterStatus().toString(),
                                    entries.indexOf(entry));
                        }
                    } else {
                        sorted.get(entry.getProtocolNumber()).put(
                                entry.getPortNumber(), entry.getFilterStatus());
                    }
                } else {
                    sorted.put(entry.getProtocolNumber(),
                            new HashMap<Integer,
                                PaAttributePortFilterStatus>());
                    sorted.get(entry.getProtocolNumber()).put(
                            entry.getPortNumber(), entry.getFilterStatus());
                }
            }
        }
    }
}
