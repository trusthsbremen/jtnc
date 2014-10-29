package org.ietf.nea.pb.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersionBuilder;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.util.ByteArrayHelper;

class PbMessageErrorParameterVersionSubValueReader implements TnccsReader< PbMessageValueErrorParameterVersion>{

	private  PbMessageValueErrorParameterVersionBuilder builder;
	
	PbMessageErrorParameterVersionSubValueReader(PbMessageValueErrorParameterVersionBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public  PbMessageValueErrorParameterVersion read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PbMessageValueErrorParameterVersion value = null;
		builder = (PbMessageValueErrorParameterVersionBuilder)builder.clear();

		try{
			
			try{
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* bad version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short badVersion = ByteArrayHelper.toShort(buffer);
				this.builder.setBadVersion(badVersion);
				errorOffset += byteSize;
				
				/* max version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short maxVersion = ByteArrayHelper.toShort(buffer);
				this.builder.setMaxVersion(maxVersion);
				errorOffset += byteSize;
				
				/* min version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short minVersion = ByteArrayHelper.toShort(buffer);
				this.builder.setMinVersion(minVersion);
				errorOffset += byteSize;
				
				/* ignore reserved */
				byteSize = 1;
				ByteArrayHelper.arrayFromStream(in, byteSize);
				errorOffset += byteSize;
			
			}catch (IOException e){
				throw new SerializationException("Returned data for message header is to short or stream may be closed.",e,true);
			}

			value = (PbMessageValueErrorParameterVersion)builder.toValue();
			
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
