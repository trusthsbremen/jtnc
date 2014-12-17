package org.ietf.nea.pa.attribute;

import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;

public class PaAttributeValueTestingBuilderIetf implements PaAttributeValueTestingBuilder{

	private long length;
	private String content;
	
	public PaAttributeValueTestingBuilderIetf(){
		this.length = 0;
		this.content = "";

	}
	
	@Override
	public void setContent(String content) throws RuleException {

		if(content != null){
			this.content = content;
			this.updateLength();
		}
		
	}
	
	@Override
	public PaAttributeValueTesting toObject() {
		return new PaAttributeValueTesting(this.length, this.content);
	}

	@Override
	public PaAttributeValueTestingBuilder newInstance() {
		return new PaAttributeValueTestingBuilderIetf();
	}
	
	private void updateLength(){
		this.length = 0;
		if(content.length() > 0){
			this.length += content.getBytes(Charset.forName("UTF-8")).length;
		}
	}

	

}
