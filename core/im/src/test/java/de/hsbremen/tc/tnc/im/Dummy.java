package de.hsbremen.tc.tnc.im;

import java.util.Arrays;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImRawComponent;
import de.hsbremen.tc.tnc.im.adapter.data.enums.PaComponentTypeEnum;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.report.enums.HandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;

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
				HandshakeRetryReasonEnum reasonE = HandshakeRetryReasonEnum.fromId(reason);
				if(reasonE == null || reasonE.toString().contains("IMV")){
				 throw new org.trustedcomputinggroup.tnc.ifimc.TNCException("Invalid reason code for IMCConnection and IMC: " + ((reasonE == null)? "null" : reasonE.toString()), org.trustedcomputinggroup.tnc.ifimc.TNCException.TNC_RESULT_INVALID_PARAMETER);
				}
				System.out.println("Handshake retry requested:" + HandshakeRetryReasonEnum.fromId(reason).toString().toString());
			}
		};
	}
	
	public static final ImRawComponent getRawComponentForImv(){
		
		long collectorId = 109;
		byte[] message = new byte[] {1, 0, 0, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 35, 16, 51, 46, 56, 46, 48, 45, 51, 51, 45, 103, 101, 110, 101, 114, 105, 99, 0, 4, 105, 54,
				56, 54, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 60, 0, 0, 0, 0, 0, 35, 52, 56, 45, 85, 98, 117, 110, 116, 117, 32, 83, 77, 80, 32, 87, 101, 100, 32, 79, 99, 116, 32, 50, 51, 32,
				49, 55, 58, 50, 54, 58, 51, 52, 32, 85, 84, 67, 32, 50, 48, 49, 51, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 28, 0, 0, 0, 3, 0, 0, 0, 8, 0, 0, 0, 0, 0, 33, 0, 0};

		
		return ImComponentFactory.createRawComponent((byte)0, IETFConstants.IETF_PEN_VENDORID, PaComponentTypeEnum.IETF_PA_OPERATING_SYSTEM.id(),collectorId, TNCConstants.TNC_IMVID_ANY, message);
		
	}
	
	public static final ImRawComponent getRawComponentForImc(){
		
		long validatorId = 109;
		byte[] message = new byte[] {1, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 1,
                0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 4};
		
		return ImComponentFactory.createRawComponent((byte)0, IETFConstants.IETF_PEN_VENDORID, PaComponentTypeEnum.IETF_PA_OPERATING_SYSTEM.id(), TNCConstants.TNC_IMCID_ANY, validatorId, message);
		
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
				HandshakeRetryReasonEnum reasonE = HandshakeRetryReasonEnum.fromId(reason);
				if(reasonE == null || reasonE.toString().contains("IMC")){
				 throw new org.trustedcomputinggroup.tnc.ifimv.TNCException("Invalid reason code for IMVConnection and IMV: " + ((reasonE == null)? "null" : reasonE.toString()), org.trustedcomputinggroup.tnc.ifimv.TNCException.TNC_RESULT_INVALID_PARAMETER);
				}
				System.out.println("Handshake retry requested:" + HandshakeRetryReasonEnum.fromId(reason).toString().toString());
			}
			
			@Override
			public void provideRecommendation(long recommendation, long evaluation)
					throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
				System.out.println("Recommendation provided: " + ImvActionRecommendationEnum.fromId(recommendation).toString() + ImvEvaluationResultEnum.fromId(evaluation).toString());
				
			}
			
			@Override
			public Object getAttribute(long attributeID) throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
				System.out.println("getAttribute() called with ID " + attributeID);
				return null;
			}
		};
	}
	
}
