package org.openfeed.messaging.encoding;

import java.io.IOException;
import java.io.OutputStream;

public final class EncodedOutput {

	public static final int DEFAULT_BUFFER_SIZE = 4096;

	private static final int MAX_TYPE = 65535;

	private final OutputStream output;

	public EncodedOutput(OutputStream output) {
		this.output = output;
	}

	public void writeType(int typeCode) throws IOException {
		if (typeCode < 0 || typeCode > MAX_TYPE) {
			throw new IllegalArgumentException("Message type outside of range: " + typeCode);
		}
		writeUnsignedShort(typeCode);
		// writeRawVarint32(typeCode);
	}

	public void writeLength(int length) throws IOException {
		writeRawVarint32(length);
	}

	public void writeBody(byte[] bytes) throws IOException {
		output.write(bytes);
	}

	// The following is borrowed from Google's protocol buffers
	// CodedOutputStream

	private void writeRawVarint32(int value) throws IOException {
		while (true) {
			if ((value & ~0x7F) == 0) {
				output.write(value);
				return;
			} else {
				output.write((value & 0x7F) | 0x80);
				value >>>= 7;
			}
		}
	}

	// Big endian
	private void writeUnsignedShort(int value) throws IOException {
		output.write(value >> 24);
		output.write(value);
	}

}
