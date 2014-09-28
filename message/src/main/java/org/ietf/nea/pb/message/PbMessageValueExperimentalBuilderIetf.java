package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public class PbMessageValueExperimentalBuilderIetf implements
		PbMessageValueExperimentalBuilder {
	
	private String message; //ImMessage as byte[]
	
	public PbMessageValueExperimentalBuilderIetf(){
		this.message = "";
	}
	
	@Override
	public void setMessage(String message) throws ValidationException {
		// no checks necessary because experimental
		if(message != null){
			this.message = message;
		}
	}
	
	@Override
	public TnccsMessageValue toValue() throws ValidationException {

		return new PbMessageValueExperimental(this.message);
	}

	@Override
	public TnccsMessageValueBuilder clear() {
		
		return new PbMessageValueExperimentalBuilderIetf();
	}


}
