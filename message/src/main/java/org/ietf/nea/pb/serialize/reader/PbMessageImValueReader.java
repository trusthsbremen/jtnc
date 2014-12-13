package org.ietf.nea.pb.serialize.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueImBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PbMessageImValueReader implements TnccsReader<PbMessageValueIm>{

	private PbMessageValueImBuilder builder;
	
	PbMessageImValueReader(PbMessageValueImBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PbMessageValueIm read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PbMessageValueIm value = null;
		builder = (PbMessageValueImBuilder)builder.clear();

		try{
			
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* flags */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				this.builder.setImFlags(buffer[0]);
				errorOffset += byteSize;
			
				/* sub vendor ID */ 
				byteSize = 3;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long subVendorId = ByteArrayHelper.toLong(buffer);
				this.builder.setSubVendorId(subVendorId);
				errorOffset += byteSize;
				
				/* sub message type */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long subType = ByteArrayHelper.toLong(buffer);
				this.builder.setSubType(subType);
				errorOffset += byteSize;
				
				/* collector ID */
				byteSize = 2;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long collectorId = ByteArrayHelper.toLong(buffer);
				this.builder.setCollectorId(collectorId);
				errorOffset += byteSize;
				
				/* validator ID */
				byteSize = 2;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long validatorId = ByteArrayHelper.toLong(buffer);
				this.builder.setValidatorId(validatorId);	
				errorOffset += byteSize;
			
				/* PA message */
				byte[] imMessage = new byte[0];
				buffer = new byte[0];
				
				for(long l = messageLength - errorOffset; l > 0; l -= buffer.length){
					
					buffer = ByteArrayHelper.arrayFromStream(in, ((l < 65535) ?(int)l : 65535));
					imMessage = ByteArrayHelper.mergeArrays(imMessage, Arrays.copyOfRange(buffer, 0, buffer.length));
				}
				
				byteSize = imMessage.length;
				this.builder.setMessage(imMessage);
				errorOffset += byteSize;
			
			}catch (IOException e){
				throw new SerializationException(
						"Returned data for message value is to short or stream may be closed.", e, true);
			}

			value = (PbMessageValueIm)builder.toValue();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLengthEnum.IM_VALUE.length();
	}

}
