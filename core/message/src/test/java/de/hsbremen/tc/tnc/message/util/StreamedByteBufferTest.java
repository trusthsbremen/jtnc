package de.hsbremen.tc.tnc.message.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;

public class StreamedByteBufferTest {

    
    @Test
    public void testWriteAndRead(){
       
        ByteArrayOutputStream out = new ByteArrayOutputStream(100);
                
        ByteBuffer buf = new StreamedWriteOnlyByteBuffer(out);
        
        for(int i = 0; i <=1; i++){
            buf.writeByte((byte)i);
        }
        
        Assert.assertEquals(2, buf.bytesWritten());
        Assert.assertEquals(-1, buf.bytesRead());
        
        buf.clear();
        
        Assert.assertEquals(0, buf.bytesWritten());
        Assert.assertEquals(-1, buf.bytesRead());
        
        int val = 10;
        
        out = new ByteArrayOutputStream(100);
        buf = new StreamedWriteOnlyByteBuffer(out);
        
        for(int i = 0; i < val; i++){
            buf.writeByte((byte)(i%128));
        }
        
        Assert.assertEquals(val, buf.bytesWritten());
        Assert.assertEquals(-1, buf.bytesRead());
        ByteBuffer buf2 = new StreamedReadOnlyByteBuffer(new ByteArrayInputStream(out.toByteArray()));
        for(int i = 0; i < 6; i++){
            Assert.assertEquals(i, buf2.readByte());
        }
        
        Assert.assertEquals(val, buf.bytesWritten());
        Assert.assertEquals(6, buf2.bytesRead());

        buf.clear();
        buf2.clear();

        Assert.assertEquals(0, buf.bytesWritten());
        Assert.assertEquals(0, buf2.bytesRead());
    }
    
    @Test
    public void conversion(){
        ByteArrayOutputStream out = new ByteArrayOutputStream(100);
        
        ByteBuffer buf = new StreamedWriteOnlyByteBuffer(out);
        
        short i = 128;
        Assert.assertEquals(-128,(byte)i);

        buf.writeDigits(123456789, (byte)4);
        
        
        ByteBuffer buf2 = new StreamedReadOnlyByteBuffer(new ByteArrayInputStream(out.toByteArray()));
        long l = buf2.readLong((byte) 4);
        Assert.assertEquals(123456789,l);
        
        
        buf.writeByte((byte)1);
        buf2 = new StreamedReadOnlyByteBuffer(new ByteArrayInputStream(out.toByteArray()));
        l = buf2.readLong((byte) 4);
        l = buf2.readLong((byte) 1);
        Assert.assertEquals(1,l);

    }
    
    @Test
    public void testByteArrays(){
        
        ByteArrayOutputStream out = new ByteArrayOutputStream(100);
        
        ByteBuffer buf = new StreamedWriteOnlyByteBuffer(out);
        
        byte[] b = new byte[]{0,1,2,3,4,5,6,7,8,9,10};
        
        for (int i = 0; i < 6; i++){
            buf.write(b);
        }
        
        Assert.assertEquals(6*b.length, buf.bytesWritten());
        Assert.assertEquals(-1, buf.bytesRead());
        
        
        out = new ByteArrayOutputStream(100);
        buf = new StreamedWriteOnlyByteBuffer(out);
        
        byte[] b1 = new byte[]{0,1,2,3,4};
        
        for (int i = 0; i < 2; i++){
            buf.write(b1);
        }
        
        Assert.assertEquals(2*b1.length, buf.bytesWritten());
        Assert.assertEquals(-1, buf.bytesRead());
        
        ByteBuffer buf2 = new StreamedReadOnlyByteBuffer(new ByteArrayInputStream(out.toByteArray()));
        for(int i = 0; i < 2; i++){
            Assert.assertArrayEquals(b1, buf2.read(b1.length));
        }
        
        Assert.assertEquals((2*b1.length), buf.bytesWritten());
        Assert.assertEquals((2*b1.length), buf2.bytesRead());
        
        for(int i = 0; i <=1; i++){
            buf.writeByte((byte)i);
        }
        
        Assert.assertEquals((2*b1.length)+2, buf.bytesWritten());
        Assert.assertEquals((2*b1.length), buf2.bytesRead());
        
        buf.clear();
        buf2.clear();
        
        Assert.assertEquals(0, buf.bytesWritten());
        Assert.assertEquals(0, buf2.bytesRead());
        
    }
    
    @Test
    public void testBytePerByte(){
        
        ByteArrayOutputStream out = new ByteArrayOutputStream(100);
        
        ByteBuffer buf = new StreamedWriteOnlyByteBuffer(out);
        
        for(int i = 0; i <=1; i++){
            buf.writeByte((byte)i);
        }
        
        Assert.assertEquals(2, buf.bytesWritten());
        Assert.assertEquals(-1, buf.bytesRead());
        
        buf.clear();
        
        Assert.assertEquals(0, buf.bytesWritten());
        Assert.assertEquals(-1, buf.bytesRead());
        
        int val = 10;
        
        out = new ByteArrayOutputStream(100);
        buf = new StreamedWriteOnlyByteBuffer(out);
        
        for(int i = 0; i < val; i++){
            buf.writeByte((byte)(i%128));
        }
        
        Assert.assertEquals(val, buf.bytesWritten());
        Assert.assertEquals(-1, buf.bytesRead());
    
        ByteBuffer buf2 = new StreamedReadOnlyByteBuffer(new ByteArrayInputStream(out.toByteArray()));
        for(int i = 0; i < 6; i++){
            Assert.assertEquals(i, buf2.readByte());
        }
        
        Assert.assertEquals(val, buf.bytesWritten());
        Assert.assertEquals(6, buf2.bytesRead());
        
        buf.clear();
        buf2.clear();

        Assert.assertEquals(0, buf.bytesWritten());
        Assert.assertEquals(0, buf2.bytesRead());
        
    }
    
    @Test 
    public void testReadBufferToBuffer(){
        byte[] array = {1,23,2,43,56,6,67,6,67,67,89,3,3,23,2};
        ByteBuffer b = new StreamedReadOnlyByteBuffer(new ByteArrayInputStream(array));
        
        ByteBuffer b1 = new DefaultByteBuffer(array.length);
        
        b1.write(b.read(array.length));
        Assert.assertEquals(b.bytesRead(), b1.bytesWritten());
        
        
    }
    
    @Test 
    public void testWriterBufferToBuffer(){
        byte[] array = {1,23,2,43,56,6,67,6,67,67,89,3,3,23,2};
        ByteBuffer b = new DefaultByteBuffer(100);
        b.write(array);
        
        ByteBuffer b1 = new StreamedWriteOnlyByteBuffer(new ByteArrayOutputStream((int)b.bytesWritten()));
        
        b1.write(b);
        Assert.assertEquals(b.bytesRead(), b1.bytesWritten());
    }
}
