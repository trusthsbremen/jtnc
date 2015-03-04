package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pb.message.PbMessageValueRemediationParameters;
import org.ietf.nea.pb.message.PbMessageValueRemediationParametersBuilder;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUri;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageRemediationParametersValueReader implements TnccsReader<PbMessageValueRemediationParameters>{

	private PbMessageValueRemediationParametersBuilder baseBuilder;
	
	// TODO should be a map to make the remediation parameters more customizable
	private final PbMessageRemediationParameterStringSubValueReader stringReader;
	private final PbMessageRemediationParameterUriSubValueReader uriReader;
	
	public PbMessageRemediationParametersValueReader(PbMessageValueRemediationParametersBuilder builder, PbMessageRemediationParameterStringSubValueReader stringReader, PbMessageRemediationParameterUriSubValueReader uriReader ){
		this.baseBuilder = builder;
		this.stringReader = stringReader;
		this.uriReader = uriReader;
	}
	
	@Override
	public PbMessageValueRemediationParameters read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
	    NotNull.check("Buffer cannot be null.", buffer);
	    
		long errorOffset = 0;
		
		PbMessageValueRemediationParameters value = null;
		PbMessageValueRemediationParametersBuilder builder = (PbMessageValueRemediationParametersBuilder)this.baseBuilder.newInstance();

		long rpVendorId = 0L;
		long rpType = 0L;
		
		try{
			
			try{
				
				/* ignore reserved 8 bit(s) */
				errorOffset = buffer.bytesRead();
				buffer.readByte();
				
				/* vendor ID 24 bit(s) */
				errorOffset = buffer.bytesRead();
				rpVendorId = buffer.readLong((byte)3);
				builder.setRpVendorId(rpVendorId);
				
				/* remediation type 32 bit(s)*/
				errorOffset = buffer.bytesRead();
				rpType = buffer.readLong((byte)4);
				builder.setRpType(rpType);
		
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}
			
			/* remediation parameter */
			// value length = overall message length - header length
			long valueLength = messageLength - this.getMinDataLength();


			if(rpType == PbMessageRemediationParameterTypeEnum.IETF_URI.id()){
				PbMessageValueRemediationParameterUri paramUri = this.uriReader.read(buffer, valueLength);
				builder.setParameter(paramUri);
				
			}else if(rpType == PbMessageRemediationParameterTypeEnum.IETF_STRING.id()){
				PbMessageValueRemediationParameterString paramString = this.stringReader.read(buffer, valueLength);
			    builder.setParameter(paramString);
			}else{
				try{
					// skip the remaining bytes of the message
					buffer.read(valueLength);
				}catch (BufferUnderflowException e){
					throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
				}
				// TODO make a default remediation object?
				return null;
			}
	
			value = (PbMessageValueRemediationParameters)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLengthEnum.REM_PAR_VALUE.length();
	}

}
