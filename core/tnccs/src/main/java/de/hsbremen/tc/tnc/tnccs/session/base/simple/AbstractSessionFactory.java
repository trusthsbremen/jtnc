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
package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Generic base for a TNC(C/S) session factory.
 * Especially important for inheritance.
 *
 *
 */
abstract class AbstractSessionFactory implements SessionFactory {

    private final TcgProtocolBindingIdentifier tnccsProtocol;

    private final TnccsWriter<TnccsBatch> writer;
    private final TnccsReader<TnccsBatchContainer> reader;

    /**
     * Creates a the TNC(C/S) session factory base using the given
     * writer and reader.
     *
     * @param tnccsProtocol the IF-TNCCS protocol identifier
     * @param writer the message writer
     * @param reader the message reader
     */
    AbstractSessionFactory(final TcgProtocolBindingIdentifier tnccsProtocol,
            final TnccsWriter<TnccsBatch> writer,
            final TnccsReader<TnccsBatchContainer> reader) {
        NotNull.check("Constructor agruments cannot be null.",
                tnccsProtocol, writer, reader);

        this.tnccsProtocol = tnccsProtocol;
        this.writer = writer;
        this.reader = reader;
    }

    /**
     * Returns the IF-TNCCS protocol identifier supported by a session.
     *
     * @return the IF-TNCCS protocol identifier
     */
    protected TcgProtocolBindingIdentifier getTnccsProtocolIdentifier() {
        return this.tnccsProtocol;
    }


    /**
     * Returns the writer to serialize a message.
     * @return the writer to serialize a message
     */
    protected TnccsWriter<TnccsBatch> getWriter() {
        return this.writer;
    }

    /**
     * Returns the reader to parse a message.
     * @return the reader to parse a message
     */
    protected TnccsReader<TnccsBatchContainer> getReader() {
        return this.reader;
    }

}
