package org.openfeed.proto.stream.handlers;

import java.io.IOException;

import org.openfeed.proto.inst.PacketType;
import org.openfeed.proto.inst.InstrumentPacket;
import org.openfeed.proto.stream.PacketDecoder;
import org.openfeed.proto.stream.PacketHeader;
import org.openfeed.proto.stream.PacketVisitor;

import com.google.protobuf.CodedInputStream;

public class InstrumentPacketHandler implements PacketDecoder {

	private static final PacketType TYPE = PacketType.INSTRUMENT;

	private final PacketVisitor<InstrumentPacket> visitor;

	public InstrumentPacketHandler(PacketVisitor<InstrumentPacket> visitor) {
		this.visitor = visitor;
	}

	@Override
	public void consume(PacketHeader header, CodedInputStream coded) throws IOException {
		InstrumentPacket.Builder packetBuilder = InstrumentPacket.newBuilder().mergeFrom(coded);
		mergeHeader(header, packetBuilder);
		InstrumentPacket packet = packetBuilder.build();
		visitor.visit(packet);
	}

	private void mergeHeader(PacketHeader header, InstrumentPacket.Builder builder) {
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
