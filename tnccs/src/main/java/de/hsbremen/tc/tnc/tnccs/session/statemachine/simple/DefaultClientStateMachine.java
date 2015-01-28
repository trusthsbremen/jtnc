package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.Decided;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.End;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception.StateMachineAccessException;

public class DefaultClientStateMachine implements StateMachine {
	
	private State state;
	private Boolean busy;
	
	private Object closeLock;
	private Boolean connectionDeleteSet;
	
	private final StateHelper<? extends TnccContentHandler> stateHelper;
	
	public DefaultClientStateMachine(StateHelper<? extends TnccContentHandler> stateHelper){
		
		if(stateHelper == null){
			throw new NullPointerException("StateHelper cannot be null.");
		}
		this.stateHelper = stateHelper;
		
		
		this.state = null;
		this.busy = Boolean.FALSE;
		this.closeLock = new Object();
		this.connectionDeleteSet = Boolean.TRUE;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.statemachine.StateMachine#start(boolean)
	 */
	@Override
	public TnccsBatch start(boolean selfInitiated) throws StateMachineAccessException{
		synchronized (this) {
			if(this.isClosed()){
				synchronized (this.closeLock) {

					this.busy = Boolean.TRUE;
					
					this.stateHelper.getHandler().setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_CREATE);
					this.connectionDeleteSet = Boolean.FALSE;
					
					this.stateHelper.getHandler().setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE);					
					TnccsBatch b = null;
					
					if(selfInitiated){
						this.state = this.stateHelper.createState(TnccsStateEnum.INIT);
						b = this.state.collect();
						this.state = this.state.getConclusiveState();			
					}else{
						this.state = this.stateHelper.createState(TnccsStateEnum.SERVER_WORKING);
					}
					if(this.state instanceof End){
						this.stop();
					}
					this.busy = Boolean.FALSE;
					
					return b;
				}
				
			}else{	
				throw new StateMachineAccessException("Object cannot be started, because it is already running.");
			}
		}
		
		
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.statemachine.StateMachine#submitBatch(de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer)
	 */
	@Override
	public TnccsBatch receiveBatch(TnccsBatchContainer newBatch) throws StateMachineAccessException{
		synchronized (this) {
			if(!this.isClosed() && !this.busy){
				this.busy = Boolean.TRUE;
			}else{
				throw new StateMachineAccessException("While the object is working, no other messages can be received.");
			}
		}
		
		TnccsBatch b = null;
		synchronized(this.closeLock){
			if(!this.isClosed()){
				this.state = this.state.getProcessorState(newBatch.getResult());
				b = this.state.handle(newBatch);
				this.state = this.state.getConclusiveState();
				if(this.state instanceof End){
					this.stop();
				}
			}
		}

		this.busy = Boolean.FALSE;
		
		return b;
		
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.statemachine.StateMachine#retryHandshake()
	 */
	@Override
	public List<TnccsBatch> retryHandshake(ImHandshakeRetryReasonEnum reason) throws TncException{
		synchronized (this) {
			if(!this.isClosed() && !this.busy){
				if(this.state instanceof Decided){
					this.busy = Boolean.TRUE;
				}else{
					throw new TncException("Current state " +this.state.toString()+  " does not allow retry.",TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY, this.state.toString());
				}
			}else{
				throw new TncException("Retry not possible, because object ist busy.",TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY);
			}
		}

		TnccsBatch b = null;
		synchronized(this.closeLock){
			if(!this.isClosed()){
				this.state = this.stateHelper.createState(TnccsStateEnum.RETRY);
				
				b = this.state.collect();
				
				this.state = this.state.getConclusiveState();
				if(this.state instanceof End){
					this.stop();
				}
			}
		}
		
		this.busy = Boolean.FALSE;
		
		return (b != null) ? Arrays.asList(new TnccsBatch[]{b}) : new ArrayList<TnccsBatch>(0);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine#close()
	 */
	@Override
	public TnccsBatch close() throws StateMachineAccessException{
		synchronized (this) {
			if(!this.isClosed() && !this.busy){
				this.busy = Boolean.TRUE;
			}else{
				throw new StateMachineAccessException("While the object is working, no other messages can be received.");
			}
		}
		
		TnccsBatch b = null;
		synchronized(this.closeLock){
			if(!this.isClosed()){
				this.state = this.stateHelper.createState(TnccsStateEnum.END);
				b = this.state.collect();
				this.state = this.state.getConclusiveState();
				if(this.state instanceof End){
					this.stop();
				}
			}
		}

		this.busy = Boolean.FALSE;
		
		return b;
		
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.statemachine.StateMachine#close()
	 */
	@Override
	public void stop(){
		synchronized(this.closeLock){
			this.state = (this.state instanceof End) ? this.state : this.stateHelper.createState(TnccsStateEnum.END);
			if(!this.connectionDeleteSet){
				this.stateHelper.getHandler().setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE);
				this.connectionDeleteSet = Boolean.TRUE;
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.statemachine.StateMachine#isClosed()
	 */
	@Override
	public boolean isClosed(){
		return (this.state == null || this.state instanceof End);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.statemachine.StateMachine#canRetry()
	 */
	@Override
	public boolean canRetry() {
		return ((!this.isClosed() && !this.busy) && (this.state instanceof Decided));
	}
	
	
	
	
	
}
