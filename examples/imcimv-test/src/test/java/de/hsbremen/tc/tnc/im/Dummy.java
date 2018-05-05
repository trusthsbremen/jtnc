package de.hsbremen.tc.tnc.im;

import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

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
	
}
