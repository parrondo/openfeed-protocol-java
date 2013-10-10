package org.openfeed.messaging;

/**
 * 
 * Message types for testing. Must be unique within an application.
 * 
 */
public enum TestMessageType {

	STRING(100);
	
	
	private final int value;

	private TestMessageType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}

