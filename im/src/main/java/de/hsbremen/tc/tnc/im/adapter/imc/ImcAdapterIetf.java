package de.hsbremen.tc.tnc.im.adapter.imc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.ietf.nea.pa.serialize.reader.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.adapter.ImConnectionStateEnum;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapter;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapterIetfFactory;
import de.hsbremen.tc.tnc.im.evaluate.DefaultImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;
import de.hsbremen.tc.tnc.im.session.DefaultImcSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImSession;
import de.hsbremen.tc.tnc.im.session.ImcSessionFactory;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.m.message.ImMessage;
import de.hsbremen.tc.tnc.m.serialize.ImReader;

public class ImcAdapterIetf extends ImcAdapter{

	private final ImParameter parameter;
	
	private final ImcConnectionAdapterFactory connectionFactory;
	
	private final ImcSessionFactory sessionFactory;
	private final Map<IMCConnection, ImSession> sessions;

	private final ImEvaluatorFactory evaluatorFactory;
	private Map<Long, ImEvaluator> evaluators;
	
	private TnccAdapter tncc;
	
	
	public ImcAdapterIetf(){
		// FIXME this is only a default constructor and should only be used for testing purpose.
		this(new DefaultImcSessionFactory(),
				new DefaultImEvaluatorFactory(),
				new ImcConnectionAdapterFactoryIetf(PaWriterFactory.createProductionDefault()),
				PaReaderFactory.createProductionDefault());
	}
	
	public ImcAdapterIetf(ImcSessionFactory sessionFactory, ImEvaluatorFactory evaluatorFactory, ImcConnectionAdapterFactory connectionFactory, ImReader<? extends ImMessage> imReader){
		super(imReader);
		
		this.parameter = new ImParameter();
		
		this.connectionFactory = connectionFactory;
		this.evaluatorFactory = evaluatorFactory;
		this.sessionFactory = sessionFactory;
		
		this.sessions = new HashMap<IMCConnection, ImSession>();
	}
	
	@Override
	public void initialize(TNCC tncc) throws TNCException {
		if(this.tncc == null){
			this.tncc = (TnccAdapterIetfFactory.createTnccAdapter(this,tncc));
			this.tncc.reportMessageTypes(this.evaluatorFactory.getSupportedMessageTypes());
		}else{
			throw new TNCException("IMC already initialized by " + this.tncc.toString() + ".", TNCException.TNC_RESULT_ALREADY_INITIALIZED);
		}
	}

	@Override
	public void terminate() throws TNCException {
		checkInitialization();
		
		for (ImSession session : this.sessions.values()) {
			session.terminate();
		}
		this.sessions.clear();
		
		for (ImEvaluator evaluator : this.evaluators.values()) {
			evaluator.terminate();
		}
		this.evaluators.clear();
		
		this.parameter.setPrimaryId(ImParameter.UNKNOWN_PRIMARY_ID);
		this.parameter.setSupportedMessageTypes(new HashSet<SupportedMessageType>());
		
		this.tncc = null;
	}

	@Override
	public void notifyConnectionChange(IMCConnection c, long newState)
			throws TNCException {
		checkInitialization();
		
		this.findSessionByConnection(c).setConnectionState(ImConnectionStateEnum.fromState(newState));
	}

	@Override
	public void beginHandshake(IMCConnection c) throws TNCException {
		checkInitialization();
		this.findSessionByConnection(c).triggerMessage(ImMessageTriggerEnum.BEGIN_HANDSHAKE);
		
	}

	@Override
	public void receiveMessage(IMCConnection c, long messageType, byte[] message)
			throws TNCException {
		checkInitialization();
		
		if(message != null && message.length > 0){
			this.findSessionByConnection(c).handleMessage(this.receiveMessage(ImComponentFactory.createLegacyRawComponent(messageType, message)));
		}
		
	}

	@Override
	public void batchEnding(IMCConnection c) throws TNCException {
		checkInitialization();
		this.findSessionByConnection(c).triggerMessage(ImMessageTriggerEnum.BATCH_ENDING);
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
		if(attributeID == AttributeSupport.TNC_ATTRIBUTEID_PRIMARY_IMC_ID){
			
			if(attributeValue instanceof Long){
				if(this.parameter.getPrimaryId() <= 0){
					this.parameter.setPrimaryId((Long)attributeValue);
					this.initializeEvaluators(this.parameter.getPrimaryId());
				}else{
					throw new TNCException("Primary ID already set.", TNCException.TNC_RESULT_OTHER);
				}
			}else{
				throw new TNCException("Unexpected attribute type. Attribute with attribute ID " + attributeID + " cannot be of type " + attributeValue.getClass().getSimpleName() + ".", TNCException.TNC_RESULT_INVALID_PARAMETER);
			}
			
		}else{
			throw new TNCException("The attribute with ID " + attributeID + " is unknown.", TNCException.TNC_RESULT_INVALID_PARAMETER);
		}
		
	}
	
	private ImSession findSessionByConnection(IMCConnection connection){
		if(this.sessions.containsKey(connection)){
			return this.sessions.get(connection);
		}
		
		ImSession newSession = this.sessionFactory.createSession(this.connectionFactory.createConnectionAdapter(connection),evaluators);
		this.sessions.put(connection, newSession);
		
		return newSession;
	}
	
	private void initializeEvaluators(long primaryKey){
		this.evaluators = this.evaluatorFactory.getUnits(this.tncc, this.parameter);
	}
	
	protected void checkInitialization() throws TNCException{
		if (this.tncc == null || this.parameter.getPrimaryId() < 0){
			throw new TNCException("IMC is not initialized.",TNCException.TNC_RESULT_NOT_INITIALIZED);
		}
	}
}
