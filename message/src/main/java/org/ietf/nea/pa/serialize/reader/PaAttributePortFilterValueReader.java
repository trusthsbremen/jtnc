package org.ietf.nea.pa.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValuePortFilter;
import org.ietf.nea.pa.attribute.PaAttributeValuePortFilterBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributePortFilterStatus;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributePortFilterValueReader implements ImReader<PaAttributeValuePortFilter>{

	private PaAttributeValuePortFilterBuilder builder;
	
	PaAttributePortFilterValueReader(PaAttributeValuePortFilterBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PaAttributeValuePortFilter read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValuePortFilter value = null;
		builder = (PaAttributeValuePortFilterBuilder)builder.clear();

		try{
			
			try{
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];

				do{
					/* attribute values */
					PaAttributePortFilterStatus blocked;
					short protocol;
					int port;
					int count = 0;
					
					/* ignore reserved but get blocked*/
					byteSize = 1;
					buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
					blocked = PaAttributePortFilterStatus.fromBlockedBit((byte)(buffer[0] & 0x01));
					count += byteSize;

					/* protocol */
					byteSize = 1;
					buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
					protocol = ByteArrayHelper.toShort(buffer);
					count += byteSize;
					
					/* port number */
					byteSize = 2;
					buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
					port = ByteArrayHelper.toInt(buffer);
					count += byteSize;
					
					this.builder.addEntries(new PortFilterEntry(blocked, protocol, port));
					
					// TODO error offset is vague because it cannot be calculated to the exact offset. 
					
					errorOffset += count;
					
				}while(messageLength - errorOffset > 0);

			}catch (IOException e){
				throw new SerializationException("Returned data for attribute value is to short or stream may be closed.",e,true);
			}

			value = (PaAttributeValuePortFilter)builder.toValue();
			
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
