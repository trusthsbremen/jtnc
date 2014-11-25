package de.hsbremen.tc.tnc.session;

public class SessionParameter {

	private final String tnccsProtocol;
	private final String tnccsVersion;
	private String preferredLanguage;
	
	public SessionParameter(String tnccsProtocol, String tnccsVersion,
			String preferredLanguage) {
		
		this.tnccsProtocol = tnccsProtocol;
		this.tnccsVersion = tnccsVersion;
		this.preferredLanguage = preferredLanguage;
	
	}

	/**
	 * @return the tnccsProtocol
	 */
	public String getTnccsProtocol() {
		return this.tnccsProtocol;
	}

	/**
	 * @return the tnccsVersion
	 */
	public String getTnccsVersion() {
		return this.tnccsVersion;
	}

	/**
	 * @return the preferredLanguage
	 */
	public String getPreferredLanguage() {
		return this.preferredLanguage;
	}

	/**
	 * @param preferredLanguage the preferredLanguage to set
	 */
	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}
	
	
	
	
}
