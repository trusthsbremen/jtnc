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
package de.hsbremen.tc.tnc.im.evaluate;

import java.util.List;

import de.hsbremen.tc.tnc.im.session.ImSessionContext;

/**
 * Generic base for the composition of integrity measurement components.
 *
 *
 * @param <T> a type, related to a component message, which is evaluated or
 * returned by the specific derived class
 */
public interface ImEvaluationComponent<T> {

    /**
     * Conducts the integrity evaluation for its component type. For the
     * evaluation, it can take connection specific values from the context in to
     * account.
     *
     * @param context the context, which holds connection specific values
     * @return a list of results of the specific type
     */
    List<T> evaluate(ImSessionContext context);

    /**
     * Evaluates a list of types, related to a component message. For the
     * evaluation, it can take connection specific values from the context in to
     * account.
     *
     * @param components a list of types, related to a component message
     * @param context the context, which holds connection specific values
     * @return a list of results of the specific type
     */
    List<T> handle(List<? extends T> components, ImSessionContext context);

    /**
     * Asks for more results, before all messages will be send to the lower
     * layers. For this, it can take connection specific values from the
     * context in to account.
     *
     * @param context the context, which holds connection specific values
     * @return a list of results of the specific type
     */
    List<T> lastCall(ImSessionContext context);

    /**
     * Terminates this integrity measurement component part.
     */
    void terminate();
}
