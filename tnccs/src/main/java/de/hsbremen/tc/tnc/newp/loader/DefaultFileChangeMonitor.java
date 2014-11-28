package de.hsbremen.tc.tnc.newp.loader;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultFileChangeMonitor implements Runnable, FileChangeMonitor{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFileChangeMonitor.class);
	
	private final File config;
	private final Set<FileChangeListener> listener;
	
	private final long intervall;
	private final boolean paranoid;
	
	private final String messageDigestIdentifier;
	private byte[] change;
	
	public DefaultFileChangeMonitor(File config){
		this(config,5000,false);
	}
	
	public DefaultFileChangeMonitor(File config, long intervall, boolean paranoid){
		//if(config != null && config.exists && config.canRead()){	
		this.config = config;
		this.intervall = intervall;
		this.messageDigestIdentifier = "SHA1";
		this.paranoid = paranoid;
		this.listener = new HashSet<>();
		this.change = new byte[0];
	}

	@Override
	public void run() {
		
		while(!Thread.interrupted()){
			if(!this.config.exists()){
				this.notifyDelete(this.config);
				break;
			}
			
			byte[] changeTracker = new byte[0];
			if(paranoid){
				try{
					changeTracker =  this.checkFileChecksum();
				} catch (NoSuchAlgorithmException e) {
					LOGGER.error("Message digest could not be calculated, because the algorithm was not found.", e);
					changeTracker = checkFileTimestamp();
				} catch (FileNotFoundException e) {
					LOGGER.error("Message digest could not be calculated, because the file was not found.", e);
					changeTracker = checkFileTimestamp();
				} catch(IOException e){
					LOGGER.error("Message digest could not be calculated, because of an I/O error. Using timestamp instead.", e);
					changeTracker = checkFileTimestamp();
				}
			}else{
				changeTracker = this.checkFileTimestamp();
			}
			
			if(!Arrays.equals(this.change, changeTracker)){
				this.change = changeTracker;
				this.notifyChange(this.config);
			}
			
			try{
				Thread.sleep(this.intervall);
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
			}
		}
	}
	
	private byte[] checkFileChecksum() throws IOException, NoSuchAlgorithmException{
		byte[] newDigest = new byte[0];
		MessageDigest md = MessageDigest.getInstance(this.messageDigestIdentifier);
		DigestInputStream dis = null;
		try{
			dis = new DigestInputStream(new BufferedInputStream(new FileInputStream(this.config)),md);
			
			while ((dis.read()) != -1){}
			newDigest = md.digest();

		}finally{
			if(dis != null){
				dis.close();
			}
		}
		
		return newDigest;
	}
	
	private byte[] checkFileTimestamp(){
		byte[] newTime = ByteBuffer.allocate(8).putLong(this.config.lastModified()).array();
		return newTime;
	}
	
	private void notifyChange(File file){
		for (FileChangeListener listener : this.listener) {
			listener.notifyChange(this.config);
		}
	}
	
	private void notifyDelete(File file){
		for (FileChangeListener listener : this.listener) {
			listener.notifyDelete(this.config);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.imhandler.loader.FileChangeMonitor#add(de.hsbremen.tc.tnc.imhandler.loader.FileChangeObserver)
	 */
	@Override
	public void add(FileChangeListener listener){
		synchronized (listener) {
			this.listener.add(listener);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.imhandler.loader.FileChangeMonitor#remove(de.hsbremen.tc.tnc.imhandler.loader.FileChangeObserver)
	 */
	@Override
	public void remove(FileChangeListener listener){
		synchronized (listener) {
			this.listener.remove(listener);
		}
	}
	
}
