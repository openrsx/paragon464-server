package com.paragon464.gameserver.net;

import com.paragon464.gameserver.util.IoBufferUtils;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * Represents a single packet.
 *
 * @author Graham Edgecombe <grahamedgecombe@gmail.com>
 */
public class Packet {

    /**
     * The opcode.
     */
    private int opcode;
    /**
     * The type.
     */
    private Type type;
    /**
     * The payload.
     */
    private IoBuffer payload;

    /**
     * Creates a packet.
     *
     * @param opcode  The opcode.
     * @param type    The type.
     * @param payload The payload.
     */
    public Packet(int opcode, Type type, IoBuffer payload) {
        this.opcode = opcode;
        this.type = type;
        this.payload = payload;
    }

    /**
     * Checks if this packet is raw. A raw packet does not have the usual
     * headers such as opcode or size.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean isRaw() {
        return opcode == -1;
    }

    /**
     * Gets the opcode.
     *
     * @return The opcode.
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * Gets the type.
     *
     * @return The type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the payload.
     *
     * @return The payload.
     */
    public IoBuffer getPayload() {
        return payload;
    }

    /**
     * Gets the length.
     *
     * @return The length.
     */
    public int getLength() {
        return payload.limit();
    }

    /**
     * Reads several bytes.
     *
     * @param b The target array.
     */
    public void get(byte[] b) {
        payload.get(b);
    }

    /**
     * Reads a byte.
     *
     * @return A single byte.
     */
    public byte getByte() {
        return get();
    }

    /**
     * Reads a single byte.
     *
     * @return A single byte.
     */
    public byte get() {
        return payload.get();
    }

    /**
     * Reads an unsigned byte.
     *
     * @return An unsigned byte.
     */
    public int getUnsignedByte() {
        return payload.getUnsigned();
    }

    /**
     * Reads an unsigned short.
     *
     * @return An unsigned short.
     */
    public int getUnsignedShort() {
        return payload.getUnsignedShort();
    }

    /**
     * Reads an integer.
     *
     * @return An integer.
     */
    public int getInt() {
        return payload.getInt();
    }

    /**
     * Reads a long.
     *
     * @return A long.
     */
    public long getLong() {
        return payload.getLong();
    }

    /**
     * Reads a type C byte.
     *
     * @return A type C byte.
     */
    public byte getByteC() {
        return (byte) (-get());
    }

    /**
     * Gets a type S byte.
     *
     * @return A type S byte.
     */
    public byte getByteS() {
        return (byte) (128 - get());
    }

    /**
     * Reads a little-endian type A short.
     *
     * @return A little-endian type A short.
     */
    public short getLEShortA() {
        int i = (payload.get() - 128 & 0xFF) | ((payload.get() & 0xFF) << 8);
        if (i > 32767)
            i -= 0x10000;
        return (short) i;
    }

    /**
     * Reads a little-endian short.
     *
     * @return A little-endian short.
     */
    public short getLEShort() {
        int i = (payload.get() & 0xFF) | ((payload.get() & 0xFF) << 8);
        if (i > 32767)
            i -= 0x10000;
        return (short) i;
    }

    /**
     * Reads a V1 integer.
     *
     * @return A V1 integer.
     */
    public int getInt1() {
        return ((payload.get() & 0xff) << 8) | ((payload.get() & 0xff)) | ((payload.get() & 0xff) << 24)
            | ((payload.get() & 0xff) << 16);
    }

    /**
     * Reads a V2 integer.
     *
     * @return A V2 integer.
     */
    public int getInt2() {
        return ((payload.get() & 0xff) << 16) | ((payload.get() & 0xff) << 24) | ((payload.get() & 0xff))
            | ((payload.get() & 0xff) << 8);
    }

    /**
     * Reads a little-endian integer.
     *
     * @return A little-endian integer.
     */
    public int getLEInt() {
        return (payload.get() & 0xff) | ((payload.get() & 0xff) << 8) | ((payload.get() & 0xff) << 16)
            | ((payload.get() & 0xff) << 24);
    }

    /**
     * Gets a 3-byte integer.
     *
     * @return The 3-byte integer.
     */
    public int getTriByte() {
        return ((payload.get() << 16) & 0xFF) | ((payload.get() << 8) & 0xFF) | (payload.get() & 0xFF);
    }

    /**
     * Reads a RuneScape string.
     *
     * @return The string.
     */
    public String getRS2String() {
        return IoBufferUtils.getRS2String(payload);
    }

    /**
     * Reads a type A short.
     *
     * @return A type A short.
     */
    public short getShortA() {
        int i = ((payload.get() & 0xFF) << 8) | (payload.get() - 128 & 0xFF);
        if (i > 32767)
            i -= 0x10000;
        return (short) i;
    }

    /**
     * Reads a series of bytes in reverse.
     *
     * @param is     The target byte array.
     * @param offset The offset.
     * @param length The length.
     */
    public void getReverse(byte[] is, int offset, int length) {
        for (int i = (offset + length - 1); i >= offset; i--) {
            is[i] = payload.get();
        }
    }

    /**
     * Reads a series of type A bytes in reverse.
     *
     * @param is     The target byte array.
     * @param offset The offset.
     * @param length The length.
     */
    public void getReverseA(byte[] is, int offset, int length) {
        for (int i = (offset + length - 1); i >= offset; i--) {
            is[i] = getByteA();
        }
    }

    /**
     * Reads a type A byte.
     *
     * @return A type A byte.
     */
    public byte getByteA() {
        return (byte) (get() - 128);
    }

    /**
     * Reads a series of bytes.
     *
     * @param is     The target byte array.
     * @param offset The offset.
     * @param length The length.
     */
    public void get(byte[] is, int offset, int length) {
        for (int i = 0; i < length; i++) {
            is[offset + i] = payload.get();
        }
    }

    /**
     * Gets a smart.
     *
     * @return The smart.
     */
    public int getSmart() {
        int peek = payload.get(payload.position());
        if (peek < 128) {
            return (get() & 0xFF);
        } else {
            return (getShort() & 0xFFFF) - 32768;
        }
    }

    /**
     * Reads a short.
     *
     * @return A short.
     */
    public short getShort() {
        return payload.getShort();
    }

    /**
     * Gets a signed smart.
     *
     * @return The signed smart.
     */
    public int getSignedSmart() {
        int peek = payload.get(payload.position());
        if (peek < 128) {
            return ((get() & 0xFF) - 64);
        } else {
            return ((getShort() & 0xFFFF) - 49152);
        }
    }

    /**
     * The type of packet.
     *
     * @author Graham Edgecombe <grahamedgecombe@gmail.com>
     */
    public enum Type {

        /**
         * A fixed size packet where the size never changes.
         */
        FIXED,

        /**
         * A variable packet where the size is described by a byte.
         */
        VARIABLE,

        /**
         * A variable packet where the size is described by a word.
         */
        VARIABLE_SHORT

    }
}
