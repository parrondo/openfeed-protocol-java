package org.openfeed.proto.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.openfeed.proto.parser.decoder.PacketDecoder;

import com.google.protobuf.CodedInputStream;

public final class StreamParser {

	private final Map<Integer, PacketDecoder> map;

	private StreamParser(Builder builder) {
		this.map = builder.map;
	}

	public void parse(InputStream is) throws IOException {
		int firstByte;
		while ((firstByte = is.read()) != -1) {
			int length = CodedInputStream.readRawVarint32(firstByte, is);
			int packetType = CodedInputStream.readRawVarint32(is.read(), is);
			int subType = CodedInputStream.readRawVarint32(is.read(), is);
			LimitedInputStream limitedInputStream = new LimitedInputStream(is, length);
			CodedInputStream coded = CodedInputStream.newInstance(limitedInputStream);

			PacketDecoder packetDecoder = map.get(packetType);
			if (packetDecoder != null) {
				packetDecoder.decode(packetType, subType, coded);
			}
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
