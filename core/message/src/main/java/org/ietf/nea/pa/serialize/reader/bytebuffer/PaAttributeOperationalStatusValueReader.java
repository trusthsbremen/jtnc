package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueOperationalStatus;
import org.ietf.nea.pa.attribute.PaAttributeValueOperationalStatusBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeOperationalStatusValueReader implements ImReader<PaAttributeValueOperationalStatus>{

	private PaAttributeValueOperationalStatusBuilder baseBuilder;
	
	PaAttributeOperationalStatusValueReader(PaAttributeValueOperationalStatusBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueOperationalStatus read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaAttributeValueOperationalStatus value = null;
		PaAttributeValueOperationalStatusBuilder builder = (PaAttributeValueOperationalStatusBuilder)baseBuilder.newInstance();

		try{
			try{
				
				/* status */
				errorOffset = buffer.bytesRead();
				short status = buffer.readShort((byte)1);
				builder.setStatus(status);
				
				/* result */
				errorOffset = buffer.bytesRead();
				short result = buffer.readShort((byte)1);
				builder.setResult(result);
				
				/* ignore reserved */
				errorOffset = buffer.bytesRead();
				buffer.read(2);
				
				/* last use */
				errorOffset = buffer.bytesRead();
				byte[] sData = buffer.read(20);
				String lastUse = new String(sData, Charset.forName("US-ASCII"));
				builder.setLastUse(lastUse);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
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
