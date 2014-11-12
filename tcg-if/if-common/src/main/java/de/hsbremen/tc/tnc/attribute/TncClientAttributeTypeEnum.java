package de.hsbremen.tc.tnc.attribute;

public enum TncClientAttributeTypeEnum implements TncAttributeType{

    /**
     * Contents of SOHR (type byte [], may get from an IMCConnection)
     */
    TNC_ATTRIBUTEID_SOHR (0x00559708),

    /**
     * Contents of SSOHR (type byte [], may get from an IMCConnection)
     */
    TNC_ATTRIBUTEID_SSOHR (0x00559709),

    /**
     * Flag to indicate if IMC supports TNCS sending first message 
	 * (type boolean, may get from a IMC) 
     */
    TNC_ATTRIBUTEID_IMC_SPTS_TNCS1 (0x0055970F),

    /**
     * IMC identifier assigned by the TNCC when the TNCC loaded 
	 * this IMC
     */
    TNC_ATTRIBUTEID_PRIMARY_IMC_ID (0x00559711);
	
   	
	private long id;
	
	private TncClientAttributeTypeEnum(long id){
		this.id = id;
	}

	@Override
	public long id() {
		return this.id; 
	}

}
