package org.ietf.nea.pa.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParametersBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeRemediationParameterTypeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterString;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUri;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

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
	public PaAttributeValueRemediationParameters read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueRemediationParameters value = null;
		PaAttributeValueRemediationParametersBuilder builder = (PaAttributeValueRemediationParametersBuilder)this.baseBuilder.newInstance();

		long rpVendorId = 0L;
		long rpType = 0L;
		
		try{
			
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* ignore reserved */
				byteSize = 1;
				ByteArrayHelper.arrayFromStream(in, byteSize);
				errorOffset += byteSize;
				
				/* vendor ID */
				byteSize = 3;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				rpVendorId = ByteArrayHelper.toLong(buffer);
				builder.setRpVendorId(rpVendorId);
				errorOffset += byteSize;
				
				/* remediation type */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				rpType = ByteArrayHelper.toLong(buffer);
				builder.setRpType(rpType);
				errorOffset += byteSize;
			
			}catch (IOException e){
				throw new SerializationException(
						"Returned data for attribute value is to short or stream may be closed.", e, true);
			}
			
			
			/* remediation parameter */
			// value length = header length - overall message length
			long valueLength = messageLength - errorOffset;

			try{
				if(rpType == PaAttributeRemediationParameterTypeEnum.IETF_URI.type()){
					PaAttributeValueRemediationParameterUri paramUri = this.uriReader.read(in, valueLength);
					builder.setParameter(paramUri);
					
				}else if(rpType == PaAttributeRemediationParameterTypeEnum.IETF_STRING.type()){
					PaAttributeValueRemediationParameterString paramString = this.stringReader.read(in, valueLength);
			        builder.setParameter(paramString);
				}else{
					try{
						// skip the remaining bytes of the message
						in.skip(valueLength);
					}catch (IOException e1){
						throw new SerializationException("Bytes from InputStream could not be skipped, stream seems closed.", true);
					}
					return null;
				}
			}catch(ValidationException e){
				// catch exception and add throw with recalculated offset, pass on the rule exception
				throw new ValidationException(e.getMessage(), e.getCause(),e.getExceptionOffset() + errorOffset);
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
