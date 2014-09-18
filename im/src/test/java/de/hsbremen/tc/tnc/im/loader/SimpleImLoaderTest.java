package de.hsbremen.tc.tnc.im.loader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class SimpleImLoaderTest {

	@Test
	public void regExpTest(){
		String javaNaming = "([a-zA-Z_$]{1}[a-zA-Z_$0-9]*(\\.[a-zA-Z_$]{1}[a-zA-Z_$0-9]*)*)";
		String filePath = "((?!.*//.*)(?!.*/ .*)/{1}([^\\\\(){}:\\*\\?<>\\|\\\"\\'])+\\.(jar))";
		Pattern p = Pattern.compile("\"([^\"]+)\" " + javaNaming + " " + filePath);
		
		System.out.println(p.toString());
		
		String test = "JAVA-IMC \"Ein Test\" de.hsbremen.tc.tnc.TestImc.Abx /home/sidanetdev/tnc/testimc.jar";
		System.out.println(test);
		Matcher m = p.matcher(test);
		m.find();
		System.out.println(m.group(4));
	}
}
