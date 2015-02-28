package org.ietf.nea.pa.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.message.PaMessageHeader;
import org.ietf.nea.pa.message.PaMessageHeaderBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

public class PaMessageHeaderReader implements ImReader<PaMessageHeader> {

	private PaMessageHeaderBuilder baseBuilder;
	
	PaMessageHeaderReader(PaMessageHeaderBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaMessageHeader read(InputStream in, long length)
			throws SerializationException, ValidationException{

		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaMessageHeader messageHeader = null;
		PaMessageHeaderBuilder builder = (PaMessageHeaderBuilder) this.baseBuilder.newInstance();
		
		try{
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* version */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				builder.setVersion(buffer[0]);
				errorOffset += byteSize;

				/* ignore reserved */
				byteSize = 3;
				ByteArrayHelper.arrayFromStream(in, byteSize);
				errorOffset += byteSize;
				
				/* identifier */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long identifier = ByteArrayHelper.toLong(buffer);
				builder.setIdentifier(identifier);
				errorOffset += byteSize;
				
			}catch (IOException e){
				throw new SerializationException("Returned data for message header is to short or stream may be closed.",e,true);
			}

			messageHeader = (PaMessageHeader)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return messageHeader;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.MESSAGE.length();
	}
}
