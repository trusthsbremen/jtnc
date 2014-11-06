package de.hsbremen.tc.tnc.imhandler.loader;

import java.net.URL;

public class ImLoadParameter {

	private final String name;
	private final String mainClassName;
	private final URL path;
	
	public ImLoadParameter(String name, String mainClassName, URL path) {
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

	
	
}
