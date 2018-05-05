package de.hsbremen.tc.tnc.im.example;

import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.imv.ImvAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TncsAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.example.test.TestImvEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;
import de.hsbremen.tc.tnc.im.session.DefaultImvSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImvSession;

public class TestImv extends ImvAdapterIetf{
    
    public TestImv(){
        super(new ImParameter(), new TncsAdapterFactoryIetf(),
                new DefaultImvSessionFactory(),
                new DefaultImSessionManager<IMVConnection, ImvSession>(3000),
                new TestImvEvaluatorFactory("Test Value"),
                new ImvConnectionAdapterFactoryIetf(PaWriterFactory.createTestingDefault()),
                PaReaderFactory.createTestingDefault());
    }
    
}