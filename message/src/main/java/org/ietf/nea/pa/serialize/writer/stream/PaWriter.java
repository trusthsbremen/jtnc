package org.ietf.nea.pa.serialize.writer.stream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeValue;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.message.PaMessageHeader;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.message.ImMessage;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImWriter;
import de.hsbremen.tc.tnc.message.util.Combined;

class PaWriter implements ImWriter<ImMessage>, Combined<ImWriter<PaAttributeValue>> {

	private final ImWriter<PaMessageHeader> mHeadWriter;
	private final ImWriter<PaAttributeHeader> aHeadWriter;
	private final Map<Long, Map<Long, ImWriter<PaAttributeValue>>> valueWriter;
	
	PaWriter(ImWriter<PaMessageHeader> mHeadWriter,
			ImWriter<PaAttributeHeader> aHeadWriter) {
		this(mHeadWriter, aHeadWriter, new HashMap<Long, Map<Long, ImWriter<PaAttributeValue>>>());

	}
	
	
	PaWriter(ImWriter<PaMessageHeader> mHeadWriter,
			ImWriter<PaAttributeHeader> aHeadWriter,
			Map<Long, Map<Long, ImWriter<PaAttributeValue>>> valueWriter) {
		this.mHeadWriter = mHeadWriter;
		this.aHeadWriter = aHeadWriter;
		this.valueWriter = valueWriter;
	}

	@Override
	public void write(final ImMessage message, final OutputStream out)
			throws SerializationException{
		if(message == null){
			throw new NullPointerException("Message cannot be null.");
		}
		if(out == null){
			throw new NullPointerException("OutputStream cannot be null.");
		}
		
		if(!(message instanceof PaMessage)){
			throw new IllegalArgumentException("Message of type " + message.getClass().getCanonicalName() + " is not supported. Message must be of type " +PaMessage.class.getCanonicalName()+ "." );
		}
		
		PaMessage paMessage = (PaMessage) message;
		
		BufferedOutputStream bOut = (out instanceof BufferedOutputStream)? (BufferedOutputStream)out: new BufferedOutputStream(out);

		/* message header */
		
		PaMessageHeader mHead = paMessage.getHeader();
		mHeadWriter.write(mHead, bOut);
		
		/* attributes */
		List<PaAttribute> attributes = paMessage.getAttributes();
		if(attributes != null){
			for (PaAttribute paAttribute : attributes) {
				
				PaAttributeHeader aHead = paAttribute.getHeader();
				long vendor = aHead.getVendorId();
				long type = aHead.getAttributeType();
				
				if(valueWriter.containsKey(vendor)){
					if(valueWriter.get(vendor).containsKey(type)){
						aHeadWriter.write(aHead, bOut);
						valueWriter.get(vendor).get(type).write(paAttribute.getValue(), bOut);
					}else{
						throw new SerializationException("Attribute type is not supported.",false , Long.toString(vendor), Long.toString(type));
					}
				} else {
					throw new SerializationException("Attribute vendor is not supported.",false, Long.toString(vendor), Long.toString(type));
				}
			}
		}
		
		try {
			bOut.flush();
		} catch (IOException e) {
			throw new SerializationException("Stream could not be flushed.", e, true);
		}

	}

	@Override
	public void add(final Long vendorId, final Long messageType,
			final ImWriter<PaAttributeValue> reader) {
		if(valueWriter.containsKey(vendorId)){
			valueWriter.get(vendorId).put(messageType, reader);
		}else{
			valueWriter.put(vendorId, new HashMap<Long, ImWriter<PaAttributeValue>>());
			valueWriter.get(vendorId).put(messageType, reader);
		}
		
	}

	@Override
	public void remove(final Long vendorId, final Long messageType) {
		if(valueWriter.containsKey(vendorId)){
			if(valueWriter.get(vendorId).containsKey(messageType)){
				valueWriter.get(vendorId).remove(messageType);
			}
			if(valueWriter.get(vendorId).isEmpty()){
				valueWriter.remove(vendorId);
			}
		}
	}

}
