package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;

public class DefaultSessionAttributes implements Attributed {
	
	private final String tnccsProtocol;
	private final String tnccsVersion;
	private final long maxRoundTrips;
	
	private String preferedLanguage;
	
	
	public DefaultSessionAttributes(String tnccsProtocol, String tnccsVersion){
		this(tnccsProtocol, tnccsVersion, HSBConstants.HSB_DEFAULT_LANGUAGE, HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN);
	}
	
	
	public DefaultSessionAttributes(String tnccsProtocol, String tnccsVersion,
			String preferedLanguage, long maxRoundTrips) {
		this.tnccsProtocol = tnccsProtocol;
		this.tnccsVersion = tnccsVersion;
		this.maxRoundTrips = maxRoundTrips;
		this.preferedLanguage = preferedLanguage;
		
	}

	/**
	 * @return the maxRoundTrips
	 */
	public long getMaxRoundTrips() {
		return this.maxRoundTrips;
	}

	/**
	 * @param preferedLanguage the preferedLanguage to set
	 */
	public void setPreferedLanguage(String preferedLanguage) {
		this.preferedLanguage = preferedLanguage;
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
