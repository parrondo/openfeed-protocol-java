/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.util.date;

public final class ProtoDateUtil {

	public static int YEAR_BITS = 11;
	public static int YEAR_MASK = 0x7FF;

	public static int MONTH_BITS = 4;
	public static int MONTH_MASK = 0xF;

	public static int DAY_BITS = 5;
	public static int DAY_MASK = 0x1F;

	public static int HOUR_BITS = 5;
	public static int HOUR_MASK = 0x1F;

	public static int MINUTE_BITS = 6;
	public static int MINUTE_MASK = 0x3F;

	public static int SECOND_BITS = 6;
	public static int SECOND_MASK = 0x3F;

	public static int MILLIS_BITS = 10;
	public static int MILLIS_MASK = 0x3FF;

	public static int countBits(long value) {

		int count = 0;

		while (value > 0) {
			value >>= 1;
			count++;
		}

		return count;

	}

	public static int countBytes(final long value) {

		return (int) Math.ceil(countBits(value) / 8.0);

	}

	/**
	 * 
	 * binary-coded date/only fields:
	 * 
	 * year.month.date
	 * 
	 * size : 11.4.5 = 20 bits or 3 bytes
	 * 
	 */
	public static DateOnlyValue fromBinaryDateOnly(int value) {

		final int day = value & DAY_MASK;
		value >>>= DAY_BITS;

		final int month = value & MONTH_MASK;
		value >>>= MONTH_BITS;

		final int year = value & YEAR_MASK;

		return new DateOnlyValue(year, month, day);

	}

	/**
	 * 
	 * binary-coded date/time fields:
	 * 
	 * year.month.date.hour.minute.second.millis
	 * 
	 * size: 11.4.5.5.6.6.10 = 47 bits or 6 bytes
	 * 
	 */
	public static DateTimeValue fromBinaryDateTime(long value) {

		final int millis = (int) (value & MILLIS_MASK);
		value >>>= MILLIS_BITS;

		final int second = (int) (value & SECOND_MASK);
		value >>>= SECOND_BITS;

		final int minute = (int) (value & MINUTE_MASK);
		value >>>= MINUTE_BITS;

		final int hour = (int) (value & HOUR_MASK);
		value >>>= HOUR_BITS;

		final int day = (int) (value & DAY_MASK);
		value >>>= DAY_BITS;

		final int month = (int) (value & MONTH_MASK);
		value >>>= MONTH_BITS;

		final int year = (int) value;

		return new DateTimeValue(year, month, day, hour, minute, second, millis);

	}

	/**
	 * 
	 * decimal-coded date/only fields
	 * 
	 * year.month.date in digits
	 * 
	 * 2012.07.04
	 * 
	 * size = 4 bytes
	 * 
	 */
	public static DateOnlyValue fromDecimalDateOnly(int value) {

		final int day = value % 100;
		value /= 100;

		final int month = value % 100;
		value /= 100;

		final int year = value;

		return new DateOnlyValue(year, month, day);

	}

	/**
	 * 
	 * decimal-coded date/time fields
	 * 
	 * year.month.date.hour.minute.second.millis in digits :
	 * 
	 * 2012.07.04.12.30.12.123
	 * 
	 * size = 7 bytes
	 * 
	 */
	public static DateTimeValue fromDecimalDateTime(long value) {

		final int millis = (int) (value % 1000);
		value /= 1000;

		final int second = (int) (value % 100);
		value /= 100;

		final int minute = (int) (value % 100);
		value /= 100;

		final int hour = (int) (value % 100);
		value /= 100;

		final int day = (int) (value % 100);
		value /= 100;

		final int month = (int) (value % 100);
		value /= 100;

		final int year = (int) value;

		return new DateTimeValue(year, month, day, hour, minute, second, millis);

	}

	public static int intoBinaryDateOnly(final DateOnlyValue value) {
		return intoBinaryDateOnly(value.getYear(), value.getMonth(),
				value.getDay());
	}

	public static int intoBinaryDateOnly(//
			final int year, //
			final int month, //
			final int day //
	) {

		int value = year;

		value <<= MONTH_BITS;
		value |= month;

		value <<= DAY_BITS;
		value |= day;

		return value;

	}

	public static long intoBinaryDateTime(final DateTimeValue value) {
		return intoBinaryDateTime(//
				value.getYear(), //
				value.getMonth(), //
				value.getDay(), //
				value.getHour(), //
				value.getMinute(), //
				value.getSecond(), //
				value.getMillis() //
		);

	}

	public static long intoBinaryDateTime(//
			final int year, //
			final int month, //
			final int day, //
			final int hour, //
			final int minute, //
			final int second, //
			final int millis //
	) {

		long value = year;

		value <<= MONTH_BITS;
		value |= month;

		value <<= DAY_BITS;
		value |= day;

		value <<= HOUR_BITS;
		value |= hour;

		value <<= MINUTE_BITS;
		value |= minute;

		value <<= SECOND_BITS;
		value |= second;

		value <<= MILLIS_BITS;
		value |= millis;

		return value;

	}

	public static int intoDecimalDateOnly(final DateOnlyValue value) {
		return intoDecimalDateOnly(value.getYear(), value.getMonth(),
				value.getDay());
	}

	public static int intoDecimalDateOnly(//
			final int year, //
			final int month, //
			final int day //
	) {
		return ((year * 100) + month) * 100 + day;
	}

	public static long intoDecimalDateTime(final DateTimeValue value) {
		return intoDecimalDateTime(//
				value.getYear(), //
				value.getMonth(), //
				value.getDay(), //
				value.getHour(), //
				value.getMinute(), //
				value.getSecond(), //
				value.getMillis() //
		);
	}

	public static long intoDecimalDateTime(//
			final int year, //
			final int month, //
			final int day, //
			final int hour, //
			final int minute, //
			final int second, //
			final int millis //
	) {
		return (((((year * 100L + month) * 100L + day) * 100L + hour) * 100L + minute) * 100L + second)
				* 1000L + millis;
	}

	private ProtoDateUtil() {
	}

}
