package de.hsbremen.tc.tnc.message.util;

public interface Combined<T> {
	
	public void add(Long vendorId, Long messageType, T component);
	
	public void remove(Long id,  Long messageType);

}
