package org.ietf.nea.pa.attribute;

import java.util.Collections;
import java.util.List;

import org.ietf.nea.pa.attribute.util.PackageEntry;

public class PaAttributeValueInstalledPackages extends AbstractPaAttributeValue {
	
	private final List<PackageEntry> packages; // 0 or bigger
	
	PaAttributeValueInstalledPackages(long length, List<PackageEntry> packages) {
		super(length);
		
		this.packages = packages;
	}

	/**
	 * @return the references
	 */
	public List<PackageEntry> getPackages() {
		return Collections.unmodifiableList(this.packages);
	}
	
	public int getPackageCount(){
		return this.packages.size();
	}

	
}
