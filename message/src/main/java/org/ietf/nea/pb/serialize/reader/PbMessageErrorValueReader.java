package org.ietf.nea.pb.serialize.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.PbMessageValueErrorBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;

class PbMessageErrorValueReader implements TnccsReader<PbMessageValueError>{

	private PbMessageValueErrorBuilder builder;
	
	PbMessageErrorValueReader(PbMessageValueErrorBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PbMessageValueError read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PbMessageValueError value = null;
		builder = (PbMessageValueErrorBuilder)builder.clear();

		try{
			
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* flags */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				this.builder.setErrorFlags(buffer[0]);
				errorOffset += byteSize;
				
				/* vendor ID */
				byteSize = 3;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long errorVendorId = ByteArrayHelper.toLong(buffer);
				this.builder.setErrorVendorId(errorVendorId);
				errorOffset += byteSize;
				
				/* error Code */
				byteSize = 2;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short errorCode = ByteArrayHelper.toShort(buffer);
				this.builder.setErrorCode(errorCode);
				errorOffset += byteSize;
				
				/* ignore Reserved */
				byteSize = 2;
				ByteArrayHelper.arrayFromStream(in, byteSize);
				errorOffset += byteSize;
				
				/* error content */
				byte[] content = new byte[0];
				buffer = new byte[0];
				
				for(long l = messageLength - errorOffset; l > 0; l -= buffer.length){
					
					buffer = ByteArrayHelper.arrayFromStream(in, ((l < 65535) ?(int)l : 65535));
					content = ByteArrayHelper.mergeArrays(content, Arrays.copyOfRange(buffer, 0, buffer.length));
				}
				
				byteSize = content.length;
				this.builder.setErrorParameter(content);
				errorOffset += byteSize;
			
			}catch (IOException e){
				throw new SerializationException(
						"Returned data for message value is to short or stream may be closed.", e, true);
			}

			value = (PbMessageValueError)builder.toValue();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLength.ERR_VALUE.length();
	}

}
