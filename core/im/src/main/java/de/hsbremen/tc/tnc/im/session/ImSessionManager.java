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

/**
 * Holds all sessions and manages them according to their state.
 *
 *
 * @param <K> the connection type (e.g. IMC or IMV connection)
 * @param <V> the session type (e.g. IMC or IMV session)
 */
public interface ImSessionManager<K, V> {

    /**
     * Returns a session for the given connection.
     * @param connection the related connection
     * @return the session
     */
    V getSession(K connection);

    /**
     * Adds a session and it underlying connection to
     * be managed by the manager.
     * @param connection the related connection
     * @param session the session
     * @return the session
     */
    V putSession(K connection, V session);

    /**
     * Initializes the manager.
     */
    void initialize();

    /**
     * Terminates the manager.
     */
    void terminate();

}
