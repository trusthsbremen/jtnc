package org.ietf.nea.pb.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffsetBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PbMessageErrorParameterOffsetSubValueReader implements TnccsReader<PbMessageValueErrorParameterOffset>{

	private  PbMessageValueErrorParameterOffsetBuilder builder;
	
	PbMessageErrorParameterOffsetSubValueReader(PbMessageValueErrorParameterOffsetBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public  PbMessageValueErrorParameterOffset read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PbMessageValueErrorParameterOffset value = null;
		builder = (PbMessageValueErrorParameterOffsetBuilder)builder.clear();

		try{
			
			try{
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long offset = ByteArrayHelper.toLong(buffer);
				this.builder.setOffset(offset);
				errorOffset += byteSize;
			
			}catch (IOException e){
				throw new SerializationException("Returned data for message value is to short or stream may be closed.",e,true);
			}

			value = (PbMessageValueErrorParameterOffset)builder.toValue();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {

		return PbMessageTlvFixedLength.ERR_SUB_VALUE.length(); 
	}
}
