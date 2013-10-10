package org.openfeed.messaging;

import java.io.IOException;
import java.nio.charset.Charset;

import org.openfeed.messaging.MessageCodec;

/**
 * Used as an example codec for testing. Normally messaging should be protobuf
 * or something like it
 * 
 * 
 */
public final class UTF8StringCodec implements MessageCodec<String> {

	private static final Charset CHARSET = Charset.forName("UTF-8");

	@Override
	public String decode(byte[] bytes) throws IOException {
		return new String(bytes, 0, bytes.length, CHARSET.name());
	}

	@Override
	public byte[] encode(String message) throws IOException {
		return message.getBytes(CHARSET);
	}

	public static UTF8StringCodec create() {
		return new UTF8StringCodec();
	}

	@Override
	public int getTypeCode() {
		return TestMessageType.STRING.getValue();
	}

	@Override
	public Class<String> getMessageClass() {
		return String.class;
	}

}
