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
