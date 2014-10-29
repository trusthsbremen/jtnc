package org.ietf.nea.pa.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeHeaderBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttribute;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttributeBuilder;
import org.ietf.nea.pa.message.PaMessageHeader;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.m.serialize.ImReader;
import de.hsbremen.tc.tnc.util.ByteArrayHelper;

class PaAttributeErrorInformationUnsupportedAttributeValueReader implements ImReader<PaAttributeValueErrorInformationUnsupportedAttribute>{

	private PaAttributeValueErrorInformationUnsupportedAttributeBuilder builder;
	private PaAttributeHeaderBuilder attributeHeaderBuilder;
	private PaMessageHeaderReader reader;
	
	PaAttributeErrorInformationUnsupportedAttributeValueReader(PaAttributeValueErrorInformationUnsupportedAttributeBuilder builder, PaAttributeHeaderBuilder attributeHeaderBuilder ,PaMessageHeaderReader reader){
		this.builder = builder;
		this.reader = reader;
	}
	
	@Override
	public PaAttributeValueErrorInformationUnsupportedAttribute read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueErrorInformationUnsupportedAttribute value = null;
		builder = (PaAttributeValueErrorInformationUnsupportedAttributeBuilder)builder.clear();
		attributeHeaderBuilder = (PaAttributeHeaderBuilder) attributeHeaderBuilder.clear();
		
		/* message header */
		PaMessageHeader messageHeader = this.reader.read(in, messageLength);
		this.builder.setMessageHeader(messageHeader);
		errorOffset += PaAttributeTlvFixedLength.MESSAGE.length();
		
		try{
			
			try{

				int byteSize = 0;
				byte[] buffer = new byte[byteSize];

				/* max version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				this.attributeHeaderBuilder.setFlags(buffer[1]);
				errorOffset += byteSize;
				
				/* attribute vendor ID */
				byteSize = 3;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long vendorId =  ByteArrayHelper.toLong(buffer);
				this.attributeHeaderBuilder.setVendorId(vendorId);
				errorOffset += byteSize;
				
				/* attribute type */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long type =  ByteArrayHelper.toLong(buffer);
				this.attributeHeaderBuilder.setType(type);
				errorOffset += byteSize;
				
				// set dummy length = 0 
				this.attributeHeaderBuilder.setLength(0);
				
				PaAttributeHeader attributeHeader = (PaAttributeHeader)attributeHeaderBuilder.toAttributeHeader();
				this.builder.setAttributeHeader(attributeHeader);
				
			}catch (IOException e){
				throw new SerializationException("Returned data for attribute value is to short or stream may be closed.",e,true);
			}

			value = (PaAttributeValueErrorInformationUnsupportedAttribute)builder.toValue();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
	
		return (byte)(PaAttributeTlvFixedLength.ERR_INF.length() + PaAttributeTlvFixedLength.ATTRIBUTE.length() - 4); // -4 = ignore length
	}
}
