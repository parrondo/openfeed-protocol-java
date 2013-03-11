package org.openfeed.proto.stream.handlers;

import java.io.IOException;

import org.openfeed.proto.data.MarketPacket;
import org.openfeed.proto.data.PacketType;
import org.openfeed.proto.stream.PacketDecoder;
import org.openfeed.proto.stream.PacketHeader;
import org.openfeed.proto.stream.PacketVisitor;

import com.google.protobuf.CodedInputStream;

public class MarketPacketDecoder implements PacketDecoder {

	private static final PacketType TYPE = PacketType.DATA;

	private final PacketVisitor<MarketPacket> visitor;

	public MarketPacketDecoder(PacketVisitor<MarketPacket> visitor) {
		this.visitor = visitor;
	}

	@Override
	public void consume(PacketHeader header, CodedInputStream coded) throws IOException {
		MarketPacket.Builder packetBuilder = MarketPacket.newBuilder().mergeFrom(coded);
		mergeHeader(header, packetBuilder);
		MarketPacket packet = packetBuilder.build();
		visitor.visit(packet);
	}

	private void mergeHeader(PacketHeader header, MarketPacket.Builder builder) {
		if (header.hasChannel()) {
			builder.setChannel(header.getChannel());
		}
		if (header.hasSequence()) {
			builder.setSequence(header.getSequence());
		}
		if (header.hasTimeStamp()) {
			builder.setTimeStamp(header.getTimeStamp());
		}
		if (header.hasType()) {
			builder.setType(TYPE);
		}
	}

	@Override
	public int getType() {
		return TYPE.getNumber();
	}

}
