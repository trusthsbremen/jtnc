package de.hsbremen.tc.tnc.im.adapter.imv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;
import org.trustedcomputinggroup.tnc.ifimv.IMVLong;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.attribute.TncClientAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapterFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.ImSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImvSession;
import de.hsbremen.tc.tnc.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.m.serialize.ImReader;

public class ImvAdapterIetfLong extends ImvAdapterIetf implements IMVLong{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImvAdapterIetfLong.class);
	
	public ImvAdapterIetfLong() {
		super();
	}

	public ImvAdapterIetfLong(ImParameter parameter, TncsAdapterFactory tncsFactory,
			ImSessionFactory<ImvSession> sessionFactory,
			ImEvaluatorFactory evaluatorFactory,
			ImvConnectionAdapterFactory connectionFactory,
			ImReader<? extends ImMessageContainer> imReader) {
		super(parameter, tncsFactory, sessionFactory, evaluatorFactory, connectionFactory, imReader);
	}

	@Override
	public void initialize(TNCS tncs) throws TNCException {
		
			try{
				Object o = tncs.getAttribute(TncClientAttributeTypeEnum.TNC_ATTRIBUTEID_PRIMARY_IMC_ID.id());
				if(o instanceof Long){
					long id = ((Long)o).longValue();
					super.setPrimaryId(id);
				}
			}catch (TNCException | UnsupportedOperationException e){
				LOGGER.warn("Primary ID initalization failed, this IMV will work with reduced functionality.",e);
			}

		super.initialize(tncs);
	}
	
	@Override
	public void receiveMessageLong(IMVConnection c, long messageFlags,
			long messageVendorID, long messageSubtype, byte[] message,
			long sourceIMVID, long destinationIMCID) throws TNCException {
		super.checkInitialization();
		try{
			ImObjectComponent component = super.receiveMessage(ImComponentFactory.createRawComponent((byte)(messageFlags & 0xFF), messageVendorID, messageSubtype, destinationIMCID, sourceIMVID, message));
			super.findSessionByConnection(c).handleMessage(component);
		}catch(TncException e){
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
	}
}
