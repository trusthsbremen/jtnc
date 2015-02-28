package org.ietf.nea.pa.attribute;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueStringVersionBuilder extends ImAttributeValueBuilder{

	void setProductVersion(String productVersion) throws RuleException;

	void setBuildNumber(String buildNumber) throws RuleException;

	void setConfigurationVersion(String configVersion) throws RuleException;

}
