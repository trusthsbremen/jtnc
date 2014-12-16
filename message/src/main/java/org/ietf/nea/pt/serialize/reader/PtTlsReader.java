package org.ietf.nea.pt.serialize.reader;

import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.message.DefaultTransportMessageContainer;
import org.ietf.nea.pt.message.PtTlsMessage;
import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.validate.rules.MinMessageLength;
import org.ietf.nea.pt.value.PtTlsMessageValue;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.Combined;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;

class PtTlsReader implements TransportReader<TransportMessageContainer>, Combined<TransportReader<PtTlsMessageValue>> {

	//private static final Logger LOGGER = LoggerFactory.getLogger(PtTlsReader.class);
			
	private final TransportReader<PtTlsMessageHeader> mHeadReader;
	private final Map<Long, Map<Long, TransportReader<PtTlsMessageValue>>> valueReader;
	
	PtTlsReader(TransportReader<PtTlsMessageHeader> mHeadReader) {
		this(mHeadReader, new HashMap<Long, Map<Long, TransportReader<PtTlsMessageValue>>>());
	}
	
	
	public PtTlsReader(TransportReader<PtTlsMessageHeader> mHeadReader,
			Map<Long, Map<Long, TransportReader<PtTlsMessageValue>>> valueReader) {
		this.mHeadReader = mHeadReader;
		this.valueReader = valueReader;
	}

	@Override
	public TransportMessageContainer read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException {
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be null.");
		}
		
		if(!buffer.isReadable()){
			throw new IllegalArgumentException("Buffer must be readable.");
		}
		
		List<ValidationException> minorExceptions = new LinkedList<>();
		
		/* Message header */
		PtTlsMessageHeader mHead = null;
		
		ByteBuffer fullBuffer = null;
		
		byte[] headerByteCopy = buffer.read(this.getMinDataLength());
		
		if(headerByteCopy.length < this.getMinDataLength()){
			throw new SerializationException("Buffer does not contain enough data.",true, headerByteCopy.length);
		}
		
		ByteBuffer tempBuf = new DefaultByteBuffer(headerByteCopy.length);
		tempBuf.write(headerByteCopy);
		
		// ignore length here, because header has a length field.
		try{
			mHead = mHeadReader.read(tempBuf, -1);
		}catch(ValidationException e){
			throw new ValidationException(e.getMessage(), e.getCause(), e.getExceptionOffset(), headerByteCopy);
		}
		
		try{
			if(buffer.isReadable() && !buffer.isWriteable()){
				fullBuffer = new DefaultByteBuffer(mHead.getLength());
				fullBuffer.write(headerByteCopy); // copy the header data back in
				fullBuffer.write(buffer.read(mHead.getLength()-this.getMinDataLength())); // get the rest of the data
				fullBuffer.read(headerByteCopy.length); // set correct position
			}else{
				fullBuffer = buffer;
			}
			
		}catch(BufferUnderflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
		
		/* Value */					
		long vendor = mHead.getVendorId();
		long type = mHead.getMessageType();
		
		PtTlsMessageValue mValue = null;
		
		
		if(valueReader.containsKey(vendor)){
			if(valueReader.get(vendor).containsKey(type)){			
				TransportReader<PtTlsMessageValue> vr = valueReader.get(vendor).get(type);
							
				// Do a length check before trying to parse the value.
				long valueLength = mHead.getLength()-PtTlsMessageTlvFixedLengthEnum.MESSAGE.length();
				
				try{
					MinMessageLength.check(valueLength, vr.getMinDataLength());
				}catch(RuleException e1){
					// Remove 8 from header offset because this is a late header check. Length is the second last field
					// in the message header.
					throw new ValidationException(e1.getMessage(), e1,PtTlsMessageTlvFixedLengthEnum.MESSAGE.length() - 8, headerByteCopy);
				}
				
				try{
					mValue = vr.read(fullBuffer, valueLength);
				}catch(ValidationException e){
					throw new ValidationException(e.getMessage(),e.getCause(), e.getExceptionOffset(), headerByteCopy);
				}
							
			}else{
				throw new ValidationException("Message type is not supported.", 
						new RuleException(
								"Message with vendor ID " +vendor+ " and type "+type+" not supported.", 
								false, 
								PtTlsMessageErrorCodeEnum.IETF_UNSUPPORTED_MESSAGE_TYPE.code(), 
								PtTlsErrorCauseEnum.MESSAGE_TYPE_NOT_SUPPORTED.number()
						), 
						4,headerByteCopy,Long.toString(vendor), Long.toString(type));
			} 
		}else{
			throw new ValidationException("Message type is not supported.", 
					new RuleException(
							"Message with vendor ID " +vendor+ " and type "+type+" not supported.", 
							false, 
							PtTlsMessageErrorCodeEnum.IETF_UNSUPPORTED_MESSAGE_TYPE.code(), 
							PtTlsErrorCauseEnum.MESSAGE_TYPE_NOT_SUPPORTED.number()
					), 
					1,headerByteCopy, Long.toString(vendor), Long.toString(type));
		}
		
		
		PtTlsMessage m = new PtTlsMessage(mHead, mValue);
		
		return new DefaultTransportMessageContainer(m, minorExceptions);
		
	}

	@Override
	public byte getMinDataLength() {
		// message header is minimal requirement
		return mHeadReader.getMinDataLength();
	}

	@Override
	public void add(final Long vendorId, final Long messageType,
			final TransportReader<PtTlsMessageValue> reader) {
		if(valueReader.containsKey(vendorId)){
			valueReader.get(vendorId).put(messageType, reader);
		}else{
			valueReader.put(vendorId, new HashMap<Long, TransportReader<PtTlsMessageValue>>());
			valueReader.get(vendorId).put(messageType, reader);
		}
		
	}

	@Override
	public void remove(final Long vendorId, final Long messageType) {
		if(valueReader.containsKey(vendorId)){
			if(valueReader.get(vendorId).containsKey(messageType)){
				valueReader.get(vendorId).remove(messageType);
			}
			if(valueReader.get(vendorId).isEmpty()){
				valueReader.remove(vendorId);
			}
		}
	}

}
