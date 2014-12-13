package org.ietf.nea.pa.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersionBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributeNumericVersionValueReader implements ImReader<PaAttributeValueNumericVersion>{

	private PaAttributeValueNumericVersionBuilder builder;
	
	PaAttributeNumericVersionValueReader(PaAttributeValueNumericVersionBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PaAttributeValueNumericVersion read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaAttributeValueNumericVersion value = null;
		builder = (PaAttributeValueNumericVersionBuilder)builder.clear();

		try{
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* major version */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long majorVersion = ByteArrayHelper.toLong(buffer);
				builder.setMajorVersion(majorVersion);
				errorOffset += byteSize;
				
				/* minor version */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long minorVersion = ByteArrayHelper.toLong(buffer);
				builder.setMinorVersion(minorVersion);
				errorOffset += byteSize;
				
				/* build version */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long buildVersion = ByteArrayHelper.toLong(buffer);
				builder.setBuildVersion(buildVersion);
				errorOffset += byteSize;
				
				/* service pack major */
				byteSize = 2;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				int servicePackMajor = ByteArrayHelper.toInt(buffer);
				builder.setServicePackMajor(servicePackMajor);
				errorOffset += byteSize;
				
				byteSize = 2;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				int servicePackMinor = ByteArrayHelper.toInt(buffer);
				builder.setServicePackMinor(servicePackMinor);
				errorOffset += byteSize;
				
				
			}catch (IOException e){
				throw new SerializationException("Returned data for attribute value is to short or stream may be closed.",e,true);
			}

			value = (PaAttributeValueNumericVersion)builder.toValue();
			
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
