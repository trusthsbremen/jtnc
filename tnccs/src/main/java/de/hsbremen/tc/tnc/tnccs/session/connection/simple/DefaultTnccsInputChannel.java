package de.hsbremen.tc.tnc.tnccs.session.connection.simple;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pb.batch.DefaultTnccsBatchContainer;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsInputChannel;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsInputChannelListener;
import de.hsbremen.tc.tnc.tnccs.session.connection.exception.ListenerClosedException;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class DefaultTnccsInputChannel implements TnccsInputChannel, Runnable{

	private TnccsInputChannelListener listener;
	private final TransportConnection connection;
	private final TnccsReader<? extends TnccsBatchContainer> reader;
	
	DefaultTnccsInputChannel(TransportConnection connection, TnccsReader<? extends TnccsBatchContainer> reader){
		this.connection = connection;
		this.reader = reader;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.connection.TnccsInputChannel#register(de.hsbremen.tc.tnc.session.connection.BatchReceiver)
	 */
	@Override
	public void register(TnccsInputChannelListener listener){
		if(this.listener == null){
			this.listener = listener;
		}else{
			throw new IllegalStateException("Listener already registered.");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.connection.TnccsInputChannel#run()
	 */
	@Override
	public void run(){
		try{
			while(!Thread.currentThread().isInterrupted()){
				TnccsBatchContainer bc = this.receive();
				this.listener.receive(bc);
			}
		}catch(ConnectionException | SerializationException e){
			this.listener.handle(e);
		}catch(ListenerClosedException e){
			// ignore just close
		}finally{
			this.connection.close();
		}
	}
	
	
	
	private TnccsBatchContainer receive() throws ConnectionException, SerializationException{
		this.checkConnection();
		
		TnccsBatchContainer result = null;
		try {
			
			result = this.reader.read(this.connection.getInputStream(), -1);
		
		} catch (ValidationException e) {
			
			List<ValidationException> exceptions = new ArrayList<>();
			exceptions.add(e);
			result = new DefaultTnccsBatchContainer(null,exceptions);
			
		}
		return result;
	}
	
	private void checkConnection() throws ConnectionException{
		if(!connection.isOpen()){
			throw new ConnectionException("Underlying connection is closed.");
		}
	}

}
