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
package de.hsbremen.tc.tnc.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Factory to create the standard attribute types.
 *
 * @author Carl-Heinz Genzel
 */
public final class DefaultTncAttributeTypeFactory implements
        TncAttributeTypeFactory {

    private final List<TncAttributeType> allAttributes;
    private final List<TncAttributeType> clientAttributes;
    private final List<TncAttributeType> serverAttributes;

    /**
     * Singleton to instantiate the factory only on first access.
     *
     * @author Carl-Heinz Genzel
     *
     */
    private static class Singleton {
        private static final TncAttributeTypeFactory INSTANCE =
                new DefaultTncAttributeTypeFactory();
    }

    /**
     * Returns the singleton instance of this factory.
     * @return the factory
     */
    public static TncAttributeTypeFactory getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Creates the factory and sorts the attributes into lists.
     */
    private DefaultTncAttributeTypeFactory() {
        Comparator<TncAttributeType> comparator = new IdComparator();

        List<TncAttributeType> commonAttributes = new ArrayList<>();
        commonAttributes.addAll(Arrays.asList(TncCommonAttributeTypeEnum
                .values()));

        this.allAttributes = new ArrayList<>();
        this.allAttributes.addAll(commonAttributes);

        this.clientAttributes = new ArrayList<>();
        this.clientAttributes.addAll(Arrays.asList(TncClientAttributeTypeEnum
                .values()));

        this.allAttributes.addAll(this.clientAttributes);

        this.clientAttributes.addAll(commonAttributes);
        Collections.sort(this.clientAttributes, comparator);

        this.serverAttributes = new ArrayList<>();
        this.serverAttributes.addAll(Arrays.asList(TncServerAttributeTypeEnum
                .values()));

        this.allAttributes.addAll(this.serverAttributes);

        this.serverAttributes.addAll(commonAttributes);
        Collections.sort(this.serverAttributes, comparator);

        Collections.sort(this.allAttributes, comparator);
    }

    @Override
    public TncAttributeType fromId(final long id) {

        Comparator<TncAttributeType> comparator = new IdComparator();

        int index = Collections.binarySearch(this.allAttributes,
                new TncAttributeType() {

                    @Override
                    public long id() {
                        return id;
                    }
                }, comparator);

        if (index >= 0) {
            return this.allAttributes.get(index);
        }

        return null;
    }

    @Override
    public TncAttributeType fromIdClientOnly(final long id) {

        Comparator<TncAttributeType> comparator = new IdComparator();

        int index = Collections.binarySearch(this.clientAttributes,
                new TncAttributeType() {

                    @Override
                    public long id() {
                        return id;
                    }
                }, comparator);

        if (index >= 0) {
            return this.clientAttributes.get(index);
        }

        return null;
    }

    @Override
    public TncAttributeType fromIdServerOnly(final long id) {

        Comparator<TncAttributeType> comparator = new IdComparator();

        int index = Collections.binarySearch(this.serverAttributes,
                new TncAttributeType() {

                    @Override
                    public long id() {
                        return id;
                    }
                }, comparator);

        if (index >= 0) {
            return this.serverAttributes.get(index);
        }

        return null;
    }

    @Override
    public List<TncAttributeType> getAllTypes() {
        return Collections.unmodifiableList(this.allAttributes);
    }

    @Override
    public List<TncAttributeType> getClientTypes() {
        return Collections.unmodifiableList(this.clientAttributes);
    }

    @Override
    public List<TncAttributeType> getServerTypes() {
        return Collections.unmodifiableList(this.serverAttributes);
    }

    /**
     * Comparator to sort and find attributes by type.
     * @author Carl-Heinz Genzel
     *
     */
    private class IdComparator implements Comparator<TncAttributeType> {
        @Override
        public int compare(final TncAttributeType o1,
                final TncAttributeType o2) {
            return (o1.id() < o2.id()) ? -1 : ((o1.id() == o2.id()) ? 0 : 1);
        }
    }

}
