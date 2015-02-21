package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.RawMessageHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersion;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersionBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeErrorInformationUnsupportedVersionValueReader implements ImReader<PaAttributeValueErrorInformationUnsupportedVersion>{

	private PaAttributeValueErrorInformationUnsupportedVersionBuilder baseBuilder;
	
	PaAttributeErrorInformationUnsupportedVersionValueReader(PaAttributeValueErrorInformationUnsupportedVersionBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueErrorInformationUnsupportedVersion read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueErrorInformationUnsupportedVersion value = null;
		PaAttributeValueErrorInformationUnsupportedVersionBuilder builder = (PaAttributeValueErrorInformationUnsupportedVersionBuilder) this.baseBuilder.newInstance();
		
		try{
			
			try{

				/* message header */
				/* copy version */
				errorOffset = buffer.bytesRead();
				short version = buffer.readShort((byte)1);

				/* copy reserved */
				byte[] reserved = buffer.read(3);
	
				/* copy identifier */
				long identifier = buffer.readLong((byte)4);
	
				builder.setMessageHeader(new RawMessageHeader(version, reserved, identifier));
				
				
				/* max version */
				errorOffset = buffer.bytesRead();
				short maxVersion =  buffer.readShort((byte)1);
				builder.setMaxVersion(maxVersion);
				
				/* min version */
				errorOffset = buffer.bytesRead();
				short minVersion = buffer.readShort((byte)1);
				builder.setMinVersion(minVersion);

				
				/* ignore reserved */
				errorOffset = buffer.bytesRead();
				buffer.read(2);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueErrorInformationUnsupportedVersion)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
	
		return (byte)(PaAttributeTlvFixedLengthEnum.ERR_INF.length() + 4); // 4 = min + max and reserved
	}
}
