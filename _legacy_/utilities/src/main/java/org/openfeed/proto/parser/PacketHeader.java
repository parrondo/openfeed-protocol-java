package org.openfeed.proto.parser;

import java.io.IOException;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.WireFormat;

/**
 * fast {@link MarketPacket} header peek (100 nanos)
 * 
 * note: relies on {@link MarketPacket#TYPE_FIELD_NUMBER} as field search stop
 */
public final class PacketHeader {

	private PacketHeader() {
	}

	public static final int EOF = 0;

	private static final int CHANNEL_FIELD_NUMBER = 1;

	private static final int SEQUENCE_FIELD_NUMBER = 2;

	private static final int TIMESTAMP_FIELD_NUMBER = 3;

	private static final int TYPE_FIELD_NUMBER = 4;

	private int channel;

	private boolean hasChannel;

	private long sequence;

	private boolean hasSequence;

	private long timeStamp;

	private boolean hasTimeStamp;

	private int type;

	private boolean hasType;
	
	private int subtype;
	
	private boolean hasSubtype;

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
		if (hasType) {
			text.append(" type=");
			text.append(type);
		}
		if (hasSubtype) {
			text.append(" subtype=");
			text.append(subtype);
		}
		return text.toString();
	}

	public static PacketHeader from(CodedInputStream coded) throws IOException {
		final PacketHeader header = new PacketHeader();
		while (true) {

			final int number = WireFormat.getTagFieldNumber(coded.readTag());

			if (number == EOF || number > TYPE_FIELD_NUMBER) {
				return header;
			}

			if (number == CHANNEL_FIELD_NUMBER) {
				header.channel = coded.readSInt32();
				header.hasChannel = true;
			}

			if (number == SEQUENCE_FIELD_NUMBER) {
				header.sequence = coded.readSInt64();
				header.hasSequence = true;
			}

			if (number == TIMESTAMP_FIELD_NUMBER) {
				header.timeStamp = coded.readSInt64();
				header.hasTimeStamp = true;
			}

			if (number == TYPE_FIELD_NUMBER) {
				header.type = coded.readEnum();
				header.hasType = true;
				return header; // Don't wait to read the next tag
			}

		}

	}

	public static PacketHeader from(final byte[] array) throws Exception {

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

	public int getTypeEnumValue() {
		return type;
	}

	public boolean hasType() {
		return hasType;
	}
	
	public int getSubtype() {
		return subtype;
	}
	
	public boolean hasSubtype() {
		return hasSubtype;
	}
	
}
