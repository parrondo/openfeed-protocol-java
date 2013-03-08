package org.openfeed.proto.test.benchmarks;

import java.io.IOException;
import java.io.InputStream;

import org.openfeed.proto.test.benchmarks.headermessagetest.NestedHeaderMarketPacket;
import org.openfeed.proto.test.benchmarks.headermessagetest.PacketHeader;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.WireFormat;

public class NestedPacketStreamParser {

	private static final int HEADER_TAG_NUMBER = 1;

	public void decode(InputStream is, PacketVisitor visitor) throws IOException {
		int firstByte;
		while ((firstByte = is.read()) != -1) {
			int length = CodedInputStream.readRawVarint32(firstByte, is);
			LimitedInputStream limitedInputStream = new LimitedInputStream(is, length);
			CodedInputStream coded = CodedInputStream.newInstance(limitedInputStream);
			int tag = coded.readTag();
			if (WireFormat.getTagFieldNumber(tag) != HEADER_TAG_NUMBER) {
				throw new RuntimeException("Cannot parse message without header.");
			}
			PacketHeader.Builder headerBuilder = PacketHeader.newBuilder();
			coded.readMessage(headerBuilder, ExtensionRegistryLite.getEmptyRegistry());
			PacketHeader header = headerBuilder.build();
			if (header.hasType()) {
				switch (header.getType()) {
				case MARKET_UPDATE_PACKET:
					NestedHeaderMarketPacket packet = NestedHeaderMarketPacket.newBuilder().mergeFrom(coded).setHeader(header).build();
					visitor.visit(packet);
					break;
				}
			}
		}
	}
	
}
