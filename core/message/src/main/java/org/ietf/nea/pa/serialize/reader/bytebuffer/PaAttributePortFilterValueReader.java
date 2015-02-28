package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.PaAttributeValuePortFilter;
import org.ietf.nea.pa.attribute.PaAttributeValuePortFilterBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributePortFilterStatus;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributePortFilterValueReader implements ImReader<PaAttributeValuePortFilter>{

	private PaAttributeValuePortFilterBuilder baseBuilder;
	
	PaAttributePortFilterValueReader(PaAttributeValuePortFilterBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValuePortFilter read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValuePortFilter value = null;
		PaAttributeValuePortFilterBuilder builder = (PaAttributeValuePortFilterBuilder)this.baseBuilder.newInstance();

		try{
			
			try{

				long counter = 0;
				do{
					// TODO error offset is vague because it cannot be calculated to the exact offset. 
					errorOffset = buffer.bytesRead();
					
					/* attribute values */
					PaAttributePortFilterStatus blocked;
					short protocol;
					int port;

					/* ignore reserved but get blocked*/
					byte bit = buffer.readByte();
					blocked = PaAttributePortFilterStatus.fromBlockedBit((byte)(bit & 0x01));
					counter++;

					/* protocol */
					protocol = buffer.readShort((byte)1);
					counter++;

					/* port number */
					port = buffer.readInt((byte)2);
					counter += 2;

					builder.addEntries(new PortFilterEntry(blocked, protocol, port));

				}while(messageLength - counter > 0);

			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValuePortFilter)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
	
		return PaAttributeTlvFixedLengthEnum.PORT_FT.length(); 
	}
}
