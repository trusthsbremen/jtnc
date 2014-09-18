package org.ietf.nea.pb.validate;

import org.ietf.nea.pb.serialize.TestData;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class ValidateTest {
	
	TestData batch;
	
	@Before
	public void setUp(){
		batch = new TestData();
	}
	
	@Test(expected = ValidationException.class)
	public void validateIm() throws ValidationException{
		try{ 
			batch.getInvalidImMessage();
		}catch(ValidationException e){
			e.printStackTrace();
			throw e;
		}
	}
}
