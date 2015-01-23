package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.ietf.nea.pb.batch.DefaultTnccsBatchContainer;
import org.ietf.nea.pb.batch.PbBatchHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.session.base.Session;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionAttributes;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception.StateMachineAccessException;
import de.hsbremen.tc.tnc.transport.TnccsValueListener;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;
import de.hsbremen.tc.tnc.transport.exception.ListenerClosedException;

public class DefaultSession implements Session{
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultSession.class);
	
	private final SessionAttributes attributes;
	private final ExecutorService runner;
	private final TnccsWriter<TnccsBatch> writer;
	private final TnccsReader<TnccsBatchContainer> reader;
	
	private TransportConnection connection;
	private StateMachine machine;
	private boolean closed;
	
	public DefaultSession(SessionAttributes attributes, TnccsWriter<TnccsBatch> writer, TnccsReader<TnccsBatchContainer> reader,ExecutorService runner){
		if(attributes == null){
			throw new NullPointerException("Constructor arguments cannot be null.");
		}
		this.attributes = attributes;
		this.writer = writer;
		this.reader = reader;
		this.closed = true;
		this.runner = (runner != null) ? runner : Executors.newSingleThreadExecutor();
	}
	

	@Override
	public void registerStatemachine(StateMachine m){
		if(this.machine == null){
			this.machine = m;
			if(LOGGER.isDebugEnabled()){
				StringBuilder b = new StringBuilder();
				b.append("Machine set to " +  m.toString() + ". \n");
				LOGGER.debug(b.toString());
			}
		}else{
			throw new IllegalStateException("State machine already registered.");
		}
	}
	
	@Override
	public void registerConnection(TransportConnection c){
		if(this.connection == null){
			this.connection = c;
			if(LOGGER.isDebugEnabled()){
				StringBuilder b = new StringBuilder();
				b.append("Connection set to " +  c.toString() + ". \n");
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
				this.machine.isClosed() && this.connection != null){
			LOGGER.info("All session fields are set and in the correct state. Session starts.");
			
			this.runner.execute(new Start(selfInitiated, this));
			
		}else{
			throw new IllegalStateException("Not all necessary objects are registered and in the correct state.");
		}
		
	}
	
	@Override
	public void receive(ByteBuffer batchContainer) throws ListenerClosedException {
		if(this.isClosed()){
			throw new ListenerClosedException("Session is closed and cannot receive objects.");
		}
		
		this.runner.execute(new Receive(batchContainer));
		
	}

	
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.transport.TnccsValueListener#notifyClose()
	 */
	@Override
	public void notifyClose() {
		if(!this.closed){
			LOGGER.info("Underlying transport was closed. Closing session.");
			this.close();
		}
	}


	@Override
	public void retryHandshake(ImHandshakeRetryReasonEnum reason) throws TncException {
		
		if(this.isClosed()){
			throw new TncException("Retry not allowed.", TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY);
		}
	
		if(this.machine.canRetry()){
			Future<Boolean> future = this.runner.submit(new Retry(reason));
			// this is a blocking call
			try{
				future.get();
			}catch(ExecutionException e){
				if(e.getCause() instanceof TncException){
					throw (TncException)e.getCause();
				}
			} catch (InterruptedException e) {
				throw new TncException("Retry cancled, because the thread was interrupted.",e,TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY);
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
	
	@Override
	public void close(){
		if(!this.closed){
			this.closed = true;
			LOGGER.debug("Session close() called. Closing session...");
			this.runner.shutdown();
			
			if(this.connection.isOpen()){
				if(!this.machine.isClosed()){
					try {
						// try to close gracefully
						TnccsBatch b = this.machine.close();
						if(b != null){
							ByteBuffer buf = new DefaultByteBuffer(((PbBatchHeader)b.getHeader()).getLength());
							this.writer.write(b, buf);
							this.connection.send(buf);
							this.incrementRoundTrips();
						}
					} catch (StateMachineAccessException | ConnectionException | SerializationException e) {
						LOGGER.error("Fatal error discovered. Session terminates.", e);
						this.machine.stop();
					}
				}
				this.connection.close();
			}
			
			if(!this.machine.isClosed()){
				this.machine.stop();
			}
			
			this.runner.shutdownNow();
		}
	}

	@Override
	public boolean isClosed() {
		return closed;
	}
	
	private void incrementRoundTrips(){
		this.attributes.setCurrentRoundTrips(this.attributes.getCurrentRoundTrips()+1);
	}
	
	private class Start implements Runnable{

		private boolean selfInitiated;
		private TnccsValueListener listener;
		
		protected Start(boolean selfInitiated, TnccsValueListener listener){
			this.selfInitiated = selfInitiated;
			this.listener = listener;
		}
		
		@Override
		public void run() {
			closed = false;
			try{
				TnccsBatch batch = machine.start(selfInitiated);
				try {
					connection.open(listener);
				
					if(batch != null){
						ByteBuffer buf = new DefaultByteBuffer(((PbBatchHeader)batch.getHeader()).getLength());
						writer.write(batch, buf);
						connection.send(buf);
						incrementRoundTrips();
					}
					
					if(machine.isClosed()){
						LOGGER.info("State machine has reached the end state. Session terminates.");
						close();
					}
					
				} catch (SerializationException | ConnectionException e) {
					LOGGER.error("Fatal error discovered. Session terminates.", e);
					close();
				}
			}catch(StateMachineAccessException e){
				LOGGER.error("Peer has surprisingly send a message. Session terminates.", e);
				close();
			}
			
			
		}
		
	}
	
	private class Receive implements Runnable{

		ByteBuffer buffer;
		protected Receive(ByteBuffer buffer){
			this.buffer = buffer;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try{
				TnccsBatchContainer container = null;
				
				try{
					container = reader.read(buffer, -1);
				}catch(ValidationException e){
					
					List<ValidationException> exceptions = new ArrayList<>();
					exceptions.add(e);
					container = new DefaultTnccsBatchContainer(null,exceptions);	
				}
				
				TnccsBatch batch = machine.receiveBatch(container);
				
				
				if(batch != null){
	
					ByteBuffer buf = new DefaultByteBuffer(((PbBatchHeader)batch.getHeader()).getLength());
					writer.write(batch, buf);
					connection.send(buf);
					incrementRoundTrips();
				}
				
				if(machine.isClosed()){
					LOGGER.info("State machine has reached the end state. Session terminates.");
					close();
				}
					
			} catch (SerializationException | ConnectionException e) {
				LOGGER.error("Fatal error discovered. Session terminates.", e);
				close();
					
			}catch(StateMachineAccessException e){
				LOGGER.error("Peer has surprisingly send a message. Session terminates.", e);
				close();
			}
		}
	}
	
	
	private class Retry implements Callable<Boolean>{
		
		private ImHandshakeRetryReasonEnum reason;
		
		protected Retry(ImHandshakeRetryReasonEnum reason){
			this.reason = reason;
		}
		
		@Override
		public Boolean call() throws Exception {

			Boolean success = Boolean.FALSE;
			
			List<TnccsBatch> batches = machine.retryHandshake(reason);
			
			try {
				if(batches != null){
					for (TnccsBatch batch : batches) {
						if(batch != null){
							ByteBuffer buf = new DefaultByteBuffer(((PbBatchHeader)batch.getHeader()).getLength());
							writer.write(batch, buf);
							connection.send(buf);
							incrementRoundTrips();
						}
					}
					
				}
				
				success = Boolean.TRUE;
				
				if(machine.isClosed()){
					LOGGER.info("State machine has reached the end state. Session terminates.");
					close();
				}
			} catch (SerializationException | ConnectionException e) {
				LOGGER.error("Fatal error discovered. Session terminates.", e);
				close();
			}
			
			return success;
		}
		
	}	
	
}
