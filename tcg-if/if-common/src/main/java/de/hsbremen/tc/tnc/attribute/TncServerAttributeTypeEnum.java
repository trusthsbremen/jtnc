package de.hsbremen.tc.tnc.attribute;


public enum TncServerAttributeTypeEnum implements TncAttributeType {

    /**
     * Reason for IMV Recommendation (type String, may set for
     * an IMVConnection)
     */
    TNC_ATTRIBUTEID_REASON_STRING (2L),
    
    /**
     * Language(s) for Reason String as an RFC 3066 language tag
     * (type String, may set for an IMVConnection)
     */
    TNC_ATTRIBUTEID_REASON_LANGUAGE (3L),

    /**
     * Contents of SOH (type byte [], may get from an IMVConnection)
     */
    TNC_ATTRIBUTEID_SOH (0x00559706L),

    /**
     * Contents of SSOH (type byte [], may get from an IMVConnection)
     */
    TNC_ATTRIBUTEID_SSOH (0x00559707L),

    /**
     * Primary ID for IMV (type Long, may get from a TNCS or 
	 * an IMVConnection)
     */
    TNC_ATTRIBUTEID_PRIMARY_IMV_ID (0x00559710L);
	
	private long id;
	
	private TncServerAttributeTypeEnum(long id){
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.TncAttributeType#id()
	 */
	@Override
	public long id(){
		return this.id;
	}
}
