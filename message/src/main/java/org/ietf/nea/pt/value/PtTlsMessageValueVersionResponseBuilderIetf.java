package org.ietf.nea.pt.value;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.validate.rules.MessageVersion;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.rules.VersionLimits;

import de.hsbremen.tc.tnc.IETFConstants;

public class PtTlsMessageValueVersionResponseBuilderIetf implements PtTlsMessageValueVersionResponseBuilder{
	
	private final short PREFERED_VERSION = IETFConstants.IETF_RFC6876_VERSION_NUMBER;
	
	private long length;
    private short version;
    
    public PtTlsMessageValueVersionResponseBuilderIetf(){
    	this.length = PtTlsMessageTlvFixedLengthEnum.VER_RES.length();
    	this.version = PREFERED_VERSION;
    }

	@Override
	public PtTlsMessageValueVersionResponseBuilder setVersion(short version)
			throws RuleException {
		
		VersionLimits.check(version);
		MessageVersion.check(version, PREFERED_VERSION);
		
		this.version = version;
		return this;
	}

	@Override
	public PtTlsMessageValueVersionResponse toValue() {

		return new PtTlsMessageValueVersionResponse(length, version);
	}

	@Override
	public PtTlsMessageValueVersionResponseBuilder clear() {

		return new PtTlsMessageValueVersionResponseBuilderIetf();
	}

}
