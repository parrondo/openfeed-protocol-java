/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.buf.data;

import com.barchart.proto.buf.data.MarketPacket;
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

	//

	private int channel;

	private boolean hasChannel;

	//

	private long sequence;

	private boolean hasSequence;

	//

	private long timeStamp;

	private boolean hasTimeStamp;

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

	//

	public static PacketHeader from(final byte[] array) throws Exception {

		final PacketHeader header = new PacketHeader();

		final CodedInputStream input = CodedInputStream.newInstance(array);

		while (true) {

			final int number = WireFormat.getTagFieldNumber(input.readTag());

			if (number == EOF || number >= MarketPacket.TYPE_FIELD_NUMBER) {
				return header;
			}

			if (number == MarketPacket.CHANNEL_FIELD_NUMBER) {
				header.channel = input.readSInt32();
				header.hasChannel = true;
			}

			if (number == MarketPacket.SEQUENCE_FIELD_NUMBER) {
				header.sequence = input.readSInt64();
				header.hasSequence = true;
			}

			if (number == MarketPacket.TIMESTAMP_FIELD_NUMBER) {
				header.timeStamp = input.readSInt64();
				header.hasTimeStamp = true;
			}

		}

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

}
