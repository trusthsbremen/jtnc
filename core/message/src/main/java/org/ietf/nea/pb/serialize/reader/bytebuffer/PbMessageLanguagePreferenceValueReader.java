package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreferenceBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageLanguagePreferenceValueReader implements TnccsReader<PbMessageValueLanguagePreference>{

	private PbMessageValueLanguagePreferenceBuilder baseBuilder;
	
	PbMessageLanguagePreferenceValueReader(PbMessageValueLanguagePreferenceBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PbMessageValueLanguagePreference read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
	    NotNull.check("Buffer cannot be null.", buffer);
	    
		long errorOffset = 0;
		
		PbMessageValueLanguagePreference value = null;
		PbMessageValueLanguagePreferenceBuilder builder = (PbMessageValueLanguagePreferenceBuilder)this.baseBuilder.newInstance();

		try{
			
			try{

				/* language preference */
				errorOffset = buffer.bytesRead();
				// FIXME this could lead to trouble because java can only handle a String of Integer.MAX_VALUE 
				// the length is shortened here and may switch to a negative value.
				byte[] sData = buffer.read((int)messageLength);
				String languagePreference = new String(sData, Charset.forName("US-ASCII"));
				builder.setLanguagePreference(languagePreference);
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PbMessageValueLanguagePreference)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
	
		// no minimal length
		return 0; 
	}
}
