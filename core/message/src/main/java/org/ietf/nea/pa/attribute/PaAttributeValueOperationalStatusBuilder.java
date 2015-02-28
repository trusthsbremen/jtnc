package org.ietf.nea.pa.attribute;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueOperationalStatusBuilder extends ImAttributeValueBuilder{
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(short status);
	
	/**
	 * @param result the result to set
	 */
	public abstract void setResult(short result);
	
	/**
	 * @param lastUse the lastUse to set
	 * @throws RuleException 
	 */
	public abstract void setLastUse(String dateTime) throws RuleException;
}
