package de.hsbremen.tc.tnc.natives;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;

public interface CLibrary extends Library {

	CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);
	
	public static class UTSNAME extends Structure{
			/** C type : char[64 + 1] */
			public byte[] sysname = new byte[64 + 1];
			/** C type : char[64 + 1] */
			public byte[] nodename = new byte[64 + 1];
			/** C type : char[64 + 1] */
			public byte[] release = new byte[64 + 1];
			/** C type : char[64 + 1] */
			public byte[] version = new byte[64 + 1];
			/** C type : char[64 + 1] */
			public byte[] machine = new byte[64 + 1];
			/** C type : char[64 + 1] */
			public byte[] domainname = new byte[64 + 1];
			
			protected List<? > getFieldOrder() {
				return Arrays.asList("sysname", "nodename", "release", "version", "machine", "domainname");
			}
			
			public UTSNAME() {
				// TODO Auto-generated constructor stub
			}
	}
	
	int uname(UTSNAME utsname);
	
}
