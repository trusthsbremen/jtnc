package org.ietf.nea.pb.serialize.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TrackedBufferedInputStream extends BufferedInputStream {

	private long tracked;
	private long marked;
	private int readLimit;
	
	public TrackedBufferedInputStream(InputStream in) {
		super(in);
		this.tracked = 0L;
		this.marked = 0L;
		this.readLimit = 0;
		
	}
	
	public TrackedBufferedInputStream(InputStream in, int bufferSize) {
		super(in, bufferSize);
		this.tracked = 0L;
		this.marked = 0L;
		this.readLimit = 0;
	}

	@Override
	public int read() throws IOException {
		int count = super.read(); 
		this.incrementTracked(count);
		return count;
	}

	/* (non-Javadoc)
	 * @see java.io.BufferedInputStream#read(byte[], int, int)
	 */
	@Override
	public synchronized int read(byte[] b, int off, int len) throws IOException {
		int count = super.read(b, off, len); 
		this.incrementTracked(count);
		return count;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int count = this.read(b,0,b.length);
		return count;
	}
	
	/* (non-Javadoc)
	 * @see java.io.BufferedInputStream#skip(long)
	 */
	@Override
	public synchronized long skip(long n) throws IOException {
		long count = super.skip(n);
		this.incrementTracked(count);
		return count;
	}

	/* (non-Javadoc)
	 * @see java.io.BufferedInputStream#available()
	 */
	@Override
	public synchronized int available() throws IOException {
		return super.available();
	}

	/* (non-Javadoc)
	 * @see java.io.BufferedInputStream#mark(int)
	 */
	@Override
	public synchronized void mark(int readlimit) {
		this.setMarked();
		this.readLimit = readlimit;
		super.mark(readlimit);
	}

	/* (non-Javadoc)
	 * @see java.io.BufferedInputStream#reset()
	 */
	@Override
	public synchronized void reset() throws IOException {
		this.resetTracked();
		super.reset();
	}

	/* (non-Javadoc)
	 * @see java.io.BufferedInputStream#markSupported()
	 */
	@Override
	public boolean markSupported() {
		// TODO Auto-generated method stub
		return super.markSupported();
	}

	/* (non-Javadoc)
	 * @see java.io.BufferedInputStream#close()
	 */
	@Override
	public void close() throws IOException {
		
		this.marked = 0L;
		this.readLimit = 0;
		this.tracked = 0L;
		super.close();
	}

	private void incrementTracked(long count){
		if(readLimit > 0){
			this.decrementReadlimit(count);
		}
		if(count >  0 && this.tracked > -1){
			if(Long.MAX_VALUE - count >= this.tracked){
				this.tracked = this.tracked + count;
			}else{
				this.tracked = -1;
			}
		}
	}

	private void decrementReadlimit(long count) {
		readLimit -= count;
		if(readLimit < 0){
			readLimit = 0;
		}
	}

	private void setMarked(){
		this.marked = this.tracked;
	}
	
	private void resetTracked(){
		if(this.readLimit > 0){
			if(this.marked != 0)
				this.tracked = this.marked;
				this.marked = 0;
		}
		this.readLimit = 0;
	}
	
	public long getTracked(){
		return this.tracked;
	}
}
