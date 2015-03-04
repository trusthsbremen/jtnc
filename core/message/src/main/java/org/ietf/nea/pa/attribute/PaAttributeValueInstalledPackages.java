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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ietf.nea.pa.attribute.util.PackageEntry;

/**
 * IETF RFC 5792 integrity measurement installed packages attribute value.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PaAttributeValueInstalledPackages extends
    AbstractPaAttributeValue {

    private final List<PackageEntry> packages; // 0 or larger

    /**
     * Creates the attribute value with the given values.
     * @param length the value length
     * @param packages the list of installed packages
     */
    PaAttributeValueInstalledPackages(final long length,
            final List<PackageEntry> packages) {
        super(length);

        this.packages = (packages != null)
                ? packages : new ArrayList<PackageEntry>(0);
    }

    /**
     * Returns the list installed packages.
     * @return the package list
     */
    public List<PackageEntry> getPackages() {
        return Collections.unmodifiableList(this.packages);
    }

    /**
     * Returns the number of contained packages.
     * @return the package count
     */
    public int getPackageCount() {
        return this.packages.size();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PaAttributeValueInstalledPackages [packages="
                + Arrays.toString(this.packages.toArray(new PackageEntry[0]))
                + "]";
    }
}
