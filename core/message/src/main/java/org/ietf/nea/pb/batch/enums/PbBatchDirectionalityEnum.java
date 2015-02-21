package org.ietf.nea.pb.batch.enums;



public enum PbBatchDirectionalityEnum {
    TO_PBC  (false),
    TO_PBS  (true);
    
    private final boolean directionality;
   
    private PbBatchDirectionalityEnum(boolean directionality){
        this.directionality = directionality;
    }
    
	public boolean directionality(){
        return this.directionality;
    }
	
	public byte toDirectionalityBit(){
		return (byte) (this.directionality ? 1 : 0); 
	}
    
    public static PbBatchDirectionalityEnum fromDirectionalityBit(byte directionality){
    	
    	if(directionality == 0){
    		return TO_PBC;
    	}
    	
    	if(directionality == 1){
    		return TO_PBS;
    	}
    	
    	return null;
    }
}
