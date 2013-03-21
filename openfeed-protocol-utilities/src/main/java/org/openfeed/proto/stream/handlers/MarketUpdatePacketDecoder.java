package org.openfeed.proto.stream.handlers;

import java.io.IOException;

import org.openfeed.proto.data.MarketUpdatePacket;
import org.openfeed.proto.data.PacketType;
import org.openfeed.proto.stream.PacketDecoder;
import org.openfeed.proto.stream.PacketHeader;

import com.google.protobuf.CodedInputStream;

public abstract class MarketUpdatePacketDecoder implements PacketDecoder {

	private static final PacketType TYPE = PacketType.MARKET_UPDATE;

	@Override
	public final void consume(PacketHeader header, CodedInputStream coded) throws IOException {
		MarketUpdatePacket.Builder packetBuilder = MarketUpdatePacket.newBuilder().mergeFrom(coded);
		mergeHeader(header, packetBuilder);
		MarketUpdatePacket packet = packetBuilder.build();
		acceptMarketUpdatePacket(packet);
	}

	protected abstract void acceptMarketUpdatePacket(MarketUpdatePacket packet);

	private void mergeHeader(PacketHeader header, MarketUpdatePacket.Builder builder) {
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
