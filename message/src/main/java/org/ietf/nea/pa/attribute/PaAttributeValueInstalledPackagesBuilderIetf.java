package org.ietf.nea.pa.attribute;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.attribute.util.PackageEntry;
import org.ietf.nea.pa.validate.rules.NoNullTerminatedString;

public class PaAttributeValueInstalledPackagesBuilderIetf implements
PaAttributeValueInstalledPackagesBuilder {
	
	private static final byte LENGTH_FIELDS_LENGTH = 2;
	
	private long length;
	private List<PackageEntry> packages;
	
	public PaAttributeValueInstalledPackagesBuilderIetf(){
		this.length = PaAttributeTlvFixedLength.INS_PKG.length();
		this.packages = new LinkedList<>();
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PaAttributeValueAssessmentResultBuilder#setResult(long)
	 */
	@Override
	public PaAttributeValueInstalledPackagesBuilder addPackages(PackageEntry pkg, PackageEntry... pkgs) throws RuleException {
		
		List<PackageEntry> temp = new ArrayList<>();
		
		if(pkg != null){
			NoNullTerminatedString.check(pkg.getPackageName());
			NoNullTerminatedString.check(pkg.getPackageVersion());
			temp.add(pkg);
		}
		
		if(pkgs != null){
			for (PackageEntry pkgEntry : pkgs) {
				if(pkgEntry != null){
					NoNullTerminatedString.check(pkgEntry.getPackageName());
					NoNullTerminatedString.check(pkgEntry.getPackageVersion());
					temp.add(pkgEntry);
				}
			}
		}

		this.packages.addAll(temp);
		this.updateLength();
		
		return this;
	}

	private void updateLength() {
		this.length = PaAttributeTlvFixedLength.INS_PKG.length();
		for (PackageEntry pkg : this.packages) {
			this.length += (pkg.getPackageNameLength() + pkg.getPackageVersionLength() + LENGTH_FIELDS_LENGTH) ; // 2 bytes for length values
		}
	}

	@Override
	public PaAttributeValueInstalledPackages toValue(){
		
		return new PaAttributeValueInstalledPackages(this.length, this.packages);
	}

	@Override
	public PaAttributeValueInstalledPackagesBuilder clear() {
		// TODO Auto-generated method stub
		return new PaAttributeValueInstalledPackagesBuilderIetf();
	}

}
