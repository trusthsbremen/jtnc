package org.ietf.nea.pt.socket.sasl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.security.sasl.SaslServer;

public class SaslServerMechansims implements SaslMechanismSelection{
    
    private Map<Byte, Map<String, SaslServer>> mechanisms;

    public SaslServerMechansims(){
       
        this.mechanisms = new HashMap<Byte, Map<String,SaslServer>>();
    }
    
    public byte getStageCount(){
        return (byte)this.mechanisms.size();
    }
    
    public int getStageMechanismCount(byte stage){
        Byte key = new Byte(stage);
        if(this.mechanisms.containsKey(key)){
            return this.mechanisms.get(key).size();
        }else{
            return 0;
        }
    }
    
    public void addMechanismToStage(byte stage, SaslServer server){
        if(server.isComplete()){
            throw new IllegalArgumentException("Server was already used. Hence it cannot be added."); 
        }
        
        Byte tempStage = new Byte(stage);
        
        if(tempStage.byteValue() <= 0){
            throw new IllegalArgumentException("Stage numbers must be greater or equal to 1.");
        }
        
        if(tempStage.byteValue() > 1){
            if(!this.mechanisms.containsKey(new Byte((byte)(tempStage.byteValue()-1)))){
                throw new IllegalStateException("Stages must be added in a consecutive way.");
            }
        }
        
        if(!this.mechanisms.containsKey(tempStage)){
            this.mechanisms.put(tempStage, new HashMap<String, SaslServer>());
        }
        
        this.mechanisms.get(tempStage).put(server.getMechanismName(), server);
       
    }
    
    public Map<String,SaslServer> getAllMechanismsByStage(byte stage){
        Byte key = new Byte(stage);
        if(this.mechanisms.containsKey(key)){
            return Collections.unmodifiableMap(this.mechanisms.get(key));
        }else{
            return null;
        }
        
    }
    
    public SaslServer getMechanismByStage(byte stage, String mechanism){
        Byte key = new Byte(stage);
        if(this.mechanisms.containsKey(key)){
            return this.mechanisms.get(key).get(mechanism);
        }else{
            return null;
        }
        
    }
    
    public Map<Byte, Map<String, SaslServer>> getAllMechansims(){
        Map<Byte,Map<String, SaslServer>> unmodifiable = new HashMap<>();
        
        Set<Entry<Byte,Map<String, SaslServer>>> entrySet = this.mechanisms.entrySet();
        for (Entry<Byte, Map<String, SaslServer>> entry : entrySet) {
            unmodifiable.put(entry.getKey(), Collections.unmodifiableMap(entry.getValue()));
        }
        
        return Collections.unmodifiableMap(unmodifiable);
    }
}
