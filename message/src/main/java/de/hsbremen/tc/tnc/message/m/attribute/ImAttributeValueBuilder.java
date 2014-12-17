package de.hsbremen.tc.tnc.message.m.attribute;

import de.hsbremen.tc.tnc.message.util.TransmissionObjectBuilder;


public interface ImAttributeValueBuilder extends TransmissionObjectBuilder<ImAttributeValue>{

	public abstract ImAttributeValue toObject();

	public abstract ImAttributeValueBuilder newInstance();
}
