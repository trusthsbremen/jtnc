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
package de.hsbremen.tc.tnc.transport;

/**
 * Generic TransportConnectionBuilder to create a TransportConnection with an
 * underlying transport object (e.g. Socket).
 *
 * @author Carl-Heinz Genzel
 *
 * @param <T> the type of the underlying transport object
 */
public interface TransportConnectionBuilder<T extends Object> {

    /**
     * Creates a TransportConnection with the given parameters and a defined ID.
     *
     * @param id the definied ID.
     * @param selfInitiated the parameter, which identifies, if the connection
     * was initiated by this side
     * @param server the parameter, which identifies, if the connection is a
     * server connection
     * @param underlying the underlying transport object (e.g. Socket)
     * @return the TransportConnection
     */
    TransportConnection toConnection(String id, boolean selfInitiated,
            boolean server, T underlying);

    /**
     * Creates a TransportConnection with the given parameters and a generated
     * ID.
     *
     * @param selfInitiated the parameter, which identifies, if the connection
     * was initiated by this side
     * @param server the parameter, which identifies, if the connection is a
     * server connection
     * @param underlying the underlying transport object (e.g. Socket)
     * @return the TransportConnection
     */
    TransportConnection toConnection(boolean selfInitiated, boolean server,
            T underlying);
}
