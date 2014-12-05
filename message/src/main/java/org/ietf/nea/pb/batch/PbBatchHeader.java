package org.ietf.nea.pb.batch;

import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatchHeader;

public class PbBatchHeader implements TnccsBatchHeader{

	 	private final short version;                       //  8 bit(s)
	    private final PbBatchDirectionalityEnum directionality;  //  1 bit(s)
	    private final PbBatchTypeEnum type;                      //  4 bit(s)
	    
	    private final long length;

	    PbBatchHeader(final short version, final PbBatchDirectionalityEnum directionality,
				final PbBatchTypeEnum type, final long length) {
			this.version = version;
	    	this.directionality = directionality;
			this.type = type;
			this.length = length;
		}
	    
		@Override
		public short getVersion() {
			return version;
		}    
		
		/**
	     * @return the directionality
	     */
	    public PbBatchDirectionalityEnum getDirectionality() {
	        return directionality;
	    }

	    /**
	     * @return the type
	     */
	    public PbBatchTypeEnum getType() {
	        return type;
	    }

	    /**
	     * @return the length
	     */
	    public long getLength() {
	        return length;
	    }
}
