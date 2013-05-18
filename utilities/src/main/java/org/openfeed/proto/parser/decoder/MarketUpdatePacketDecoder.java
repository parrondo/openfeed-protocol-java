package org.openfeed.proto.parser.decoder;

import java.io.IOException;

import org.openfeed.proto.data.MarketUpdatePacket;
import org.openfeed.proto.data.PacketType;

import com.google.protobuf.CodedInputStream;

public abstract class MarketUpdatePacketDecoder implements PacketDecoder {

	private static final PacketType TYPE = PacketType.MARKET_UPDATE;

	@Override
	public int getType() {
		return TYPE.getNumber();
	}

	@Override
	public final void decode(int packetType, int subType, CodedInputStream coded) throws IOException {
		acceptMarketUpdatePacket(MarketUpdatePacket.PARSER.parseFrom(coded));
	}

	protected abstract void acceptMarketUpdatePacket(MarketUpdatePacket packet);

}
