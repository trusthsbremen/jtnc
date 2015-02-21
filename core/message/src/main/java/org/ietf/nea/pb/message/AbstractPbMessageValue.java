package org.ietf.nea.pb.message;


public abstract class AbstractPbMessageValue implements PbMessageValue {

	private final long length;
	private final boolean omittable;  
	
	
	protected AbstractPbMessageValue(final long length) {
		this(length, true);
	}
	
	protected AbstractPbMessageValue(final long length, final boolean omittable){
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
