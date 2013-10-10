package org.openfeed.messaging;

import java.io.IOException;
import java.io.OutputStream;

import org.openfeed.messaging.encoding.EncodedOutput;

public final class MessageStreamWriter {

	private final OutputStream output;

	private final EncodedOutput encodedOutput;

	public MessageStreamWriter(OutputStream output) {
		this.output = output;
		this.encodedOutput = new EncodedOutput(output);
	}

	public <T> void write(final MessageCodec<T> codec, T message) throws IOException {
		byte[] bytes = codec.encode(message);
		encodedOutput.writeType(codec.getTypeCode());
		encodedOutput.writeLength(bytes.length);
		encodedOutput.writeBody(bytes);
		output.flush();
	}

}
