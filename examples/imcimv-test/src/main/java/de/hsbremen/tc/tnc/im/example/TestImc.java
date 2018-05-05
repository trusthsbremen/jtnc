package de.hsbremen.tc.tnc.im.example;

import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.imc.ImcAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.example.test.TestImcEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;
import de.hsbremen.tc.tnc.im.session.DefaultImcSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImcSession;

public class TestImc extends ImcAdapterIetf {

    public TestImc() {
        super(new ImParameter(), new TnccAdapterFactoryIetf(),
                new DefaultImcSessionFactory(),
                new DefaultImSessionManager<IMCConnection, ImcSession>(3000),
                new TestImcEvaluatorFactory("Test Value"),
                new ImcConnectionAdapterFactoryIetf(
                        PaWriterFactory.createTestingDefault()),
                PaReaderFactory.createTestingDefault());
    }
}
