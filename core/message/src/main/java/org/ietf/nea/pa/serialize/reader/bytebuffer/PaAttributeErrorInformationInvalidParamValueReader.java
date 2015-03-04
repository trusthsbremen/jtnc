package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParam;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParamBuilder;
import org.ietf.nea.pa.attribute.util.MessageHeaderDump;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeErrorInformationInvalidParamValueReader implements ImReader<PaAttributeValueErrorInformationInvalidParam>{

	private PaAttributeValueErrorInformationInvalidParamBuilder baseBuilder;
	
	PaAttributeErrorInformationInvalidParamValueReader(PaAttributeValueErrorInformationInvalidParamBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueErrorInformationInvalidParam read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueErrorInformationInvalidParam value = null;
		PaAttributeValueErrorInformationInvalidParamBuilder builder = (PaAttributeValueErrorInformationInvalidParamBuilder)this.baseBuilder.newInstance();
		
		try{
			
			try{
		
				errorOffset = buffer.bytesRead();
				/* message header */
				/* copy version */
				short version = buffer.readShort((byte)1);

				/* copy reserved */
				byte[] reserved = buffer.read(3);
	
				/* copy identifier */
				long identifier = buffer.readLong((byte)4);
	
				builder.setMessageHeader(new MessageHeaderDump(version, reserved, identifier));
				
				errorOffset = buffer.bytesRead();
				/* offset */
				long offset =  buffer.readLong((byte)4);
				builder.setOffset(offset);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueErrorInformationInvalidParam)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
	
		return (byte)(PaAttributeTlvFixedLengthEnum.ERR_INF.length() + 4); // 4 = offset 
	}
}
