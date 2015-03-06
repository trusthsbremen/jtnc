/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
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
