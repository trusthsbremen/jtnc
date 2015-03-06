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
import java.io.OutputStream;
import java.nio.BufferOverflowException;

/**
 * Write only byte buffer backed by an output stream. It can theoretically
 * allocate an infinite number of bytes. If the stream contains more bytes
 * than MAX_LONG, the contained pointers are set to -1 after reaching MAX_LONG.
 * The buffer cannot be reverted, because of the stream nature. If the stream
 * throws an error the error is signaled by a buffer overflow.
 *
 *
 */
public class StreamedWriteOnlyByteBuffer implements ByteBuffer {

    private static final int DEFAULT_STREAM_CHUNK = 8192;

    private long writePointer;
    private OutputStream stream;

    /**
     * Wraps the given stream into this buffer.
     *
     * @param out the output stream to use
     */
    public StreamedWriteOnlyByteBuffer(final OutputStream out) {
        this.stream = out;
        this.writePointer = 0;

    }

    @Override
    public void write(final ByteBuffer buffer) {
        if (buffer == null) {
            throw new NullPointerException("Array cannot be null.");
        }
        if (this.stream == null) {
            throw new BufferOverflowException();
        }
        try {
            while (buffer.bytesWritten() > buffer.bytesRead()) {

                if ((buffer.bytesWritten() - buffer.bytesRead())
                        > DEFAULT_STREAM_CHUNK) {
                    this.stream.write(buffer.read(DEFAULT_STREAM_CHUNK));
                    this.incrementWritePointer(DEFAULT_STREAM_CHUNK);
                } else {
                    int length = (int) (buffer.bytesWritten()
                            - buffer.bytesRead());
                    this.stream.write(buffer.read(length));
                    this.incrementWritePointer(length);
                }

            }
        } catch (IOException e) {
            throw new BufferOverflowException();
        }

    }

    @Override
    public void write(final byte[] array) {
        if (array == null) {
            throw new NullPointerException("Array cannot be null.");
        }
        if (this.stream == null) {
            throw new BufferOverflowException();
        }

        int length = array.length;

        try {

            this.stream.write(array);

        } catch (IOException e) {
            throw new BufferOverflowException();
        }

        this.incrementWritePointer(length);
    }

    @Override
    public void writeByte(final byte b) {

        if (this.stream == null) {
            throw new BufferOverflowException();
        }

        try {
            this.stream.write(b);
        } catch (IOException e) {
            throw new BufferOverflowException();
        }

        this.incrementWritePointer(1);

    }

    @Override
    public void writeUnsignedByte(final short unsigned) {
        this.writeByte((byte) (unsigned));
    }

    @Override
    public void writeShort(final short signed) {
        byte[] b = new byte[] {(byte) (signed >>> 8),
                (byte) (signed)};
        this.write(b);
    }

    @Override
    public void writeUnsignedShort(final int unsigned) {
        byte[] b = new byte[] {(byte) (unsigned >>> 8),
                (byte) (unsigned)};
        this.write(b);
    }

    @Override
    public void writeInt(final int signed) {
        byte[] b = new byte[] {(byte) (signed >>> 24),
                (byte) (signed >>> 16),
                (byte) (signed >>> 8),
                (byte) (signed) };
        this.write(b);
    }

    @Override
    public void writeUnsignedInt(final long unsigned) {
        byte[] b = new byte[] {(byte) (unsigned >>> 24),
                (byte) (unsigned >>> 16),
                (byte) (unsigned >>> 8),
                (byte) (unsigned) };
        this.write(b);
    }

    @Override
    public void writeLong(final long signed) {
        byte[] b = new byte[] {

        (byte) (signed >>> 56), (byte) (signed >>> 48),
        (byte) (signed >>> 40), (byte) (signed >>> 32),
        (byte) (signed >>> 24), (byte) (signed >>> 16),
        (byte) (signed >>> 8), (byte) (signed) };
        this.write(b);
    }

    @Override
    public void writeDigits(final long number, final byte length) {
        if (length <= 0) {
            return;
        }

        if (length > 8) {
            throw new IllegalArgumentException("Supplied length " + length
                    + " is to long.");
        }

        byte[] b = new byte[length];

        byte l = (byte) (length - 1);

        for (int i = 0; i < length; i++) {

            b[i] = (byte) (number >>> (8 * (l - i)));

        }
        this.write(b);
    }

    @Override
    public ByteBuffer read(final long length) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is write only.");
    }

    @Override
    public byte[] read(final int length) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is write only.");
    }

    @Override
    public byte readByte() {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is write only.");

    }

    @Override
    public short readShort() {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is write only.");
    }

    @Override
    public short readShort(final byte length) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is write only.");
    }

    @Override
    public int readInt() {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is write only.");
    }

    @Override
    public int readInt(final byte length) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is write only.");
    }

    @Override
    public long readLong() {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is write only.");
    }

    @Override
    public long readLong(final byte length) {
        throw new UnsupportedOperationException(
                "Operation not supported, because buffer is write only.");
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
        try {
            this.stream.flush();
        } catch (IOException e) {
           // ignore
           this.stream = null;
        }

        this.writePointer = 0;
    }

    @Override
    public long bytesWritten() {
        return this.writePointer;
    }

    @Override
    public long bytesRead() {
        return -1;
    }

    @Override
    public long capacity() {
        return -1;
    }

    @Override
    public boolean isReadable() {
        return false;
    }

    @Override
    public boolean isWriteable() {
        return true;
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
    private void incrementWritePointer(final long bytesRead) {
        if (this.writePointer > -1
                && this.writePointer <= (Long.MAX_VALUE - bytesRead)) {
            this.writePointer += bytesRead;
        } else if (this.writePointer != -1) {
            this.writePointer = -1;
        }
    }

}
