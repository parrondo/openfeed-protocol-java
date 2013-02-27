/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.util.date;

public class DateTimeValue extends DateOnlyValue {

	private final byte hour;
	private final byte minute;
	private final byte second;
	private final short millis;

	public DateTimeValue(//
			final int year, //
			final int month, //
			final int day, //
			final int hour, //
			final int minute, //
			final int second, //
			final int millis //
	) {
		super(year, month, day);
		this.hour = (byte) hour;
		this.minute = (byte) minute;
		this.second = (byte) second;
		this.millis = (short) millis;
	}

	public byte getHour() {
		return hour;
	}

	public byte getMinute() {
		return minute;
	}

	public byte getSecond() {
		return second;
	}

	public short getMillis() {
		return millis;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + hour + minute + second + millis;
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof DateTimeValue) {
			final DateTimeValue that = (DateTimeValue) other;
			return super.equals(that) && this.hour == that.hour
					&& this.minute == that.minute && this.second == that.second
					&& this.millis == that.millis;
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("%sT%02d:%02d:%02d.%03d", super.toString(), hour,
				minute, second, millis);
	}

}
