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
package de.hsbremen.tc.tnc.message.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

/**
 * Exception that signals errors writting bytes to or reading byte from a byte
 * containing object such as stream or byte buffer.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class SerializationException extends ComprehensibleException {

    private static final long serialVersionUID = 6714101079655195252L;
    private final boolean fatal;

    /**
     * Creates an exception with a specified message and optional
     * reason varargs where a fatal flag can be set to indicate that
     * the whole read or write process has failed or that only one read
     * or write operation could not be executed and further read/write
     * operations are possible.
     *
     * @param message the specified exception message
     * @param fatal true if the whole read/write process failed
     * @param reasons the optional reasons, which let to the exception
     */
    public SerializationException(final String message,
            final boolean fatal, final Object... reasons) {
        super(message, reasons);
        this.fatal = fatal;
    }

    /**
     * Creates an exception with a specified message and optional
     * reason varargs based on an existing exception where a fatal
     * flag can be set to indicate that the whole read or write process
     * has failed or that only one read or write operation could not
     * be executed and further read/write operations are possible.
     *
     * @param message the specified exception message
     * @param exception the existing exception, which has to be encapsulated
     * @param fatal true if the whole read/write process failed
     * @param reasons the optional reasons, which let to the exception
     */
    public SerializationException(final String message,
            final Throwable exception, final boolean fatal,
            final Object... reasons) {
        super(message, exception, reasons);
        this.fatal = fatal;
    }

    /**
     * Returns whether the whole read ore write process failed.
     * @return the fatal flag
     */
    public boolean isFatal() {
        return this.fatal;
    }

    @Override
    public String toString() {
        return super.toString() + "SerializationException [fatal="
                + this.fatal + "]";
    }

}
