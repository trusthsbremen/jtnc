package de.hsbremen.tc.tnc.tnccs;

import org.junit.Assert;
import org.junit.Test;

public class Test1 {

	private long rxCounter = 0;
	private long txCounter = 0;

	@Test
	public void test(){
		
		byte[][] b = {{5,3,3},{1,3,1},{4,2,2},{5,8,5},{9,6,6}};
		
		for (byte[] cs : b) {
			
			rxCounter = cs[0];
			txCounter = cs[1];
			
			System.out.println("Rx: " + rxCounter);
			System.out.println("Tx: " + txCounter);
			
			long result = 0;
			
			result = Math.min(rxCounter, txCounter);
			Assert.assertEquals(cs[2], result);
			System.out.println("Result:" + result);
		}
		
	}
}

