package org.openfeed.proto.parser.decoder;

import org.openfeed.proto.assm.PacketType;

public abstract class CustomPacketDecoder implements PacketDecoder {

	private static final PacketType TYPE = PacketType.CUSTOM;
	
	@Override
	public int getType() {
		return TYPE.getNumber();
	}

}
