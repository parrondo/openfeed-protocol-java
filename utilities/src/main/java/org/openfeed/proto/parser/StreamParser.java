package org.openfeed.proto.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.openfeed.proto.common.Packet;
import org.openfeed.proto.parser.decoder.PacketDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.WireFormat;

public final class StreamParser {

	private static final Logger logger = LoggerFactory
			.getLogger(StreamParser.class);

	public static final int EOF = 0;

	private final Map<Integer, PacketDecoder> map;

	private StreamParser(final Builder builder) {
		this.map = builder.map;
	}

	// no length header, only one message
	// added for use with netty protobuf handler
	public void parseSingleMessage(final InputStream is) throws IOException {
		final int packetType = getPacketType(is);
		final CodedInputStream coded = CodedInputStream.newInstance(is);
		final PacketDecoder packetDecoder = map.get(packetType);
		if (packetDecoder != null) {
			packetDecoder.decode(packetType, 0, coded);
		} else {
			logger.warn("No decoder for packet type: " + packetType);
		}
	}

	public void parse(final InputStream is) throws IOException {
		int firstByte;
		while ((firstByte = is.read()) != -1) {
			final int length = CodedInputStream.readRawVarint32(firstByte, is);
			final LimitedInputStream limitedInputStream = new LimitedInputStream(
					is, length);
			final int packetType = getPacketType(limitedInputStream);
			final CodedInputStream coded = CodedInputStream
					.newInstance(limitedInputStream);
			final PacketDecoder packetDecoder = map.get(packetType);
			if (packetDecoder != null) {
				packetDecoder.decode(packetType, 0, coded);
			}
		}
	}

	private int getPacketType(final InputStream is) throws IOException {
		is.mark(256);
		try {
			final CodedInputStream input = CodedInputStream.newInstance(is);
			while (true) {
				final int number = WireFormat
						.getTagFieldNumber(input.readTag());

				switch (number) {
				case EOF:
					throw new RuntimeException(
							"No packet type found. End of stream.");
				case Packet.CHANNEL_FIELD_NUMBER:
					final int channel = input.readSInt32();
					logger.info("Channel : " + channel);
					break;
				case Packet.SEQUENCE_FIELD_NUMBER:
					final long seq = input.readSInt64();
					logger.info("Seq: " + seq);
					break;
				case Packet.TIMESTAMP_FIELD_NUMBER:
					final long timestamp = input.readSInt64();
					logger.info("timestamp: " + timestamp);
					break;
				case Packet.TYPE_FIELD_NUMBER:
					final int type = input.readEnum();
					return type;
				default:
					throw new RuntimeException(
							"No packet type found. Reached tag " + number);
				}
			}
		} finally {
			is.reset();
		}
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private final HashMap<Integer, PacketDecoder> map;

		private Builder() {
			this.map = new HashMap<Integer, PacketDecoder>();
		}

		public Builder register(final PacketDecoder handler) {
			map.put(handler.getType(), handler);
			return this;
		}

		public StreamParser build() {
			return new StreamParser(this);
		}
	}

}
