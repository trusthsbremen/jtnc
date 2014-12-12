package de.hsbremen.tc.tnc.message.util;

public interface ByteBuffer {

	public abstract void write(byte[] array);

	public abstract void writeByte(byte b);

	public abstract void writeUnsignedByte(short unsigned);

	public abstract void writeShort(short signed);

	public abstract void writeUnsignedShort(int unsigned);

	public abstract void writeInt(int signed);

	public abstract void writeUnsignedInt(long unsigned);

	public abstract void writeLong(long signed);

	public abstract void writeDigits(long number, byte length);

	public abstract byte[] read(int length);

	public abstract byte readByte();

	public abstract short readShort();

	public abstract short readShort(byte length);

	public abstract int readInt();

	public abstract int readInt(byte length);

	public abstract long readLong();

	public abstract long readLong(byte length);

	public abstract void clear();

	public abstract long writePos();

	public abstract long readPos();

}