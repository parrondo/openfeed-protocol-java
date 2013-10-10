package org.openfeed.messaging;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.openfeed.messaging.encoding.EncodedOutput;

public class MessageWriter {

	private final Map<Class<?>, MessageCodec<Object>> map;

	public MessageWriter() {
		this.map = new HashMap<Class<?>, MessageCodec<Object>>();
	}

	@SuppressWarnings("unchecked")
	public void register(MessageCodec<? extends Object> codec) {
		map.put(codec.getMessageClass(), (MessageCodec<Object>) codec);
	}

	private MessageCodec<Object> findCodec(Object message) throws IOException {
		MessageCodec<Object> codec = map.get(message.getClass());
		if (codec == null) {
			throw new IllegalArgumentException("Unknown codec for class " + message.getClass());
		}
		return codec;
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
