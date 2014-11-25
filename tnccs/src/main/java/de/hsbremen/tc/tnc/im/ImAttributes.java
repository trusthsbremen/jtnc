package de.hsbremen.tc.tnc.im;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncClientAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncServerAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;

public class ImAttributes implements Attributed {

	private final long primaryId;
	private boolean tncsFirstSupport;
	private String preferedLanguage;
	
	public ImAttributes(long primaryId) {
		this(primaryId, false, "Accept-Language: en");
	}
	
	public ImAttributes(long primaryId, boolean support) {
		this(primaryId, support, "Accept-Language: en");
	}

	public ImAttributes(long primaryId, boolean support, String preferredLanguage) {
		this.primaryId = primaryId;
		this.tncsFirstSupport = support;
		this.preferedLanguage = preferredLanguage;
	}
	
	public long getPrimaryId(){ 
		return this.primaryId; 
	}

	public boolean hasTncsFirstSupport(){
		return this.tncsFirstSupport;
	}

	public void setTncsFirstSupport(boolean support){
		this.tncsFirstSupport = support;
	}

	/**
	 * @return the preferedLanguage
	 */
	public String getPreferedLanguage() {
		return this.preferedLanguage;
	}

	/**
	 * @param preferedLanguage the preferedLanguage to set
	 */
	public void setPreferedLanguage(String preferedLanguage) {
		this.preferedLanguage = preferedLanguage;
	}

	@Override
	public Object getAttribute(TncAttributeType type) throws TncException {
		if(type.id() == TncClientAttributeTypeEnum.TNC_ATTRIBUTEID_PRIMARY_IMC_ID.id() ||
				type.id() == TncServerAttributeTypeEnum.TNC_ATTRIBUTEID_PRIMARY_IMV_ID.id()){
			return new Long(this.primaryId);
		}
		
		if(type.id() == TncClientAttributeTypeEnum.TNC_ATTRIBUTEID_IMC_SPTS_TNCS1.id()){
			return new Boolean(this.hasTncsFirstSupport());
		}
			
		
		if(type.id() == TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_PREFERRED_LANGUAGE.id()){
			return this.preferedLanguage;
		}
		
		throw new TncException("The attribute with ID " + type.id() + " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER, new Long(type.id()));
	}

	@Override
	public Object setAttribute(TncAttributeType type, Object value)
			throws TncException {
		throw new UnsupportedOperationException("The operation setAttribute(...) is not supported, because there are no attributes to set.");
	}
	
	
	
}