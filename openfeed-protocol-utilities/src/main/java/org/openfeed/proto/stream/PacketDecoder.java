package org.openfeed.proto.stream;

import java.io.IOException;

import com.google.protobuf.CodedInputStream;

public interface PacketDecoder {

	public void consume(PacketHeader header, CodedInputStream coded) throws IOException;

	public int getType();
	
}
