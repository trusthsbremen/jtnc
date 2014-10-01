package org.ietf.nea.pb.serialize;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.serialize.util.TrackedBufferedInputStream;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbBatchSerializerBuffered implements TnccsSerializer<PbBatch>{

	private final TnccsSerializer<PbBatch> serializer;
	private final int bufferSize;
	
	PbBatchSerializerBuffered(final TnccsSerializer<PbBatch> serializer){
		this(serializer, 8192);
	}
	
	PbBatchSerializerBuffered(final TnccsSerializer<PbBatch> serializer, final int bufferSize){
		this.serializer = serializer;
		this.bufferSize = bufferSize;
	}
	
	@Override
	public void encode(PbBatch data, OutputStream out)
			throws SerializationException {
		
		BufferedOutputStream bOut = new BufferedOutputStream(out, this.bufferSize);
		serializer.encode(data, bOut);
		try {
			bOut.flush();
		} catch (IOException e) {
			new SerializationException("OutputStream could not be flushed.",e , true, 0);
		}
		
	}

	@Override
	public PbBatch decode(InputStream in, long length)
			throws SerializationException{
		
		TrackedBufferedInputStream tIn = new TrackedBufferedInputStream(in, this.bufferSize);
		PbBatch batch = null;
		try{
			batch = serializer.decode(tIn, length);
		}catch(ValidationException e){
			// TODO does not work, because some problems are discovered later as they are read. getTracked is to far gone at the time
			throw new SerializationException("ValidationException occured: " + e.getMessage(), e, false, tIn.getTracked());
		}
		
		return batch;
	}

}
