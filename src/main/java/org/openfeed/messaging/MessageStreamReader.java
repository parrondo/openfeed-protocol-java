package org.openfeed.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openfeed.messaging.encoding.EncodedInput;

public final class MessageStreamReader {

	private static final int END_OF_STREAM = -1;

	private final Map<Integer, CodecBinding<?>> codecBindings;

	private final EncodedInput encodedInput;

	public MessageStreamReader(InputStream input) {
		this.encodedInput = new EncodedInput(input);
		this.codecBindings = new HashMap<Integer, CodecBinding<?>>();
	}

	public <T> void register(MessageCodec<T> codec, MessageReceiver<T> messageReceiver) throws IOException {
		@SuppressWarnings("unchecked")
		CodecBinding<T> binding = (CodecBinding<T>) codecBindings.get(codec.getTypeCode());
		if (binding == null) {
			binding = new CodecBinding<T>(codec);
			codecBindings.put(codec.getTypeCode(), binding);
		}
		binding.addReceiver(messageReceiver);
	}

	/**
	 * @return true if the next message was successfully dispatched
	 * @throws IOException
	 */
	public boolean processNextMessage() throws IOException {
		final int type = encodedInput.readType();
		if (type == END_OF_STREAM) {
			return false;
		}
		final int length = encodedInput.readLength();
		final CodecBinding<?> binding = find(type);

		if (binding == null) {
			encodedInput.skipBody(length);
		} else {
			try {
				byte[] bytes = encodedInput.readBody(length);
				binding.dispatch(bytes);
				return true;
			} catch (Exception e) {
				throw new MessageStreamException("Error while decoding message", e);
			}
		}
		return false;
	}

	private CodecBinding<?> find(int type) {
		return codecBindings.get(type);
	}

	private static final class CodecBinding<T> {

		private final MessageCodec<T> codec;

		private final List<MessageReceiver<T>> receivers;

		CodecBinding(MessageCodec<T> codec) {
			this.codec = codec;
			this.receivers = new ArrayList<MessageReceiver<T>>();
		}

		public void dispatch(byte[] bytes) throws Exception {
			final T decodedMessage = codec.decode(bytes);
			for (MessageReceiver<T> receiver : receivers) {
				receiver.receive(decodedMessage);
			}
		}

		void addReceiver(MessageReceiver<T> messageReceiver) {
			receivers.add(messageReceiver);
		}

	}

}
