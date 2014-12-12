package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.session.base.Session;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsInputChannel;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsInputChannelListener;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsOutputChannel;
import de.hsbremen.tc.tnc.tnccs.session.connection.exception.ListenerClosedException;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception.StateMachineAccessException;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class DefaultSessionRunnable  {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultSessionRunnable.class);
	
	private final DefaultSessionAttributes attributes;
	private StateMachine machine;
	private final TransportConnection connection;
	private boolean closed;
	private long roundTripCounter; // TODO implement roundtrip handling
	
	
	public DefaultSessionRunnable(DefaultSessionAttributes attributes, TransportConnection connection){
		if(attributes == null || connection == null){
			throw new NullPointerException("Constructor arguments cannot be null.");
		}
		this.attributes = attributes;
		this.closed = true;
		this.connection = connection;
		this.roundTripCounter = 0;
	}
	
	@Override
	public void registerStatemachine(StateMachine m){
		if(this.machine == null){
			this.machine = m;
			if(LOGGER.isDebugEnabled()){
				StringBuilder b = new StringBuilder();
				b.append("Machine was set to " +  m.toString() + ". \n");
				LOGGER.debug(b.toString());
			}
		}else{
			throw new IllegalStateException("State machine already registered.");
		}
	}
	
	@Override
	public Attributed getAttributes() {
		return this.attributes;
	}

	@Override
	public void start(boolean selfInitiated){
		if(this.machine != null &&
				this.machine.isClosed()){
			LOGGER.info("All session fields are set and in the correct state. Session starts.");
			this.closed = false;
			
			try{
				
				TnccsBatch batch = this.machine.start(selfInitiated);
				
				try {
					if(batch != null){
						this.output.send(batch);
					}
					if(this.machine.isClosed()){
						LOGGER.info("State machine has reached the end state. Session terminates.");
						this.close();
					}
				} catch (SerializationException | ConnectionException e) {
					LOGGER.error("Fatal error discovered. Session terminates.", e);
					this.close();
				}
			}catch(StateMachineAccessException e){
				LOGGER.error("Peer has surprisingly send a message. Session terminates.", e);
				this.close();
			}
		}else{
			throw new IllegalStateException("Not all necessary objects are registered and in the correct state.");
		}
		
	}
	

	@Override
	public void receive(TnccsBatchContainer batchContainer) throws ListenerClosedException {
		if(this.isClosed()){
			throw new ListenerClosedException("Session is closed and cannot receive objects.");
		}
		
		try{
			TnccsBatch batch = this.machine.receiveBatch(batchContainer);
			this.roundTripCounter++;
			if(batch != null){
				try {
					if(batch != null){
						this.output.send(batch);
					}
					if(this.machine.isClosed()){
						LOGGER.info("State machine has reached the end state. Session terminates.");
						this.close();
					}
				} catch (SerializationException | ConnectionException e) {
					LOGGER.error("Fatal error discovered. Session terminates.", e);
					this.close();
				}
			}
		}catch(StateMachineAccessException e){
			LOGGER.error("Peer has surprisingly send a message. Session terminates.", e);
			this.close();
		}
		
		

	}

	@Override
	public void retryHandshake(ImHandshakeRetryReasonEnum reason) throws TncException {
		
		if(this.isClosed()){
			throw new TncException("Retry not allowed.", TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY);
		}
		
		if(this.machine.canRetry()){
			List<TnccsBatch> batches = this.machine.retryHandshake(reason);
			try {
				if(batches != null){
					for (TnccsBatch batch : batches) {
						if(batch != null){
							this.output.send(batch);
						}
					}
					
				}
				if(this.machine.isClosed()){
					LOGGER.info("State machine has reached the end state. Session terminates.");
					this.close();
				}
			} catch (SerializationException | ConnectionException e) {
				LOGGER.error("Fatal error discovered. Session terminates.", e);
				this.close();
			}
		}else{
			throw new TncException("Retry not allowed.", TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY);
		}
	}

	@Override
	public void handle(ComprehensibleException e) {
		LOGGER.error("Fatal error discovered. Session terminates.", e);
		this.close();
		
	}
	
	public void close(){
		if(!this.closed){
			if(this.input.isAlive() && !this.input.isInterrupted() ){
				if(!machine.isClosed()){
					try {
						// try to close gracefully
						TnccsBatch b = this.machine.close();
						if(b != null){
							this.output.send(b);
						}
					} catch (StateMachineAccessException | ConnectionException | SerializationException e) {
						LOGGER.error("Fatal error discovered. Session terminates.", e);
						this.machine.stop();
					}
				}
			
				this.input.interrupt();
			}
			
			this.output.close();
			this.closed = true;
		}
	}

	@Override
	public boolean isClosed() {
		return closed;
	}
	
	
	private static class InputChannel{
		
		
		
	}
	
}
