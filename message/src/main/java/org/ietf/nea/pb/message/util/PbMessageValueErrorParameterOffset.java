package org.ietf.nea.pb.message.util;

public class PbMessageValueErrorParameterOffset extends AbstractPbMessageValueErrorParameter{

    //protected final short reserved;   // 16 bit(s) should be 0
    
    private final long offset;  //16 bit(s)
    
    PbMessageValueErrorParameterOffset(final long length,
			final long offset) {
		super(length);
		this.offset = offset;
	}

	/**
     * @return the recommendationEnum
     */
    public long getOffset() {
        return this.offset;
        
    }

}
