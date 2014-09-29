package org.ietf.nea.pb.batch;

import java.util.Collections;
import java.util.List;

import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;

public class PbBatch implements TnccsBatch {
	
    private final byte version;                       //  8 bit(s)
    private final PbBatchDirectionalityEnum directionality;  //  1 bit(s)
    private final int reserved;       // 19 bit(s) should be 0
    private final PbBatchTypeEnum type;                      //  4 bit(s)
    
    private final long length;                              // 32 bit(s) min value is 8 for the 8 bytes in this header
    private final List<PbMessage> messages;

    
    
	PbBatch(byte version, PbBatchDirectionalityEnum directionality, int reserved,
			PbBatchTypeEnum type, long length, List<PbMessage> messages) {
		
		this.version = version;
		this.directionality = directionality;
		this.reserved = reserved;
		this.type = type;
		this.length = length;
		this.messages = messages;
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

    /**
     * @return the messages
     */
    public List<PbMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /**
     * @return the version
     */
    public byte getVersion() {
        return version;
    }

    /**
     * @return the reserved
     */
    public int getReserved() {
        return reserved;
    }
}
