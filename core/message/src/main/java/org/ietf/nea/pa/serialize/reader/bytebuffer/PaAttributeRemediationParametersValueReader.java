package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParametersBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeRemediationParameterTypeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterString;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUri;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeRemediationParametersValueReader implements ImReader<PaAttributeValueRemediationParameters>{

	private PaAttributeValueRemediationParametersBuilder baseBuilder;
	
	// TODO should be a map to make the remediation parameters more customizable
	private final PaAttributeRemediationParameterStringValueReader stringReader;
	private final PaAttributeRemediationParameterUriValueReader uriReader;
	
	public PaAttributeRemediationParametersValueReader(PaAttributeValueRemediationParametersBuilder builder, PaAttributeRemediationParameterStringValueReader stringReader, PaAttributeRemediationParameterUriValueReader uriReader ){
		this.baseBuilder = builder;
		this.stringReader = stringReader;
		this.uriReader = uriReader;
	}
	
	@Override
	public PaAttributeValueRemediationParameters read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueRemediationParameters value = null;
		PaAttributeValueRemediationParametersBuilder builder = (PaAttributeValueRemediationParametersBuilder)this.baseBuilder.newInstance();

		long rpVendorId = 0L;
		long rpType = 0L;
		
		try{
			
			try{
				
				
				/* ignore reserved */
				errorOffset = buffer.bytesRead();
				buffer.readByte();
				
				/* vendor ID */
				errorOffset = buffer.bytesRead();
				rpVendorId = buffer.readLong((byte)3);
				builder.setRpVendorId(rpVendorId);

				
				/* remediation type */
				errorOffset = buffer.bytesRead();
				rpType = buffer.readLong((byte)4);
				builder.setRpType(rpType);
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}
			
			
			/* remediation parameter */
			// value length = overall message length - header length
			long valueLength = messageLength - PaAttributeTlvFixedLengthEnum.REM_PAR.length();

			if(rpType == PaAttributeRemediationParameterTypeEnum.IETF_URI.id()){
				PaAttributeValueRemediationParameterUri paramUri = this.uriReader.read(buffer, valueLength);
				builder.setParameter(paramUri);
					
			}else if(rpType == PaAttributeRemediationParameterTypeEnum.IETF_STRING.id()){
				PaAttributeValueRemediationParameterString paramString = this.stringReader.read(buffer, valueLength);
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
			

			value = (PaAttributeValueRemediationParameters)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.REM_PAR.length();
	}

}
