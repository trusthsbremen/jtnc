package org.ietf.nea.pa.attribute.util;

import java.util.Arrays;

public class RawMessageHeader {

	private final short version;
	private final byte[] reserved;
	private final long identifier;
	
	
	public RawMessageHeader(short version, byte[] reserved, long identifier) {
		this.version = version;
		this.reserved = (reserved != null) ? reserved : new byte[0] ;
		this.identifier = identifier;
	}
	/**
	 * @return the version
	 */
	public short getVersion() {
		return this.version;
	}
	/**
	 * @return the reserved
	 */
	public byte[] getReserved() {
		return Arrays.copyOf(this.reserved, this.reserved.length);
	}
	/**
	 * @return the identifier
	 */
	public long getIdentifier() {
		return this.identifier;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (this.identifier ^ (this.identifier >>> 32));
		result = prime * result + Arrays.hashCode(this.reserved);
		result = prime * result + this.version;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RawMessageHeader other = (RawMessageHeader) obj;
		if (this.identifier != other.identifier) {
			return false;
		}
		if (!Arrays.equals(this.reserved, other.reserved)) {
			return false;
		}
		if (this.version != other.version) {
			return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RawMessageHeader [version=" + this.version + ", reserved="
				+ Arrays.toString(this.reserved) + ", identifier="
				+ this.identifier + "]";
	}
	
	
	
}
