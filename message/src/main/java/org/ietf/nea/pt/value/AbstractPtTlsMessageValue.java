package org.ietf.nea.pt.value;


public class AbstractPtTlsMessageValue implements PtTlsMessageValue{

	private final long length; 
	
	protected AbstractPtTlsMessageValue(final long length){
		this.length = length;
	}
	
	@Override
	public long getLength(){
		return this.length;
	}

}
