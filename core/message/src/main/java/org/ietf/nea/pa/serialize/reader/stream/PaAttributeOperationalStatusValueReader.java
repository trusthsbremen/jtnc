package org.ietf.nea.pa.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.PaAttributeValueOperationalStatus;
import org.ietf.nea.pa.attribute.PaAttributeValueOperationalStatusBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributeOperationalStatusValueReader implements ImReader<PaAttributeValueOperationalStatus>{

	private PaAttributeValueOperationalStatusBuilder baseBuilder;
	
	PaAttributeOperationalStatusValueReader(PaAttributeValueOperationalStatusBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueOperationalStatus read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaAttributeValueOperationalStatus value = null;
		PaAttributeValueOperationalStatusBuilder builder = (PaAttributeValueOperationalStatusBuilder)baseBuilder.newInstance();

		try{
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* status */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short status = ByteArrayHelper.toShort(buffer);
				builder.setStatus(status);
				errorOffset += byteSize;
				
				/* result */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				short result = ByteArrayHelper.toShort(buffer);
				builder.setResult(result);
				errorOffset += byteSize;
				
				/* ignore reserved */
				byteSize = 2;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				errorOffset += byteSize;
				
				/* last use */
				byteSize = 20;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				String lastUse = new String(buffer, Charset.forName("US-ASCII"));
				builder.setLastUse(lastUse);
				errorOffset += byteSize;
				
				
			}catch (IOException e){
				throw new SerializationException("Returned data for attribute value is to short or stream may be closed.",e,true);
			}

			value = (PaAttributeValueOperationalStatus)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.OP_STAT.length();
	}

}
