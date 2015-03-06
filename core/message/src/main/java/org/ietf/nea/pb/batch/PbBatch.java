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
package org.ietf.nea.pb.batch;

import java.util.Collections;
import java.util.List;

import org.ietf.nea.pb.message.PbMessage;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * IETF RFC 5793 TNCCS batch.
 *
 *
 */
public class PbBatch implements TnccsBatch {

    private final PbBatchHeader header;
    private final List<PbMessage> messages;

    /**
     * Creates the batch with the given batch header and messages.
     * @param header the IETF RFC 5793 compliant header
     * @param messages the IETF RFC 5793 compliant messages
     */
    public PbBatch(final PbBatchHeader header, final List<PbMessage> messages) {
        NotNull.check("Batch header cannot be null.", header);
        NotNull.check("Messages cannot be null.", messages);
        this.header = header;
        this.messages = messages;
    }

    /**
     * @return the messages
     */
    @Override
    public List<PbMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public PbBatchHeader getHeader() {
        return this.header;
    }

}
