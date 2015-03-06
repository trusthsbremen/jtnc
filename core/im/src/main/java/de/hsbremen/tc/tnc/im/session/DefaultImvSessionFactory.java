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
package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.im.adapter.connection.ImConnectionAdapter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapter;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluatorManager;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 *
 * Default factory to create a session for an IMV.
 *
 *
 */
public class DefaultImvSessionFactory implements ImSessionFactory<ImvSession> {

    /**
     * Singleton to instantiate the factory only on first access.
     *
         *
     */
    private static class Singleton {
        private static final ImSessionFactory<ImvSession> INSTANCE =
                new DefaultImvSessionFactory();
    }

    /**
     * Returns the singleton instance of this factory.
     * @return the factory
     */
    public static ImSessionFactory<ImvSession> getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public <S extends ImConnectionAdapter, T extends ImEvaluatorManager>
    ImvSession createSession(final S connection, final T evaluator) {

        NotNull.check("Connection cannot be null.", connection);

        if (!(connection instanceof ImvConnectionAdapter)) {
            throw new IllegalArgumentException(
                    "Connection must be instance of "
                            + ImvConnectionAdapter.class.getCanonicalName()
                            + ".");
        }

        NotNull.check("Evaluators cannot be null.", evaluator);

        if (!(evaluator instanceof ImvEvaluatorManager)) {
            throw new IllegalArgumentException(
                    "Evaluator manager must be instance of "
                            + ImvEvaluatorManager.class.getCanonicalName()
                            + ".");
        }

        ImvSession session = new DefaultImvSession(
                (ImvConnectionAdapter) connection,
                DefaultTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN,
                (ImvEvaluatorManager) evaluator);

        return session;
    }

}
