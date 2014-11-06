package de.hsbremen.tc.tnc.natives;

import org.junit.Test;

import de.hsbremen.tc.tnc.natives.CLibrary.UTSNAME;

public class NativesFirstTest {

	CLibrary uts = CLibrary.INSTANCE;
	
	@Test
	public void test(){
		UTSNAME u = new UTSNAME();
		int i = uts.uname(u);
		
		System.out.println("Returncode: " + i);
		
		System.out.println(new String(u.nodename));
		System.out.println(new String(u.sysname));
		System.out.println(new String(u.version));
		System.out.println(new String(u.release));
		System.out.println(new String(u.machine));
		System.out.println(new String(u.domainname));
		
		System.out.println("----------------------");
		System.out.println(System.getProperty("os.arch"));
		System.out.println(System.getProperty("os.name"));
		System.out.println(System.getProperty("os.version"));

	}
}
