package de.hsbremen.tc.tnc.message.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.util.Arrays;

public class StreamedReadOnlyBuffer implements ByteBuffer {

	private final static int DEFAULT_STREAM_CHUNK = 8192;
	private long capacity;; 
	
	private int readPointer;
	private long writePointer;
	private InputStream stream;
	
	public StreamedReadOnlyBuffer(InputStream in){
		this.stream = in;
		this.capacity = Long.MAX_VALUE;
		this.writePointer = Long.MAX_VALUE;
		this.readPointer = 0;
		
	}
	
	@Override
	public void write(ByteBuffer buffer) {
		throw new UnsupportedOperationException("Operation not supported, because buffer is read only.");

	}

	@Override
	public void write(byte[] array) {
		throw new UnsupportedOperationException("Operation not supported, because buffer is read only.");

	}

	@Override
	public void writeByte(byte b) {
		throw new UnsupportedOperationException("Operation not supported, because buffer is read only.");

	}

	@Override
	public void writeUnsignedByte(short unsigned) {
		throw new UnsupportedOperationException("Operation not supported, because buffer is read only.");

	}

	@Override
	public void writeShort(short signed) {
		throw new UnsupportedOperationException("Operation not supported, because buffer is read only.");

	}

	@Override
	public void writeUnsignedShort(int unsigned) {
		throw new UnsupportedOperationException("Operation not supported, because buffer is read only.");

	}

	@Override
	public void writeInt(int signed) {
		throw new UnsupportedOperationException("Operation not supported, because buffer is read only.");

	}

	@Override
	public void writeUnsignedInt(long unsigned) {
		throw new UnsupportedOperationException("Operation not supported, because buffer is read only.");

	}

	@Override
	public void writeLong(long signed) {
		throw new UnsupportedOperationException("Operation not supported, because buffer is read only.");

	}

	@Override
	public void writeDigits(long number, byte length) {
		throw new UnsupportedOperationException("Operation not supported, because buffer is read only.");

	}

	@Override
	public ByteBuffer read(long length) {
		if(this.stream == null){
			throw new BufferUnderflowException();
		}
		ByteBuffer b = new DefaultByteBuffer(length);
		int count = 0;
		while((length - b.bytesWritten()) > 0 && count > -1){
			try{
				byte[] buf = null;
				if(length > DEFAULT_STREAM_CHUNK){
					buf = new byte[DEFAULT_STREAM_CHUNK];
					count = this.stream.read(buf);
				}else{
					buf = new byte[(int)length];
					count = this.stream.read(buf);
				}
				if(count > -1){
					b.write(Arrays.copyOf(buf, count));
				}
				
			}catch(IOException e){
				throw new BufferUnderflowException();
			}
		}
		
		this.readPointer += b.bytesWritten();
		if(count < 0){
			this.writePointer = readPointer;
		}
		return b;
	}

	@Override
	public byte[] read(int length) {
		if(this.stream == null){
			throw new BufferUnderflowException();
		}
		if(length <= 0){
			return new byte[0];
		}
		
		byte [] ret = new byte[0];
		int count = 0;
		try{

			byte[] buf = new byte[length];
			
			count = this.stream.read(buf);
			
			if(count > -1){
				ret = Arrays.copyOf(buf, count);
			}
			
		}catch(IOException e){
			throw new BufferUnderflowException();
		}
		
		this.readPointer += ret.length;
		
		if(count < 0){
			this.writePointer = readPointer;
		}
		
		return ret;
	}

	@Override
	public byte readByte() {
		if(this.stream == null){
			throw new BufferUnderflowException();
		}
		byte b = 0;
		try {
			b = (byte)this.stream.read();
		} catch (IOException e) {
			throw new BufferUnderflowException();
		}
		this.readPointer ++;
		
		if(b < 0){
			this.writePointer = readPointer;
		}
		
		return b;

	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#readShort()
	 */
	@Override
	public short readShort(){
		return this.readShort((byte)2);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#readShort(byte)
	 */
	@Override
	public short readShort(byte length){
		if( length == 0 || length > 2 ){
			throw new IllegalArgumentException("Supplied length is to large.");
		}
		
		byte[] b = this.read(length);
		
		short value = 0;
		for(int i = 0; i < b.length; i++){
			value = (short) ((value << 8) + (b[i] & 0xFF)); 
		}
		
		return value;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#readInt()
	 */
	@Override
	public int readInt(){
		return this.readInt((byte)4);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#readInt(byte)
	 */
	@Override
	public int readInt(byte length){
		if( length == 0 || length > 4 ){
			throw new IllegalArgumentException("Supplied length is to large.");
		}
		
		byte[] b = this.read(length);
		int value = 0;
		for(int i = 0; i < b.length; i++){
			value = (int) ((value << 8) + (b[i] & 0xFF)); 
		}
		
		return value;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#readLong()
	 */
	@Override
	public long readLong(){
		return this.readLong((byte)8);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#readLong(byte)
	 */
	@Override
	public long readLong(byte length){
		if( length == 0 || length > 8 ){
			throw new IllegalArgumentException("Supplied length is to large.");
		}
		
		byte[] b = this.read(length);
		
		long value = 0L;
		for(int i = 0; i < b.length; i++){
			value = (value << 8) + (b[i] & 0xFF); 
		}
		
		return value;
	}
	

	@Override
	public void clear() {
		this.stream = null;
		
		this.readPointer = 0;
		this.writePointer = 0;
		this.capacity = 0;

	}

	@Override
	public long bytesWritten() {
		return this.writePointer;
	}

	@Override
	public long bytesRead() {
		return this.readPointer;
	}

	@Override
	public long capacity() {
		return this.capacity;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.message.util.ByteBuffer#isReadable()
	 */
	@Override
	public boolean isReadable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.message.util.ByteBuffer#isWriteable()
	 */
	@Override
	public boolean isWriteable() {
		return false;
	}

	
}
