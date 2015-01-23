package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncHsbAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionAttributes;

public class DefaultSessionAttributes implements SessionAttributes {
	
	private final String tnccsProtocol;
	private final String tnccsVersion;
	private long currentRoundTrips;
	
	private String preferedLanguage;
	
	public DefaultSessionAttributes(String tnccsProtocol, String tnccsVersion){
		this(tnccsProtocol, tnccsVersion, HSBConstants.HSB_DEFAULT_LANGUAGE, HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN);
	}
	
	public DefaultSessionAttributes(String tnccsProtocol, String tnccsVersion,
			String preferedLanguage, long maxRoundTrips) {
		this.tnccsProtocol = tnccsProtocol;
		this.tnccsVersion = tnccsVersion;
		this.currentRoundTrips = 0;
		this.preferedLanguage = preferedLanguage;
		
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.base.simple.SessionAttributes#getTnccsProtocol()
	 */
	@Override
	public String getTnccsProtocol() {
		return this.tnccsProtocol;
	}


	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.base.simple.SessionAttributes#getTnccsVersion()
	 */
	@Override
	public String getTnccsVersion() {
		return this.tnccsVersion;
	}


	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.base.simple.SessionAttributes#getCurrentRoundTrips()
	 */
	@Override
	public long getCurrentRoundTrips() {
		return this.currentRoundTrips;
	}


	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.base.simple.SessionAttributes#getPreferedLanguage()
	 */
	@Override
	public String getPreferedLanguage() {
		return this.preferedLanguage;
	}


	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.base.simple.SessionAttributes#setCurrentRoundTrips(long)
	 */
	@Override
	public void setCurrentRoundTrips(long roundtrips) {
		// round trips cannot be negative and will return to zero if 
		// positive long range is overrun
		this.currentRoundTrips = (roundtrips >= 0)?roundtrips:0;
		
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.base.simple.SessionAttributes#setPreferedLanguage(java.lang.String)
	 */
	@Override
	public void setPreferedLanguage(String preferedLanguage) {
		if(preferedLanguage != null && !preferedLanguage.isEmpty()) {
			this.preferedLanguage = preferedLanguage;
		}
	}

	@Override
	public Object getAttribute(TncAttributeType type) throws TncException {
		if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFTNCCS_PROTOCOL)){
			return this.tnccsProtocol;
		}
		
		if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFTNCCS_VERSION)){
			return this.tnccsVersion;
		}
		
		if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_PREFERRED_LANGUAGE)){
			return this.preferedLanguage;
		}
		
		if(type.equals(TncHsbAttributeTypeEnum.HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS)){
			return this.currentRoundTrips;
		}
		
		throw new TncException("The attribute with ID " + type.id() + " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
	}

	@Override
	public void setAttribute(TncAttributeType type, Object value)
			throws TncException {
		if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_PREFERRED_LANGUAGE)){
			if(value instanceof String){
				this.preferedLanguage = (String)value;
			}
		}

		throw new TncException("The attribute with ID " + type.id() + " is unknown or not writeable.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
	}

}
