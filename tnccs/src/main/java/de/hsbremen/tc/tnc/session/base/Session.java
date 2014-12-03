package de.hsbremen.tc.tnc.session.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.session.connection.BatchReceiver;
import de.hsbremen.tc.tnc.session.connection.BatchSender;
import de.hsbremen.tc.tnc.session.connection.TnccsInputChannel;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;

public class Session implements BatchReceiver, HandshakeRetryListener, SessionBase {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(Session.class);
	
	private StateMachine machine;
	private Thread input;
	private BatchSender output;
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.eventdriven.SessionBase#registerStatemachine(de.hsbremen.tc.tnc.session.eventdriven.StateMachine)
	 */
	@Override
	public void registerStatemachine(StateMachine m){
		if(this.machine == null){
			this.machine = m;
		}else{
			throw new IllegalStateException("State machine already registered.");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.eventdriven.SessionBase#registerInput(de.hsbremen.tc.tnc.session.connection.TnccsInputChannel)
	 */
	@Override
	public void registerInput(TnccsInputChannel channel){
		if(this.input == null){
			channel.register(this);
			this.input = new Thread(channel);
		}else{
			throw new IllegalStateException("Input already registered.");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.eventdriven.SessionBase#registerOutput(de.hsbremen.tc.tnc.session.connection.BatchSender)
	 */
	@Override
	public void registerOutput(BatchSender sender){
		if(this.output == null){
			this.output = sender;
		}else{
			throw new IllegalStateException("Output already registered.");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.eventdriven.SessionBase#start(boolean)
	 */
	@Override
	public synchronized void start(boolean selfInitiated){
		if(this.machine != null && this.input != null && this.output != null){
			this.input.start();
			if(selfInitiated){
				TnccsBatch batch = this.machine.initiateHandshake();
				try {
					this.output.send(batch);
					if(this.machine.isClosed()){
						LOGGER.info("State machine has reached the end state. Session terminates.");
						this.close();
					}
				} catch (ComprehensibleException e) {
					LOGGER.error("Fatal error discovered. Session terminates.", e);
					this.close();
				}
			}
		}else{
			throw new IllegalStateException("Not all necessary objects are registered.");
		}
	}
	

	@Override
	public synchronized void receive(TnccsBatchContainer batchContainer) {

		TnccsBatch batch = this.machine.handle(batchContainer);
		try {
			this.output.send(batch);
			if(this.machine.isClosed()){
				LOGGER.info("State machine has reached the end state. Session terminates.");
				this.close();
			}
		} catch (ComprehensibleException e) {
			LOGGER.error("Fatal error discovered. Session terminates.", e);
			this.close();
		}
		
		

	}

	@Override
	public synchronized void retryHandshake(ImHandshakeRetryReasonEnum reason) throws TncException {
		if(this.machine.canRetry()){
			TnccsBatch batch = this.machine.retryHandshake(reason);
			try {
				this.output.send(batch);
				if(this.machine.isClosed()){
					LOGGER.info("State machine has reached the end state. Session terminates.");
					this.close();
				}
			} catch (ComprehensibleException e) {
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
		if(!this.input.isInterrupted()){
			this.input.interrupt();
		}
		
		if(!machine.isClosed()){
			this.machine.close();
		}
		
		this.output.close();
	}
}
