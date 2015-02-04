package de.hsbremen.tc.tnc.im.adapter.imv;

import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;
import org.trustedcomputinggroup.tnc.ifimv.IMVTNCSFirst;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapterIetfFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImcEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImvSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImvSession;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;
import de.hsbremen.tc.tnc.im.session.ImSessionManager;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;

public class ImvAdapterTncsFirstIetfLong extends ImvAdapterIetfLong implements IMVTNCSFirst{
	
	public ImvAdapterTncsFirstIetfLong() {
		this(new ImParameter(true), new TncsAdapterIetfFactory(),
				new DefaultImvSessionFactory(),
				new DefaultImSessionManager<IMVConnection, ImvSession>(),
				DefaultImcEvaluatorFactory.getInstance(),
				new ImvConnectionAdapterFactoryIetf(PaWriterFactory.createProductionDefault()),
				PaReaderFactory.createProductionDefault());
	}

	public ImvAdapterTncsFirstIetfLong(ImParameter parameter,
			TncsAdapterFactory tncsFactory,
			ImSessionFactory<ImvSession> sessionFactory,
			ImSessionManager<IMVConnection, ImvSession> sessionsManager,
			ImEvaluatorFactory evaluatorFactory,
			ImvConnectionAdapterFactory connectionFactory,
			ImReader<? extends ImMessageContainer> imReader) {
		super(parameter, tncsFactory, sessionFactory, sessionsManager, evaluatorFactory, connectionFactory, imReader);
	}

	@Override
	public void beginHandshake(IMVConnection c) throws TNCException {
		
			super.checkInitialization();
			try{
				super.findSessionByConnection(c).triggerMessage(ImMessageTriggerEnum.BEGIN_HANDSHAKE);
			}catch(TncException e){
				throw new TNCException(e.getMessage(),e.getResultCode().id());
			
			}
		
	}
}
