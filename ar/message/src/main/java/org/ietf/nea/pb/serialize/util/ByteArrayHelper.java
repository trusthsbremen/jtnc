package org.ietf.nea.pb.serialize.util;

public class ByteArrayHelper {

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
	
	public static byte[] mergeArrays(byte[] first,byte[] second){
		byte[] merged = new byte[first.length + second.length];
		System.arraycopy(first, 0, merged, 0, first.length);
		System.arraycopy(second, 0, merged, first.length, second.length);
		return merged;
	}
}
