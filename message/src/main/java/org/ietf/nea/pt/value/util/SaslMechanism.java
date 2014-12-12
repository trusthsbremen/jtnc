package org.ietf.nea.pt.value.util;

import java.nio.charset.Charset;

public class SaslMechanism {

	private final byte nameLength;   //  5 bit(s)
	private final String name;     // variable length 1-20 byte(s)

	public SaslMechanism(String name) {
		this.name = name;
		this.nameLength = (byte)name.getBytes(Charset.forName("US-ASCII")).length;
	}

	/**
	 * @return the nameLength
	 */
	public byte getNameLength() {
		return this.nameLength;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + this.nameLength;
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
		SaslMechanism other = (SaslMechanism) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.nameLength != other.nameLength) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SaslMechanism [nameLength=" + this.nameLength + ", name="
				+ this.name + "]";
	}
	
	
	
	
}
