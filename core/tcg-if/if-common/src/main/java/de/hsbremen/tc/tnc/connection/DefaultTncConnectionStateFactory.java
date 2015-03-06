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
package de.hsbremen.tc.tnc.connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Factory to create the standard connection states.
 *
 */
public final class DefaultTncConnectionStateFactory implements
        TncConnectionStateFactory {

    private final List<TncConnectionState> allAttributes;

    /**
     * Singleton to instantiate the factory only on first access.
     *
         *
     */
    private static class Singleton {
        private static final TncConnectionStateFactory INSTANCE =
                new DefaultTncConnectionStateFactory();
    }

    /**
     * Returns the singleton instance of this factory.
     * @return the factory
     */
    public static TncConnectionStateFactory getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Creates the factory an sorts the connection states into a list.
     */
    private DefaultTncConnectionStateFactory() {
        Comparator<TncConnectionState> comparator = new StateComparator();

        this.allAttributes = new ArrayList<>();
        allAttributes.addAll(Arrays.asList(DefaultTncConnectionStateEnum
                .values()));

        Collections.sort(this.allAttributes, comparator);
    }

    @Override
    public TncConnectionState fromId(final long state) {

        Comparator<TncConnectionState> comparator = new StateComparator();

        int index = Collections.binarySearch(this.allAttributes,
                new TncConnectionState() {

                    @Override
                    public long id() {
                        // TODO Auto-generated method stub
                        return state;
                    }
                }, comparator);

        if (index >= 0) {
            return this.allAttributes.get(index);
        }

        return null;
    }

    @Override
    public List<TncConnectionState> getAllStates() {
        return Collections.unmodifiableList(this.allAttributes);
    }

    /**
     * Comparator to sort and find connections by state value.
         *
     */
    private class StateComparator implements Comparator<TncConnectionState> {

        @Override
        public int compare(final TncConnectionState o1,
                final TncConnectionState o2) {
            return (o1.id() < o2.id()) ? -1
                    : ((o1.id() == o2.id()) ? 0 : 1);
        }
    }

}
