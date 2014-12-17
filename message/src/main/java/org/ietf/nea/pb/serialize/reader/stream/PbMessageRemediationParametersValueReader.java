package org.ietf.nea.pb.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameters;
import org.ietf.nea.pb.message.PbMessageValueRemediationParametersBuilder;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUri;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

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
	public PbMessageValueRemediationParameters read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PbMessageValueRemediationParameters value = null;
		PbMessageValueRemediationParametersBuilder builder = (PbMessageValueRemediationParametersBuilder)this.baseBuilder.newInstance();

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
						"Returned data for message value is to short or stream may be closed.", e, true);
			}
			
			
			/* remediation parameter */
			// value length = header length - overall message length
			long valueLength = messageLength - errorOffset;

			try{
				if(rpType == PbMessageRemediationParameterTypeEnum.IETF_URI.type()){
					PbMessageValueRemediationParameterUri paramUri = this.uriReader.read(in, valueLength);
					builder.setParameter(paramUri);
					
				}else if(rpType == PbMessageRemediationParameterTypeEnum.IETF_STRING.type()){
					PbMessageValueRemediationParameterString paramString = this.stringReader.read(in, valueLength);
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
