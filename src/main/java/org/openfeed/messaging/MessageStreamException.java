package org.openfeed.messaging;

import java.io.IOException;

public class MessageStreamException extends IOException {

	private static final long serialVersionUID = 1L;

	public MessageStreamException(String message, Throwable t) {
		super(message, t);
	}

	public MessageStreamException(String message) {
		super(message);
	}
}
