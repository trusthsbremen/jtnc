/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package org.ietf.nea.pa.attribute;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PackageEntry;
import org.ietf.nea.pa.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pa.validate.rules.StringLengthLimit;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement installed packages attribute value
 * compliant to RFC 5792. It evaluates the given values and can be used in a
 * fluent way.
 *
 *
 */
public class PaAttributeValueInstalledPackagesBuilderIetf implements
        PaAttributeValueInstalledPackagesBuilder {

    private static final byte LENGTH_FIELDS_LENGTH = 2;
    private static final int MAX_STRING_LENGTH = 0xFF;
    private long length;
    private List<PackageEntry> packages;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Packages: Empty list</li>
     * </ul>
     */
    public PaAttributeValueInstalledPackagesBuilderIetf() {
        this.length = PaAttributeTlvFixedLengthEnum.INS_PKG.length();
        this.packages = new LinkedList<>();
    }

    @Override
    public PaAttributeValueInstalledPackagesBuilder addPackages(
            final PackageEntry... pkgs) throws RuleException {

        List<PackageEntry> temp = new ArrayList<>();

        if (pkgs != null) {
            for (PackageEntry pkgEntry : pkgs) {
                if (pkgEntry != null) {
                    NoNullTerminatedString.check(pkgEntry.getPackageName());
                    StringLengthLimit.check(pkgEntry.getPackageName(),
                            MAX_STRING_LENGTH);
                    NoNullTerminatedString.check(pkgEntry.getPackageVersion());
                    StringLengthLimit.check(pkgEntry.getPackageVersion(),
                            MAX_STRING_LENGTH);
                    temp.add(pkgEntry);
                }
            }
        }

        this.packages.addAll(temp);
        this.updateLength();

        return this;
    }

    /**
     * Updates the length according to the list of packages.
     */
    private void updateLength() {
        this.length = PaAttributeTlvFixedLengthEnum.INS_PKG.length();
        for (PackageEntry pkg : this.packages) {
            this.length += (pkg.getPackageNameLength()
                    + pkg.getPackageVersionLength() + LENGTH_FIELDS_LENGTH);
        }
    }

    @Override
    public PaAttributeValueInstalledPackages toObject() {

        return new PaAttributeValueInstalledPackages(this.length,
                this.packages);
    }

    @Override
    public PaAttributeValueInstalledPackagesBuilder newInstance() {
        return new PaAttributeValueInstalledPackagesBuilderIetf();
    }

}
