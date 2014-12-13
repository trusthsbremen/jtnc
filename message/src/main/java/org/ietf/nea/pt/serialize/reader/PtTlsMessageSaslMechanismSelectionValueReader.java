package org.ietf.nea.pt.serialize.reader;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelection;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelectionBuilder;
import org.ietf.nea.pt.value.util.SaslMechanism;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageSaslMechanismSelectionValueReader implements TransportReader<PtTlsMessageValueSaslMechanismSelection>{

	private PtTlsMessageValueSaslMechanismSelectionBuilder builder;
	
	PtTlsMessageSaslMechanismSelectionValueReader(PtTlsMessageValueSaslMechanismSelectionBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PtTlsMessageValueSaslMechanismSelection read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageValueSaslMechanismSelection mValue = null;
				PtTlsMessageValueSaslMechanismSelectionBuilder valueBuilder = (PtTlsMessageValueSaslMechanismSelectionBuilder)builder.clear();
				
				try{
					try{

						/* reserved + name length */
						errorOffset = buffer.bytesRead();
						byte b = buffer.readByte();
						int nameLength = b & 0x1F;
							
						/* name */
						byte[] sData = buffer.read(nameLength);
						String name = new String(sData, Charset.forName("US-ASCII")); 
						SaslMechanism mech = new SaslMechanism(name);
						valueBuilder.setMechanism(mech);
						
						/* optional initial SASL data */
						int counter = (int)(length - 1) - sData.length;
						if(counter > 0){
							errorOffset = buffer.bytesRead();
							byte[] initialSaslMsg = buffer.read(counter);
							valueBuilder.setInitialSaslMessage(initialSaslMsg);
						}


					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mValue = (PtTlsMessageValueSaslMechanismSelection)valueBuilder.toValue();
					
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
