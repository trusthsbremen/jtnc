package org.ietf.nea.pb.message.util;

public class PbMessageValueErrorParameterOffset extends AbstractPbMessageValueErrorParameter{

    private final long offset;  //32 bit(s)
    
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
