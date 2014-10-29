package de.hsbremen.tc.tnc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public final class ByteArrayHelper {

	public static long toLong(byte[] b){
		if(b == null || b.length > 8 ){
			throw new IllegalArgumentException("Supplied array is to large.");
		}
		long value = 0L;
		for(int i = 0; i < b.length; i++){
			value = (value << 8) + (b[i] & 0xFF); 
		}
		
		return value;
	}
	
	public static short toShort(byte[] b){
		if(b == null || b.length > 2 ){
			throw new IllegalArgumentException("Supplied array is to large.");
		}
		short value = 0;
		for(int i = 0; i < b.length; i++){
			value = (short) ((value << 8) + (b[i] & 0xFF)); 
		}
		
		return value;
	}
	
	public static int toInt(byte[] b){
		if(b == null || b.length > 4 ){
			throw new IllegalArgumentException("Supplied array is to large.");
		}
		int value = 0;
		for(int i = 0; i < b.length; i++){
			value = (int) ((value << 8) + (b[i] & 0xFF)); 
		}
		
		return value;
	}
	
	public static byte[] mergeArrays(byte[] first,byte[] second){
		byte[] merged = new byte[first.length + second.length];
		System.arraycopy(first, 0, merged, 0, first.length);
		System.arraycopy(second, 0, merged, first.length, second.length);
		return merged;
	}
	
	public static byte[] arrayFromStream(InputStream in, int length) throws IOException{
		byte[] buffer = new byte[0];
		
		if(length > 0){
			buffer = new byte[length];
		}
	
		int count = 0;
		
		count = in.read(buffer); // blocks until data is available or stream is closed
		
		if(count >= length){
			// shorten the array to the actual data
			return Arrays.copyOf(buffer, count);
		}else{
			throw new IOException("Stream must be closed, not enough data to read.");
		}

	}
}
