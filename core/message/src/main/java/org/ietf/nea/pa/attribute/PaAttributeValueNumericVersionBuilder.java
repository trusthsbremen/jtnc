package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueNumericVersionBuilder extends ImAttributeValueBuilder{

	void setMajorVersion(long majorVersion) throws RuleException;

	void setMinorVersion(long minorVersion) throws RuleException;

	void setBuildVersion(long buildVersion) throws RuleException;

	void setServicePackMajor(int servicePackMajor) throws RuleException;

	void setServicePackMinor(int servicePackMinor) throws RuleException;

}
