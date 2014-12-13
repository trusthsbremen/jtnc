package org.ietf.nea.pt.value;

import org.ietf.nea.exception.RuleException;

public class PtTlsMessageValueSaslAuthenticationDataBuilderIetf implements
		PtTlsMessageValueSaslAuthenticationDataBuilder {
	
	private long length;
	private byte[] authenticationData; // variable string
	
	public PtTlsMessageValueSaslAuthenticationDataBuilderIetf(){
		this.length = 0;
		this.authenticationData = null;
	}
	
	@Override
	public PtTlsMessageValueSaslAuthenticationDataBuilder setAuthenticationData(byte[] data) throws RuleException {
		// no checks necessary because opaque
		if(data != null){
			this.authenticationData = data;
			this.length = data.length;
		}
		
		return this;
	}
	
	@Override
	public PtTlsMessageValueSaslAuthenticationData toValue(){

		if(this.authenticationData == null){
			throw new IllegalStateException("The SASL authentication data has to be set.");
		}
		
		return new PtTlsMessageValueSaslAuthenticationData(this.length,this.authenticationData);
	}

	@Override
	public PtTlsMessageValueSaslAuthenticationDataBuilder clear() {
		
		return new PtTlsMessageValueSaslAuthenticationDataBuilderIetf();
	}


}
