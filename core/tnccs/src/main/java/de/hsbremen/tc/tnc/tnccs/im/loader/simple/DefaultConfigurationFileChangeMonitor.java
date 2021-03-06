/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileChangeMonitor;

/**
 * Default file change monitor, that watches a file for changes or deletion and
 * notifies change handlers about these events. It checks the file
 * in a regular timed interval.
 *
 *
 */
public class DefaultConfigurationFileChangeMonitor implements
        ConfigurationFileChangeMonitor {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultConfigurationFileChangeMonitor.class);
    private static final long DEFAULT_CHECK_INTERVAL = 5000;

    private final long interval;
    private final Set<ConfigurationFileChangeListener> listener;
    private final FileChecker checker;
    private ScheduledExecutorService executor;

    /**
     * Creates a file change monitor for the given file with
     * default values.
     * <ul>
     *  <li>Check interval: 5 sec</li>
     *  <li>Check type: last modification time</li>
     * </ul>
     *
     * @param file the file to monitor
     */
    public DefaultConfigurationFileChangeMonitor(final File file) {
        this(file, DEFAULT_CHECK_INTERVAL, false);
    }

    /**
     * Creates a file change monitor for the given file based
     * on the given values.
     *
     * @param file the file to monitor
     * @param interval the check interval
     * @param improved if true a hash sum is used to monitor the
     * file instead of the last modification time
     */
    public DefaultConfigurationFileChangeMonitor(final File file,
            final long interval, final boolean improved) {

        this.interval = interval;
        this.listener = new HashSet<>();
        this.checker = new FileChecker(file, improved);

    }

    @Override
    public void start() {
        this.executor = Executors.newScheduledThreadPool(1);
        // check once, than start the regular checking
        this.executor.schedule(this.checker, 0, TimeUnit.MILLISECONDS);
        this.executor.scheduleAtFixedRate(this.checker, interval, interval,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        this.executor.shutdownNow();
    }

    @Override
    public void add(final ConfigurationFileChangeListener listener) {
        synchronized (listener) {
            this.listener.add(listener);
        }

    }

    @Override
    public void remove(final ConfigurationFileChangeListener listener) {
        synchronized (listener) {
            this.listener.remove(listener);
        }
    }

    /**
     * Runnable to check a files for changes.
     *
         *
     */
    private class FileChecker implements Runnable {

        private final File file;
        private final boolean improved;
        private final String messageDigestIdentifier;

        private boolean noFileExists;
        private byte[] change;

        /**
         * Creates the file checker for the given file.
         *
         * @param file the file to check
         * @param improved if true a hash sum is used to monitor the
         * file instead of the last modification time
         */
        public FileChecker(final File file, final boolean improved) {
            this(file, (improved) ? "SHA1" : null);
        }

        /**
         * Creates the file checker for the given file.
         *
         * @param file the file to check
         * @param messageDigestIdentifier the identifier for the message digest
         * algorithm, that should be used, if null only modification time check
         * is used
         */
        public FileChecker(final File file,
                final String messageDigestIdentifier) {
            this.file = file;
            this.improved = (messageDigestIdentifier != null
                    && !messageDigestIdentifier.isEmpty());
            this.messageDigestIdentifier =  messageDigestIdentifier;
            this.change = new byte[0];
        }

        @Override
        public void run() {

            if (!this.file.exists()) {
                if (!this.noFileExists) {
                    notifyDelete();
                    this.noFileExists = true;
                }
                return;
            }

            if (this.noFileExists) {
                this.noFileExists = false;
            }

            byte[] changeTracker = new byte[0];
            if (this.improved) {
                try {
                    changeTracker = this.checkFileChecksum();
                } catch (NoSuchAlgorithmException e) {
                    LOGGER.error("Message digest could not be calculated, "
                            + "because the algorithm was not found.", e);
                    changeTracker = checkFileTimestamp();
                } catch (FileNotFoundException e) {
                    LOGGER.error("Message digest could not be calculated, "
                            + "because the file was not found.", e);
                    changeTracker = checkFileTimestamp();
                } catch (IOException e) {
                    LOGGER.error("Message digest could not be calculated, "
                            + "because of an I/O error. "
                            + "Using timestamp instead.", e);
                    changeTracker = checkFileTimestamp();
                }
            } else {
                changeTracker = this.checkFileTimestamp();
            }

            if (!Arrays.equals(this.change, changeTracker)) {
                LOGGER.debug("Configuration file changed. Notify handler.");
                this.change = changeTracker;
                notifyChange();
            }
        }

        /**
         * Notifies the listener that the monitored file has changed.
         */
        private void notifyChange() {
            for (ConfigurationFileChangeListener lis : listener) {
                lis.notifyChange(this.file);
            }
        }

        /**
         * Notifies the listener that the monitored file was deleted.
         */
        private void notifyDelete() {
            for (ConfigurationFileChangeListener lis : listener) {
                lis.notifyDelete();
            }
        }

        /**
         * Monitors the file using a check sum.
         * @return the checksum
         * @throws IOException if file access fails
         * @throws NoSuchAlgorithmException if message digest
         * algorithm identifier is not known
         */
        private byte[] checkFileChecksum() throws IOException,
                NoSuchAlgorithmException {
            byte[] newDigest = new byte[0];
            MessageDigest md = MessageDigest
                    .getInstance(this.messageDigestIdentifier);
            DigestInputStream dis = null;
            try {
                dis = new DigestInputStream(new BufferedInputStream(
                        new FileInputStream(this.file)), md);

                while ((dis.read()) != -1) {
                    // read the digest
                }
                newDigest = md.digest();

            } finally {
                if (dis != null) {
                    dis.close();
                }
            }

            return newDigest;
        }

        /**
         * Monitors the file using the last modified time.
         * @return the time
         */
        private byte[] checkFileTimestamp() {
            final int longByteLength = 8;
            byte[] newTime = ByteBuffer.allocate(longByteLength)
                    .putLong(this.file.lastModified()).array();
            return newTime;
        }
    }
}
