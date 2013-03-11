package org.openfeed.proto.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.CodedInputStream;

public final class StreamDecoder {

	private final Map<Integer, PacketDecoder> map;

	private StreamDecoder(Builder builder) {
		this.map = builder.map;
	}

	public void decode(InputStream is) throws IOException {
		int firstByte;
		while ((firstByte = is.read()) != -1) {
			int length = CodedInputStream.readRawVarint32(firstByte, is);
			LimitedInputStream limitedInputStream = new LimitedInputStream(is, length);
			CodedInputStream coded = CodedInputStream.newInstance(limitedInputStream);
			PacketHeader header = PacketHeader.from(coded);
			PacketDecoder packetHandler = map.get(header.getTypeEnumValue());
			if (packetHandler != null) {
				packetHandler.consume(header, coded);
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

		public void register(PacketDecoder handler) {
			map.put(handler.getType(), handler);
		}

		public StreamDecoder build() {
			return new StreamDecoder(this);
		}
	}

}
