/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
