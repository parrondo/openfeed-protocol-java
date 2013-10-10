package org.openfeed.messaging;

public interface MessageReceiver<T> {

	public void receive(T message) throws Exception;
	
}
