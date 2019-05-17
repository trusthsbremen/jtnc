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
     * Notifies the evaluation component about a connection change and
     * enables the component to react on a connection change.
     * @param context the context, which holds connection specific values
     */
    void notifyConnectionChange(ImSessionContext context);

    /**
     * Terminates this integrity measurement component part.
     */
    void terminate();
}
