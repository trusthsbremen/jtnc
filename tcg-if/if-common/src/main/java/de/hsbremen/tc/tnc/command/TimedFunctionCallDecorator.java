package de.hsbremen.tc.tnc.command;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.trustedcomputinggroup.tnc.ifimc.TNCException;

public class TimedFunctionCallDecorator implements FunctionCall{
    
    private static final long DEFAULT_TIMEOUT = 500;
    
    private final long timeout;
    
    private final ScheduledExecutorService exec;

	private final FunctionCall function;
    
	public TimedFunctionCallDecorator(final FunctionCall function){
		this(function, DEFAULT_TIMEOUT);
	}
	
    public TimedFunctionCallDecorator(final FunctionCall function, long timeout){
        this.exec = Executors.newScheduledThreadPool(2);
        this.function = function;
        this.timeout = timeout;
    }

    
    public void call() throws TNCException{
    	
    	if(this.function == null || timeout <= 0){
    		this.exec.shutdown();
        	return;
        }
    	
    	//Use executor to execute a command and stop it after a fixed time length.
        final Future<?> task = exec.submit(new Callable<Boolean>(){

        	@Override
			public Boolean call() throws TNCException {
				function.call();
				return Boolean.TRUE;
			}	

        });
        // second task which is sheduled to stop the first one via the future
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
			if(t instanceof TNCException){
				throw (TNCException) t;
			}
		}
//          try {
//              // It has to be something blocking here, because the send method in state cann't send
//              // messages if the IMC has not returned from the message.
//              exec.awaitTermination(this.timeout*3, TimeUnit.MILLISECONDS);
//          } catch (InterruptedException e) {
//              // TODO Auto-generated catch block
//              e.printStackTrace();
//          }
        exec.shutdown();
          
      }
      
    @SuppressWarnings("rawtypes")
    class WatchDog implements Runnable{

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
