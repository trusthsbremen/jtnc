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

import org.ietf.nea.pa.validate.rules.MessageVersion;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.rules.VersionLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

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
	public PtTlsMessageValueVersionResponse toObject() {

		return new PtTlsMessageValueVersionResponse(length, version);
	}

	@Override
	public PtTlsMessageValueVersionResponseBuilder newInstance() {

		return new PtTlsMessageValueVersionResponseBuilderIetf();
	}

}
