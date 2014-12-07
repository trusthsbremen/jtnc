package de.hsbremen.tc.tnc.transport.connection.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NoCloseOutputStream extends FilterOutputStream{

	boolean closed;
	
	public NoCloseOutputStream(OutputStream out) {
		super(out);
		this.closed = false;
	}
	
	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		if(!closed){
			super.write(b);
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException {
		if(!closed){
			super.write(b);
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if(!closed){
			super.write(b, off, len);
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#flush()
	 */
	@Override
	public void flush() throws IOException {
		if(!closed){
			super.flush();
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		if(!closed){
			this.closed = true;
		}else{
			throw new IOException("Stream pretends to be closed.");
		}
	}
	
	

}
