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
package de.hsbremen.tc.tnc.im.example;

import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.imv.ImvAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TncsAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImvEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;
import de.hsbremen.tc.tnc.im.session.DefaultImvSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImvSession;

/**
 * Custom operating system IMV.
 *
 *
 */
public class OsImv extends ImvAdapterIetf {

    /**
     * Creates the operating system IMV.
     */
    public OsImv() {
        super(new ImParameter(), new TncsAdapterFactoryIetf(),
                new DefaultImvSessionFactory(),
                new DefaultImSessionManager<IMVConnection, ImvSession>(3000),
                new OsImvEvaluatorFactory("/validation/os_imv.properties"),
                new ImvConnectionAdapterFactoryIetf(
                        PaWriterFactory.createProductionDefault()),
                        PaReaderFactory.createProductionDefault());
    }
}
