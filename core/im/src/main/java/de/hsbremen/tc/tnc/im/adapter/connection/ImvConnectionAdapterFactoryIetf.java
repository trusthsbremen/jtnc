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
package de.hsbremen.tc.tnc.im.adapter.connection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.attribute.DefaultTncAttributeTypeFactory;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.m.message.ImMessage;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Adapter factory for an IMV connection according to IETF/TCG specifications.
 *
 */
public class ImvConnectionAdapterFactoryIetf implements
        ImvConnectionAdapterFactory {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImvConnectionAdapterFactory.class);
    private final ImWriter<ImMessage> writer;

    /**
     * Creates a connection factory with the specified message writer.
     * @param writer the message writer
     */
    @SuppressWarnings("unchecked")
    public ImvConnectionAdapterFactoryIetf(
            final ImWriter<? extends ImMessage> writer) {
        NotNull.check("Writer cannot be null.", writer);

        this.writer = (ImWriter<ImMessage>) writer;
    }

    @Override
    public ImvConnectionAdapter createConnectionAdapter(
            final IMVConnection connection) {

        NotNull.check("Connection cannot be null.", connection);

        ImvConnectionAdapter adapter = new ImvConnectionAdapterIetf(writer,
                connection);

        if (LOGGER.isDebugEnabled()) {
            this.writeConnectionInformationToDebugLog(adapter);
        }

        return adapter;
    }

    /**
     * Formats the available connection information and writes it to a log file
     * for debugging.
     *
     * @param connection the connection to examine
     */
    private void writeConnectionInformationToDebugLog(
            final ImvConnectionAdapter connection) {
        StringBuilder b = new StringBuilder();
        b.append("Create session with connection ")
                .append(connection.toString()).append(".\n");

        b.append("The following parameters are set and accessible:\n");
        List<TncAttributeType> clientTypes = DefaultTncAttributeTypeFactory
                .getInstance().getServerTypes();
        try {
            for (TncAttributeType tncAttributeType : clientTypes) {

                try {
                    Object o = connection.getAttribute(tncAttributeType);
                    if (o != null) {
                        b.append(tncAttributeType.toString() + ": ");
                        b.append(o.toString()).append("\n");
                    }
                } catch (TncException e) {
                    b.append("Not accessible. Reason: ")
                            .append(e.getResultCode().toString()).append("\n");
                }
            }
        } catch (UnsupportedOperationException e) {
            b.append("Connection does not support parameter access.");
        }

        LOGGER.debug(b.toString());
    }

}
