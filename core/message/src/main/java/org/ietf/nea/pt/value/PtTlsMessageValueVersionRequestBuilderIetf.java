/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package org.ietf.nea.pt.value;

import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.rules.VersionLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

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
	public PtTlsMessageValueVersionRequest toObject() {

		return new PtTlsMessageValueVersionRequest(length, preferredVersion, maxVersion, minVersion);
	}

	@Override
	public PtTlsMessageValueVersionRequestBuilder newInstance() {

		return new PtTlsMessageValueVersionRequestBuilderIetf();
	}

}
