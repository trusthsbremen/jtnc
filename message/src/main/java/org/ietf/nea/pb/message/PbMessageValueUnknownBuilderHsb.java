package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class PbMessageValueUnknownBuilderHsb implements
		PbMessageValueUnknownBuilder {
	
	private byte[] message; //ImMessage as byte[]
	private boolean ommittable;
	
	
	public PbMessageValueUnknownBuilderHsb(){
		this.message = new byte[0];
		this.ommittable = Boolean.FALSE;
	}
	
	@Override
    public PbMessageValueUnknownBuilder setOmmittable(boolean ommittable){
    	
		this.ommittable = ommittable;
		
		return this;
	}
	
	@Override
	public PbMessageValueUnknownBuilder setMessage(byte[] message) throws ValidationException {
		// no checks necessary because experimental
		if(message != null){
			this.message = message;
		}

		return this;
	}
	
	@Override
	public PbMessageValueUnknown toValue() throws ValidationException {

		return new PbMessageValueUnknown(this.ommittable, this.message);
	}

	@Override
	public PbMessageValueUnknownBuilder clear() {
		
		return new PbMessageValueUnknownBuilderHsb();
	}


}
