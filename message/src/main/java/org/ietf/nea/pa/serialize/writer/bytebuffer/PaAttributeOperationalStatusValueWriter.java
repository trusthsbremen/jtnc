package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.ietf.nea.pa.attribute.PaAttributeValueOperationalStatus;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeOperationalStatusValueWriter implements ImWriter<PaAttributeValueOperationalStatus>{

	private static final byte[] RESERVED = new byte[]{0,0};
	
	private SimpleDateFormat dateFormater;
	
	PaAttributeOperationalStatusValueWriter() {
		this.dateFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		this.dateFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override
	public void write(final PaAttributeValueOperationalStatus data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueOperationalStatus aValue = data;
		
		try{
			/* status 8 bit(s) */
			buffer.writeUnsignedByte(aValue.getStatus().status());
			
			/* result 8 bit(s) */
			buffer.writeUnsignedByte(aValue.getResult().result());
			
			/* reserved 24 bit(s) */
			buffer.write(RESERVED);
			
			
			/* last use 160 bit(s) */
			buffer.write(this.dateFormater.format(aValue.getLastUse()).getBytes(Charset.forName("US-ASCII")));
			
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
