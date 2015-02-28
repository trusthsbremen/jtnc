package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.PbMessageValueReasonStringBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageReasonStringValueReader implements TnccsReader<PbMessageValueReasonString>{

	private PbMessageValueReasonStringBuilder baseBuilder;
	
	PbMessageReasonStringValueReader(PbMessageValueReasonStringBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PbMessageValueReasonString read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
	    NotNull.check("Buffer cannot be null.", buffer);
	    
		long errorOffset = 0;
		
		PbMessageValueReasonString value = null;
		PbMessageValueReasonStringBuilder builder = (PbMessageValueReasonStringBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				/* First 4 bytes are the reason string length. */
				errorOffset = buffer.bytesRead();
				long reasonLength =  buffer.readLong((byte)4);

				/* reason string */
				errorOffset = buffer.bytesRead();
				// FIXME this could lead to trouble because java can only handle a String of Integer.MAX_VALUE 
				// the length is shortened here and may switch to a negative value.
				byte[] sData = buffer.read((int)reasonLength);
				String reasonString = new String(sData, Charset.forName("UTF-8"));
				builder.setReasonString(reasonString);

				
				/* Last byte is the language code length */
				errorOffset = buffer.bytesRead();
				short langLength =  buffer.readShort((byte)1);
				
				/* language code */ 
				errorOffset = buffer.bytesRead();
				byte[] lData = buffer.read(langLength);
				String langCode = new String(lData, Charset.forName("US-ASCII"));
				builder.setLangCode(langCode);
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PbMessageValueReasonString)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}


	@Override
	public byte getMinDataLength() {
	
		return PbMessageTlvFixedLengthEnum.REA_STR_VALUE.length(); 
	}
}
