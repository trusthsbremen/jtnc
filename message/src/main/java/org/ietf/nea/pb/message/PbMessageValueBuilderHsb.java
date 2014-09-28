package org.ietf.nea.pb.message;


public class PbMessageValueBuilderHsb {
	
	
	public static PbMessageValueUnknown createUnknownValue(final boolean noSkip, final byte[] message){
		
		if(message == null){
			throw new NullPointerException("Result cannot be null.");
		}
		
		return new PbMessageValueUnknown(noSkip, message);
	}
	
}
