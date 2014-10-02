package org.ietf.nea.pb.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageHeader;
import org.ietf.nea.pb.message.PbMessageHeaderBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;

public class PbMessageHeaderReader implements TnccsReader<PbMessageHeader>{

	private PbMessageHeaderBuilder builder;
	
	public PbMessageHeaderReader(PbMessageHeaderBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PbMessageHeader read(InputStream in, long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PbMessageHeader messageHeader = null;
		builder = (PbMessageHeaderBuilder)builder.clear();

		try{
			try{
				
			int byteSize = 0;
			byte[] buffer = new byte[byteSize];
			
			/* flags */
			byteSize = 1;
			buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
			builder.setFlags(buffer[0]);
			errorOffset += byteSize;
			
			/* vendor ID */
			byteSize = 3;
			buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
			long vendorId = ByteArrayHelper.toLong(buffer);
			builder.setVendorId(vendorId);
			errorOffset += byteSize;
			
			/* message type */
			byteSize = 4;
			buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
			long messageType = ByteArrayHelper.toLong(buffer);
			builder.setType(messageType);
			errorOffset += byteSize;
			
			/* message length */
			byteSize = 4;
			buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
			long length = ByteArrayHelper.toLong(buffer);
			builder.setLength(length);
			errorOffset += byteSize;
			
			
			}catch (IOException e){
				throw new SerializationException("Returned data for batch header is to short or stream may be closed.",e,true);
			}

			messageHeader = (PbMessageHeader)builder.toMessageHeader();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return messageHeader;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLength.MESSAGE.length();
	}

}