package org.ietf.nea.pa.attribute;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PackageEntry;
import org.ietf.nea.pa.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pa.validate.rules.StringLengthLimit;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PaAttributeValueInstalledPackagesBuilderIetf implements
PaAttributeValueInstalledPackagesBuilder {
	
	private static final byte LENGTH_FIELDS_LENGTH = 2;
	
	private long length;
	private List<PackageEntry> packages;
	
	public PaAttributeValueInstalledPackagesBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.INS_PKG.length();
		this.packages = new LinkedList<>();
	}

	@Override
	public PaAttributeValueInstalledPackagesBuilder addPackages(PackageEntry pkg, PackageEntry... pkgs) throws RuleException {
		
		List<PackageEntry> temp = new ArrayList<>();
		
		if(pkg != null){
			NoNullTerminatedString.check(pkg.getPackageName());
			StringLengthLimit.check(pkg.getPackageName(), 0xFF);
			NoNullTerminatedString.check(pkg.getPackageVersion());
			StringLengthLimit.check(pkg.getPackageVersion(), 0xFF);
			temp.add(pkg);
		}
		
		if(pkgs != null){
			for (PackageEntry pkgEntry : pkgs) {
				if(pkgEntry != null){
					NoNullTerminatedString.check(pkgEntry.getPackageName());
					StringLengthLimit.check(pkgEntry.getPackageName(), 0xFF);
					NoNullTerminatedString.check(pkgEntry.getPackageVersion());
					StringLengthLimit.check(pkgEntry.getPackageVersion(), 0xFF);
					temp.add(pkgEntry);
				}
			}
		}

		this.packages.addAll(temp);
		this.updateLength();
		
		return this;
	}

	private void updateLength() {
		this.length = PaAttributeTlvFixedLengthEnum.INS_PKG.length();
		for (PackageEntry pkg : this.packages) {
			this.length += (pkg.getPackageNameLength() + pkg.getPackageVersionLength() + LENGTH_FIELDS_LENGTH) ; // 2 bytes for length values
		}
	}

	@Override
	public PaAttributeValueInstalledPackages toObject(){
		
		return new PaAttributeValueInstalledPackages(this.length, this.packages);
	}

	@Override
	public PaAttributeValueInstalledPackagesBuilder newInstance() {
		return new PaAttributeValueInstalledPackagesBuilderIetf();
	}

}
