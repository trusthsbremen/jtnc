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
package de.hsbremen.tc.tnc.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Factory to create the standard attribute types.
 *
 */
public final class DefaultTncAttributeTypeFactory implements
        TncAttributeTypeFactory {

    private final List<TncAttributeType> allAttributes;
    private final List<TncAttributeType> clientAttributes;
    private final List<TncAttributeType> serverAttributes;

    /**
     * Singleton to instantiate the factory only on first access.
     *
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
