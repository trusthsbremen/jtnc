package org.ietf.nea.pa.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.pa.attribute.RawMessageHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersion;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersionBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributeErrorInformationUnsupportedVersionValueReader implements ImReader<PaAttributeValueErrorInformationUnsupportedVersion>{

	private PaAttributeValueErrorInformationUnsupportedVersionBuilder baseBuilder;
	
	PaAttributeErrorInformationUnsupportedVersionValueReader(PaAttributeValueErrorInformationUnsupportedVersionBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueErrorInformationUnsupportedVersion read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueErrorInformationUnsupportedVersion value = null;
		PaAttributeValueErrorInformationUnsupportedVersionBuilder builder = (PaAttributeValueErrorInformationUnsupportedVersionBuilder) this.baseBuilder.newInstance();
		
		try{
			
			try{

				int byteSize = 0;
				byte[] buffer = new byte[byteSize];

				/* message header */
				/* copy version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short version = buffer[0];

				/* copy reserved */
				byteSize = 3;
				byte[] reserved = ByteArrayHelper.arrayFromStream(in, byteSize);
	
				/* copy identifier */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long identifier = ByteArrayHelper.toLong(buffer);
	
				builder.setMessageHeader(new RawMessageHeader(version, reserved, identifier));
				errorOffset += PaAttributeTlvFixedLengthEnum.MESSAGE.length();
				
				/* max version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short maxVersion =  ByteArrayHelper.toShort(buffer);
				builder.setMaxVersion(maxVersion);
				errorOffset += byteSize;
				
				/* min version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short minVersion =  ByteArrayHelper.toShort(buffer);
				builder.setMinVersion(minVersion);
				errorOffset += byteSize;
				
				/* ignore reserved */
				byteSize = 2;
				ByteArrayHelper.arrayFromStream(in, byteSize);
				errorOffset += byteSize;
				
			}catch (IOException e){
				throw new SerializationException("Returned data for attribute value is to short or stream may be closed.",e,true);
			}

			value = (PaAttributeValueErrorInformationUnsupportedVersion)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
	
		return (byte)(PaAttributeTlvFixedLengthEnum.ERR_INF.length() + 4); // 4 = min + max and reserved
	}
}
