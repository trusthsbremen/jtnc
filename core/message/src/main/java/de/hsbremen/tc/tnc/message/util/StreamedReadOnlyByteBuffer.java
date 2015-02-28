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
package de.hsbremen.tc.tnc.message.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.util.Arrays;

/**
 * Read only byte buffer backed by an input stream. It can theoretically
 * allocate an infinite number of bytes. If the stream contains more bytes
 * than MAX_LONG, the contained pointers are set to -1 after reaching
 * MAX_LONG. The buffer cannot be reverted, because of the stream nature.
 * If the stream throws an error the error is signaled by a buffer underflow.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class StreamedReadOnlyByteBuffer implements ByteBuffer {

    private static final int DEFAULT_STREAM_CHUNK = 8192;

    private long readPointer;
    private InputStream stream;

    /**
     * Wraps the given stream into this buffer.
     *
     * @param in the input stream to use
     */
    public StreamedReadOnlyByteBuffer(final InputStream in) {
        this.stream = in;
        this.readPointer = 0;

    }

    @Override
    public void write(final ByteBuffer buffer) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is read only.");

    }

    @Override
    public void write(final byte[] array) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is read only.");

    }

    @Override
    public void writeByte(final byte b) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is read only.");

    }

    @Override
    public void writeUnsignedByte(final short unsigned) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is read only.");

    }

    @Override
    public void writeShort(final short signed) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is read only.");

    }

    @Override
    public void writeUnsignedShort(final int unsigned) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is read only.");

    }

    @Override
    public void writeInt(final int signed) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is read only.");

    }

    @Override
    public void writeUnsignedInt(final long unsigned) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is read only.");

    }

    @Override
    public void writeLong(final long signed) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is read only.");

    }

    @Override
    public void writeDigits(final long number, final byte length) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is read only.");

    }

    @Override
    public ByteBuffer read(final long length) {
        if (this.stream == null) {
            throw new BufferUnderflowException();
        }
        ByteBuffer b = new DefaultByteBuffer(length);
        int count = 0;
        while ((length - b.bytesWritten()) > 0 && count > -1) {
            try {
                byte[] buf = null;
                if (length > DEFAULT_STREAM_CHUNK) {
                    buf = new byte[DEFAULT_STREAM_CHUNK];
                    count = this.stream.read(buf);
                } else {
                    buf = new byte[(int) length];
                    count = this.stream.read(buf);
                }
                if (count > -1) {
                    b.write(Arrays.copyOf(buf, count));
                }

            } catch (IOException e) {
                throw new BufferUnderflowException();
            }
        }

        this.incrementReadPointer(b.bytesWritten());

        return b;
    }

    @Override
    public byte[] read(final int length) {
        if (this.stream == null) {
            throw new BufferUnderflowException();
        }
        if (length <= 0) {
            return new byte[0];
        }

        byte[] ret = new byte[0];
        int count = 0;
        try {

            byte[] buf = new byte[length];

            count = this.stream.read(buf);

            if (count > -1) {
                ret = Arrays.copyOf(buf, count);
            }

        } catch (IOException e) {
            throw new BufferUnderflowException();
        }

        this.incrementReadPointer(ret.length);

        return ret;
    }

    @Override
    public byte readByte() {
        if (this.stream == null) {
            throw new BufferUnderflowException();
        }
        byte b = 0;
        try {
            b = (byte) this.stream.read();
        } catch (IOException e) {
            throw new BufferUnderflowException();
        }
        this.incrementReadPointer(1);

        return b;

    }

    @Override
    public short readShort() {
        return this.readShort((byte) 2);
    }

    @Override
    public short readShort(final byte length) {
        if (length == 0 || length > 2) {
            throw new IllegalArgumentException("Supplied length is to large.");
        }

        byte[] b = this.read(length);

        short value = 0;
        for (int i = 0; i < b.length; i++) {
            value = (short) ((value << 8) + (b[i] & 0xFF));
        }

        return value;
    }

    @Override
    public int readInt() {
        return this.readInt((byte) 4);
    }

    @Override
    public int readInt(final byte length) {
        if (length == 0 || length > 4) {
            throw new IllegalArgumentException("Supplied length is to large.");
        }

        byte[] b = this.read(length);
        int value = 0;
        for (int i = 0; i < b.length; i++) {
            value = (int) ((value << 8) + (b[i] & 0xFF));
        }

        return value;
    }

    @Override
    public long readLong() {
        return this.readLong((byte) 8);
    }

    @Override
    public long readLong(final byte length) {
        if (length == 0 || length > 8) {
            throw new IllegalArgumentException("Supplied length is to large.");
        }

        byte[] b = this.read(length);

        long value = 0L;
        for (int i = 0; i < b.length; i++) {
            value = (value << 8) + (b[i] & 0xFF);
        }

        return value;
    }

    @Override
    public long revertWrite(final long length) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is stream based.");
    }

    @Override
    public long revertRead(final long length) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is stream based.");

    }

    @Override
    public void clear() {
        this.stream = null;
        this.readPointer = 0;
    }

    @Override
    public long bytesWritten() {
        return -1;
    }

    @Override
    public long bytesRead() {
        return this.readPointer;
    }

    @Override
    public long capacity() {
        return -1;
    }

    @Override
    public boolean isReadable() {
        return true;
    }

    @Override
    public boolean isWriteable() {
        return false;
    }

    @Override
    public boolean isRevertable() {
        return false;
    }

    /**
     * Increments the read pointer with the
     * given number of byte read. Sets the pointer
     * finally to -1 if MAX_LONG is exceeded.
     *
     * @param bytesRead the number of bytes read
     */
    private void incrementReadPointer(final long bytesRead) {
        if (this.readPointer > -1
                && this.readPointer <= (Long.MAX_VALUE - bytesRead)) {
            this.readPointer += bytesRead;
        } else if (this.readPointer != -1) {
            this.readPointer = -1;
        }
    }

}
