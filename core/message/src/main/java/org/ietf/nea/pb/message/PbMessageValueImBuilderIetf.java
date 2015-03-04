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
package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.ImIdLimits;
import org.ietf.nea.pb.validate.rules.ImMessageTypeReservedAndLimits;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PbMessageValueImBuilderIetf implements PbMessageValueImBuilder{

	private PbMessageImFlagEnum[] imFlags; //  8 bit(s)
	   
    private long subVendorId;                                           // 24 bit(s)
    private long subType;                                               // 32 bit(s)
    private long collectorId;                                            // 16 bit(s)
    private long validatorId;                                            // 16 bit(s)
    private long length;
    
    private byte[] message; //ImMessage as byte[]

    public PbMessageValueImBuilderIetf(){
    	this.imFlags = new PbMessageImFlagEnum[0];
    	this.subVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.subType = 0;
    	this.collectorId = TNCConstants.TNC_IMCID_ANY;
    	this.validatorId = TNCConstants.TNC_IMVID_ANY;
    	this.length = PbMessageTlvFixedLengthEnum.IM_VALUE.length();
    	this.message = new byte[0];
    }
    
	@Override
	public PbMessageValueImBuilder setImFlags(byte imFlags) {
		
		if ((byte)(imFlags & 0x80)  == PbMessageImFlagEnum.EXCL.bit()) {
			this.imFlags = new PbMessageImFlagEnum[]{PbMessageImFlagEnum.EXCL};
		}
		
		return this;
	}

	@Override
	public PbMessageValueImBuilder setSubVendorId(long subVendorId) throws RuleException {
		
		ImMessageTypeReservedAndLimits.check(subVendorId);
		this.subVendorId = subVendorId;
		
		return this;
	}

	@Override
	public PbMessageValueImBuilder setSubType(long subType) throws RuleException {
		
		ImMessageTypeReservedAndLimits.check(subType);
		this.subType = subType;
		
		return this;
	}

	@Override
	public PbMessageValueImBuilder setCollectorId(long collectorId) throws RuleException {
		
		ImIdLimits.check(collectorId);
		this.collectorId = collectorId;
		
		return this;
	}

	@Override
	public PbMessageValueImBuilder setValidatorId(long validatorId) throws RuleException {
		
		ImIdLimits.check(validatorId);
		this.validatorId = validatorId;
		
		return this;
	}

	@Override
	public PbMessageValueImBuilder setMessage(byte[] message) {
		
		if(message != null){
			this.message = message;
			this.length = PbMessageTlvFixedLengthEnum.IM_VALUE.length() + message.length;
		}
		
		return this;
	}

	@Override
	public PbMessageValueIm toObject(){

		return new PbMessageValueIm(this.imFlags, this.subVendorId, this.subType, this.collectorId, this.validatorId, this.length, this.message);
	}

	@Override
	public PbMessageValueImBuilder newInstance() {

		return new PbMessageValueImBuilderIetf();
	}

}
