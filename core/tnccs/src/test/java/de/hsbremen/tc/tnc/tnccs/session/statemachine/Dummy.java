package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchFactoryIetf;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;
import de.hsbremen.tc.tnc.tnccs.AbstractDummy;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImvHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception.StateMachineAccessException;

public class Dummy extends AbstractDummy{

	protected static Attributed getTransportAttributes() {
		return new Attributed() {
			
			private String tProtocol = TcgTProtocolBindingEnum.TLS1.label();
			private String tVersion = TcgTProtocolBindingEnum.TLS1.version();
			private Long maxMessageSize = new Long(512);
			private Long maxRoundtrips = new Long(HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN);
			
			@Override
			public Object getAttribute(TncAttributeType type) throws TncException {
				System.out.println("getAttribute() called with type " + type.toString() +".");
				if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFT_PROTOCOL)){
					return this.tProtocol;
				}
				
				if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFT_VERSION)){
					return this.tVersion;
				}
				
				if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_MESSAGE_SIZE)){
					return this.maxMessageSize;
				}
				
				if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_ROUND_TRIPS)){
					return this.maxRoundtrips;
				}
				
				throw new TncException("The attribute with ID " + type.id() + " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
			}

			@Override
			public void setAttribute(TncAttributeType type, Object value)
					throws TncException {
				System.out.println("setAttribute() called with type " + type.toString() +" and value "+ value.toString()+".");
				throw new UnsupportedOperationException("Operation not supported because there is no attribute to set.");
			}
			
			
		};
	}

	
	protected static PbBatch getClientDataBatchWithImMessage() throws ValidationException{
		
		
		PbMessageImFlagEnum[] imFlags = new PbMessageImFlagEnum[0];
		long subVendorId = IETFConstants.IETF_PEN_VENDORID;
		long subType = 1L;
		short collectorId = 1;
		short validatorId = (short)0xFFFF;
		byte[] message = "PWND".getBytes(Charset.forName("US-ASCII"));
		List<TnccsMessage> messages = new ArrayList<>();
		messages.add(PbMessageFactoryIetf.createIm(imFlags, subVendorId, subType, collectorId, validatorId, message));
		return PbBatchFactoryIetf.createClientData(messages);
	}
	
	protected static byte[] getBatchWithImMessageAsByte(){
		return new byte[]{2, -128, 0, 1, 0, 0, 0, 36, -128, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 28, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, -1, -1, 80, 87, 78, 68};
	}
	
	protected static byte[] getBatchWithImMessageAsByteShortenedFaulty(){
		return new byte[]{2, -128, 0, 1, 0, 0, 0, 36, -128, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 28, 0, 0, 0, 0, 0, 0, 0, 1, 0};
	}

	protected static StateMachine getStateMachine(){
		return new StateMachine() {
			
			boolean closed = true;
			
			@Override
			public TnccsBatch receiveBatch(TnccsBatchContainer newBatch)
					throws StateMachineAccessException {
				System.out.println("Batch container received.");
				if(closed){
					throw new StateMachineAccessException("Machine closed.");
				}
				return null;
			}
			
			@Override
			public TnccsBatch start(boolean selfInitiated)
					throws StateMachineAccessException {
				System.out.println("Start called with: " + selfInitiated);
				if(!closed){
					throw new StateMachineAccessException("Machine  already running.");
				}
				this.closed = false;
				
				return null;
			}
			
			@Override
			public List<TnccsBatch> retryHandshake(ImHandshakeRetryReasonEnum reason)
					throws TncException {
				System.out.println("Retry handshake called with " + reason.toString() +".");
				if(closed){
					throw new TncException("Machine closed.", TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY);
				}
				return new ArrayList<>();
			}
			
			@Override
			public boolean isClosed() {
				return closed;
			}
			
			@Override
			public void stop() {
				System.out.println("Stop called.");
				this.closed = true;
				
			}
			
			@Override
			public boolean canRetry() {
				return !this.closed;
			}

			@Override
			public TnccsBatch close() throws StateMachineAccessException {
				System.out.println("Close called.");
				try {
					return PbBatchFactoryIetf.createClientClose(new ArrayList<TnccsMessage>());
				} catch (ValidationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
	}

	protected static TnccsBatch getServerDataBatch() throws ValidationException{

		PbMessageImFlagEnum[] imFlags = new PbMessageImFlagEnum[0];
		long subVendorId = IETFConstants.IETF_PEN_VENDORID;
		long subType = 1L;
		short collectorId = (short)0xFFFF;
		short validatorId = 1;
		byte[] message = "PWND".getBytes(Charset.forName("US-ASCII"));
		List<TnccsMessage> messages = new ArrayList<>();
		messages.add(PbMessageFactoryIetf.createIm(imFlags, subVendorId, subType, collectorId, validatorId, message));
		messages.add(PbMessageFactoryIetf.createLanguagePreference(HSBConstants.HSB_DEFAULT_LANGUAGE));
		return PbBatchFactoryIetf.createServerData(messages);
	}
	
	protected static TnccsBatch getServerResultBatch() throws ValidationException{

		PbMessageImFlagEnum[] imFlags = new PbMessageImFlagEnum[0];
		long subVendorId = IETFConstants.IETF_PEN_VENDORID;
		long subType = 1L;
		short collectorId = (short)0xFFFF;
		short validatorId = 1;
		byte[] message = "PWND".getBytes(Charset.forName("US-ASCII"));
		List<TnccsMessage> messages = new ArrayList<>();
		messages.add(PbMessageFactoryIetf.createIm(imFlags, subVendorId, subType, collectorId, validatorId, message));
		messages.add(PbMessageFactoryIetf.createAccessRecommendation(PbMessageAccessRecommendationEnum.ALLOWED));
		messages.add(PbMessageFactoryIetf.createAssessmentResult(PbMessageAssessmentResultEnum.COMPLIANT));
		return PbBatchFactoryIetf.createResult(messages);
	}
	
	protected static TnccsBatch getServerCloseBatch() throws ValidationException{

		List<TnccsMessage> messages = new ArrayList<>();
		return PbBatchFactoryIetf.createServerClose(messages);
	}
	
	protected static ImcHandler getImcHandler() {
		return new ImcHandler() {
			
			boolean handShakeBegin = false;
			 
			@Override
			public void setConnectionState(TncConnectionState state) {
				if(state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE)){
					this.handShakeBegin = true;
				}
				if(state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE)){
					this.handShakeBegin = false;
				}
				System.out.println("setConnectionState() called with " + state.toString() +".");
			}
			
			@Override
			public List<TnccsMessage> requestMessages() {
				System.out.println("requestMessages() called.");
				return this.createMessageList();
			}
			
			@Override
			public List<TnccsMessage> forwardMessage(TnccsMessage value) {
				System.out.println("forwardMessages() called.");
				return this.createMessageList();
			}
			
			private List<TnccsMessage> createMessageList(){
				List<TnccsMessage> messages = new ArrayList<>();
				if(handShakeBegin){
					Random r = new Random();
					try {
						PbMessage m = PbMessageFactoryIetf.createIm(new PbMessageImFlagEnum[0],r.nextInt(10) , r.nextInt(4), (short)r.nextInt(7), (short)r.nextInt(7), new byte[0]);
						messages.add(m);
					} catch (ValidationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handShakeBegin = false;
				}
				return messages;
			}

			@Override
			public List<TnccsMessage> lastCall() {
				System.out.println("lastCall() called.");
				return new ArrayList<>();
			}

			@Override
			public void dumpMessage(TnccsMessage message) {
				System.out.println("dumpMessages() called.");
			}
			
		};
	}

	public static ImvHandler getImvHandler() {
		// TODO Auto-generated method stub
		return new ImvHandler() {
			
			boolean handShakeBegin = false;
			 
			@Override
			public void setConnectionState(TncConnectionState state) {
				if(state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE)){
					this.handShakeBegin = true;
				}
				if(state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE)){
					this.handShakeBegin = false;
				}
				System.out.println("setConnectionState() called with " + state.toString() +".");
			}
			
			@Override
			public List<TnccsMessage> requestMessages() {
				System.out.println("requestMessages() called.");
				return this.createMessageList();
			}
			
			@Override
			public List<TnccsMessage> forwardMessage(TnccsMessage value) {
				System.out.println("forwardMessages() called.");
				return this.createMessageList();
			}
			
			private List<TnccsMessage> createMessageList(){
				List<TnccsMessage> messages = new ArrayList<>();
				if(handShakeBegin){
					Random r = new Random();
					try {
						PbMessage m = PbMessageFactoryIetf.createIm(new PbMessageImFlagEnum[0],r.nextInt(10) , r.nextInt(4), (short)r.nextInt(7), (short)r.nextInt(7), new byte[0]);
						messages.add(m);
					} catch (ValidationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handShakeBegin = false;
				}
				return messages;
			}

			@Override
			public List<TnccsMessage> lastCall() {
				System.out.println("lastCall() called.");
				return new ArrayList<>();
			}
			
			@Override
			public void dumpMessage(TnccsMessage message) {
				System.out.println("dumpMessages() called.");
			}
			
			@Override
			public List<ImvRecommendationPair> solicitRecommendation() {
				System.out.println("solicitRecommendation() called.");
				List<ImvRecommendationPair> recommendationPairs = new ArrayList<>();
				recommendationPairs.add(ImvRecommendationPairFactory.getDefaultRecommendationPair());
				recommendationPairs.add(ImvRecommendationPairFactory.createRecommendationPair(ImvActionRecommendationEnum.TNC_IMV_ACTION_RECOMMENDATION_ALLOW, ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MINOR));
				recommendationPairs.add(ImvRecommendationPairFactory.createRecommendationPair(ImvActionRecommendationEnum.TNC_IMV_ACTION_RECOMMENDATION_ISOLATE, ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR));
				return recommendationPairs;
			}
		};
	}

	public static TnccsBatch getClientDataBatch() throws ValidationException{
		PbMessageImFlagEnum[] imFlags = new PbMessageImFlagEnum[0];
		long subVendorId = IETFConstants.IETF_PEN_VENDORID;
		long subType = 1L;
		short collectorId = 1;
		short validatorId = (short)0xFFFF;
		byte[] message = "PWND".getBytes(Charset.forName("US-ASCII"));
		List<TnccsMessage> messages = new ArrayList<>();
		messages.add(PbMessageFactoryIetf.createIm(imFlags, subVendorId, subType, collectorId, validatorId, message));
		messages.add(PbMessageFactoryIetf.createLanguagePreference(HSBConstants.HSB_DEFAULT_LANGUAGE));
		return PbBatchFactoryIetf.createClientData(messages);
	}
	
	public static TnccsBatch getEmptyClientDataBatch() throws ValidationException{
		List<TnccsMessage> messages = new ArrayList<>();
		return PbBatchFactoryIetf.createClientData(messages);
	}

	protected static TnccsBatch getClientCloseBatch() throws ValidationException{

		List<TnccsMessage> messages = new ArrayList<>();
		return PbBatchFactoryIetf.createClientClose(messages);
	}
	
	
}
