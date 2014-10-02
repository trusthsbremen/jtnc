package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;

public class PbMessageValueExperimentalBuilderIetf implements
		PbMessageValueExperimentalBuilder {
	
	private long length;
	private String message; //ImMessage as byte[]
	
	public PbMessageValueExperimentalBuilderIetf(){
		this.length = 0;
		this.message = "";
	}
	
	@Override
	public PbMessageValueExperimentalBuilder setMessage(String message) throws RuleException {
		// no checks necessary because experimental
		if(message != null){
			this.message = message;
			this.length = message.getBytes().length;
		}
		
		return this;
	}
	
	@Override
	public PbMessageValueExperimental toValue() throws RuleException {

		return new PbMessageValueExperimental(this.length,this.message);
	}

	@Override
	public PbMessageValueExperimentalBuilder clear() {
		
		return new PbMessageValueExperimentalBuilderIetf();
	}


}
