package de.hsbremen.tc.tnc.transport.connection.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NoCloseInputStream extends FilterInputStream{

	boolean closed;
	
	public NoCloseInputStream(InputStream in) {
		super(in);
		this.closed = false;
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#read()
	 */
	@Override
	public int read() throws IOException {
		if(!closed){
			return super.read();
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException {
		if(!closed){
			return super.read(b);
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if(!closed){
			return super.read(b, off, len);
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException {
		if(!closed){
			return super.skip(n);
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#available()
	 */
	@Override
	public int available() throws IOException {
		if(!closed){
			return super.available();
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#close()
	 */
	@Override
	public void close() throws IOException {
		if(!closed){
			this.closed = true;
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#mark(int)
	 */
	@Override
	public synchronized void mark(int readlimit) {
		super.mark(readlimit);
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#reset()
	 */
	@Override
	public synchronized void reset() throws IOException {
		if(!closed){
			super.reset();
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#markSupported()
	 */
	@Override
	public boolean markSupported() {
		return super.markSupported();
	}

	

}
