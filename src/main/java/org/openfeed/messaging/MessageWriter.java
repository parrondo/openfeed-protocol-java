package org.openfeed.messaging;

import java.io.IOException;
import java.io.OutputStream;

import org.openfeed.messaging.encoding.EncodedOutput;

public class MessageWriter {

	private final CodecRegistry codecRegistry;

	public MessageWriter(CodecRegistry codecRegistry) {
		this.codecRegistry = codecRegistry;
	}

	@SuppressWarnings("unchecked")
	private MessageCodec<Object> findCodec(Object message) throws IOException {
		MessageCodec<? extends Object> codec = codecRegistry.getCodecByClass(message.getClass());
		if (codec == null) {
			throw new IllegalArgumentException("Unknown codec for class " + message.getClass());
		}
		return (MessageCodec<Object>) codec;
	}

	public void write(Object message, OutputStream os) throws IOException {
		EncodedOutput output = new EncodedOutput(os);
		MessageCodec<Object> codec = findCodec(message);
		byte[] body = codec.encode(message);
		output.writeType(codec.getTypeCode());
		output.writeLength(body.length);
		output.writeBody(body);
	}

}
