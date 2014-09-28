package org.ietf.nea.pb.message;


public class PbMessageValueBuilderHsb {
	
	
	public static PbMessageValueUnknown createUnknownValue(final boolean ommittable, final byte[] message){
		
		if(message == null){
			throw new NullPointerException("Result cannot be null.");
		}
		
		return new PbMessageValueUnknown(ommittable, message);
	}
	
}
