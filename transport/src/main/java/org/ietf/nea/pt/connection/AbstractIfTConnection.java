package org.ietf.nea.pt.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.connection.IfTConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public abstract class AbstractIfTConnection implements IfTConnection {
    
    //should only be set once and never changed
    private final String connectionId;
    private final TnccsReader<TnccsBatch> reader;
    private final TnccsWriter<TnccsBatch> writer;
    private final Map<Long,Object> attributes;
    //boolean selfInitiated;
    
    protected AbstractIfTConnection(final String connectionId, final TnccsReader<TnccsBatch> reader, final TnccsWriter<TnccsBatch> writer, final Map<Long,Object> attributes /*, boolean selfInitated*/){
        this.connectionId = connectionId;
        this.reader = reader;
        this.writer = writer;	
        this.attributes = attributes;
        //this.selfInitiated = selfInitated;
       // this.connection = connection;
    }
//    /**
//     * @return the selfInitiated
//     */
//    public boolean isSelfInitiated() {
//        return selfInitiated;
//    }
    
    @Override
    public String getId() {
        return connectionId;
    }
   
    @Override
    public Object getAttribute(final Long id){
   
    	Object o = null;
    	if(this.attributes.containsKey(id)){
    		o = this.attributes.get(id);
    	}
    	
    	return o;
    }
    
    @Override 
    public Map<Long,Object> getAttributes(){
    	return Collections.unmodifiableMap(this.attributes);
    }
    
    @Override
    public boolean setAttribute(Long id, Object value){
    	boolean success = false;
    	if(id == AttributeSupport.TNC_ATTRIBUTEID_PREFERRED_LANGUAGE){
    		if(value instanceof String){
    			this.attributes.put(id, new String(((String)value).toString()));
    			success = true;
    		}
    	}
    	
    	return success;
    }
    
    public abstract void open() throws ConnectionException;
    
    public abstract void close();
    
    @Override
    public void send(final TnccsBatch data) throws SerializationException, ConnectionException{
    	OutputStream out = getOutputStream();
    	if(out == null){
    		throw new ConnectionException("Could not get OutputStream, got null instead.");
    	}
    	writer.write(data, out);
    }
    
    @Override
    public TnccsBatch receive() throws ConnectionException, SerializationException, ValidationException{
    	InputStream in = getInputStream();
    	if(in == null){
    		throw new ConnectionException("Could not get InputStream, got null instead.");
    	}
    	
    	TnccsBatch b = null;
    	reader.read(in, -1);
    	
    	return b;
    }
    
    protected abstract OutputStream getOutputStream();
    
    protected abstract InputStream getInputStream();
 
   // not needed return types are usually only primitive box types.
    
//   @Override
//   public Object getAttribute(final Long id){
//    	
//    	Object o = null;
//    	if(attributes.containsKey(id)){
//    		o = deepCopy(attributes.get(id));
//    	}
//    	return o;
//
//   }    
//    private Object deepCopy(final Object o){
//    	Object o1 = null;
//    	
//    	ByteArrayOutputStream out = new ByteArrayOutputStream();
//    	
//    	try {
//			new ObjectOutputStream(out).writeObject(o);
//		} catch (IOException e) {
//			//TODO LOG
//			e.printStackTrace();
//			return o1;
//		}
//    	
//    	ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
//    	
//    	
//    	
//    	try {
//			o1 = new ObjectInputStream(in).readObject();
//		} catch (ClassNotFoundException e) {
//			//TODO LOG
//			e.printStackTrace();
//			o1 = null;
//			return o1;
//		} catch (IOException e) {
//			//TODO LOG
//			e.printStackTrace();
//			o1 = null;
//			return o1;
//		}
//    	
//    	return o1;
//    }
}
