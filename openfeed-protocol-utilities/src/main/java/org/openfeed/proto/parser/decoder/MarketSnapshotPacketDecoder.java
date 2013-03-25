package org.openfeed.proto.parser.decoder;

import java.io.IOException;

import org.openfeed.proto.data.MarketSnapshotPacket;
import org.openfeed.proto.data.PacketType;

import com.google.protobuf.CodedInputStream;

public abstract class MarketSnapshotPacketDecoder implements PacketDecoder {

	private static final PacketType TYPE = PacketType.MARKET_SNAPSHOT;

	@Override
	public void decode(int packetType, int subType, CodedInputStream coded) throws IOException {
		acceptMarketSnapshotPacket(MarketSnapshotPacket.newBuilder().mergeFrom(coded).build());
	}

	protected abstract void acceptMarketSnapshotPacket(MarketSnapshotPacket packet);

	@Override
	public int getType() {
		return TYPE.getNumber();
	}

}
