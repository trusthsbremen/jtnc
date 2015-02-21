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
package de.hsbremen.tc.tnc.tnccs.adapter.connection;

/*
 * FIXME This is a tradeoff, because I could not figure out another
 * way to fix the circular dependency between session and IMC/VConnection.
 */

/**
 * Generic IM(C/V) connection adapter base containing common connection
 * attributes and functions to control a connection.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface ImConnectionAdapter {

    /**
     * Allows a connection to receive messages.
     */
    void allowMessageReceipt();

    /**
     * Denies a connection to receive messages.
     */
    void denyMessageReceipt();

    /**
     * Returns the receiving state of the connection.
     * @return true if message receipt is allowed.
     */
    boolean isReceiving();

    /**
     * Returns the ID from the IM(C/V) the connection
     * is associated with.
     * @return the IM(C/V) ID
     */
    long getImId();
}
