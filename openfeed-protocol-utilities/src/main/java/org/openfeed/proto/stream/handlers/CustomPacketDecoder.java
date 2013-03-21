package org.openfeed.proto.stream.handlers;

import org.openfeed.proto.assm.PacketType;
import org.openfeed.proto.stream.PacketDecoder;
import org.openfeed.proto.stream.PacketHeader;

import com.google.protobuf.CodedInputStream;

public abstract class CustomPacketDecoder implements PacketDecoder {

	private static final PacketType TYPE = PacketType.CUSTOM;
	
	@Override
	public abstract void consume(PacketHeader header, CodedInputStream coded);

	@Override
	public int getType() {
		return TYPE.getNumber();
	}

}
