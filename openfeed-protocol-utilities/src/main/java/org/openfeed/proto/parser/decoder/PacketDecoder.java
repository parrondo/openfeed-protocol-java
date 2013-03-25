package org.openfeed.proto.parser.decoder;

import java.io.IOException;

import com.google.protobuf.CodedInputStream;

public interface PacketDecoder {

	public void decode(int packetType, int subType, CodedInputStream coded) throws IOException;
	
	public int getType();
	
}
