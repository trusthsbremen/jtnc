package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersionBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeNumericVersionValueReader implements ImReader<PaAttributeValueNumericVersion>{

	private PaAttributeValueNumericVersionBuilder baseBuilder;
	
	PaAttributeNumericVersionValueReader(PaAttributeValueNumericVersionBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueNumericVersion read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaAttributeValueNumericVersion value = null;
		PaAttributeValueNumericVersionBuilder builder = (PaAttributeValueNumericVersionBuilder)baseBuilder.newInstance();

		try{
			try{
				
				/* major version */
				errorOffset = buffer.bytesRead();
				long majorVersion = buffer.readLong((byte)4);
				builder.setMajorVersion(majorVersion);
			
				
				/* minor version */
				errorOffset = buffer.bytesRead();
				long minorVersion = buffer.readLong((byte)4);
				builder.setMinorVersion(minorVersion);
				
				/* build version */
				errorOffset = buffer.bytesRead();
				long buildVersion = buffer.readLong((byte)4);
				builder.setBuildVersion(buildVersion);

				/* service pack major */
				errorOffset = buffer.bytesRead();
				int servicePackMajor = buffer.readInt((byte)2);
				builder.setServicePackMajor(servicePackMajor);
				
				/* service pack minor */
				errorOffset = buffer.bytesRead();
				int servicePackMinor = buffer.readInt((byte)2);
				builder.setServicePackMinor(servicePackMinor);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueNumericVersion)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.NUM_VER.length();
	}

}
