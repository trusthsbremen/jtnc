package de.hsbremen.tc.tnc.transport.example;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;

public class DefaultTransportAttributes implements Attributed{

	private String tVersion;
	private String tProtocol;
	private long maxMessageLength;
	private long maxRoundTrips;
	
	public DefaultTransportAttributes(String tProtocol, String tVersion){
		this(tProtocol, tVersion, HSBConstants.TCG_IM_MAX_MESSAGE_SIZE_UNKNOWN, HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN);
	}
	
	public DefaultTransportAttributes(String tProtocol, String tVersion, 
			long maxMessageLength, long maxRoundTrips) {
		this.tProtocol = tProtocol;
		this.tVersion = tVersion;
		this.maxMessageLength = maxMessageLength;
		this.maxRoundTrips = maxRoundTrips;
	}

	/**
	 * @return the tVersion
	 */
	public String geTransportVersion() {
		return this.tVersion;
	}

	/**
	 * @return the tProtocol
	 */
	public String geTransportProtocol() {
		return this.tProtocol;
	}

	/**
	 * @return the maxMessageLength
	 */
	public long getMaxMessageLength() {
		return this.maxMessageLength;
	}

	/**
	 * @return the maxRoundTrips
	 */
	public long getMaxRoundTrips() {
		return this.maxRoundTrips;
	}

	@Override
	public Object getAttribute(TncAttributeType type) throws TncException {
		
		if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFT_PROTOCOL)){
			return this.tProtocol;
		}
		
		if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFT_VERSION)){
			return this.tVersion;
		}
		
		if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_MESSAGE_SIZE)){
			return this.maxMessageLength;
		}
		
		if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_ROUND_TRIPS)){
			return this.maxRoundTrips;
		}
		
		throw new TncException("The attribute with ID " + type.id() + " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
	}

	@Override
	public void setAttribute(TncAttributeType type, Object value)
			throws TncException {
		throw new UnsupportedOperationException("The operation setAttribute(...) is not supported, because there are no attributes to set.");	
	}

}
