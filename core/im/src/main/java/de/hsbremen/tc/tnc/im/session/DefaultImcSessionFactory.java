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
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 *
 * Default factory to create a session for an IMC.
 *
 *
 */
public class DefaultImcSessionFactory implements ImSessionFactory<ImcSession> {

    /**
     * Singleton to instantiate the factory only on first access.
     *
         *
     */
    private static class Singleton {
        private static final ImSessionFactory<ImcSession> INSTANCE =
                new DefaultImcSessionFactory();
    }

    /**
     * Returns the singleton instance of this factory.
     *
     * @return the factory
     */
    public static ImSessionFactory<ImcSession> getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public <S extends ImConnectionAdapter, E extends ImEvaluatorManager>
    ImcSession createSession(final S connection, final E evaluator) {
        NotNull.check("Connection cannot be null.", connection);

        if (!(connection instanceof ImcConnectionAdapter)) {
            throw new IllegalArgumentException(
                    "Connection must be instance of "
                            + ImcConnectionAdapter.class.getCanonicalName()
                            + ".");
        }

        NotNull.check("Evaluators cannot be null.", evaluator);

        if (!(evaluator instanceof ImEvaluatorManager)) {
            throw new IllegalArgumentException(
                    "Evaluator manager must be instance of "
                            + ImEvaluatorManager.class.getCanonicalName()
                            + ".");
        }

        ImcSession session = new DefaultImcSession(
                (ImcConnectionAdapter) connection,
                DefaultTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN,
                evaluator);

        return session;
    }

}
