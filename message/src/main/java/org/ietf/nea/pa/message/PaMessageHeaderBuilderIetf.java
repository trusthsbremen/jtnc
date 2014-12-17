package org.ietf.nea.pa.message;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.validate.rules.MessageVersion;

public class PaMessageHeaderBuilderIetf implements PaMessageHeaderBuilder{

	private static final byte SUPPORTED_VERSION = 1;
	
	private short version;
	private long identifier;

	public PaMessageHeaderBuilderIetf(){
		this.version = SUPPORTED_VERSION;
		this.identifier = 0L;

	}
	
	@Override
	public PaMessageHeaderBuilder setVersion(short version)
			throws RuleException {
		
		MessageVersion.check(version, SUPPORTED_VERSION);
		this.version = version;
		
		return this;
	}

	@Override
	public PaMessageHeaderBuilder setIdentifier(long identifier)
			throws RuleException {
		
		// nothing to check here
		this.identifier = identifier;

		return this;		
	}
	
	@Override
	public PaMessageHeader toObject() {
		
		return new PaMessageHeader(this.version, this.identifier);
	}
	
	@Override
	public PaMessageHeaderBuilder newInstance() {
		return new PaMessageHeaderBuilderIetf();
	}

}
