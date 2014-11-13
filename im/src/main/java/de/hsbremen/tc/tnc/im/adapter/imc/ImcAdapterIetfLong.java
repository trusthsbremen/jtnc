package de.hsbremen.tc.tnc.im.adapter.imc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.attribute.TncClientAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.ImSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImcSession;
import de.hsbremen.tc.tnc.m.message.ImMessage;
import de.hsbremen.tc.tnc.m.serialize.ImReader;

public class ImcAdapterIetfLong extends ImcAdapterIetf implements IMCLong{

	private static final Logger LOGGER = LoggerFactory.getLogger(ImcAdapterIetfLong.class);
	
	public ImcAdapterIetfLong(){
		super();
	}
	
	public ImcAdapterIetfLong(ImParameter parameter, ImSessionFactory<ImcSession> sessionFactory, ImEvaluatorFactory evaluatorFactory, ImcConnectionAdapterFactory connectionFactory, ImReader<? extends ImMessage> imReader){
		super(parameter, sessionFactory, evaluatorFactory, connectionFactory, imReader);

	}
	
	@Override
	public void initialize(TNCC tncc) throws TNCException {
		if(tncc instanceof AttributeSupport){
			try{
				Object o = ((AttributeSupport) tncc).getAttribute(TncClientAttributeTypeEnum.TNC_ATTRIBUTEID_PRIMARY_IMC_ID.id());
				if(o instanceof Long){
					long id = ((Long)o).longValue();
					super.setPrimaryId(id);
				}
			}catch (TNCException | UnsupportedOperationException e){
				LOGGER.warn("Primary ID initalization failed, this IMC will only work in basic mode.",e);
			}
			
		}
		super.initialize(tncc);
	}

	@Override
	public void receiveMessageLong(IMCConnection c, long messageFlags,
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
