package de.hsbremen.tc.tnc.nar.connection;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;

public abstract class AbstractIfTConnection implements IfTConnection {
    
    //should only be set once and never changed
    private String connectionId;
    boolean selfInitiated;
    
    public AbstractIfTConnection(String connectionId, boolean selfInitated){
        this.connectionId = connectionId;
        this.selfInitiated = selfInitated;
       // this.connection = connection;
    }
    /**
     * @return the selfInitiated
     */
    public boolean isSelfInitiated() {
        return selfInitiated;
    }
    
    /**
     * @return the connectionId
     */
    public String getId() {
        return connectionId;
    }
   
    public abstract void open();
    
    public abstract void close();
    
    public abstract void send(TnccsBatch data);

    
    public abstract TnccsBatch receive();
 
    
}
