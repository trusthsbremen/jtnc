package org.ietf.nea.pb.batch.enums;



public enum PbBatchDirectionalityEnum {
    TO_PBC  ((byte)0),
    TO_PBS  ((byte)1);
    
    private final byte directionality;
   
    private PbBatchDirectionalityEnum(byte directionality){
        this.directionality = directionality;
    }
    
	public byte directionality(){
        return this.directionality;
    }
    
    public static PbBatchDirectionalityEnum fromDirectionality(byte directionality){
    	
    	if(directionality == TO_PBC.directionality){
    		return TO_PBC;
    	}
    	
    	if(directionality == TO_PBS.directionality){
    		return TO_PBS;
    	}
    	
    	return null;
    }
}
