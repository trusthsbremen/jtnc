/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
