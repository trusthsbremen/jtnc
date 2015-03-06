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
