package org.ietf.nea.pa.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeHeaderBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributeHeaderReader implements ImReader<PaAttributeHeader>{

	private PaAttributeHeaderBuilder baseBuilder;
	
	PaAttributeHeaderReader(PaAttributeHeaderBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeHeader read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaAttributeHeader messageHeader = null;
		PaAttributeHeaderBuilder builder = (PaAttributeHeaderBuilder)this.baseBuilder.newInstance();

		try{
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* flags */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				builder.setFlags(buffer[0]);
				errorOffset += byteSize;
				
				/* vendor ID */
				byteSize = 3;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long vendorId = ByteArrayHelper.toLong(buffer);
				builder.setVendorId(vendorId);
				errorOffset += byteSize;
				
				/* attribute type */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long messageType = ByteArrayHelper.toLong(buffer);
				builder.setType(messageType);
				errorOffset += byteSize;
				
				/* attribute length */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long length = ByteArrayHelper.toLong(buffer);
				builder.setLength(length);
				errorOffset += byteSize;
				
				
			}catch (IOException e){
				throw new SerializationException("Returned data for attribute header is to short or stream may be closed.",e,true);
			}

			messageHeader = (PaAttributeHeader)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return messageHeader;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length();
	}

}
