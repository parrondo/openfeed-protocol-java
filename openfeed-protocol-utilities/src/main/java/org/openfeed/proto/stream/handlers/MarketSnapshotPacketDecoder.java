package org.openfeed.proto.stream.handlers;

import java.io.IOException;

import org.openfeed.proto.data.MarketSnapshotPacket;
import org.openfeed.proto.data.PacketType;
import org.openfeed.proto.stream.PacketDecoder;
import org.openfeed.proto.stream.PacketHeader;
import org.openfeed.proto.stream.PacketVisitor;

import com.google.protobuf.CodedInputStream;

public abstract class MarketSnapshotPacketDecoder implements PacketDecoder {

	private static final PacketType TYPE = PacketType.MARKET_SNAPSHOT;

	@Override
	public final void consume(PacketHeader header, CodedInputStream coded) throws IOException {
		MarketSnapshotPacket.Builder packetBuilder = MarketSnapshotPacket.newBuilder().mergeFrom(coded);
		mergeHeader(header, packetBuilder);
		MarketSnapshotPacket packet = packetBuilder.build();
		acceptMarketSnapshotPacket(packet);
	}

	protected abstract void acceptMarketSnapshotPacket(MarketSnapshotPacket packet);

	private void mergeHeader(PacketHeader header, MarketSnapshotPacket.Builder builder) {
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