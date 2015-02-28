package org.ietf.nea.pa.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeHeaderBuilder;
import org.ietf.nea.pa.attribute.RawMessageHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttribute;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttributeBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributeErrorInformationUnsupportedAttributeValueReader implements ImReader<PaAttributeValueErrorInformationUnsupportedAttribute>{

	private PaAttributeValueErrorInformationUnsupportedAttributeBuilder baseBuilder;
	private PaAttributeHeaderBuilder baseAttributeHeaderBuilder;
	
	PaAttributeErrorInformationUnsupportedAttributeValueReader(PaAttributeValueErrorInformationUnsupportedAttributeBuilder builder, PaAttributeHeaderBuilder attributeHeaderBuilder){
		this.baseBuilder = builder;
		this.baseAttributeHeaderBuilder = attributeHeaderBuilder;
	}
	
	@Override
	public PaAttributeValueErrorInformationUnsupportedAttribute read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueErrorInformationUnsupportedAttribute value = null;
		PaAttributeValueErrorInformationUnsupportedAttributeBuilder builder = (PaAttributeValueErrorInformationUnsupportedAttributeBuilder)this.baseBuilder.newInstance();
		PaAttributeHeaderBuilder attributeHeaderBuilder = (PaAttributeHeaderBuilder) this.baseAttributeHeaderBuilder.newInstance();
		
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
				attributeHeaderBuilder.setFlags(buffer[1]);
				errorOffset += byteSize;
				
				/* attribute vendor ID */
				byteSize = 3;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long vendorId =  ByteArrayHelper.toLong(buffer);
				attributeHeaderBuilder.setVendorId(vendorId);
				errorOffset += byteSize;
				
				/* attribute type */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long type =  ByteArrayHelper.toLong(buffer);
				attributeHeaderBuilder.setType(type);
				errorOffset += byteSize;
				
				// set dummy length = 0 
				attributeHeaderBuilder.setLength(0);
				
				PaAttributeHeader attributeHeader = (PaAttributeHeader)attributeHeaderBuilder.toObject();
				builder.setAttributeHeader(attributeHeader);
				
			}catch (IOException e){
				throw new SerializationException("Returned data for attribute value is to short or stream may be closed.",e,true);
			}

			value = (PaAttributeValueErrorInformationUnsupportedAttribute)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
	
		return (byte)(PaAttributeTlvFixedLengthEnum.ERR_INF.length() + PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length() - 4); // -4 = ignore length
	}
}
