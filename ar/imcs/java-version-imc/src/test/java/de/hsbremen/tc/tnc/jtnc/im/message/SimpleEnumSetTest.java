package de.hsbremen.tc.tnc.jtnc.im.message;
//
//import java.util.EnumSet;
//import java.util.Iterator;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import de.hsbremen.tc.tnc.jtnc.im.message.enums.ImFlags;
//
//public class SimpleEnumSetTest {
//	
//	@Test
//	public void testNoSkipFlag(){
//		EnumSet<ImFlags> f = EnumSet.of(ImFlags.NOSKIP);
//		byte b = 0; 
//		for(Iterator<ImFlags> it = f.iterator(); it.hasNext();){
//			b = (byte)(b | it.next().bit());
//		}
//		Assert.assertEquals(ImFlags.NOSKIP.bit(),b);
//	}
//	
//	@Test
//	public void testNoFlag(){
//		EnumSet<ImFlags> f = EnumSet.noneOf(ImFlags.class);
//		
//		byte b = 0; 
//		for(Iterator<ImFlags> it = f.iterator(); it.hasNext();){
//			b = (byte)(b | it.next().bit());
//		}
//		Assert.assertEquals(0, b);
//	}
//	
//	@Test
//	public void testTwoFlags(){
//		EnumSet<ImFlags> f = EnumSet.of(ImFlags.NOSKIP, ImFlags.RESERED1);
//		
//		byte b = 0; 
//		for(Iterator<ImFlags> it = f.iterator(); it.hasNext();){
//			b = (byte)(b | it.next().bit());
//		}
//		Assert.assertEquals(129,(int)b & 0xff);
//	}
//	
//}
