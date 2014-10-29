package org.ietf.nea.pa.attribute;



public class PaAttributeValueTesting extends AbstractPaAttributeValue {

    private final String content;         // variable length, UTF-8 encoded, NUL termination MUST NOT be included.

	
    PaAttributeValueTesting(long length, String content) {
		super(length);
	
		this.content = content;
	}


	/**
	 * @return the content
	 */
	public String getContent() {
		return this.content;
	}

	
	
}
