package org.ietf.nea.pa.attribute;


public abstract class AbstractPaAttributeValue implements PaAttributeValue{

	private final long length;
	private final boolean omittable;  

	protected AbstractPaAttributeValue(final long length) {
		this(length, true);
	}
	
	protected AbstractPaAttributeValue(final long length, final boolean omittable){
		this.length = length;
		this.omittable = omittable;
	}
	
	@Override
	public long getLength(){
		return this.length;
	}

	@Override
	public boolean isOmittable() {
		return omittable;
	}
	
}
