/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.util.date;

public class DateOnlyValue {

	private final short year;
	private final byte month;
	private final byte day;

	public DateOnlyValue(//
			final int year, //
			final int month, //
			final int day //
	) {
		this.year = (short) year;
		this.month = (byte) month;
		this.day = (byte) day;
	}

	public short getYear() {
		return year;
	}

	public byte getMonth() {
		return month;
	}

	public byte getDay() {
		return day;
	}

	@Override
	public int hashCode() {
		return year + month + day;
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof DateOnlyValue) {
			final DateOnlyValue that = (DateOnlyValue) other;
			return this.year == that.year && this.month == that.month
					&& this.day == that.day;
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("%04d-%02d-%02d", year, month, day);
	}

}
