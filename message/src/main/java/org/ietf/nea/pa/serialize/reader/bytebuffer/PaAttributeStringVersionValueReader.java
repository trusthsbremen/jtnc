package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersion;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersionBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeStringVersionValueReader implements ImReader<PaAttributeValueStringVersion>{

	private PaAttributeValueStringVersionBuilder baseBuilder;
	
	PaAttributeStringVersionValueReader(PaAttributeValueStringVersionBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueStringVersion read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueStringVersion value = null;
		PaAttributeValueStringVersionBuilder builder = (PaAttributeValueStringVersionBuilder)this.baseBuilder.newInstance();

		try{
			
			try{

				
				// First byte is the string length.
				errorOffset = buffer.bytesRead();
				int productLength = buffer.readShort((byte)1);
				
				/* product version number */
				errorOffset = buffer.bytesRead();
				byte[] psData = buffer.read(productLength);
				String productVersion = new String(psData, Charset.forName("UTF-8"));
				builder.setProductVersion(productVersion);

				
				// First byte is the string length.
				errorOffset = buffer.bytesRead();
				int buildLength =  buffer.readShort((byte)1);
				
				/* build version number */
				errorOffset = buffer.bytesRead();
				byte[] bsData = buffer.read(buildLength);
				String buildNumber = new String(bsData, Charset.forName("UTF-8"));
				builder.setBuildNumber(buildNumber);
				
				// First byte is the string length.
				errorOffset = buffer.bytesRead();
				int configLength =  buffer.readShort((byte)1);

				/* product version number */
				errorOffset = buffer.bytesRead();
				byte[] csData = buffer.read(configLength);
				String configVersion = new String(csData, Charset.forName("UTF-8"));
				builder.setConfigurationVersion(configVersion);

			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueStringVersion)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}
	
	@Override
	public byte getMinDataLength() {
	
		return PaAttributeTlvFixedLengthEnum.STR_VER.length(); 
	}
}
