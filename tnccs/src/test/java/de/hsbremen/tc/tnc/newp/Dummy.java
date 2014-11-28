package de.hsbremen.tc.tnc.newp;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.PbMessageValue;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.AbstractDummy;
import de.hsbremen.tc.tnc.attribute.DefaultTncAttributeTypeFactory;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateFactory;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;
import de.hsbremen.tc.tnc.session.context.SessionConnectionContext;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public class Dummy extends AbstractDummy{
	
	public static final TNCC getTncc(){
		return new TNCC() {
			
			
			
			@Override
			public void requestHandshakeRetry(IMC imc, long reason) throws org.trustedcomputinggroup.tnc.ifimc.TNCException {
				System.out.println("Handshake retry requested.");
				
			}
			
			@Override
			public void reportMessageTypes(IMC imc, long[] supportedTypes)
					throws org.trustedcomputinggroup.tnc.ifimc.TNCException {
				System.out.println("Messages reported from:" + imc.toString());
				List<SupportedMessageType> type = SupportedMessageTypeFactory.createSupportedMessageTypesLegacy(supportedTypes);
				for (SupportedMessageType supportedMessageType : type) {
					System.out.println(supportedMessageType.toString());
				}
			}
		};
	}
	
	public static final IMCConnection getIMCConnection(){
		
		return new IMCConnection() {
			
			@Override
			public void sendMessage(long messageType, byte[] message)
					throws org.trustedcomputinggroup.tnc.ifimc.TNCException {
				System.out.println("Message from IMC received:");
				System.out.println("Message type:" + SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(messageType).toString());
				System.out.println(Arrays.toString(message));
				
			}
			
			@Override
			public void requestHandshakeRetry(long reason) throws org.trustedcomputinggroup.tnc.ifimc.TNCException {
				ImHandshakeRetryReasonEnum reasonE = ImHandshakeRetryReasonEnum.fromCode(reason);
				if(reasonE == null || reasonE.toString().contains("IMV")){
				 throw new org.trustedcomputinggroup.tnc.ifimc.TNCException("Invalid reason code for IMCConnection and IMC: " + ((reasonE == null)? "null" : reasonE.toString()), org.trustedcomputinggroup.tnc.ifimc.TNCException.TNC_RESULT_INVALID_PARAMETER);
				}
				System.out.println("Handshake retry requested:" + ImHandshakeRetryReasonEnum.fromCode(reason).toString().toString());
			}
		};
	}
	
	public static final TNCS getTncs(){
		return new TNCS() {
			
			@Override
			public void setAttribute(long attributeID, Object attributeValue)
					throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
				System.out.println("setAttribute() called with ID " + attributeID);
				
			}
			
			@Override
			public void requestHandshakeRetry(IMV imv, long reason) throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
				System.out.println("Handshake retry requested.");
				
			}
			
			@Override
			public void reportMessageTypes(IMV imv, long[] supportedTypes)
					throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
				System.out.println("Messages reported from:" + imv.toString());
				List<SupportedMessageType> type = SupportedMessageTypeFactory.createSupportedMessageTypesLegacy(supportedTypes);
				for (SupportedMessageType supportedMessageType : type) {
					System.out.println(supportedMessageType.toString());
				}
			}
			
			@Override
			public Object getAttribute(long attributeID)
					throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
				System.out.println("getAttribute() called with ID " + attributeID);
				return null;
			}
		};
	}

	public static IMVConnection getIMVConnection() {
		// TODO Auto-generated method stub
		return new IMVConnection() {
			
			@Override
			public void setAttribute(long attributeID, Object attributeValue)
					throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
				System.out.println("setAttribute() called with ID " + attributeID);
				
			}
			
			@Override
			public void sendMessage(long messageType, byte[] message)
					throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
				System.out.println("Message from IMV received:");
				System.out.println("Message type:" + SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(messageType).toString());
				System.out.println(Arrays.toString(message));
				
			}
			
			@Override
			public void requestHandshakeRetry(long reason) throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
				ImHandshakeRetryReasonEnum reasonE = ImHandshakeRetryReasonEnum.fromCode(reason);
				if(reasonE == null || reasonE.toString().contains("IMC")){
				 throw new org.trustedcomputinggroup.tnc.ifimv.TNCException("Invalid reason code for IMVConnection and IMV: " + ((reasonE == null)? "null" : reasonE.toString()), org.trustedcomputinggroup.tnc.ifimv.TNCException.TNC_RESULT_INVALID_PARAMETER);
				}
				System.out.println("Handshake retry requested:" + ImHandshakeRetryReasonEnum.fromCode(reason).toString().toString());
			}
			
			@Override
			public void provideRecommendation(long recommendation, long evaluation)
					throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
				System.out.println("Recommendation provided: " + ImvActionRecommendationEnum.fromNumber(recommendation).toString() + ImvEvaluationResultEnum.fromResult(evaluation).toString());
				
			}
			
			@Override
			public Object getAttribute(long attributeID) throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
				System.out.println("getAttribute() called with ID " + attributeID);
				return null;
			}
		};
	}
	
	public static IMC getIMC(){
		return new IMC() {

			@Override
			public void initialize(TNCC tncc) throws TNCException {
				if(tncc instanceof AttributeSupport){
					for (TncAttributeType type : DefaultTncAttributeTypeFactory.getInstance().getClientTypes()) {
						try{
							System.out.println("Attribute with id " + type.id() +": " + ((AttributeSupport) tncc).getAttribute(type.id()));
						}catch(TNCException e){
							System.out.println("Attribute with id " + type.id() +" not supported. " + e.getMessage());
						}
					}
				}
			}

			@Override
			public void terminate() throws TNCException {
				System.out.println("terminat() called.");
				
			}

			@Override
			public void notifyConnectionChange(IMCConnection c, long newState)
					throws TNCException {
				System.out.println("notifyConnectionChange() called with state: " + DefaultTncConnectionStateFactory.getInstance().fromState(newState).toString());
				
			}

			@Override
			public void beginHandshake(IMCConnection c) throws TNCException {
				System.out.println("beginHandshake() called. " );
				System.out.println("Createing delay of 900.");
				long start = System.currentTimeMillis();
				while (System.currentTimeMillis() - start < 900){
					
				}
				System.out.println("Delay of 800 ended.");
				
			}

			@Override
			public void receiveMessage(IMCConnection c, long messageType,
					byte[] message) throws TNCException {
				System.out.println("receiveMessage() called for type " + SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(messageType).toString() + " and message length " + message.length + ".");
			}

			@Override
			public void batchEnding(IMCConnection c) throws TNCException {
				System.out.println("batchEnding() called.");
				
			}
			
		};
	}
	
	public static IMC getIMCwithMessageSupport(final Set<SupportedMessageType> types){
		return new IMC() {

			@Override
			public void initialize(TNCC tncc) throws TNCException {
				if(tncc instanceof AttributeSupport){
					for (TncAttributeType type : DefaultTncAttributeTypeFactory.getInstance().getClientTypes()) {
						try{
							System.out.println("Attribute with id " + type.id() +": " + ((AttributeSupport) tncc).getAttribute(type.id()));
						}catch(TNCException e){
							System.out.println("Attribute with id " + type.id() +" not supported. " + e.getMessage());
						}
					}
				}
				
				tncc.reportMessageTypes(this, SupportedMessageTypeFactory.createSupportedMessageTypeArrayLegacy(types));
			}

			@Override
			public void terminate() throws TNCException {
				System.out.println("terminat() called.");
				
			}

			@Override
			public void notifyConnectionChange(IMCConnection c, long newState)
					throws TNCException {
				System.out.println("notifyConnectionChange() called with state: " + DefaultTncConnectionStateFactory.getInstance().fromState(newState).toString());
				
			}

			@Override
			public void beginHandshake(IMCConnection c) throws TNCException {
				System.out.println("beginHandshake() called. " );
				System.out.println("Createing delay of 900.");
				long start = System.currentTimeMillis();
				while (System.currentTimeMillis() - start < 900){
					
				}
				System.out.println("Delay of 800 ended.");
				
			}

			@Override
			public void receiveMessage(IMCConnection c, long messageType,
					byte[] message) throws TNCException {
				System.out.println("receiveMessage() called for type " + SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(messageType).toString() + " and message length " + message.length + ".");
			}

			@Override
			public void batchEnding(IMCConnection c) throws TNCException {
				System.out.println("batchEnding() called.");
				
			}
			
		};
	}
	
	public static SessionConnectionContext getConnectionContext(){
		return new SessionConnectionContext() {
			
			@Override
			public void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason)
					throws TncException {
				System.out.println("requestHandshake() called with reason:" + reason.toString());
				
			}
			
			@Override
			public Object getAttribute(TncAttributeType type) throws TncException {
				System.out.println("getAttribute() with id " + type.id() +" called.");
				throw new UnsupportedOperationException("Operation is in dummy not supported.");
			}
			
			@Override
			public void addMessage(TnccsMessage message) throws TncException {
				System.out.println("addMessage() called with type: " + message.getClass().getCanonicalName());
				
			}
		};
	}
	
	public static PbMessageValue getPaAssessmentResult() throws ValidationException{
		return PbMessageFactoryIetf.createIm(new PbMessageImFlagsEnum[0], 0, 1, (int)TNCConstants.TNC_IMCID_ANY, 15, new byte[] {1, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 1,
	            0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 4}).getValue();
	}
	
	public static GlobalHandshakeRetryListener getRetryListener(){
		return new GlobalHandshakeRetryListener() {
			
			@Override
			public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason)
					throws TncException {
				System.out.println("Retry requested with reason: "+ reason.toString());
				
			}
		};
	}

	public static TnccsSessionContext getSessionContext() {
		return new TnccsSessionContext() {
			
			@Override
			public void setTnccsAttribute(TncAttributeType type, Object value) {
				System.out.println("setAttribute() with id " + type.id() +" and object " +value.toString()+ " called.");
				throw new UnsupportedOperationException("Operation is in dummy not supported.");
			}
			
			@Override
			public Object getTnccsAttribute(TncAttributeType type) {
				System.out.println("getAttribute() with id " + type.id() +" called.");
				throw new UnsupportedOperationException("Operation is in dummy not supported.");
			}
			
		};
	}
}
