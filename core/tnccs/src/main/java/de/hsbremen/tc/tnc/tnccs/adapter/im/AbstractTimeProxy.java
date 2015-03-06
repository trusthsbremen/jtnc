/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
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

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;

/**
 * Generic proxy base to control the time a function
 * call needs to finish.
 *
 *
 */
public abstract class AbstractTimeProxy {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractTimeProxy.class);

    private final long timeout;

    /**
     * Creates the proxy base with the given timeout for a function call.
     *
     * @param timeout the function timeout
     */
    protected AbstractTimeProxy(final long timeout) {
        if (timeout < HSBConstants.TCG_IM_MAX_FUNCTION_RUNTIME) {
            this.timeout = timeout;
        } else {
            throw new IllegalArgumentException(new StringBuilder()
                    .append("Timeout of ").append(timeout)
                    .append(" milliseconds is to large, ")
                    .append("timeout must be less than one second ")
                    .append("( < 1000 milliseconds).").toString());
        }
    }

    /**
     * Executes a function contained in a callable object.
     *
     * @param function the callable function
     * @throws TncException if function execution fails
     * @throws TerminatedException if the called IM(C/V) is terminated
     */
    protected void execute(final Callable<Boolean> function)
            throws TncException, TerminatedException {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
        // Use executor to execute a command and stop it after a fixed time
        // length.
        final Future<?> task = exec.submit(function);
        exec.schedule(new WatchDog(task), this.timeout, TimeUnit.MILLISECONDS);
        // shutdown the executor after finished, timeout is 3 times the command
        // timeout should not
        // ever be used.
        try {
            // It has to be something blocking here, because the send method in
            // state cann't send
            // messages if the IMC has not returned from the message.
            LOGGER.debug("Wait on the task " + task.toString() + " to finish.");
            task.get();
        } catch (InterruptedException e) {
            LOGGER.debug("Thread " + Thread.currentThread().toString()
                    + " interrupted.");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            Throwable t = e.getCause();
            // Cast to TNCException and throw further;
            if (t instanceof TncException) {
                throw (TncException) t;
            } else if (t instanceof TerminatedException) {
                throw (TerminatedException) t;
            }
        } catch (CancellationException e) {
            LOGGER.debug("Task " + task.toString()
                    + " was cancelled, it may have taken more than "
                    + this.timeout + " milliseconds.");
        }
        exec.shutdown();
        LOGGER.debug("Task " + task.toString()
                + " finished and time control resources released.");
    }

    /**
     * Runnable that control the execution time of a function
     * and stops its execution, if a timeout is reached.
     *
         *
     */
    @SuppressWarnings("rawtypes")
    private class WatchDog implements Runnable {

        private Future task;

        /**
         * Creates the runnable with the given task to
         * watch out for.
         *
         * @param task the function task to control
         */
        public WatchDog(final Future task) {
            this.task = task;
        }

        @Override
        public void run() {
            if (!this.task.isDone()) {
                LOGGER.debug("WatchDog released to cancel method call.");
                this.task.cancel(true);
            }
        }
    }
}
