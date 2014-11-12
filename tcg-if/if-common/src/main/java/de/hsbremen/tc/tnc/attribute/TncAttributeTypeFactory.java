package de.hsbremen.tc.tnc.attribute;

import java.util.List;

public interface TncAttributeTypeFactory {

	public abstract TncAttributeType fromId(long id);

	public abstract TncAttributeType fromIdClientOnly(long id);

	public abstract TncAttributeType fromIdServerOnly(long id);

	public abstract List<TncAttributeType> getAllTypes();
	
	public abstract List<TncAttributeType> getClientTypes();
	
	public abstract List<TncAttributeType> getServerTypes();
}