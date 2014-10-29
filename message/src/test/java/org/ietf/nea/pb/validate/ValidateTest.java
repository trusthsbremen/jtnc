package org.ietf.nea.pb.validate;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.serialize.TestData;
import org.junit.Before;
import org.junit.Test;

public class ValidateTest {
	
	TestData batch;
	
	@Before
	public void setUp(){
		batch = new TestData();
	}
	
	@Test(expected = RuleException.class)
	public void validateIm() throws RuleException{
		try{ 
			batch.getInvalidImMessage();
		}catch(RuleException e){
			e.printStackTrace();
			throw e;
		}
	}
}
