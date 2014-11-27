package de.hsbremen.tc.tnc.adapter.im;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.ImConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class ImcAdapterTimeProxy implements ImcAdapter{

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
			ImConnectionState state) throws TncException, TerminatedException {
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
        	task.get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			Throwable t = e.getCause();
			// Cast to TNCException and throw further;
			if(t instanceof TncException){
				throw (TncException)t;
			}else if(t instanceof TerminatedException){
				throw (TerminatedException) t;
			}
		}
        exec.shutdown();
	}
	
	      
	@SuppressWarnings("rawtypes")
	private class WatchDog implements Runnable{

		private Future task;
		          
		public WatchDog(Future task){
		   this.task = task;
		}
		          
		@Override
		public void run() {
		   this.task.cancel(true);
		}
	}
	
	
}
