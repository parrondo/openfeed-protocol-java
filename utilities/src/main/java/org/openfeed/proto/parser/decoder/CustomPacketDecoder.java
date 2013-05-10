package org.openfeed.proto.parser.decoder;

import org.openfeed.proto.generic.PacketType;

public abstract class CustomPacketDecoder implements PacketDecoder {

	private static final PacketType TYPE = PacketType.UNKNOWN;

	@Override
	public int getType() {
		return TYPE.getNumber();
	}

}
