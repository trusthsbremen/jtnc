package de.hsbremen.tc.tnc.attribute;



public enum TncHsbAttributeTypeEnum implements TncAttributeType {

    /**
     * Current round trips provided by the TNCC/S session for other components and IMC/V  
     * (type long, may get from a IM(C/V)Connection)
     * Has the value 255 because the lower numbers may be used by more important attributes. 
     */
    HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS (0x0025B1FFL);

	private long id;
	
	private TncHsbAttributeTypeEnum(long id){
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
