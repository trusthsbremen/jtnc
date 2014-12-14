package de.hsbremen.tc.tnc.message.util;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultByteBuffer implements ByteBuffer {
	
	private final List<byte[]> buffer;
	
	private final int chunkSize;
	private final long capacity; 
	private final int lastRowSize;
	
	private int readColPointer;
	private int readRowPointer;
	private int writeColPointer;
	private int writeRowPointer;

	public DefaultByteBuffer(long capacity){
		this(capacity, Integer.MAX_VALUE);
	}
	
	protected DefaultByteBuffer(long capacity,int chunkSize){
		this.chunkSize = chunkSize;
		if(capacity <= this.chunkSize){
			this.buffer = new ArrayList<>(1);
			this.buffer.add(new byte[(int)capacity]);
			this.lastRowSize = (int)capacity;
		}else{
			
			// count last row size
			this.lastRowSize = (int)(capacity%this.chunkSize);
			
			// count columns
			long cols = (capacity/this.chunkSize);
			cols = (this.lastRowSize > 0 ) ? cols + 1 : cols;
			
			
			if(cols <= Integer.MAX_VALUE){
				
				this.buffer = new ArrayList<>((int)cols);
				this.buffer.add(new byte[this.chunkSize]);
				
			}else{
				
				throw new IllegalArgumentException("Capacity is to large and cannot be supported by this implementation.");
		
			}
		}
		
		this.capacity = capacity;
		this.readColPointer  = 0;
		this.readRowPointer  = -1;
		this.writeColPointer = 0;
		this.writeRowPointer = -1;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#write(byte[])
	 */
	@Override
	public void write(byte[] array){
		if(array == null){
			throw new NullPointerException("Array cannot be null.");
		}
		int length = array.length;
		
		this.checkWriteBufferOverflow(length);
		
		
		
		int index = 0;
		
		if(this.writeRowPointer  > (this.chunkSize - length)){
			while(length > 0){
				this.writeRowPointer++;
				if(this.writeRowPointer >= this.chunkSize){
					if((this.writeColPointer + 1) < (capacity/this.chunkSize)){
						this.buffer.add(new byte[this.chunkSize]);
					}else{
						this.buffer.add(new byte[this.lastRowSize]);
					}
					
					this.writeRowPointer = 0;
					this.writeColPointer ++;
				}
				
				int cpLength = ((this.chunkSize - this.writeRowPointer)  < length) ? this.chunkSize - this.writeRowPointer  : length;
				System.arraycopy(array,index,this.buffer.get(this.writeColPointer), this.writeRowPointer, cpLength);
				length -= cpLength;
				index += cpLength;
				this.writeRowPointer += cpLength -1;
			}
		}else{
			this.writeRowPointer++;
			System.arraycopy(array,index,this.buffer.get(this.writeColPointer), this.writeRowPointer, length);
			this.writeRowPointer += (length -1);
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.message.util.ByteBuffer#write(de.hsbremen.tc.tnc.message.util.ByteBuffer)
	 */
	@Override
	public void write(ByteBuffer byteBuffer) {
		
		this.checkWriteBufferOverflow(byteBuffer.bytesWritten());
		
		while(byteBuffer.bytesWritten() > byteBuffer.bytesRead()){
			if((byteBuffer.bytesWritten() - byteBuffer.bytesRead()) > this.chunkSize){
				this.write(byteBuffer.read(this.chunkSize));
			}else{
				this.write(byteBuffer.read((int)(byteBuffer.bytesWritten() - byteBuffer.bytesRead())));
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#writeByte(byte)
	 */
	@Override
	public void writeByte(byte b){
		// check bounds 
		this.checkWriteBufferOverflow(1);
		
		this.writeRowPointer++;
		
		if(this.writeRowPointer >= this.chunkSize){
			if((this.writeColPointer + 1) < (capacity/this.chunkSize)){
				this.buffer.add(new byte[this.chunkSize]);
			}else{
				this.buffer.add(new byte[this.lastRowSize]);
			}
			
			this.writeRowPointer = 0;
			this.writeColPointer ++;
		}
		
		this.buffer.get(this.writeColPointer)[this.writeRowPointer] = (byte)b;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#writeUnsignedByte(short)
	 */
	@Override
	public void writeUnsignedByte(short unsigned){
		this.writeByte((byte)(unsigned));
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#writeShort(short)
	 */
	@Override
	public void writeShort(short signed){
		byte[] b = new byte[]{
				(byte)(signed >>> 8),
				(byte)(signed)
				};
		this.write(b);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#writeUnsignedShort(int)
	 */
	@Override
	public void writeUnsignedShort(int unsigned){
		byte[] b = new byte[]{
				(byte)(unsigned >>> 8),
				(byte)(unsigned)
				};
		this.write(b);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#writeInt(int)
	 */
	@Override
	public void writeInt(int signed){
		byte[] b = new byte[]{
				(byte)(signed >>> 24),
				(byte)(signed >>> 16),
				(byte)(signed >>> 8),
				(byte)(signed)
				};
		this.write(b);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#writeUnsignedInt(long)
	 */
	@Override
	public void writeUnsignedInt(long unsigned){
		byte[] b = new byte[]{
				(byte)(unsigned >>> 24),
				(byte)(unsigned >>> 16),
				(byte)(unsigned >>> 8),
				(byte)(unsigned)
				};
		this.write(b);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#writeLong(long)
	 */
	@Override
	public void writeLong(long signed){
		byte[] b = new byte[]{
				
				(byte)(signed >>> 56),
				(byte)(signed >>> 48),
				(byte)(signed >>> 40),
				(byte)(signed >>> 32),
				(byte)(signed >>> 24),
				(byte)(signed >>> 16),
				(byte)(signed >>> 8),
				(byte)(signed)
				};
		this.write(b);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#writeDigits(long, byte)
	 */
	@Override
	public void writeDigits(long number, byte length){
		if( length == 0 || length > 8 ){
			throw new IllegalArgumentException("Supplied length is to large.");
		}
		byte[] b = new byte[length];
		
		byte l = (byte)(length - 1) ;
		
		for (int i = 0; i < length; i++) {
	
			b[i] = (byte)(number >>> (8*(l-i)));
	
		}
		this.write(b);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#read(int)
	 */
	@Override
	public byte[] read(int length){
		if(length <= 0){
			throw new NullPointerException("Length must be a positive integer.");
		}
		
		int length2 = length;
		
		this.checkReadBufferUnderflow(length2);
		
		byte[] array = new byte[length];
		
		int index = 0;
		
		if(this.readRowPointer  > (this.chunkSize - length2)){
			while(length2 > 0){
				this.readRowPointer++;
				if(this.readRowPointer >= this.chunkSize){
					this.readRowPointer = 0;
					this.readColPointer ++;
				}
				
				int cpLength = ((this.chunkSize - this.readRowPointer)  < length2) ? this.chunkSize - this.readRowPointer  : length2;
				System.arraycopy(this.buffer.get(this.readColPointer), this.readRowPointer, array,index,cpLength);
				length2 -= cpLength;
				index += cpLength;
				this.readRowPointer += cpLength -1;
			}
		}else{
			this.readRowPointer++;
			System.arraycopy(this.buffer.get(this.readColPointer), this.readRowPointer, array,index, length2);
			this.readRowPointer += (length2 -1);
		}
		
		return array;
	}
	
	
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.message.util.ByteBuffer#read(long)
	 */
	@Override
	public ByteBuffer read(long length) {
		
		if(length <= 0){
			throw new NullPointerException("Length must be a positive integer.");
		}
		
		this.checkReadBufferUnderflow(length);
		
		ByteBuffer b = new DefaultByteBuffer(length);
		
		while(b.bytesWritten() < length){
			if(length - b.bytesWritten() > this.chunkSize){
				b.write(this.read(this.chunkSize));
			}else{
				b.write(this.read((int)(length-b.bytesWritten())));
			}
		}
		
		return b;
		
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#readByte()
	 */
	@Override
	public byte readByte(){
		// check bounds 
			this.checkReadBufferUnderflow(1);
				
			this.readRowPointer++;
	
			if(this.readRowPointer >= this.chunkSize){
				this.readRowPointer = 0;
				this.readColPointer ++;
			}
				
			return this.buffer.get(this.readColPointer)[this.readRowPointer];
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
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#clear()
	 */
	@Override
	public void clear(){

		this.readColPointer  = 0;
		this.readRowPointer  = -1;
		this.writeColPointer = 0;
		this.writeRowPointer = -1;
		
		this.buffer.clear();

		if(capacity <= this.chunkSize){
			this.buffer.add(new byte[(int)capacity]);
		}else{
			
			// count columns
			long cols = (capacity/this.chunkSize);
			cols = (this.lastRowSize > 0 ) ? cols + 1 : cols;
			
			if(cols <= Integer.MAX_VALUE){

				this.buffer.add(new byte[this.chunkSize]);
				
			}else{
				
				throw new IllegalArgumentException("Capacity is to large and cannot be supported by this implementation.");
		
			}
		}

	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#writePos()
	 */
	@Override
	public long bytesWritten(){
		return (this.writeColPointer * this.chunkSize) + this.writeRowPointer + 1;
	}
		
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.ByteBuffer#readPos()
	 */
	@Override
	public long bytesRead(){
		return (this.readColPointer * this.chunkSize) + this.readRowPointer + 1 ;
	}
	
	
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.message.util.ByteBuffer#capacity()
	 */
	@Override
	public long capacity() {
		return this.capacity;
	}

	private void checkWriteBufferOverflow(long lookAhead) {
		if(lookAhead <= 0){
			throw new IllegalArgumentException("Parameter must be a positiv integer.");
		}
		
		long currentFullPos = (this.writeColPointer * this.chunkSize) + this.writeRowPointer + 1;
		
		if(currentFullPos > (this.capacity - lookAhead)){
			throw new BufferOverflowException();
		}
	}
	
	private void checkReadBufferUnderflow(long lookAhead) {
		
		if(lookAhead <= 0){
			throw new IllegalArgumentException("Parameter must be a positiv integer.");
		}
		
		long currentFullPosWrite = (this.writeColPointer * this.chunkSize) + this.writeRowPointer + 1;
		long currentFullPosRead = (this.readColPointer * this.chunkSize) + this.readRowPointer + 1;
		
		if(currentFullPosRead > (currentFullPosWrite - lookAhead)){	
			throw new BufferUnderflowException();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.buffer == null) ? 0 : this.buffer.hashCode());
		result = prime * result
				+ (int) (this.capacity ^ (this.capacity >>> 32));
		result = prime * result + this.chunkSize;
		result = prime * result + this.lastRowSize;
		result = prime * result + this.readColPointer;
		result = prime * result + this.readRowPointer;
		result = prime * result + this.writeColPointer;
		result = prime * result + this.writeRowPointer;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DefaultByteBuffer other = (DefaultByteBuffer) obj;
		if (this.buffer == null) {
			if (other.buffer != null) {
				return false;
			}
		} else {	
			for (int i = 0; i < this.buffer.size(); i++) {
				if(this.buffer.get(i) == null){
					if(other.buffer.get(i) != null){
						return false;
					}	
				}else{
					if(!(Arrays.equals(this.buffer.get(i), other.buffer.get(i)))){
						return false;
					}
				}
			}
		}
		if (this.capacity != other.capacity) {
			return false;
		}
		if (this.chunkSize != other.chunkSize) {
			return false;
		}
		if (this.lastRowSize != other.lastRowSize) {
			return false;
		}
		if (this.readColPointer != other.readColPointer) {
			return false;
		}
		if (this.readRowPointer != other.readRowPointer) {
			return false;
		}
		if (this.writeColPointer != other.writeColPointer) {
			return false;
		}
		if (this.writeRowPointer != other.writeRowPointer) {
			return false;
		}
		return true;
	}
	
	
	
}
