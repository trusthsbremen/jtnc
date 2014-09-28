package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class PbMessageValueExperimentalBuilderIetf implements
		PbMessageValueExperimentalBuilder {
	
	private String message; //ImMessage as byte[]
	
	public PbMessageValueExperimentalBuilderIetf(){
		this.message = "";
	}
	
	@Override
	public PbMessageValueExperimentalBuilder setMessage(String message) throws ValidationException {
		// no checks necessary because experimental
		if(message != null){
			this.message = message;
		}
		
		return this;
	}
	
	@Override
	public PbMessageValueExperimental toValue() throws ValidationException {

		return new PbMessageValueExperimental(this.message);
	}

	@Override
	public PbMessageValueExperimentalBuilder clear() {
		
		return new PbMessageValueExperimentalBuilderIetf();
	}


}
