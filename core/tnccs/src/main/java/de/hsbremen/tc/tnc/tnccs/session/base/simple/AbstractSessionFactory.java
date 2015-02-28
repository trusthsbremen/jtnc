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
 * @author Carl-Heinz Genzel
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
