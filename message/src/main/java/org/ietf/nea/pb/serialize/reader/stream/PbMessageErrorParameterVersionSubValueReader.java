package org.ietf.nea.pb.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersionBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PbMessageErrorParameterVersionSubValueReader implements TnccsReader<PbMessageValueErrorParameterVersion>{

	private  PbMessageValueErrorParameterVersionBuilder baseBuilder;
	
	PbMessageErrorParameterVersionSubValueReader(PbMessageValueErrorParameterVersionBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public  PbMessageValueErrorParameterVersion read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PbMessageValueErrorParameterVersion value = null;
		PbMessageValueErrorParameterVersionBuilder builder = (PbMessageValueErrorParameterVersionBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* bad version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short badVersion = ByteArrayHelper.toShort(buffer);
				builder.setBadVersion(badVersion);
				errorOffset += byteSize;
				
				/* max version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short maxVersion = ByteArrayHelper.toShort(buffer);
				builder.setMaxVersion(maxVersion);
				errorOffset += byteSize;
				
				/* min version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short minVersion = ByteArrayHelper.toShort(buffer);
				builder.setMinVersion(minVersion);
				errorOffset += byteSize;
				
				/* ignore reserved */
				byteSize = 1;
				ByteArrayHelper.arrayFromStream(in, byteSize);
				errorOffset += byteSize;
			
			}catch (IOException e){
				throw new SerializationException("Returned data for message header is to short or stream may be closed.",e,true);
			}

			value = (PbMessageValueErrorParameterVersion)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {

		return PbMessageTlvFixedLengthEnum.ERR_SUB_VALUE.length(); 
	}
}
