package org.ietf.nea.pb.message.util;

public class PbMessageValueErrorParameterVersion extends AbstractPbMessageValueErrorParameter{

    
    private final short badVersion;  //8 bit(s)
    private final short maxVersion;  //8 bit(s)
    private final short minVersion;  //8 bit(s)
    
	public PbMessageValueErrorParameterVersion(long length, short badVersion,
			short maxVersion, short minVersion) {
		super(length);
		this.badVersion = badVersion;
		this.maxVersion = maxVersion;
		this.minVersion = minVersion;
	}

	/**
	 * @return the badVersion
	 */
	public short getBadVersion() {
		return this.badVersion;
	}

	/**
	 * @return the maxVersion
	 */
	public short getMaxVersion() {
		return this.maxVersion;
	}

	/**
	 * @return the minVersion
	 */
	public short getMinVersion() {
		return this.minVersion;
	}
}
