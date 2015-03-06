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

/**
 * Generic byte buffer that can be used to write and read bytes from. It also
 * provides several helper functions to read or write different Java primitives
 * from/to the buffer. The buffer has two pointers to indicate the read
 * and write position. Is is not possible to read more bytes than
 * where written to the buffer before.
 *
 * The buffer can throw a underflow or overflow exception. Underflow is
 * thrown if the bytes read exceed the bytes written. Overflow is thrown
 * if the bytes written exceed the buffer capacity.
 *
 *
 */
public interface ByteBuffer {

    /**
     * Appends the content of the given buffer to
     * the content of this buffer.
     *
     * @param buffer the source buffer
     */
    void write(ByteBuffer buffer);

    /**
     * Appends the content of the given byte array
     * to the content of this buffer.
     *
     * @param array the source array
     */
    void write(byte[] array);

    /**
     * Appends the byte to the content of this buffer.
     *
     * @param signed the signed byte
     */
    void writeByte(byte signed);

    /**
     * Appends the short as unsigned byte to the content
     * of this buffer.
     *
     * @param unsigned the unsigned byte
     */
    void writeUnsignedByte(short unsigned);

    /**
     * Appends the short to the content of this buffer.
     *
     * @param signed the signed short
     */
    void writeShort(short signed);

    /**
     * Appends the int as unsigned short to the content
     * of this buffer.
     *
     * @param unsigned the unsigned short
     */
    void writeUnsignedShort(int unsigned);

    /**
     * Appends the int to the content of this buffer.
     *
     * @param signed the signed int
     */
    void writeInt(int signed);

    /**
     * Appends the long as unsigned int to the content
     * of this buffer.
     *
     * @param unsigned the unsigned int
     */
    void writeUnsignedInt(long unsigned);

    /**
     * Appends the long to the content of this buffer.
     *
     * @param signed the signed long
     */
    void writeLong(long signed);

    /**
     * Appends the digits of a long until length
     * is reached. Maximum length is 8.
     *
     * @param number the number to append
     * @param length the length of digits to use from the number
     */
    void writeDigits(long number, byte length);

    /**
     * Returns the content of length from the buffer as
     * byte buffer.
     *
     * @param length the content length to read
     * @return the resulting buffer
     */
    ByteBuffer read(long length);

    /**
     * Returns the content of length from the buffer as
     * byte array.
     *
     * @param length the content length to read
     * @return the resulting array
     */
    byte[] read(int length);

    /**
     * Returns one byte from the buffer.
     *
     * @return the resulting byte
     */
    byte readByte();

    /**
     * Returns one short from the buffer.
     *
     * @return the resulting short
     */
    short readShort();

    /**
     * Returns one short from the buffer.
     * The short can be build with 1 or
     * 2 bytes depending on the given length.
     * Maximum length is 2.
     *
     * Useful to read an unsigned byte.
     *
     * @param length the content length to read
     * @return the resulting short
     */
    short readShort(byte length);

    /**
     * Returns one int from the buffer.
     *
     * @return the resulting short
     */
    int readInt();

    /**
     * Returns one int from the buffer.
     * The int can be build with 1 to
     * 4 bytes depending on the given length.
     * Maximum length is 4.
     *
     * Useful to read an unsigned short.
     *
     * @param length the content length to read
     * @return the resulting int
     */
    int readInt(byte length);

    /**
     * Returns one long from the buffer.
     *
     * @return the resulting short
     */
    long readLong();

    /**
     * Returns one long from the buffer.
     * The long can be build with 1 to
     * b bytes depending on the given length.
     * Maximum length is 8.
     *
     * Useful to read an unsigned int.
     *
     * @param length the content length to read
     * @return the resulting long
     */
    long readLong(byte length);

    /**
     * Resets the write pointer about the given
     * length of bytes. The new position is bytes written
     * minus length. If the read pointer is larger than
     * the new position, the pointer is set too, that
     * write pointer == read pointer. If the write pointer
     * is smaller than the given length, both pointers are
     * set to zero. Revert write discards all bytes that
     * are within the reverted length.
     *
     * @param length the number of bytes to step back
     * @return the new position of the write pointer
     */
    long revertWrite(long length);

    /**
     * Resets the read pointer about the given
     * length of bytes. The new position is bytes read
     * minus length. If the read pointer is smaller than
     * the given length, the pointer is set to zero. No bytes
     * will be discarded by this operation.
     *
     * @param length the number of bytes to step back
     * @return the new position of the write pointer
     */
    long revertRead(long length);

    /**
     * Clears the buffer. The buffer will be empty
     * after this operation an all pointers will be
     * zero.
     *
     */
    void clear();

    /**
     * Returns the bytes currently written to
     * the buffer. If the buffer capacity is larger
     * than MAX_LONG and the number of bytes written
     * is larger than MAX_LONG or the buffer is not writable
     * -1 is returned.
     *
     * @return the written bytes count
     */
    long bytesWritten();

    /**
     * Returns the bytes already read from
     * the buffer. If the buffer capacity is larger
     * than MAX_LONG and the number of bytes written
     * is larger than MAX_LONG or the buffer is not readable
     * -1 is returned.
     *
     * @return the read bytes count
     */
    long bytesRead();

    /**
     * Returns true if the buffer supports reading and
     * false if the buffer is write only (e.g. if the buffer
     * is backed by an input stream it is read only).
     *
     * @return true if buffer supports reading
     */
    boolean isReadable();

    /**
     * Returns true if the buffer supports writing and
     * false if the buffer is read only (e.g. if the buffer
     * is backed by an output stream it is write only).
     *
     * @return true if buffer supports writing
     */
    boolean isWriteable();

    /**
     * Returns true if the buffer supports a revert of the
     * write and read position pointers.
     *
     * @return true if buffer supports revert
     */
    boolean isRevertable();
    /**
     * Returns the maximum capacity of the buffer, that
     * can be written and read afterwards. If the capacity
     * is not known -1 is returned.
     *
     * @return the maximum buffer capacity
     */
    long capacity();

}
