package de.hsbremen.tc.tnc.im.adapter.imc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.ietf.nea.pa.serialize.reader.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.PaWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateFactory;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImAdapter;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapter;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapterIetfFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImcEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImcSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImcSession;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.m.serialize.ImReader;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

public class ImcAdapterIetf extends ImAdapter implements IMC, AttributeSupport{

	private static final Logger LOGGER = LoggerFactory.getLogger(ImcAdapterIetf.class);
	
	private final ImParameter parameter;
	
	private final TnccAdapterFactory tnccFactory;
	private final ImcConnectionAdapterFactory connectionFactory;
	
	private final ImSessionFactory<ImcSession> sessionFactory;
	private final Map<IMCConnection, ImcSession> sessions;

	private final ImEvaluatorFactory evaluatorFactory;
	private ImEvaluatorManager evaluatorManager;
	
	private TnccAdapter tncc;
	
	
	public ImcAdapterIetf(){
		// FIXME this is only a default constructor and should only be used for testing purpose.
		this(new ImParameter(), new TnccAdapterIetfFactory(),
				new DefaultImcSessionFactory(),
				DefaultImcEvaluatorFactory.getInstance(),
				new ImcConnectionAdapterFactoryIetf(PaWriterFactory.createProductionDefault()),
				PaReaderFactory.createProductionDefault());
	}
	
	public ImcAdapterIetf(ImParameter parameter, TnccAdapterFactory tnccFactory, ImSessionFactory<ImcSession> sessionFactory, ImEvaluatorFactory evaluatorFactory, ImcConnectionAdapterFactory connectionFactory, ImReader<? extends ImMessageContainer> imReader){
		super(imReader);
		
		this.parameter = parameter;
		
		this.tnccFactory = tnccFactory;
		this.connectionFactory = connectionFactory;
		this.evaluatorFactory = evaluatorFactory;
		this.sessionFactory = sessionFactory;
		
		this.sessions = new HashMap<IMCConnection, ImcSession>();
	}
	
	@Override
	public void initialize(TNCC tncc) throws TNCException {
		if(this.tncc == null){
			this.tncc = this.tnccFactory.createTnccAdapter(this,tncc);
			this.evaluatorManager = this.evaluatorFactory.getEvaluators(this.tncc, this.parameter);
			try{
				this.tncc.reportMessageTypes(this.evaluatorManager.getSupportedMessageTypes());
			}catch(TncException e){
				throw new TNCException(e.getMessage(),e.getResultCode().result());
			}
			if(tncc instanceof AttributeSupport){
				try{
					Object o = ((AttributeSupport) tncc).getAttribute(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_PREFERRED_LANGUAGE.id());
					if(o instanceof String){
						String preferredLanguage = (String)o;
						this.parameter.setPreferredLanguage(preferredLanguage);
					}
				}catch (TNCException | UnsupportedOperationException e){
					LOGGER.info("Preferred language attribute was not accessible, using default language: " + this.parameter.getPreferredLanguage(),e);
				}
			}
			
			
		}else{
			throw new TNCException("IMC already initialized by " + this.tncc.toString() + ".", TNCException.TNC_RESULT_ALREADY_INITIALIZED);
		}
	}

	@Override
	public void terminate() throws TNCException {
		checkInitialization();
		
		for (ImcSession session : this.sessions.values()) {
			session.terminate();
		}
		this.sessions.clear();
		
		this.evaluatorManager.terminate();
		
		this.parameter.setPrimaryId(HSBConstants.HSB_IM_ID_UNKNOWN);
		this.parameter.setSupportedMessageTypes(new HashSet<SupportedMessageType>());
		
		this.tncc = null;
	}

	@Override
	public void notifyConnectionChange(IMCConnection c, long newState)
			throws TNCException {
		checkInitialization();
		try{
			this.findSessionByConnection(c).setConnectionState(DefaultTncConnectionStateFactory.getInstance().fromState(newState));
		}catch(TncException e){
			throw new TNCException(e.getMessage(),e.getResultCode().result());
		}
	}

	@Override
	public void beginHandshake(IMCConnection c) throws TNCException {
		checkInitialization();
		try{
			this.findSessionByConnection(c).triggerMessage(ImMessageTriggerEnum.BEGIN_HANDSHAKE);
		}catch(TncException e){
			throw new TNCException(e.getMessage(),e.getResultCode().result());
		}
		
	}

	@Override
	public void receiveMessage(IMCConnection c, long messageType, byte[] message)
			throws TNCException {
		checkInitialization();
		
		if(message != null && message.length > 0){
			try{
				ImObjectComponent component = super.receiveMessage(ImComponentFactory.createLegacyRawComponent(messageType, message));
				this.findSessionByConnection(c).handleMessage(component);
			}catch(TncException e){
				throw new TNCException(e.getMessage(),e.getResultCode().result());
			}
		}
		
	}

	@Override
	public void batchEnding(IMCConnection c) throws TNCException {
		checkInitialization();
		try{
			this.findSessionByConnection(c).triggerMessage(ImMessageTriggerEnum.BATCH_ENDING);
		}catch(TncException e){
			throw new TNCException(e.getMessage(),e.getResultCode().result());
		}
	}

	@Override
	public Object getAttribute(long attributeID) throws TNCException {

		if(attributeID == AttributeSupport.TNC_ATTRIBUTEID_IMC_SPTS_TNCS1){
			return (this.parameter.hasTncsFirstSupport()) ? Boolean.TRUE : Boolean.FALSE;
		}
		
		throw new TNCException("The attribute with ID " + attributeID + " is unknown.", TNCException.TNC_RESULT_INVALID_PARAMETER);
		
	}

	@Override
	public void setAttribute(long attributeID, Object attributeValue)
			throws TNCException {
		
		throw new UnsupportedOperationException("The operation setAttribute(...) is not supported, because there are no attributes to set.");
//		if(attributeID == AttributeSupport.TNC_ATTRIBUTEID_PRIMARY_IMC_ID){
//			
//			if(attributeValue instanceof Long){
//				if(this.parameter.getPrimaryId() <= 0){
//					this.parameter.setPrimaryId((Long)attributeValue);
//					this.initializeEvaluators(this.parameter.getPrimaryId());
//				}else{
//					throw new TNCException("Primary ID already set.", TNCException.TNC_RESULT_OTHER);
//				}
//			}else{
//				throw new TNCException("Unexpected attribute type. Attribute with attribute ID " + attributeID + " cannot be of type " + attributeValue.getClass().getSimpleName() + ".", TNCException.TNC_RESULT_INVALID_PARAMETER);
//			}
//			
//		}else{
//			throw new TNCException("The attribute with ID " + attributeID + " is unknown.", TNCException.TNC_RESULT_INVALID_PARAMETER);
//		}
		
	}
	
	protected ImcSession findSessionByConnection(IMCConnection connection){
		if(this.sessions.containsKey(connection)){
			return this.sessions.get(connection);
		}
		
		ImcSession newSession = this.sessionFactory.createSession(this.connectionFactory.createConnectionAdapter(connection),evaluatorManager);
		this.sessions.put(connection, newSession);
		
		return newSession;
	}
	
	protected void checkInitialization() throws TNCException{
		if (this.tncc == null){
			throw new TNCException("IMC is not initialized.",TNCException.TNC_RESULT_NOT_INITIALIZED);
		}
	}
	
	protected void setPrimaryId(long id){
		this.parameter.setPrimaryId(id);
	}
}
