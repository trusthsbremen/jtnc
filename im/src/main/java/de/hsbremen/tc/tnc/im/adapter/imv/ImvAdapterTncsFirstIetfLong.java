package de.hsbremen.tc.tnc.im.adapter.imv;

import org.ietf.nea.pa.serialize.reader.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;
import org.trustedcomputinggroup.tnc.ifimv.IMVTNCSFirst;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImcEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImvSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImvSession;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.m.message.ImMessage;
import de.hsbremen.tc.tnc.m.serialize.ImReader;

public class ImvAdapterTncsFirstIetfLong extends ImvAdapterIetfLong implements IMVTNCSFirst{
	
	public ImvAdapterTncsFirstIetfLong() {
		this(new ImParameter(true),
				new DefaultImvSessionFactory(),
				DefaultImcEvaluatorFactory.getInstance(),
				new ImvConnectionAdapterFactoryIetf(PaWriterFactory.createProductionDefault()),
				PaReaderFactory.createProductionDefault());
	}

	public ImvAdapterTncsFirstIetfLong(ImParameter parameter,
			ImSessionFactory<ImvSession> sessionFactory,
			ImEvaluatorFactory evaluatorFactory,
			ImvConnectionAdapterFactory connectionFactory,
			ImReader<? extends ImMessage> imReader) {
		super(parameter, sessionFactory, evaluatorFactory, connectionFactory, imReader);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void beginHandshake(IMVConnection c) throws TNCException {
		
			super.checkInitialization();
			try{
				super.findSessionByConnection(c).triggerMessage(ImMessageTriggerEnum.BEGIN_HANDSHAKE);
			}catch(TncException e){
				throw new TNCException(e.getMessage(),e.getResultCode().result());
			
			}
		
	}
}
