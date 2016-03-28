package org.ietf.nea.pt.socket.sasl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.SaslClient;

public class SaslClientMechansims implements SaslMechanismSelection{
    
    private Map<String, SaslClient> mechanisms;

    public SaslClientMechansims(){
       
        this.mechanisms = new HashMap<String,SaslClient>();
    }
    
    public int getMechanismCount(){
        return this.mechanisms.size();
    }
    
    public void addMechanism(SaslClient client){
        if(client.isComplete()){
            throw new IllegalArgumentException("Client was already used. Hence it cannot be added."); 
        }
        this.mechanisms.put(client.getMechanismName(), client);
       
    }
    
    public SaslClient getMechanism(String mechanismName){
        return this.mechanisms.get(mechanismName);
    }
    
    public Map<String, SaslClient> getAllMechansims(){
        
        return Collections.unmodifiableMap(mechanisms);
    }
}
