package org.openfeed.codecs;

import java.io.IOException;

import org.openfeed.HeartbeatMessage;
import org.openfeed.InstrumentDefinitionMessage;
import org.openfeed.MarketSnapshotMessage;
import org.openfeed.MarketUpdate;
import org.openfeed.MarketUpdateMessage;
import org.openfeed.OpenFeedMessageType;
import org.openfeed.messaging.CodecRegistry;
import org.openfeed.messaging.MessageCodec;

public class OpenFeedCodecs {

	public static final MessageCodec<HeartbeatMessage> HEARTBEAT_MESSAGE_CODEC = new MessageCodec<HeartbeatMessage>() {
		@Override
		public HeartbeatMessage decode(byte[] bytes) throws IOException {
			return HeartbeatMessage.parseFrom(bytes);
		}

		@Override
		public byte[] encode(HeartbeatMessage message) throws IOException {
			return message.toByteArray();
		}

		@Override
		public int getTypeCode() {
			return OpenFeedMessageType.HEARTBEAT_VALUE;
		}

		@Override
		public Class<HeartbeatMessage> getMessageClass() {
			return HeartbeatMessage.class;
		}

	};

	public static final MessageCodec<InstrumentDefinitionMessage> INSTRUMENT_DEFINITION_MESSAGE_CODEC = new MessageCodec<InstrumentDefinitionMessage>() {
		@Override
		public InstrumentDefinitionMessage decode(byte[] bytes) throws IOException {
			return InstrumentDefinitionMessage.parseFrom(bytes);
		}

		@Override
		public byte[] encode(InstrumentDefinitionMessage message) throws IOException {
			return message.toByteArray();
		}

		@Override
		public int getTypeCode() {
			return OpenFeedMessageType.INSTRUMENT_DEFINITION_MESSAGE_VALUE;
		}

		@Override
		public Class<InstrumentDefinitionMessage> getMessageClass() {
			return InstrumentDefinitionMessage.class;
		}

	};

	public static final MessageCodec<MarketUpdateMessage> MARKET_UPDATE_MESSAGE_CODEC = new MessageCodec<MarketUpdateMessage>() {
		@Override
		public MarketUpdateMessage decode(byte[] bytes) throws IOException {
			return MarketUpdateMessage.parseFrom(bytes);
		}

		@Override
		public byte[] encode(MarketUpdateMessage message) throws IOException {
			return message.toByteArray();
		}

		@Override
		public int getTypeCode() {
			return OpenFeedMessageType.MARKET_UPDATE_MESSAGE_VALUE;
		}

		@Override
		public Class<MarketUpdateMessage> getMessageClass() {
			return MarketUpdateMessage.class;
		}

	};

	public static final MessageCodec<MarketUpdate> MARKET_UPDATE_CODEC = new MessageCodec<MarketUpdate>() {
		@Override
		public MarketUpdate decode(byte[] bytes) throws IOException {
			return MarketUpdate.parseFrom(bytes);
		}

		@Override
		public byte[] encode(MarketUpdate message) throws IOException {
			return message.toByteArray();
		}

		@Override
		public int getTypeCode() {
			return OpenFeedMessageType.MARKET_UPDATE_VALUE;
		}

		@Override
		public Class<MarketUpdate> getMessageClass() {
			return MarketUpdate.class;
		}

	};

	public static final MessageCodec<MarketSnapshotMessage> MARKET_SNAPSHOT_MESSAGE_CODEC = new MessageCodec<MarketSnapshotMessage>() {
		@Override
		public MarketSnapshotMessage decode(byte[] bytes) throws IOException {
			return MarketSnapshotMessage.parseFrom(bytes);
		}

		@Override
		public byte[] encode(MarketSnapshotMessage message) throws IOException {
			return message.toByteArray();
		}

		@Override
		public int getTypeCode() {
			return OpenFeedMessageType.MARKET_SNAPSHOT_MESSAGE_VALUE;
		}

		@Override
		public Class<MarketSnapshotMessage> getMessageClass() {
			return MarketSnapshotMessage.class;
		}

	};

	public static final CodecRegistry REGISTRY = CodecRegistry.create( //
			HEARTBEAT_MESSAGE_CODEC, //
			INSTRUMENT_DEFINITION_MESSAGE_CODEC, //
			MARKET_SNAPSHOT_MESSAGE_CODEC, //
			MARKET_UPDATE_MESSAGE_CODEC, //
			MARKET_UPDATE_CODEC //
			);

}
