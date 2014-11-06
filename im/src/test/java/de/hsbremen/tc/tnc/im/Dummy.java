package de.hsbremen.tc.tnc.im;

import java.util.Arrays;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.adapter.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImRawComponent;
import de.hsbremen.tc.tnc.im.evaluate.enums.ImTypeEnum;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;
import de.hsbremen.tc.tnc.im.module.SupportedMessageTypeFactory;

public class Dummy extends AbstractDummy{
	
	public static final TNCC getTncc(){
		return new TNCC() {
			
			
			
			@Override
			public void requestHandshakeRetry(IMC imc, long reason) throws TNCException {
				System.out.println("Handshake retry requested.");
				
			}
			
			@Override
			public void reportMessageTypes(IMC imc, long[] supportedTypes)
					throws TNCException {
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
					throws TNCException {
				System.out.println("Message from IMC received:");
				System.out.println("Message type:" + SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(messageType).toString());
				System.out.println(Arrays.toString(message));
				
			}
			
			@Override
			public void requestHandshakeRetry(long reason) throws TNCException {
				ImHandshakeRetryReasonEnum reasonE = ImHandshakeRetryReasonEnum.fromCode(reason);
				if(reasonE == null || reasonE.toString().contains("IMV")){
				 throw new TNCException("Invalid reason code for IMCConnection and IMC: " + ((reasonE == null)? "null" : reasonE.toString()), TNCException.TNC_RESULT_INVALID_PARAMETER);
				}
				System.out.println("Handshake retry requested:" + ImHandshakeRetryReasonEnum.fromCode(reason).toString().toString());
			}
		};
	}
	
	public static final ImRawComponent getRawComponent(){
		
		long validatorId = 109;
		byte[] message = new byte[] {1, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 1,
                0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 4};
		
		return ImComponentFactory.createRawComponent((byte)0, IETFConstants.IETF_PEN_VENDORID, ImTypeEnum.IETF_PA_OPERATING_SYSTEM.type(), TNCConstants.TNC_IMCID_ANY, validatorId, message);
		
	}
	
}
