package org.ietf.nea.pt.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.connection.TransportAddress;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionAttributeNotFoundException;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public abstract class AbstractTransportConnection implements TransportConnection {
    
    //should only be set once and never changed
    private final TransportAddress connectionId;
    private final TnccsReader<TnccsBatch> reader;
    private final TnccsWriter<TnccsBatch> writer;
    private final Map<Long,Object> attributes;
    //boolean selfInitiated;
    
    protected AbstractTransportConnection(final TransportAddress address, final TnccsReader<TnccsBatch> reader, final TnccsWriter<TnccsBatch> writer, final Map<Long,Object> attributes /*, boolean selfInitated*/){
        this.connectionId = address;
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
    public TransportAddress getId() {
        return connectionId;
    }
   
    @Override
    public Object getAttribute( TncAttributeType type) throws ConnectionAttributeNotFoundException{

    	if(this.attributes.containsKey(type.id())){
    		return this.attributes.get(type.id());
    	}
    	
    	throw new ConnectionAttributeNotFoundException("Attribute of type " + type.toString() + " with ID " + type.id() + " not found.", type.toString(), type.id());
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
    
    protected abstract OutputStream getOutputStream() throws ConnectionException;
    
    protected abstract InputStream getInputStream() throws ConnectionException;
 
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
