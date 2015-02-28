package de.hsbremen.tc.tnc.message.util;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class ByteBufferTest {

	@Test
	public void testBytePerByte(){
		
		ByteBuffer buf = new DefaultByteBuffer(100);
		
		for(int i = 0; i <=1; i++){
			buf.writeByte((byte)i);
		}
		
		Assert.assertEquals(2, buf.bytesWritten());
		Assert.assertEquals(0, buf.bytesRead());
		
		buf.clear();
		
		Assert.assertEquals(0, buf.bytesWritten());
		Assert.assertEquals(0, buf.bytesRead());
		
		int val = 10;
		
		buf = new DefaultByteBuffer(val,3);
		
		for(int i = 0; i < val; i++){
			buf.writeByte((byte)(i%128));
		}
		
		Assert.assertEquals(val, buf.bytesWritten());
		Assert.assertEquals(0, buf.bytesRead());
	
		for(int i = 0; i < 6; i++){
			Assert.assertEquals(i, buf.readByte());
		}
		
		Assert.assertEquals(val, buf.bytesWritten());
		Assert.assertEquals(6, buf.bytesRead());
		
		buf.clear();
		
		Assert.assertEquals(0, buf.bytesWritten());
		Assert.assertEquals(0, buf.bytesRead());
		
	}
	
	@Test
	public void testByteArrays(){
		
		ByteBuffer buf = new DefaultByteBuffer(100);
		
		byte[] b = new byte[]{0,1,2,3,4,5,6,7,8,9,10};
		
		for (int i = 0; i < 6; i++){
			buf.write(b);
		}
		
		Assert.assertEquals(6*b.length, buf.bytesWritten());
		Assert.assertEquals(0, buf.bytesRead());
		
		int val = 25;
		
		buf = new DefaultByteBuffer(val,3);
		
		byte[] b1 = new byte[]{0,1,2,3,4};
		
		for (int i = 0; i < 2; i++){
			buf.write(b1);
		}
		
		Assert.assertEquals(2*b1.length, buf.bytesWritten());
		Assert.assertEquals(0, buf.bytesRead());
		
		for(int i = 0; i < 2; i++){
			Assert.assertArrayEquals(b1, buf.read(b1.length));
		}
		
		Assert.assertEquals((2*b1.length), buf.bytesWritten());
		Assert.assertEquals((2*b1.length), buf.bytesRead());
		
		for(int i = 0; i <=1; i++){
			buf.writeByte((byte)i);
		}
		
		Assert.assertEquals((2*b1.length)+2, buf.bytesWritten());
		Assert.assertEquals((2*b1.length), buf.bytesRead());
		
		buf.clear();
		
		Assert.assertEquals(0, buf.bytesWritten());
		Assert.assertEquals(0, buf.bytesRead());
		
	}
	
	@Test(expected=BufferOverflowException.class)
	public void testBoundsArrayWrite(){
		int val = 10;
		
		ByteBuffer buf = new DefaultByteBuffer(val,3);
		
		byte[] b1 = new byte[]{0,1,2,3,4};
		
		for (int i = 0; i < 3; i++){
			buf.write(b1);
		}
	}
	
	@Test(expected=BufferOverflowException.class)
	public void testNarrowBoundsArrayWrite(){
		int val = 13;
		
		ByteBuffer buf = new DefaultByteBuffer(val,3);
		
		byte[] b1 = new byte[]{0,1,2,3,4};
		
		for (int i = 0; i < 3; i++){
			buf.write(b1);
		}
	}
	
	@Test
	public void testNarrowBoundsArrayWrite2(){
		int val = 10;
		
		ByteBuffer buf = new DefaultByteBuffer(val,3);
		
		byte[] b1 = new byte[]{0,1,2,3,4};
		
		for (int i = 0; i < 2; i++){
			buf.write(b1);
		}
		
		Assert.assertEquals((2*b1.length), buf.bytesWritten());
		Assert.assertEquals(0, buf.bytesRead());
	}
	
	@Test(expected=BufferOverflowException.class)
	public void testNarrowBoundsByteWrite(){
		int val = 10;
		
		ByteBuffer buf = new DefaultByteBuffer(val,3);

		
		for (int i = 0; i < 11; i++){
			buf.writeByte((byte)i);
		}
	}

	@Test(expected=BufferUnderflowException.class)
	public void testNarrowBoundsByteRead(){
		int val = 10;
		
		ByteBuffer buf = new DefaultByteBuffer(val,3);

		
		for (int i = 0; i < 8; i++){
			buf.writeByte((byte)i);
		}
		
		for(int i = 0; i < 9; i++){
			buf.readByte();
		}
		
	}
	
	@Test(expected=BufferUnderflowException.class)
	public void testNarrowBoundsArrayRead(){
		int val = 13;
		
		ByteBuffer buf = new DefaultByteBuffer(val,3);
		
		byte[] b1 = new byte[]{0,1,2,3,4};
		
		for (int i = 0; i < 2; i++){
			buf.write(b1);
		}
		
		for (int i = 0; i < 3; i++){
			buf.read(b1.length);
		}
	}
	
	@Test
	public void conversion(){
		
		short i = 128;
		Assert.assertEquals(-128,(byte)i);
	
		ByteBuffer b = new DefaultByteBuffer(100);
		b.writeDigits(123456789, (byte)4);
		long l = b.readLong((byte) 4);
		Assert.assertEquals(123456789,l);
		
		b.writeByte((byte)1);
		l = b.readLong((byte) 1);
		Assert.assertEquals(1,l);

	}
	
	@Test
	public void equals(){
	
		byte[] array = {1,23,2,43,56,6,67,6,67,67,89,3,3,23,2};
		
		ByteBuffer a = new DefaultByteBuffer(array.length);
		a.write(array);
		ByteBuffer b = new DefaultByteBuffer(array.length);
		b.write(array);
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(b.equals(a));
		
	}
	
	
	@Test 
	public void testReadBufferToBuffer(){
		byte[] array = {1,23,2,43,56,6,67,6,67,67,89,3,3,23,2};
		ByteBuffer b = new DefaultByteBuffer(100);
		b.write(array);
		
		ByteBuffer b1 = new DefaultByteBuffer(b.bytesWritten());
	
		b1.write(b.read(b.bytesWritten()));
        Assert.assertEquals(b.bytesRead(), b1.bytesWritten());
		
	}
	
	@Test 
	public void testWriteBufferToBuffer(){
		byte[] array = {1,23,2,43,56,6,67,6,67,67,89,3,3,23,2};
		ByteBuffer b = new DefaultByteBuffer(100);
		b.write(array);
		
		ByteBuffer b1 = new DefaultByteBuffer(b.bytesWritten());
		
		b1.write(b);
	    Assert.assertEquals(b.bytesRead(), b1.bytesWritten());
		
		
	}
	
	@Test
	public void revertWriteAndRead(){
	    
	    ByteBuffer buf = new DefaultByteBuffer(100);
       
        byte[] b = new byte[]{0,1,2,3,4,5,6,7,8,9,10};
        
        for (int i = 0; i < 6; i++){
            buf.write(b);
        }
        
        Assert.assertEquals(6*b.length, buf.bytesWritten());
        
        long l = buf.revertWrite(10);
        Assert.assertEquals(0, buf.bytesRead());
        Assert.assertEquals(6*b.length - 10, l);
        
        Assert.assertEquals(0,buf.readByte());
        
        long l1  = buf.revertRead(1);
        Assert.assertEquals(0, l1);
        Assert.assertEquals(0, buf.bytesRead());
	}
	
	@Test
    public void revertWriteAndReadBoundary(){
        
        ByteBuffer buf = new DefaultByteBuffer(100,8);
       
        byte[] b = new byte[]{0,1,2,3,4,5,6,7,8,9,10};
        
        for (int i = 0; i < 6; i++){
            buf.write(b);
        }
        
        Assert.assertEquals(6*b.length, buf.bytesWritten());
        
        long l = buf.revertWrite(9);
        
        Assert.assertEquals(6*b.length - 9, l);
        Assert.assertTrue(Arrays.equals(new byte[]{0,1,2,3,4,5,6,7,8},buf.read(9)));
        long l1  = buf.revertRead(9);
        Assert.assertEquals(0, l1);
        Assert.assertEquals(0, buf.bytesRead());
        
    }
	
}
