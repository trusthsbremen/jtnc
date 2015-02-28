package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeHeaderBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttribute;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttributeBuilder;
import org.ietf.nea.pa.attribute.util.RawMessageHeader;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeErrorInformationUnsupportedAttributeValueReader implements ImReader<PaAttributeValueErrorInformationUnsupportedAttribute>{

	private PaAttributeValueErrorInformationUnsupportedAttributeBuilder baseBuilder;
	private PaAttributeHeaderBuilder baseAttributeHeaderBuilder;
	
	PaAttributeErrorInformationUnsupportedAttributeValueReader(PaAttributeValueErrorInformationUnsupportedAttributeBuilder builder, PaAttributeHeaderBuilder attributeHeaderBuilder){
		this.baseBuilder = builder;
		this.baseAttributeHeaderBuilder = attributeHeaderBuilder;
	}
	
	@Override
	public PaAttributeValueErrorInformationUnsupportedAttribute read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueErrorInformationUnsupportedAttribute value = null;
		
		PaAttributeValueErrorInformationUnsupportedAttributeBuilder builder = (PaAttributeValueErrorInformationUnsupportedAttributeBuilder)this.baseBuilder.newInstance();
		PaAttributeHeaderBuilder attributeHeaderBuilder = (PaAttributeHeaderBuilder) this.baseAttributeHeaderBuilder.newInstance();
		
		try{
			
			try{

				errorOffset = buffer.bytesRead();
				/* message header */
				/* copy version */
				short version = buffer.readShort((byte)1);

				/* copy reserved */
				byte[] reserved = buffer.read(3);
	
				/* copy identifier */
				long identifier = buffer.readLong((byte)4);
	
				builder.setMessageHeader(new RawMessageHeader(version, reserved, identifier));


				/* max version */
				errorOffset = buffer.bytesRead();
				attributeHeaderBuilder.setFlags(buffer.readByte());
				
				/* attribute vendor ID */
				errorOffset = buffer.bytesRead();
				long vendorId =  buffer.readLong((byte)3);
				attributeHeaderBuilder.setVendorId(vendorId);
				
				/* attribute type */
				errorOffset = buffer.bytesRead();
				long type =  buffer.readLong((byte)4);
				attributeHeaderBuilder.setType(type);
				
				// set dummy length = 0 
				attributeHeaderBuilder.setLength(0);
				
				PaAttributeHeader attributeHeader = (PaAttributeHeader)attributeHeaderBuilder.toObject();
				builder.setAttributeHeader(attributeHeader);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
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
