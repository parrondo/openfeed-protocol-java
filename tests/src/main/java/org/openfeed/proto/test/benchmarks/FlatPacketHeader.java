package org.openfeed.proto.test.benchmarks;

import java.io.IOException;

import org.openfeed.proto.test.benchmarks.headermessagetest.FlatHeaderMarketPacket;
import org.openfeed.proto.test.benchmarks.headermessagetest.FlatHeaderMarketPacket.Builder;
import org.openfeed.proto.test.benchmarks.headermessagetest.PacketType;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.WireFormat;

/**
 * fast {@link MarketPacket} header peek (100 nanos)
 * 
 * note: relies on {@link MarketPacket#TYPE_FIELD_NUMBER} as field search stop
 */
public final class FlatPacketHeader {

	private FlatPacketHeader() {
	}

	public static final int EOF = 0;

	//

	private int channel;

	private boolean hasChannel;

	//

	private long sequence;

	private boolean hasSequence;

	//

	private long timeStamp;

	private boolean hasTimeStamp;

	private PacketType type;

	private boolean hasType;

	//

	@Override
	public String toString() {
		final StringBuilder text = new StringBuilder(128);
		if (hasChannel) {
			text.append(" channel=");
			text.append(channel);
		}
		if (hasSequence) {
			text.append(" sequence=");
			text.append(sequence);
		}
		if (hasTimeStamp) {
			text.append(" timeStamp=");
			text.append(timeStamp);
		}
		return text.toString();
	}

	public static FlatPacketHeader from(CodedInputStream coded) throws IOException {
		final FlatPacketHeader header = new FlatPacketHeader();
		while (true) {

			final int number = WireFormat.getTagFieldNumber(coded.readTag());

			if (number == EOF || number > FlatHeaderMarketPacket.TYPE_FIELD_NUMBER) {
				return header;
			}

			if (number == FlatHeaderMarketPacket.CHANNEL_FIELD_NUMBER) {
				header.channel = coded.readSInt32();
				header.hasChannel = true;
			}

			if (number == FlatHeaderMarketPacket.SEQUENCE_FIELD_NUMBER) {
				header.sequence = coded.readSInt64();
				header.hasSequence = true;
			}

			if (number == FlatHeaderMarketPacket.TIMESTAMP_FIELD_NUMBER) {
				header.timeStamp = coded.readSInt64();
				header.hasTimeStamp = true;
			}

			if (number == FlatHeaderMarketPacket.TYPE_FIELD_NUMBER) {
				header.type = PacketType.valueOf(coded.readEnum());
				header.hasType = true;
				return header;  // Don't wait to read the next tag
			}

		}

	}

	public static FlatPacketHeader from(final byte[] array) throws Exception {

		final CodedInputStream input = CodedInputStream.newInstance(array);
		return from(input);

	}

	public int getChannel() {
		return channel;
	}

	public boolean hasChannel() {
		return hasChannel;
	}

	public long getSequence() {
		return sequence;
	}

	public boolean hasSequence() {
		return hasSequence;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public boolean hasTimeStamp() {
		return hasTimeStamp;
	}

	public PacketType getType() {
		return type;
	}

	public boolean hasType() {
		return hasType;
	}

	public void merge(Builder packetBuilder) {
		if (hasChannel) {
			packetBuilder.setChannel(channel);
		}
		if (hasSequence) {
			packetBuilder.setSequence(sequence);
		}
		if (hasTimeStamp) {
			packetBuilder.setTimeStamp(timeStamp);
		}
		if (hasType) {
			packetBuilder.setType(type);
		}
	}

}
