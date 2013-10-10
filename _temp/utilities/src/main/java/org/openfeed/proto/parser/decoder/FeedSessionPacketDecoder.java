package org.openfeed.proto.parser.decoder;

import java.io.IOException;

import org.openfeed.proto.feed.session.FeedSessionPacket;
import org.openfeed.proto.feed.session.PacketType;

import com.google.protobuf.CodedInputStream;

public abstract class FeedSessionPacketDecoder implements PacketDecoder {

	private static final PacketType TYPE = PacketType.FEED_SESSION;

	@Override
	public void decode(int packetType, int subType, CodedInputStream coded) throws IOException {
		acceptMarketSnapshotPacket(FeedSessionPacket.PARSER.parseFrom(coded));
	}

	protected abstract void acceptMarketSnapshotPacket(FeedSessionPacket packet);

	@Override
	public int getType() {
		return TYPE.getNumber();
	}

}
