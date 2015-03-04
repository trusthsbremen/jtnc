package org.ietf.nea.pt.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanisms;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismsBuilder;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageSaslMechanismsValueReader implements TransportReader<PtTlsMessageValueSaslMechanisms>{

	private PtTlsMessageValueSaslMechanismsBuilder baseBuilder;
	
	PtTlsMessageSaslMechanismsValueReader(PtTlsMessageValueSaslMechanismsBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PtTlsMessageValueSaslMechanisms read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageValueSaslMechanisms mValue = null;
				PtTlsMessageValueSaslMechanismsBuilder builder = (PtTlsMessageValueSaslMechanismsBuilder)this.baseBuilder.newInstance();
				
				try{
					try{
						
						
						long counter = 0;
						while((length - counter) > 0){
							// TODO error offset is vague because it cannot be calculated to the exact offset.  
							errorOffset = buffer.bytesRead();
							
							/* reserved + name length */
							byte b = buffer.readByte();
							int nameLength = b & 0x1F;
							counter ++;
							
							/* name */
							byte[] sData = buffer.read(nameLength);
							String name = new String(sData, Charset.forName("US-ASCII")); 
							SaslMechanismEntry mech = new SaslMechanismEntry(name);
							counter += sData.length;
							
							builder.addMechanism(mech);
						};


					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mValue = (PtTlsMessageValueSaslMechanisms)builder.toObject();
					
				}catch (RuleException e){
					throw new ValidationException(e.getMessage(), e, errorOffset);
				}
				
				return mValue;
	}
	
	@Override
	public byte getMinDataLength() {
		return 0;
	}
}