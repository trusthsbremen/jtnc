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
package org.ietf.nea.pt.socket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hsbremen.tc.tnc.transport.TransportConnectionPhase;
import de.hsbremen.tc.tnc.transport.TransportConnectionPhaseFactory;

/**
 * Factory to create the standard connection states.
 *
 */
public final class SocketTransportConnectionPhaseFactory implements
        TransportConnectionPhaseFactory {

    private final List<TransportConnectionPhase> allPhases;

    /**
     * Singleton to instantiate the factory only on first access.
     *
         *
     */
    private static class Singleton {
        private static final TransportConnectionPhaseFactory INSTANCE =
                new SocketTransportConnectionPhaseFactory();
    }

    /**
     * Returns the singleton instance of this factory.
     * @return the factory
     */
    public static TransportConnectionPhaseFactory getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Creates the factory and sorts the connection states into a list.
     */
    private SocketTransportConnectionPhaseFactory() {
        Comparator<TransportConnectionPhase> comparator = new PhaseComparator();

        this.allPhases = new ArrayList<>();
        allPhases.addAll(Arrays.asList(SocketTransportConnectionPhaseEnum
                .values()));

        Collections.sort(this.allPhases, comparator);
    }

    @Override
    public TransportConnectionPhase fromId(final long state) {

        Comparator<TransportConnectionPhase> comparator = new PhaseComparator();

        int index = Collections.binarySearch(this.allPhases,
                new TransportConnectionPhase() {

                    @Override
                    public long id() {
                        // TODO Auto-generated method stub
                        return state;
                    }
                }, comparator);

        if (index >= 0) {
            return this.allPhases.get(index);
        }

        return null;
    }

    @Override
    public List<TransportConnectionPhase> getAllPhases() {
        return Collections.unmodifiableList(this.allPhases);
    }

    /**
     * Comparator to sort and find connections by state value.
         *
     */
    private class PhaseComparator implements Comparator<TransportConnectionPhase> {

        @Override
        public int compare(final TransportConnectionPhase o1,
                final TransportConnectionPhase o2) {
            return (o1.id() < o2.id()) ? -1
                    : ((o1.id() == o2.id()) ? 0 : 1);
        }
    }

}
