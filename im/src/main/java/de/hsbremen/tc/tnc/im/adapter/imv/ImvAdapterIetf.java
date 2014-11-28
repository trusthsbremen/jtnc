package de.hsbremen.tc.tnc.im.adapter.imv;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.ietf.nea.pa.serialize.reader.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.PaWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateFactory;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImAdapter;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapter;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapterIetfFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImvEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImvSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImvSession;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.m.serialize.ImReader;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

public class ImvAdapterIetf extends ImAdapter implements IMV{

	private static final Logger LOGGER = LoggerFactory.getLogger(ImvAdapterIetf.class);
	
	private final ImParameter parameter;
	
	private final TncsAdapterFactory tncsFactory;
	private final ImvConnectionAdapterFactory connectionFactory;
	private final ImSessionFactory<ImvSession> sessionFactory;
	private final Map<IMVConnection, ImvSession> sessions;

	private final ImEvaluatorFactory evaluatorFactory;
	private ImEvaluatorManager evaluatorManager;
	
	private TncsAdapter tncs;
	
	
	public ImvAdapterIetf(){
		// FIXME this is only a default constructor and should only be used for testing purpose.
		this(new ImParameter(), new TncsAdapterIetfFactory(),
				new DefaultImvSessionFactory(),
				DefaultImvEvaluatorFactory.getInstance(),
				new ImvConnectionAdapterFactoryIetf(PaWriterFactory.createProductionDefault()),
				PaReaderFactory.createProductionDefault());
	}
	
	public ImvAdapterIetf(ImParameter parameter, TncsAdapterFactory tncsFactory, ImSessionFactory<ImvSession> sessionFactory, ImEvaluatorFactory evaluatorFactory, ImvConnectionAdapterFactory connectionFactory, ImReader<? extends ImMessageContainer> imReader){
		super(imReader);
		
		this.parameter = parameter;
		
		this.tncsFactory = tncsFactory;
		this.connectionFactory = connectionFactory;
		this.evaluatorFactory = evaluatorFactory;
		this.sessionFactory = sessionFactory;
		
		this.sessions = new HashMap<IMVConnection, ImvSession>();
	}
	
	@Override
	public void initialize(TNCS tncs) throws TNCException {
		if(this.tncs == null){
			this.tncs = this.tncsFactory.createTncsAdapter(this,tncs);
			this.evaluatorManager = this.evaluatorFactory.getEvaluators(this.tncs, this.parameter);
			try{
				this.tncs.reportMessageTypes(this.evaluatorManager.getSupportedMessageTypes());
			}catch(TncException e){
				throw new TNCException(e.getMessage(),e.getResultCode().result());
			}
			try{
				Object o = tncs.getAttribute(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_PREFERRED_LANGUAGE.id());
				if(o instanceof String){
					String preferredLanguage = (String)o;
					this.parameter.setPreferredLanguage(preferredLanguage);
				}
			}catch (TNCException | UnsupportedOperationException e){
				LOGGER.info("Preferred language attribute was not accessible, using default language: " + this.parameter.getPreferredLanguage(),e);
			}
			
		}else{
			throw new TNCException("IMV already initialized by " + this.tncs.toString() + ".", TNCException.TNC_RESULT_ALREADY_INITIALIZED);
		}
	}

	@Override
	public void terminate() throws TNCException {
		checkInitialization();
		
		for (ImvSession session : this.sessions.values()) {
			session.terminate();
		}
		this.sessions.clear();
		
		this.evaluatorManager.terminate();
		
		this.parameter.setPrimaryId(HSBConstants.HSB_IM_ID_UNKNOWN);
		this.parameter.setSupportedMessageTypes(new HashSet<SupportedMessageType>());
		
		this.tncs = null;
	}

	@Override
	public void notifyConnectionChange(IMVConnection c, long newState)
			throws TNCException {
		checkInitialization();
		try{
			this.findSessionByConnection(c).setConnectionState(DefaultTncConnectionStateFactory.getInstance().fromState(newState));
		}catch(TncException e){
			throw new TNCException(e.getMessage(),e.getResultCode().result());
		}
	}

	@Override
	public void receiveMessage(IMVConnection c, long messageType, byte[] message)
			throws TNCException {
		checkInitialization();
		
		if(message != null && message.length > 0){
			try{
				ImObjectComponent component = this.receiveMessage(ImComponentFactory.createLegacyRawComponent(messageType, message));
				this.findSessionByConnection(c).handleMessage(component);
			}catch(TncException e){
				throw new TNCException(e.getMessage(),e.getResultCode().result());
			}
		}
	}

	@Override
	public void batchEnding(IMVConnection c) throws TNCException {
		checkInitialization();
		try{
			this.findSessionByConnection(c).triggerMessage(ImMessageTriggerEnum.BATCH_ENDING);
		}catch(TncException e){
			throw new TNCException(e.getMessage(),e.getResultCode().result());
		}
	}

	@Override
	public void solicitRecommendation(IMVConnection c) throws TNCException {
		checkInitialization();
		try{
			this.findSessionByConnection(c).solicitRecommendation();
		}catch(TncException e){
			throw new TNCException(e.getMessage(),e.getResultCode().result());
		}
		
	}
	
//	@Override
//	public Object getAttribute(long attributeID) throws TNCException {
//
////		if(attributeID == TncServerAttributeTypeEnum.TNC_ATTRIBUTEID_IMV_SPTS_TNCS1){
////			return (this.parameter.hasTncsFirstSupport()) ? Boolean.TRUE : Boolean.FALSE;
////		}
//		
//		throw new TNCException("The attribute with ID " + attributeID + " is unknown.", TNCException.TNC_RESULT_INVALID_PARAMETER);
//		
//	}
//
//	@Override
//	public void setAttribute(long attributeID, Object attributeValue)
//			throws TNCException {
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
//		
//	}
	
	protected ImvSession findSessionByConnection(IMVConnection connection){
		if(this.sessions.containsKey(connection)){
			return this.sessions.get(connection);
		}
		
		ImvSession newSession = this.sessionFactory.createSession(this.connectionFactory.createConnectionAdapter(connection),evaluatorManager);
		this.sessions.put(connection, newSession);
		
		return newSession;
	}
	
	protected void checkInitialization() throws TNCException{
		if (this.tncs == null){
			throw new TNCException("IMV is not initialized.",TNCException.TNC_RESULT_NOT_INITIALIZED);
		}
	}

	protected void setPrimaryId(long id){
		this.parameter.setPrimaryId(id);
	}
}
