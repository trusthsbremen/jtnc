package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public class PbMessageValueUnknownBuilderHsb implements
		PbMessageValueUnknownBuilder {
	
	private byte[] message; //ImMessage as byte[]
	private boolean noSkip;
	
	
	public PbMessageValueUnknownBuilderHsb(){
		this.message = new byte[0];
		this.noSkip = Boolean.FALSE;
	}
	
    public void setHasNoSkip(boolean noSkip){
    	this.noSkip = noSkip;
    }
	
	@Override
	public void setMessage(byte[] message) throws ValidationException {
		// no checks necessary because experimental
		if(message != null){
			this.message = message;
		}
	}
	
	@Override
	public TnccsMessageValue toValue() throws ValidationException {

		return new PbMessageValueUnknown(this.noSkip, this.message);
	}

	@Override
	public TnccsMessageValueBuilder clear() {
		
		return new PbMessageValueUnknownBuilderHsb();
	}


}
