package org.openfeed.proto.stream;

public interface PacketVisitor<T> {

	public void visit(T packet);
	
}
