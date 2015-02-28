package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterString;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterStringBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeRemediationParameterStringValueReader implements ImReader<PaAttributeValueRemediationParameterString>{

	private PaAttributeValueRemediationParameterStringBuilder baseBuilder;
	
	PaAttributeRemediationParameterStringValueReader(PaAttributeValueRemediationParameterStringBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueRemediationParameterString read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueRemediationParameterString value = null;
		PaAttributeValueRemediationParameterStringBuilder builder = (PaAttributeValueRemediationParameterStringBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				// First 4 bytes are the reason string length.
				errorOffset = buffer.bytesRead();
				long reasonLength =  buffer.readLong((byte)4);
				
				errorOffset = buffer.bytesRead();
				// FIXME this could lead to trouble because java can only handle a String of Integer.MAX_VALUE 
				// the length is shortened here and may switch to a negative value.
				byte[] sData = buffer.read((int)reasonLength);
				String reasonString = new String(sData, Charset.forName("UTF-8"));
				builder.setRemediationString(reasonString);
				errorOffset += reasonLength;
				
				// Last byte is the language code length;
				errorOffset = buffer.bytesRead();
				int langLength = buffer.readShort((byte)1);
				
				errorOffset = buffer.bytesRead();
				byte[] lData = buffer.read(langLength);
				String langCode = new String(lData, Charset.forName("US-ASCII"));
				builder.setLangCode(langCode);
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueRemediationParameterString)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
	
		return PaAttributeTlvFixedLengthEnum.REM_PAR_STR.length(); 
	}
}
