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

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileChangeHandler;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileChangeMonitor;

public class DefaultConfigurationFileChangeMonitor implements ConfigurationFileChangeMonitor {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultConfigurationFileChangeMonitor.class);

    private final long interval;
    private final Set<ConfigurationFileChangeHandler> listener;
    private final FileChecker checker;
    private ScheduledExecutorService executor;

    public DefaultConfigurationFileChangeMonitor(File config) {
        this(config, 5000, false);
    }

    public DefaultConfigurationFileChangeMonitor(File config, long interval,
            boolean paranoid) {

        this.interval = interval;
        this.listener = new HashSet<>();
        this.checker = new FileChecker(config, paranoid);

    }

    @Override
    public void start() {
       this.executor = Executors.newScheduledThreadPool(1);
       // check once, than start the regular checking
       this.executor.schedule(this.checker, 0, TimeUnit.MILLISECONDS);
       this.executor.scheduleAtFixedRate(
               this.checker, interval, interval,
               TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        this.executor.shutdownNow();
    }

    @Override
    public void add(ConfigurationFileChangeHandler listener) {
        synchronized (listener) {
            this.listener.add(listener);
        }

    }

    @Override
    public void remove(ConfigurationFileChangeHandler listener) {
        synchronized (listener) {
            this.listener.remove(listener);
        }
    }

    private void notifyChange(File file) {
        for (ConfigurationFileChangeHandler listener : this.listener) {
            listener.notifyChange(file);
        }
    }

    private void notifyDelete(File file) {
        for (ConfigurationFileChangeHandler listener : this.listener) {
            listener.notifyDelete(file);
        }
    }

    private class FileChecker implements Runnable {

        private final File config;
        private final boolean paranoid;
        private final String messageDigestIdentifier;

        private boolean noFileExists;
        private byte[] change;

        public FileChecker(File config, boolean paranoid) {
            this(config, paranoid, null);
        }

        public FileChecker(File config, boolean paranoid,
                String messageDigestIdentifier) {
            this.config = config;
            this.paranoid = paranoid;
            this.messageDigestIdentifier = (messageDigestIdentifier != null
                    && !messageDigestIdentifier.isEmpty())
                    ? messageDigestIdentifier : "SHA1";
            this.change = new byte[0];
        }

        @Override
        public void run() {

            if (!this.config.exists()) {
                if (!this.noFileExists) {
                    notifyDelete(this.config);
                    this.noFileExists = true;
                }
                return;
            }

            if (this.noFileExists) {
                this.noFileExists = false;
            }

            byte[] changeTracker = new byte[0];
            if (this.paranoid) {
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
                notifyChange(this.config);
            }
        }

        private byte[] checkFileChecksum() throws IOException,
                NoSuchAlgorithmException {
            byte[] newDigest = new byte[0];
            MessageDigest md = MessageDigest
                    .getInstance(this.messageDigestIdentifier);
            DigestInputStream dis = null;
            try {
                dis = new DigestInputStream(new BufferedInputStream(
                        new FileInputStream(this.config)), md);

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

        private byte[] checkFileTimestamp() {
            byte[] newTime = ByteBuffer.allocate(8)
                    .putLong(this.config.lastModified()).array();
            return newTime;
        }

    }

}
