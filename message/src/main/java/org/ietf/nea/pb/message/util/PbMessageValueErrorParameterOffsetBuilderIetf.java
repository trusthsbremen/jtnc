package org.ietf.nea.pb.message.util;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;

public class PbMessageValueErrorParameterOffsetBuilderIetf implements PbMessageValueErrorParameterOffsetBuilder{

	private long length;
    private long offset;
    
    public PbMessageValueErrorParameterOffsetBuilderIetf(){
    	this.length = PbMessageTlvFixedLength.ERR_SUB_VALUE.length();
    	this.offset = 0;
    }

	@Override
	public PbMessageValueErrorParameterOffsetBuilder setOffset(long offset) throws RuleException {
		
		this.offset = offset;
		return this;
	}

	@Override
	public PbMessageValueErrorParameterOffset toValue() throws RuleException {

		return new PbMessageValueErrorParameterOffset(length, offset);
	}

	@Override
	public PbMessageValueErrorParameterOffsetBuilder clear() {

		return new PbMessageValueErrorParameterOffsetBuilderIetf();
	}

}
