package org.ietf.nea.pt.value;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.rules.VersionLimits;

import de.hsbremen.tc.tnc.IETFConstants;

public class PtTlsMessageValueVersionRequestBuilderIetf implements PtTlsMessageValueVersionRequestBuilder{
	
	private final short PREFERRED_VERSION = IETFConstants.IETF_RFC6876_VERSION_NUMBER;
	
	private long length;
    private short preferredVersion;
    private short maxVersion;
    private short minVersion;
    
    public PtTlsMessageValueVersionRequestBuilderIetf(){
    	this.length = PtTlsMessageTlvFixedLengthEnum.VER_REQ.length();
    	this.preferredVersion = PREFERRED_VERSION;
    	this.maxVersion = PREFERRED_VERSION;
    	this.minVersion = PREFERRED_VERSION;
    }

	@Override
	public PtTlsMessageValueVersionRequestBuilder setPreferredVersion(short version)
			throws RuleException {
		
		VersionLimits.check(version);
		this.preferredVersion = version;
		return this;
	}

	@Override
	public PtTlsMessageValueVersionRequestBuilder setMaxVersion(short version)
			throws RuleException {
		
		VersionLimits.check(version);
		this.maxVersion = version;
		return this;
	}

	@Override
	public PtTlsMessageValueVersionRequestBuilder setMinVersion(short version)
			throws RuleException {
		
		VersionLimits.check(version);
		this.minVersion = version;
		return this;
	}

	@Override
	public PtTlsMessageValueVersionRequest toValue() {

		return new PtTlsMessageValueVersionRequest(length, preferredVersion, maxVersion, minVersion);
	}

	@Override
	public PtTlsMessageValueVersionRequestBuilder clear() {

		return new PtTlsMessageValueVersionRequestBuilderIetf();
	}

}
