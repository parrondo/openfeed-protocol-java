package org.openfeed.messaging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.openfeed.messaging.MessageStreamReader;
import org.openfeed.messaging.MessageStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagingTest {

	private static final Logger logger = LoggerFactory.getLogger(MessagingTest.class);

	@Test
	public void testSendAndReceive() throws IOException {
		logger.info("Running");

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		MessageStreamWriter writer = new MessageStreamWriter(output);

		writer.write(UTF8StringCodec.create(), "Hello world");
		writer.write(UTF8StringCodec.create(), "Quick brown fox...");

		byte[] bytes = output.toByteArray();
		logger.info("Bytes: " + Arrays.toString(bytes));

		MessageStreamReader reader = new MessageStreamReader(new ByteArrayInputStream(bytes));
		reader.register(UTF8StringCodec.create(), new MessageReceiver<String>() {
			@Override
			public void receive(String message) {
				logger.info("Received string message: " + message);
			}
		});

		while (reader.processNextMessage())
			;

	}
}
