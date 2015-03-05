package org.ietf.nea.pt.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelection;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelectionBuilder;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageSaslMechanismSelectionValueReader implements TransportReader<PtTlsMessageValueSaslMechanismSelection>{

	private PtTlsMessageValueSaslMechanismSelectionBuilder baseBuilder;
	
	PtTlsMessageSaslMechanismSelectionValueReader(PtTlsMessageValueSaslMechanismSelectionBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PtTlsMessageValueSaslMechanismSelection read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageValueSaslMechanismSelection mValue = null;
				PtTlsMessageValueSaslMechanismSelectionBuilder builder = (PtTlsMessageValueSaslMechanismSelectionBuilder)this.baseBuilder.newInstance();
				
				try{
					try{

						/* reserved + name length */
						errorOffset = buffer.bytesRead();
						byte b = buffer.readByte();
						int nameLength = b & 0x1F;
							
						/* name */
						byte[] sData = buffer.read(nameLength);
						String name = new String(sData, Charset.forName("US-ASCII")); 
						SaslMechanismEntry mech = new SaslMechanismEntry(name);
						builder.setMechanism(mech);
						
						/* optional initial SASL data */
						int counter = (int)(length - 1) - sData.length;
						if(counter > 0){
							errorOffset = buffer.bytesRead();
							byte[] initialSaslMsg = buffer.read(counter);
							builder.setOptionalInitialSaslMessage(initialSaslMsg);
						}


					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mValue = (PtTlsMessageValueSaslMechanismSelection)builder.toObject();
					
				}catch (RuleException e){
					throw new ValidationException(e.getMessage(), e, errorOffset);
				}
				
				return mValue;
	}
	
	@Override
	public byte getMinDataLength() {
		return PtTlsMessageTlvFixedLengthEnum.SASL_SEL.length();
	}
}
