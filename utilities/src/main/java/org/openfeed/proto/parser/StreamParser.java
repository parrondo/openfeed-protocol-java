package org.openfeed.proto.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.openfeed.proto.generic.Packet;
import org.openfeed.proto.parser.decoder.PacketDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.WireFormat;

public final class StreamParser {

	private static final Logger logger = LoggerFactory.getLogger(StreamParser.class);

	public static final int EOF = 0;

	private final Map<Integer, PacketDecoder> map;

	private StreamParser(Builder builder) {
		this.map = builder.map;
	}

	// no length header, only one message
	// added for use with netty protobuf handler
	public void parseSingleMessage(InputStream is) throws IOException {
		int packetType = getPacketType(is);
		CodedInputStream coded = CodedInputStream.newInstance(is);
		PacketDecoder packetDecoder = map.get(packetType);
		if (packetDecoder != null) {
			packetDecoder.decode(packetType, 0, coded);
		} else {
			logger.warn("No decoder for packet type: " + packetType);
		}
	}

	public void parse(InputStream is) throws IOException {
		int firstByte;
		while ((firstByte = is.read()) != -1) {
			int length = CodedInputStream.readRawVarint32(firstByte, is);
			LimitedInputStream limitedInputStream = new LimitedInputStream(is, length);
			int packetType = getPacketType(limitedInputStream);
			CodedInputStream coded = CodedInputStream.newInstance(limitedInputStream);
			PacketDecoder packetDecoder = map.get(packetType);
			if (packetDecoder != null) {
				packetDecoder.decode(packetType, 0, coded);
			}
		}
	}

	private int getPacketType(InputStream is) throws IOException {
		is.mark(256);
		try {
			CodedInputStream input = CodedInputStream.newInstance(is);
			while (true) {
				final int number = WireFormat.getTagFieldNumber(input.readTag());

				switch (number) {
				case EOF:
					throw new RuntimeException("No packet type found. End of stream.");
				case Packet.CHANNEL_FIELD_NUMBER:
					int channel = input.readSInt32();
					logger.info("Channel : " + channel);
					break;
				case Packet.SEQUENCE_FIELD_NUMBER:
					long seq = input.readSInt64();
					logger.info("Seq: " + seq);
					break;
				case Packet.TIMESTAMP_FIELD_NUMBER:
					long timestamp = input.readSInt64();
					logger.info("timestamp: " + timestamp);
					break;
				case Packet.TYPE_FIELD_NUMBER:
					int type = input.readEnum();
					return type;
				default:
					throw new RuntimeException("No packet type found. Reached tag " + number);
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

		private HashMap<Integer, PacketDecoder> map;

		private Builder() {
			this.map = new HashMap<Integer, PacketDecoder>();
		}

		public Builder register(PacketDecoder handler) {
			map.put(handler.getType(), handler);
			return this;
		}

		public StreamParser build() {
			return new StreamParser(this);
		}
	}

}
