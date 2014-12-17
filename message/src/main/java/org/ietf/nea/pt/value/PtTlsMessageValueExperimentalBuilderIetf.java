package org.ietf.nea.pt.value;

import org.ietf.nea.exception.RuleException;

public class PtTlsMessageValueExperimentalBuilderIetf implements
		PtTlsMessageValueExperimentalBuilder {
	
	private long length;
	private String message; // variable string
	
	public PtTlsMessageValueExperimentalBuilderIetf(){
		this.length = 0;
		this.message = "";
	}
	
	@Override
	public PtTlsMessageValueExperimentalBuilder setMessage(String message) throws RuleException {
		// no checks necessary because experimental
		if(message != null){
			this.message = message;
			this.length = message.getBytes().length;
		}
		
		return this;
	}
	
	@Override
	public PtTlsMessageValueExperimental toObject(){

		return new PtTlsMessageValueExperimental(this.length,this.message);
	}

	@Override
	public PtTlsMessageValueExperimentalBuilder newInstance() {
		
		return new PtTlsMessageValueExperimentalBuilderIetf();
	}


}
