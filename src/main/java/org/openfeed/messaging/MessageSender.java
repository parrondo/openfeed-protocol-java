package org.openfeed.messaging;

import java.io.IOException;

public interface MessageSender<T> {

	public void send(T message) throws IOException;

}
