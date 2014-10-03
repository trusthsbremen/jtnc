package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;

public class PbMessageValueUnknownBuilderHsb implements
		PbMessageValueUnknownBuilder {
	
	private long length;
	private byte[] message; //ImMessage as byte[]
	private boolean ommittable;
	
	
	public PbMessageValueUnknownBuilderHsb(){
		this.length = 0;
		this.message = new byte[0];
		this.ommittable = Boolean.FALSE;
	}
	
	@Override
    public PbMessageValueUnknownBuilder setOmmittable(boolean ommittable){
    	
		this.ommittable = ommittable;
		
		return this;
	}
	
	@Override
	public PbMessageValueUnknownBuilder setMessage(byte[] message) throws RuleException {
		// no checks necessary because experimental
		if(message != null){
			this.message = message;
			this.length = message.length;
		}

		return this;
	}
	
	@Override
	public PbMessageValueUnknown toValue(){

		return new PbMessageValueUnknown(this.ommittable, this.length, this.message);
	}

	@Override
	public PbMessageValueUnknownBuilder clear() {
		
		return new PbMessageValueUnknownBuilderHsb();
	}


}
