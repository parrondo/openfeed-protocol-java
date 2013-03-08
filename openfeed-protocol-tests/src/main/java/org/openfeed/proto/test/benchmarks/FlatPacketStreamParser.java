package org.openfeed.proto.test.benchmarks;

import java.io.IOException;
import java.io.InputStream;

import org.openfeed.proto.test.benchmarks.headermessagetest.FlatHeaderMarketPacket;

import com.google.protobuf.CodedInputStream;

public class FlatPacketStreamParser {

	public void decode(InputStream is, PacketVisitor visitor) throws IOException {
		int firstByte;
		while ((firstByte = is.read()) != -1) {
			int length = CodedInputStream.readRawVarint32(firstByte, is);
			LimitedInputStream limitedInputStream = new LimitedInputStream(is, length);
			CodedInputStream coded = CodedInputStream.newInstance(limitedInputStream);
			FlatPacketHeader flatPacketHeader = FlatPacketHeader.from(coded);
			switch (flatPacketHeader.getType()) {
			case MARKET_UPDATE_PACKET:
				FlatHeaderMarketPacket.Builder packetBuilder = FlatHeaderMarketPacket.newBuilder().mergeFrom(coded);
				flatPacketHeader.merge(packetBuilder);
				FlatHeaderMarketPacket packet = packetBuilder.build();
				visitor.visit(packet);
			}
		}
	}

}
