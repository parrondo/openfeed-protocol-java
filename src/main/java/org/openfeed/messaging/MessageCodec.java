package org.openfeed.messaging;

import java.io.IOException;

public interface MessageCodec<T> {

	public abstract T decode(byte[] bytes) throws IOException;

	public abstract byte[] encode(T message) throws IOException;

	public abstract int getTypeCode();

	public abstract Class<T> getMessageClass();

}
