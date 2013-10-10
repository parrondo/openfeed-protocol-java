package org.openfeed.messaging.encoding;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.openfeed.messaging.MessageStreamException;

public class EncodedInput {

	private final InputStream input;

	public EncodedInput(InputStream input) {
		this.input = input;
	}

	/**
	 * 
	 * @return the next message type or -1 if end of stream
	 * @throws IOException
	 */
	public int readType() throws IOException {
		int b1 = input.read();
		int b2 = input.read();

		if (b1 == -1) {
			return -1;
		} else {
			return readUnsignedShort(b1, b2);
		}
	}

	/**
	 * 
	 * @return the next message length
	 * @throws IOException
	 */
	public int readLength() throws IOException {
		return readRawVarint32();
	}

	public void skipBody(int length) throws IOException {
		input.skip(length);
	}

	public byte[] readBody(final int length) throws IOException {
		byte[] body = new byte[length];
		int n = 0;
		while (n < length) {
			int count = input.read(body, n, length - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
		return body;
	}

	// ////////////////

	public int readRawVarint32() throws IOException {
		byte tmp = readRawByte();
		if (tmp >= 0) {
			return tmp;
		}
		int result = tmp & 0x7f;
		if ((tmp = readRawByte()) >= 0) {
			result |= tmp << 7;
		} else {
			result |= (tmp & 0x7f) << 7;
			if ((tmp = readRawByte()) >= 0) {
				result |= tmp << 14;
			} else {
				result |= (tmp & 0x7f) << 14;
				if ((tmp = readRawByte()) >= 0) {
					result |= tmp << 21;
				} else {
					result |= (tmp & 0x7f) << 21;
					result |= (tmp = readRawByte()) << 28;
					if (tmp < 0) {
						// Discard upper 32 bits.
						for (int i = 0; i < 5; i++) {
							if (readRawByte() >= 0) {
								return result;
							}
						}
						throw new MessageStreamException("Malformed VarInt");
					}
				}
			}
		}
		return result;
	}

	private int readRawVarint32(int firstByte) throws IOException {
		if ((firstByte & 0x80) == 0) {
			return firstByte;
		}

		int result = firstByte & 0x7f;
		int offset = 7;
		for (; offset < 32; offset += 7) {
			final int b = input.read();
			if (b == -1) {
				throw new MessageStreamException("Truncated message");
			}
			result |= (b & 0x7f) << offset;
			if ((b & 0x80) == 0) {
				return result;
			}
		}
		// Keep reading up to 64 bits.
		for (; offset < 64; offset += 7) {
			final int b = input.read();
			if (b == -1) {
				throw new MessageStreamException("Truncated message");
			}
			if ((b & 0x80) == 0) {
				return result;
			}
		}
		throw new MessageStreamException("Malformed VarInt");
	}

	private byte readRawByte() throws IOException {
		return (byte) input.read();
	}

	// big endian
	public static int readUnsignedShort(int b1, int b2) {
		return (b1 & 0xFF) << 8 | (b2 & 0xFF);
	}

}
