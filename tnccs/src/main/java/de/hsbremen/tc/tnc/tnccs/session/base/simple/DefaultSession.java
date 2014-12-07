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
import de.hsbremen.tc.tnc.tnccs.session.base.HandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionBase;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsInputChannel;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsInputChannelListener;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsOutputChannel;
import de.hsbremen.tc.tnc.tnccs.session.connection.exception.ListenerClosedException;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception.StateMachineAccessException;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class DefaultSession implements TnccsInputChannelListener, HandshakeRetryListener, SessionBase {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultSession.class);
	
	private DefaultSessionAttributes attributes;
	private StateMachine machine;
	private Thread input;
	private TnccsOutputChannel output;
	private boolean closed;
	private long roundTripCounter; // TODO implement roundtrip handling
	
	
	public DefaultSession(DefaultSessionAttributes attributes){
		this.attributes = attributes;
		this.closed = true;
		this.roundTripCounter = 0;
	}
	
	@Override
	public void registerStatemachine(StateMachine m){
		if(this.machine == null){
			this.machine = m;
			if(LOGGER.isDebugEnabled()){
				StringBuilder b = new StringBuilder();
				b.append("Machine was set to " +  m.toString() + ". \n");
				b.append("InputChannel " + ((this.input != null) ? " already set to " + this.input.toString() +"." : "still missing. \n"));
				b.append("OutputChannel " + ((this.output != null) ? " already set to " + this.output.toString() +"." : "still missing. \n"));
				LOGGER.debug(b.toString());
			}
		}else{
			throw new IllegalStateException("State machine already registered.");
		}
	}
	
	@Override
	public void registerInput(TnccsInputChannel in){
		if(this.input == null){
			this.input = new Thread(in);
			this.input.setName("InputChannelThread" + System.currentTimeMillis());
			if(LOGGER.isDebugEnabled()){
				StringBuilder b = new StringBuilder();
				b.append("InputChannel was set to " +  in.toString() + ". \n");
				b.append("OutputChannel " + ((this.output != null) ? " already set to " + this.output.toString() +"." : "still missing. \n"));
				b.append("Machine " + ((this.machine != null) ? " already set to " + this.machine.toString() +"." : "still missing. \n"));
				LOGGER.debug(b.toString());
			}
		}else{
			throw new IllegalStateException("Input already registered.");
		}
	}
	
	@Override
	public void registerOutput(TnccsOutputChannel out){
		if(this.output == null){
			this.output = out;
			if(LOGGER.isDebugEnabled()){
				StringBuilder b = new StringBuilder();
				b.append("OutputChannel was set to " +  out.toString() + ". \n");
				b.append("InputChannel " + ((this.input != null) ? " already set to " + this.input.toString() +"." : "still missing. \n"));
				b.append("Machine " + ((this.machine != null) ? " already set to " + this.machine.toString() +"." : "still missing. \n"));
				LOGGER.debug(b.toString());
			}
		}else{
			throw new IllegalStateException("Output already registered.");
		}
	}
	
	@Override
	public Attributed getAttributes() {
		return this.attributes;
	}

	@Override
	public void start(boolean selfInitiated){
		if(this.machine != null &&
				this.machine.isClosed() &&
				this.input != null && 
				this.output != null){
			LOGGER.info("All session fields are set and in the correct state. Session starts.");
			this.closed = false;
			
			try{
				
				TnccsBatch batch = this.machine.start(selfInitiated);
				this.input.start();
				
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
			if(!this.input.isInterrupted()){
				this.input.interrupt();
			}
			
			if(!machine.isClosed()){
				this.machine.stop();
			}
			
			this.output.close();
			this.closed = true;
		}
	}

	@Override
	public boolean isClosed() {
		return closed;
	}
	
	
}
