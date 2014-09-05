package de.hsbremen.tc.tnc.imc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.imc.util.ImcSupportedMessageType;

public class ImcContainer{

    private final IMC imc;
    
    private final long primaryId;
    
    private final List<Long> imcIds;
   
    private List<ImcSupportedMessageType> supportedMessageTypes;

    public ImcContainer(final long primaryImcId, final IMC imc) {
    	this.primaryId = primaryImcId;
    	this.imcIds = new ArrayList<>();
    	this.imcIds.add(primaryImcId);
    	this.imc = imc;
    }
    
    IMC getImc(){
    	return this.imc;
    }
    
    long getPrimaryId(){
    	return primaryId;
    }
    
    List<ImcSupportedMessageType> getSupportedMessageTypes(){
	   return Collections.unmodifiableList(supportedMessageTypes);
    }
    
    List<Long> getImcIds(){
    	return Collections.unmodifiableList(imcIds);
    }
    
    public void setSupportedMessageTypes(final List<ImcSupportedMessageType> supportedTypes){
    	if(supportedTypes != null){
    		this.supportedMessageTypes = supportedTypes;
    	}else{
    		throw new NullPointerException();
    	}
    }
    
    
    
}
