package org.openfeed.proto.test.benchmarks;

import org.openfeed.proto.test.benchmarks.headermessagetest.FlatHeaderMarketPacket;
import org.openfeed.proto.test.benchmarks.headermessagetest.NestedHeaderMarketPacket;

public interface PacketVisitor {

	public void visit(FlatHeaderMarketPacket flatPacket);
	
	public void visit(NestedHeaderMarketPacket nestedPacket);
}
