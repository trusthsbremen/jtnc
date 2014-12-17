package org.ietf.nea.pt.value;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.message.util.ByteBuffer;

public class PtTlsMessageValuePbBatchBuilderIetf implements
		PtTlsMessageValuePbBatchBuilder {
	
	private long length;
	private ByteBuffer tnccsData; // variable string
	
	public PtTlsMessageValuePbBatchBuilderIetf(){
		this.length = 0;
		this.tnccsData = null;
	}
	
	@Override
	public PtTlsMessageValuePbBatchBuilder setTnccsData(ByteBuffer data) throws RuleException {
		// no checks necessary
		if(data != null){
			this.tnccsData = data;
			this.length = data.bytesWritten();
		}
		
		return this;
	}
	
	@Override
	public PtTlsMessageValuePbBatch toObject(){
		if(this.tnccsData == null){
			throw new IllegalStateException("The batch data has to be set.");
		}
		return new PtTlsMessageValuePbBatch(this.length,tnccsData);
	}

	@Override
	public PtTlsMessageValuePbBatchBuilder newInstance() {
		
		return new PtTlsMessageValuePbBatchBuilderIetf();
	}


}
