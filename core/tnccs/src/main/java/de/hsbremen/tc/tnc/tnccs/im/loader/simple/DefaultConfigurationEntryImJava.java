package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.net.URL;

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntry;

public class DefaultConfigurationEntryImJava implements ConfigurationEntry{

	private final String name;
	private final String mainClassName;
	private final URL path;
	
	public DefaultConfigurationEntryImJava(String name, String mainClassName, URL path) {
		this.name = name;
		this.mainClassName = mainClassName;
		this.path = path;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the mainClassName
	 */
	public String getMainClassName() {
		return this.mainClassName;
	}

	/**
	 * @return the path
	 */
	public URL getPath() {
		return this.path;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.mainClassName == null) ? 0 : this.mainClassName
						.hashCode());
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result
				+ ((this.path == null) ? 0 : this.path.hashCode());
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
		DefaultConfigurationEntryImJava other = (DefaultConfigurationEntryImJava) obj;
		if (this.mainClassName == null) {
			if (other.mainClassName != null) {
				return false;
			}
		} else if (!this.mainClassName.equals(other.mainClassName)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.path == null) {
			if (other.path != null) {
				return false;
			}
		} else if (!this.path.equals(other.path)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ImConfigurationEntry [name=" + this.name + ", mainClassName="
				+ this.mainClassName + ", path=" + this.path.toString() + "]";
	}

	
	
}
