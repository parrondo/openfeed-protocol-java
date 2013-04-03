package org.openfeed.proto.parser;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.openfeed.proto.data.MarketUpdatePacket;
import org.openfeed.proto.data.PacketType;
import org.openfeed.proto.parser.decoder.MarketUpdatePacketDecoder;
import org.openfeed.proto.parser.decoder.PacketDecoder;

import com.google.protobuf.Message;

public class StreamParserTest {

	@Before
	public void setup() {
		
	}
	
	@Test
	public void testMarketdataHasDefaultPacketType() {
		MarketUpdatePacket packet = MarketUpdatePacket.newBuilder().build();
		assertEquals(PacketType.MARKET_UPDATE, packet.getType());
	}
	
	@Test
	public void testMarketUpdatePacketDecoder() throws IOException {
		MarketUpdatePacket packet = MarketUpdatePacket.newBuilder().setChannel(1).setTimeStamp(123456789L).build();
		InputStream inputStream = streamify(packet);
		final AtomicReference<MarketUpdatePacket> ref = new AtomicReference<MarketUpdatePacket>();
		PacketDecoder handler = new MarketUpdatePacketDecoder() {
			@Override
			protected void acceptMarketUpdatePacket(MarketUpdatePacket packet) {
				ref.set(packet);
			}
		};
		StreamParser parser = StreamParser.newBuilder().register(handler).build();
		parser.parse(inputStream);
		assertEquals(packet, ref.get());
	}

	private InputStream streamify(Message packet) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		packet.writeDelimitedTo(baos);
		return new ByteArrayInputStream(baos.toByteArray());
	}
	
}
