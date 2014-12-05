package de.hsbremen.tc.tnc.tnccs.adapter.im;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;

public class ImcAdapterTimeProxy implements ImcAdapter{

	private static final Logger LOGGER = LoggerFactory.getLogger(ImcAdapterTimeProxy.class);
	
	private final ImcAdapter adapter;
	private final long timeout;
	
	public ImcAdapterTimeProxy(ImcAdapter adapter, long timeout) {
		this.adapter = adapter;
		if(timeout < 1000){
	       	this.timeout = timeout;
		}else{
			throw new IllegalArgumentException("Timeout of "+ timeout + " milliseconds is to large, timeout must be less than one second ( < 1000 milliseconds).");
		}
	}

	@Override
	public long getPrimaryId() {
		return this.adapter.getPrimaryId();
	}

	@Override
	public void notifyConnectionChange(ImcConnectionAdapter connection,
			TncConnectionState state) throws TncException, TerminatedException {
		this.adapter.notifyConnectionChange(connection, state);
	}

	@Override
	public void beginHandshake(final ImcConnectionAdapter connection)
			throws TncException, TerminatedException {
		try{
			this.execute(new Callable<Boolean>(){
	
	        	@Override
				public Boolean call() throws TncException, TerminatedException {
					adapter.beginHandshake(connection);
					return Boolean.TRUE;
				}	
	
	        });
		}finally{
			connection.denyMessageReceipt();
		}
		
	}

	@Override
	public void handleMessage(final ImcConnectionAdapter connection,
			final TnccsMessageValue message) throws TncException, TerminatedException {
		try{
			this.execute(new Callable<Boolean>(){
	
	        	@Override
				public Boolean call() throws TncException, TerminatedException {
					adapter.handleMessage(connection, message);
					return Boolean.TRUE;
				}	
	
	        });
		}finally{
			connection.denyMessageReceipt();
		}
		
	}

	@Override
	public void batchEnding(final ImcConnectionAdapter connection)
			throws TncException, TerminatedException {
		try{
			this.execute(new Callable<Boolean>(){
	
	        	@Override
				public Boolean call() throws TncException, TerminatedException {
					adapter.batchEnding(connection);
					return Boolean.TRUE;
				}	
	
	        });
		}finally{
			connection.denyMessageReceipt();
		}
	}

	@Override
	public void terminate() throws TerminatedException {
		this.adapter.terminate();
	}

	@Override
	public Object getAttribute(TncAttributeType type) throws TncException {
		return this.adapter.getAttribute(type);
	}

	@Override
	public void setAttribute(TncAttributeType type, Object value)
			throws TncException {
		this.adapter.setAttribute(type, value);
		
	}

	public void execute(Callable<Boolean> function) throws TncException, TerminatedException{
		ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
		//Use executor to execute a command and stop it after a fixed time length.
        final Future<?> task = exec.submit(function);
        exec.schedule(new WatchDog(task), this.timeout, TimeUnit.MILLISECONDS);
        // shutdown the executor after finished, timeout is 3 times the command timeout should not 
        // ever be used.
        try {
        	// It has to be something blocking here, because the send method in state cann't send
        	// messages if the IMC has not returned from the message.
        	LOGGER.debug("Wait on the task " +task.toString()+ " to finish.");
        	task.get();
		} catch (InterruptedException e) {
			LOGGER.debug("Thread " + Thread.currentThread().toString() + " interrupted."); 
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			Throwable t = e.getCause();
			// Cast to TNCException and throw further;
			if(t instanceof TncException){
				throw (TncException)t;
			}else if(t instanceof TerminatedException){
				throw (TerminatedException) t;
			}
		} catch (CancellationException e){
			LOGGER.debug("Task " + task.toString() + " was cancelled, it may have taken more than " + this.timeout + " milliseconds.");
		}
        exec.shutdown();
        LOGGER.debug("Task "+task.toString()+" finished and time control resources released."); 
	}
	
	      
	@SuppressWarnings("rawtypes")
	private class WatchDog implements Runnable{

		private Future task;
		          
		public WatchDog(Future task){
		   this.task = task;
		}
		          
		@Override
		public void run() {
		  if(!this.task.isDone()){
			   LOGGER.debug("WatchDog released to cancel method call.");
			   this.task.cancel(true);
		  }
		}
	}
	
	
}
