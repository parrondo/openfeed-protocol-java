package org.openfeed.proto.test.benchmarks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.openfeed.proto.test.benchmarks.headermessagetest.FlatHeaderMarketPacket;
import org.openfeed.proto.test.benchmarks.headermessagetest.MarketEntry;
import org.openfeed.proto.test.benchmarks.headermessagetest.MarketMessage;
import org.openfeed.proto.test.benchmarks.headermessagetest.NestedHeaderMarketPacket;
import org.openfeed.proto.test.benchmarks.headermessagetest.PacketHeader;
import org.openfeed.proto.test.benchmarks.headermessagetest.PacketType;

public class OpenFeedExampleBenchmark {

	public static void main(String[] args) throws Exception {
		OpenFeedExampleBenchmark benchmark = new OpenFeedExampleBenchmark();
		benchmark.run();
	}

	private void run() throws Exception {
		int reps = 1000000;
		EncodeNested encodeNested = new EncodeNested(reps);
		EncodeFlat encodeFlat = new EncodeFlat(reps);
		DecodeNested decodeNested = new DecodeNested(reps);
		DecodeFlat decodeFlat = new DecodeFlat(reps);

		System.out.println("EncodeNested,EncodeFlat,DecodeNested,DecodeFlat");
		while (true) {
			time(encodeNested);
			time(encodeFlat);
			time(decodeNested);
			time(decodeFlat);
			System.out.println("");
		}
	}

	private void time(TestInstance instance) throws Exception {
		long start = System.nanoTime();
		instance.go();
		long stop = System.nanoTime();
		long micros = ((stop - start) / 1000);
		double each = (double) micros / instance.reps;
		System.out.print(each + ",");
	}

	private NestedHeaderMarketPacket makeNestedPacket(int id) {
		NestedHeaderMarketPacket.Builder packetBuilder = NestedHeaderMarketPacket.newBuilder();
		PacketHeader.Builder headerBuilder = PacketHeader.newBuilder();
		{
			headerBuilder.setChannel(1);
			headerBuilder.setSequence(id);
			headerBuilder.setType(PacketType.MARKET_UPDATE_PACKET);
		}
		{
			packetBuilder.setHeader(headerBuilder);
			packetBuilder.setMarketId(id);
		}

		for (int i = 0; i < 5; i++) {
			packetBuilder.addEntry(makeEntry(i));
		}

		return packetBuilder.build();
	}

	private FlatHeaderMarketPacket makeFlatPacket(int id) {
		FlatHeaderMarketPacket.Builder packetBuilder = FlatHeaderMarketPacket.newBuilder();
		{
			packetBuilder.setChannel(id);
			packetBuilder.setSequence(id);
			packetBuilder.setType(PacketType.MARKET_UPDATE_PACKET);
		}

		MarketMessage.Builder marketMessageBuilder = packetBuilder.addMessageBuilder();
		marketMessageBuilder.setMarketId(id);

		for (int i = 0; i < 5; i++) {
			marketMessageBuilder.addEntry(makeEntry(i));
		}
		return packetBuilder.build();
	}

	public class DecodeNested extends TestInstance {
		public static final int HEADER_TAG_NUMBER = 1;

		private final byte[] data;

		private final NestedPacketStreamParser decoder;

		public DecodeNested(int reps) throws IOException {
			super(reps);
			this.decoder = new NestedPacketStreamParser();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (int i = 0; i < reps; i++) {
				NestedHeaderMarketPacket packet = makeNestedPacket(i);
				packet.writeDelimitedTo(baos);
				ensureDecoding(packet);
			}
			this.data = baos.toByteArray();
		}

		private void ensureDecoding(final NestedHeaderMarketPacket packet) throws IOException {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			packet.writeDelimitedTo(output);
			PacketVisitor visitor = new EnsuringVisitor(packet);
			decoder.decode(new ByteArrayInputStream(output.toByteArray()), visitor);
		}

		@Override
		public boolean go() throws IOException {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			Visitor visitor = new Visitor();
			decoder.decode(bais, visitor);
			if (visitor.nestedCount != reps) {
				throw new IllegalStateException("Only: " + visitor.nestedCount + " messages were parsed.");
			}
			return false;
		}

	}

	public class DecodeFlat extends TestInstance {

		public static final int HEADER_TAG_NUMBER = 1;

		private final byte[] data;

		private FlatPacketStreamParser decoder;

		public DecodeFlat(int reps) throws IOException {
			super(reps);
			this.decoder = new FlatPacketStreamParser();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (int i = 0; i < reps; i++) {
				FlatHeaderMarketPacket packet = makeFlatPacket(i);
				packet.writeDelimitedTo(baos);
				ensureDecoding(packet);
			}
			this.data = baos.toByteArray();
		}

		private void ensureDecoding(final FlatHeaderMarketPacket packet) throws IOException {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			packet.writeDelimitedTo(output);
			PacketVisitor visitor = new EnsuringVisitor(packet);
			decoder.decode(new ByteArrayInputStream(output.toByteArray()), visitor);
		}

		@Override
		public boolean go() throws Exception {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			Visitor visitor = new Visitor();
			decoder.decode(bais, visitor);
			if (visitor.flatCount != reps) {
				throw new IllegalStateException("Only: " + visitor.flatCount + " messages were parsed.");
			}
			return false;
		}

	}

	public class EncodeNested extends TestInstance {
		public EncodeNested(int reps) {
			super(reps);
		}

		@Override
		public boolean go() {
			byte[] bytes;
			for (int i = 0; i < reps; i++) {
				NestedHeaderMarketPacket nested = makeNestedPacket(i);
				bytes = nested.toByteArray();
			}
			return false;
		}
	}

	public class EncodeFlat extends TestInstance {
		public EncodeFlat(int reps) {
			super(reps);
		}

		@Override
		public boolean go() {
			byte[] bytes;
			for (int i = 0; i < reps; i++) {
				FlatHeaderMarketPacket packet = makeFlatPacket(i);
				bytes = packet.toByteArray();
			}
			return false;
		}
	}

	private class EnsuringVisitor implements PacketVisitor {

		private final Object expected;

		public EnsuringVisitor(Object expected) {
			this.expected = expected;
		}
		
		@Override
		public void visit(FlatHeaderMarketPacket flatPacket) {
			if (!flatPacket.equals(expected)) {
				throw new IllegalStateException("Decoded packet did not match expected.");
			}
		}

		@Override
		public void visit(NestedHeaderMarketPacket nestedPacket) {
			if (!nestedPacket.equals(expected)) {
				throw new IllegalStateException("Decoded packet did not match expected.");
			}
		}
		
	}
	
	private class Visitor implements PacketVisitor {

		private int flatCount;
		private int nestedCount;

		@Override
		public void visit(FlatHeaderMarketPacket flatPacket) {
			this.flatCount++;
		}

		@Override
		public void visit(NestedHeaderMarketPacket nestedPacket) {
			this.nestedCount++;
		}

	}

	private MarketEntry makeEntry(int id) {
		MarketEntry.Builder entryBuilder = MarketEntry.newBuilder();
		entryBuilder.setMarketId(id);
		entryBuilder.setIndex(id);
		entryBuilder.setSizeMantissa(id);
		return entryBuilder.build();
	}

	public abstract class TestInstance {

		protected final int reps;

		public TestInstance(int reps) {
			this.reps = reps;
		}

		public abstract boolean go() throws Exception;

	}

}
