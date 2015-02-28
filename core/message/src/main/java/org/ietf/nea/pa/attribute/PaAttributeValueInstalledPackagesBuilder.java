package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.util.PackageEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueInstalledPackagesBuilder extends ImAttributeValueBuilder{

	public PaAttributeValueInstalledPackagesBuilder addPackages(PackageEntry pkg, PackageEntry... pkgs) throws RuleException;
	
	
}
