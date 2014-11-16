package org.ietf.nea.pa.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.RawMessageHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersion;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersionBuilder;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.m.serialize.ImReader;
import de.hsbremen.tc.tnc.util.ByteArrayHelper;

class PaAttributeErrorInformationUnsupportedVersionValueReader implements ImReader<PaAttributeValueErrorInformationUnsupportedVersion>{

	private PaAttributeValueErrorInformationUnsupportedVersionBuilder builder;
	
	PaAttributeErrorInformationUnsupportedVersionValueReader(PaAttributeValueErrorInformationUnsupportedVersionBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PaAttributeValueErrorInformationUnsupportedVersion read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueErrorInformationUnsupportedVersion value = null;
		builder = (PaAttributeValueErrorInformationUnsupportedVersionBuilder)builder.clear();
		
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
	
				this.builder.setMessageHeader(new RawMessageHeader(version, reserved, identifier));
				errorOffset += PaAttributeTlvFixedLength.MESSAGE.length();
				
				/* max version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short maxVersion =  ByteArrayHelper.toShort(buffer);
				this.builder.setMaxVersion(maxVersion);
				errorOffset += byteSize;
				
				/* min version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short minVersion =  ByteArrayHelper.toShort(buffer);
				this.builder.setMinVersion(minVersion);
				errorOffset += byteSize;
				
				/* ignore reserved */
				byteSize = 2;
				ByteArrayHelper.arrayFromStream(in, byteSize);
				errorOffset += byteSize;
				
			}catch (IOException e){
				throw new SerializationException("Returned data for attribute value is to short or stream may be closed.",e,true);
			}

			value = (PaAttributeValueErrorInformationUnsupportedVersion)builder.toValue();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
	
		return (byte)(PaAttributeTlvFixedLength.ERR_INF.length() + 4); // 4 = min + max and reserved
	}
}
