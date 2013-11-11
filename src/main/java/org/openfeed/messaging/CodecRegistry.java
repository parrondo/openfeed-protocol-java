package org.openfeed.messaging;

import java.util.HashMap;
import java.util.Map;

public final class CodecRegistry {

	private final Map<Integer, MessageCodec<?>> byTypeMap;
	private final Map<Class<?>, MessageCodec<?>> byClassMap;

	private CodecRegistry(MessageCodec<?>... codecs) {
		this.byTypeMap = new HashMap<Integer, MessageCodec<?>>();
		this.byClassMap = new HashMap<Class<?>, MessageCodec<?>>();
		for (MessageCodec<?> codec : codecs) {
			byTypeMap.put(codec.getTypeCode(), codec);
			byClassMap.put(codec.getMessageClass(), codec);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> MessageCodec<T> getCodec(int type) {
		return (MessageCodec<T>) byTypeMap.get(type);
	}

	public <T> MessageCodec<T> getCodecByClass(Class<T> clazz) {
		@SuppressWarnings("unchecked")
		MessageCodec<T> messageCodec = (MessageCodec<T>) byClassMap.get(clazz);
		return messageCodec;
	}

	public static CodecRegistry create(MessageCodec<?>... codecs) {
		return new CodecRegistry(codecs);
	}

}
