package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.attribute.Attributed;

public interface SessionAttributes extends Attributed{

	/**
	 * @return the tnccsProtocol
	 */
	public abstract String getTnccsProtocol();

	/**
	 * @return the tnccsVersion
	 */
	public abstract String getTnccsVersion();

	/**
	 * @return the currentRoundTrips
	 */
	public abstract long getCurrentRoundTrips();

	/**
	 * @return the preferedLanguage
	 */
	public abstract String getPreferedLanguage();

	/**
	 * @return the maxRoundTrips
	 */
	public abstract void setCurrentRoundTrips(long roundtrips);

	/**
	 * @param preferedLanguage the preferedLanguage to set
	 */
	public abstract void setPreferedLanguage(String preferedLanguage);

}