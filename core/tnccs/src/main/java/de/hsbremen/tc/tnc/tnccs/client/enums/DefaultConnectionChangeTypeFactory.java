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
package de.hsbremen.tc.tnc.tnccs.client.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Factory to create a connection change.
 *
 */
public final class DefaultConnectionChangeTypeFactory implements
        ConnectionChangeTypeFactory {

    private final List<ConnectionChangeType> allAttributes;

    /**
     * Singleton to instantiate the factory only on first access.
     *
         *
     */
    private static class Singleton {
        private static final ConnectionChangeTypeFactory INSTANCE =
                new DefaultConnectionChangeTypeFactory();
    }

    /**
     * Returns the singleton instance of this factory.
     * @return the factory
     */
    public static ConnectionChangeTypeFactory getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Creates the factory and sorts the connection changes into lists.
     */
    private DefaultConnectionChangeTypeFactory() {
        Comparator<ConnectionChangeType> comparator = new IdComparator();

        List<ConnectionChangeType> commonAttributes = new ArrayList<>();
        commonAttributes.addAll(Arrays.asList(CommonConnectionChangeTypeEnum
                .values()));

        this.allAttributes = new ArrayList<>();
        this.allAttributes.addAll(commonAttributes);

        Collections.sort(this.allAttributes, comparator);
    }

    @Override
    public ConnectionChangeType fromId(final int id) {

        Comparator<ConnectionChangeType> comparator = new IdComparator();

        int index = Collections.binarySearch(this.allAttributes,
                new ConnectionChangeType() {

                    @Override
                    public int id() {
                        return id;
                    }
                }, comparator);

        if (index >= 0) {
            return this.allAttributes.get(index);
        }

        return null;
    }


    @Override
    public List<ConnectionChangeType> getAllTypes() {
        return Collections.unmodifiableList(this.allAttributes);
    }

    /**
     * Comparator to sort and find connection changes by type.
         *
     */
    private class IdComparator implements Comparator<ConnectionChangeType> {
        @Override
        public int compare(final ConnectionChangeType o1,
                final ConnectionChangeType o2) {
            return (o1.id() < o2.id()) ? -1 : ((o1.id() == o2.id()) ? 0 : 1);
        }
    }

}
