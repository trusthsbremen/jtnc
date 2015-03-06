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
package de.hsbremen.tc.tnc.message.util;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Default byte buffer backed by a list of arrays to overcome the size
 * limitations of a usual Java byte buffer. It can theoretically allocate
 * MAX_INT*MAX_INT bytes, because it uses long for size related functions
 * the maximum capacity is MAX_LONG. The capacity is allocated in
 * chunks as soon as needed and not at once to prevent unnecessary
 * memory allocation.
 *
 *
 */
public class DefaultByteBuffer implements ByteBuffer {

    private final List<byte[]> buffer;

    private final int chunkSize;
    private final long capacity;
    private final int lastRowSize;

    private int readColPointer;
    private int readRowPointer;
    private int writeColPointer;
    private int writeRowPointer;

    /**
     * Creates the buffer with the given capacity and
     * a default chunk size of MAX_INT.
     * @param capacity the buffer capacity
     */
    public DefaultByteBuffer(final long capacity) {
        this(capacity, Integer.MAX_VALUE);
    }

    /**
     * Creates the buffer with the given capacity and the
     * given chunk size used to allocate memory.
     *
     * @param capacity the buffer capacity
     * @param chunkSize the memory allocation chunk size
     */
    public DefaultByteBuffer(final long capacity, final int chunkSize) {
        if (chunkSize < 0 || capacity < 0) {
            this.chunkSize = 0;
            this.capacity = 0;
        } else {
            if (capacity <= chunkSize) {
                this.capacity = capacity;
                this.chunkSize = (int) capacity;
            } else {
                this.capacity = capacity;
                this.chunkSize = chunkSize;
            }
        }

        if (this.capacity <= this.chunkSize) {
            this.buffer = new ArrayList<>(1);
            this.buffer.add(new byte[(int) this.capacity]);
            this.lastRowSize = (int) this.capacity;

        } else {

            // count last row size
            this.lastRowSize = (int) (capacity % this.chunkSize);

            // count columns
            long cols = (capacity / this.chunkSize);
            cols = (this.lastRowSize > 0) ? cols + 1 : cols;

            if (cols <= Integer.MAX_VALUE) {

                this.buffer = new ArrayList<>((int) cols);
                this.buffer.add(new byte[this.chunkSize]);

            } else {

                throw new IllegalArgumentException(
                        "Capacity is to large and cannot "
                        + "be supported by this implementation.");

            }
        }

        this.readColPointer = 0;
        this.readRowPointer = -1;
        this.writeColPointer = 0;
        this.writeRowPointer = -1;
    }

    @Override
    public void write(final byte[] array) {
        if (array == null) {
            throw new NullPointerException("Array cannot be null.");
        }

        int length = array.length;

        this.checkWriteBufferOverflow(length);

        int index = 0;

        if (this.writeRowPointer >= (this.chunkSize - length)) {
            while (length > 0) {
                this.writeRowPointer++;

                if (this.writeRowPointer >= this.chunkSize) {
                    if ((this.writeColPointer + 1)
                            < (capacity / this.chunkSize)) {
                        this.buffer.add(new byte[this.chunkSize]);
                    } else {
                        this.buffer.add(new byte[this.lastRowSize]);
                    }

                    this.writeRowPointer = 0;
                    this.writeColPointer++;
                }

                int cpLength = ((this.chunkSize - this.writeRowPointer)
                        < length) ? this.chunkSize
                        - this.writeRowPointer
                        : length;

                System.arraycopy(array, index,
                        this.buffer.get(this.writeColPointer),
                        this.writeRowPointer, cpLength);
                length -= cpLength;
                index += cpLength;
                this.writeRowPointer += cpLength - 1;
            }
        } else {
            if (length > 0) {
                this.writeRowPointer++;
                System.arraycopy(array, index,
                        this.buffer.get(this.writeColPointer),
                        this.writeRowPointer, length);
                this.writeRowPointer += (length - 1);
            }
        }
    }

    @Override
    public void write(final ByteBuffer byteBuffer) {

        if (byteBuffer == null) {
            throw new NullPointerException("Buffer cannot be null.");
        }

        this.checkWriteBufferOverflow(byteBuffer.bytesWritten());

        while (byteBuffer.bytesWritten() > byteBuffer.bytesRead()) {
            if ((byteBuffer.bytesWritten() - byteBuffer.bytesRead())
                    > this.chunkSize) {
                this.write(byteBuffer.read(this.chunkSize));
            } else {
                this.write(byteBuffer.read(
                        (int) (byteBuffer.bytesWritten()
                                - byteBuffer.bytesRead())));
            }
        }

    }

    @Override
    public void writeByte(final byte b) {
        // check bounds
        this.checkWriteBufferOverflow(1);

        this.writeRowPointer++;

        if (this.writeRowPointer >= this.chunkSize) {
            if ((this.writeColPointer + 1) < (capacity / this.chunkSize)) {
                this.buffer.add(new byte[this.chunkSize]);
            } else {
                this.buffer.add(new byte[this.lastRowSize]);
            }

            this.writeRowPointer = 0;
            this.writeColPointer++;
        }

        this.buffer.get(this.writeColPointer)[this.writeRowPointer] = (byte) b;
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
    public byte[] read(final int length) {
        if (length <= 0) {
            // obviously we cannot read anything so retrun 0 length;
            return new byte[0];
        }

        int length2 = length;

        this.checkReadBufferUnderflow(length2);

        byte[] array = new byte[length];

        int index = 0;

        if (this.readRowPointer >= (this.chunkSize - length2)) {
            while (length2 > 0) {
                this.readRowPointer++;
                if (this.readRowPointer >= this.chunkSize) {
                    this.readRowPointer = 0;
                    this.readColPointer++;
                }

                int cpLength = ((this.chunkSize - this.readRowPointer)
                        < length2) ? this.chunkSize
                        - this.readRowPointer
                        : length2;

                System.arraycopy(this.buffer.get(this.readColPointer),
                        this.readRowPointer, array, index, cpLength);
                length2 -= cpLength;
                index += cpLength;
                this.readRowPointer += cpLength - 1;
            }
        } else {
            this.readRowPointer++;
            System.arraycopy(this.buffer.get(this.readColPointer),
                    this.readRowPointer, array, index, length2);
            this.readRowPointer += (length2 - 1);
        }

        return array;
    }

    @Override
    public ByteBuffer read(final long length) {

        if (length <= 0) {
            // obviously we cannot read anything so retrun 0 length;
            return new DefaultByteBuffer(0);
        }

        this.checkReadBufferUnderflow(length);

        ByteBuffer b = new DefaultByteBuffer(length);

        while (b.bytesWritten() < length) {
            if (length - b.bytesWritten() > this.chunkSize) {
                b.write(this.read(this.chunkSize));
            } else {
                b.write(this.read((int) (length - b.bytesWritten())));
            }
        }

        return b;

    }

    @Override
    public byte readByte() {
        // check bounds
        this.checkReadBufferUnderflow(1);

        this.readRowPointer++;

        if (this.readRowPointer >= this.chunkSize) {
            this.readRowPointer = 0;
            this.readColPointer++;
        }

        return this.buffer.get(this.readColPointer)[this.readRowPointer];
    }

    @Override
    public short readShort() {
        return this.readShort((byte) 2);
    }

    @Override
    public short readShort(final byte length) {
        if (length <= 0) {
            return 0;
        }

        if (length > 2) {
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
        if (length <= 0) {
            return 0;
        }

        if (length > 4) {
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
        if (length <= 0) {
            return 0;
        }

        if (length > 8) {
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
        if (length < 0) {
            throw new IllegalArgumentException(
                    "Parameter must be a positiv integer.");
        }

        long currentFullPos = this.bytesWritten();

        long revertPos = ((currentFullPos - length) < 0)
                ? 0 : currentFullPos - length;

        // count last row size
        int revertedLastRowSize = (int) (revertPos % this.chunkSize);

        // count columns
        int revertedCols = (int) (revertPos / this.chunkSize);

        if (this.writeColPointer > revertedCols) {
            for (int i = this.writeColPointer; i > revertedCols; --i) {
                this.buffer.remove(i);
            }
            this.writeRowPointer = this.chunkSize;
        }

        this.writeColPointer = revertedCols;

        if (this.writeRowPointer > revertedLastRowSize) {
            byte[] array = new byte[this.chunkSize];
            System.arraycopy(this.buffer.get(this.writeColPointer), 0,
                    array, 0, revertedLastRowSize);
            this.buffer.set(this.writeColPointer, array);
        }
        this.writeRowPointer = revertedLastRowSize - 1;

        return this.bytesWritten();
    }

    @Override
    public long revertRead(final long length) {
        if (length < 0) {
            throw new IllegalArgumentException(
                    "Parameter must be a positiv integer.");
        }

        long currentFullPos = (this.readColPointer * this.chunkSize)
                + this.readRowPointer + 1;

        long revertPos = ((currentFullPos - length) < 0)
                ? 0 : currentFullPos - length;

        // count last row size
        int revertedLastRowSize = (int) (revertPos % this.chunkSize) - 1;

        // count columns
        int revertedCols = (int) (revertPos / this.chunkSize);

        this.readColPointer = revertedCols;
        this.readRowPointer = revertedLastRowSize;

        return this.bytesRead();
    }

    @Override
    public void clear() {

        this.readColPointer = 0;
        this.readRowPointer = -1;
        this.writeColPointer = 0;
        this.writeRowPointer = -1;

        this.buffer.clear();

        if (capacity <= this.chunkSize) {
            this.buffer.add(new byte[(int) capacity]);
        } else {

            // count columns
            long cols = (capacity / this.chunkSize);
            cols = (this.lastRowSize > 0) ? cols + 1 : cols;

            if (cols <= Integer.MAX_VALUE) {

                this.buffer.add(new byte[this.chunkSize]);

            } else {

                throw new IllegalArgumentException(
                        "Capacity is to large and cannot be "
                        + "supported by this implementation.");

            }
        }

    }

    @Override
    public long bytesWritten() {
        // TODO this may be not the best idea because this values
        // are needed to control write and read from the buffer
        long count = ((this.writeColPointer * this.chunkSize)
                + this.writeRowPointer
                + 1);
        return (count >= 0 ? count : -1);
    }

    @Override
    public long bytesRead() {
        // TODO this may be not the best idea because this values
        // are needed to control write and read from the buffer
        long count = ((this.readColPointer * this.chunkSize)
                + this.readRowPointer + 1);
        return (count >= 0 ? count : -1);
    }

    @Override
    public long capacity() {
        return this.capacity;
    }

    @Override
    public boolean isReadable() {
        return true;
    }

    @Override
    public boolean isWriteable() {
        return true;
    }

    @Override
    public boolean isRevertable() {
        return true;
    }

    /**
     * Checks if the length of bytes can be written to
     * the buffer without exceeding the available capacity.
     *
     * @param lookAhead the length of bytes to look ahead
     */
    private void checkWriteBufferOverflow(final long lookAhead) {
        if (lookAhead < 0) {
            throw new IllegalArgumentException(
                    "Parameter must be a positiv integer.");
        }

        long currentFullPos = (this.writeColPointer * this.chunkSize)
                + this.writeRowPointer + 1;

        if (currentFullPos > (this.capacity - lookAhead)) {
            throw new BufferOverflowException();
        }
    }

    /**
     * Checks if the length of bytes can be read from the buffer
     * without exceeding the bytes written.
     *
     * @param lookAhead the length of bytes to look ahead
     */
    private void checkReadBufferUnderflow(final long lookAhead) {

        if (lookAhead < 0) {
            throw new IllegalArgumentException(
                    "Parameter must be a positiv integer.");
        }

        long currentFullPosWrite = (this.writeColPointer * this.chunkSize)
                + this.writeRowPointer + 1;
        long currentFullPosRead = (this.readColPointer * this.chunkSize)
                + this.readRowPointer + 1;

        if (currentFullPosRead > (currentFullPosWrite - lookAhead)) {
            throw new BufferUnderflowException();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.buffer == null) ? 0 : this.buffer.hashCode());
        result = prime * result
                + (int) (this.capacity ^ (this.capacity >>> 32));
        result = prime * result + this.chunkSize;
        result = prime * result + this.lastRowSize;
        result = prime * result + this.readColPointer;
        result = prime * result + this.readRowPointer;
        result = prime * result + this.writeColPointer;
        result = prime * result + this.writeRowPointer;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DefaultByteBuffer other = (DefaultByteBuffer) obj;
        if (this.buffer == null) {
            if (other.buffer != null) {
                return false;
            }
        } else {
            for (int i = 0; i < this.buffer.size(); i++) {
                if (this.buffer.get(i) == null) {
                    if (other.buffer.get(i) != null) {
                        return false;
                    }
                } else {
                    if (!(Arrays
                            .equals(this.buffer.get(i), other.buffer.get(i)))) {
                        return false;
                    }
                }
            }
        }
        if (this.capacity != other.capacity) {
            return false;
        }
        if (this.chunkSize != other.chunkSize) {
            return false;
        }
        if (this.lastRowSize != other.lastRowSize) {
            return false;
        }
        if (this.readColPointer != other.readColPointer) {
            return false;
        }
        if (this.readRowPointer != other.readRowPointer) {
            return false;
        }
        if (this.writeColPointer != other.writeColPointer) {
            return false;
        }
        if (this.writeRowPointer != other.writeRowPointer) {
            return false;
        }
        return true;
    }

}
