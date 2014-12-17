package org.ietf.nea.pa.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueForwardingEnabled;
import org.ietf.nea.pa.attribute.PaAttributeValueForwardingEnabledBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributeForwardingEnabledValueReader implements ImReader<PaAttributeValueForwardingEnabled>{

	private PaAttributeValueForwardingEnabledBuilder baseBuilder;
	
	PaAttributeForwardingEnabledValueReader(PaAttributeValueForwardingEnabledBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueForwardingEnabled read(final InputStream in, final long attributeLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaAttributeValueForwardingEnabled value = null;
		PaAttributeValueForwardingEnabledBuilder builder = (PaAttributeValueForwardingEnabledBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* status */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long status = ByteArrayHelper.toLong(buffer);
				builder.setStatus(status);
				errorOffset += byteSize;
				
			}catch (IOException e){
				throw new SerializationException(
						"Returned data for attribute value is to short or stream may be closed.", e, true);
			}

			value = (PaAttributeValueForwardingEnabled)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.FWD_EN.length();
	}

}
