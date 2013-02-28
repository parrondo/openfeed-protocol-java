/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.buf.data;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.proto.buf.data.MarketEntry;
import com.barchart.proto.buf.data.MarketMessage;

/**
 * does message compression per {@link MessageRules}
 **/
public final class MessageOptimizer {

	private MessageOptimizer() {
	}

	private static final class CalcInt {

		private int hi = Integer.MIN_VALUE;
		private int lo = Integer.MAX_VALUE;

		void apply(final int value) {
			if (value > hi) {
				hi = value;
			}
			if (value < lo) {
				lo = value;
			}
		}

		@SuppressWarnings("unused")
		int getHi() {
			return hi;
		}

		int getLo() {
			return lo;
		}

		int getRange() {
			return hi - lo;
		}

		boolean isValid() {
			return lo <= hi;
		}

		int offLo(final int value) {
			return value - lo;
		}

		boolean shouldCompress(final int range) {
			return isValid() && getRange() <= range;
		}

	}

	private static final class CalcLong {

		private long hi = Long.MIN_VALUE;
		private long lo = Long.MAX_VALUE;

		void apply(final long value) {
			if (value > hi) {
				hi = value;
			}
			if (value < lo) {
				lo = value;
			}
		}

		@SuppressWarnings("unused")
		long getHi() {
			return hi;
		}

		long getLo() {
			return lo;
		}

		long getRange() {
			return hi - lo;
		}

		boolean isValid() {
			return lo <= hi;
		}

		long offLo(final long value) {
			return value - lo;
		}

		boolean shouldCompress(final long range) {
			return isValid() && getRange() <= range;
		}

	}

	static final Logger log = LoggerFactory.getLogger(MessageOptimizer.class);

	public static void pack(final MarketMessage.Builder message,
			final List<MarketEntry.Builder> entryList) {

		if (!message.hasType()) {
			return;
		}

		switch (message.getType()) {
		case UPDATE:
			packUpdate(message, entryList);
			return;
		case SNAPSHOT:
			packSnapshot(message, entryList);
			return;
		default:
			return;
		}

	}

	private static void packPriceExp(final MarketEntry.Builder entry,
			final int priceExpLO) {

		long mantissa = entry.getPriceMantissa();
		int exponent = entry.getPriceExponent();

		while (exponent > priceExpLO) {
			exponent--;
			mantissa *= 10;
		}

		entry.setPriceMantissa(mantissa);
		entry.clearPriceExponent();

	}

	private static void packSizeExp(final MarketEntry.Builder entry,
			final int sizeExpLO) {

		long mantissa = entry.getSizeMantissa();
		int exponent = entry.getSizeExponent();

		while (exponent > sizeExpLO) {
			exponent--;
			mantissa *= 10;
		}

		entry.setSizeMantissa(mantissa);
		entry.clearSizeExponent();

	}

	/**
	 * snapshots have same : market id, trade date, time stamp
	 * 
	 * snapshots should have similar : price, size
	 */
	private static void packSnapshot(final MarketMessage.Builder message,
			final List<MarketEntry.Builder> entryList) {

		final CalcInt calcPriceExp = new CalcInt();
		final CalcInt calcSizeExp = new CalcInt();

		for (final MarketEntry.Builder entry : entryList) {

			if (entry.hasPriceMantissa()) {
				calcPriceExp.apply(entry.getPriceExponent());
			}

			if (entry.hasSizeMantissa()) {
				calcSizeExp.apply(entry.getSizeExponent());
			}

		}

		final boolean doPriceExp = calcPriceExp.isValid();
		final boolean doSizeExp = calcSizeExp.isValid();

		final int priceExpLO = calcPriceExp.getLo();
		final int sizeExpLO = calcSizeExp.getLo();

		for (final MarketEntry.Builder entry : entryList) {

			if (doPriceExp && entry.hasPriceMantissa()) {
				packPriceExp(entry, priceExpLO);
			}

			if (doSizeExp && entry.hasSizeMantissa()) {
				packSizeExp(entry, sizeExpLO);
			}

		}

		if (doPriceExp) {
			message.setPriceExponent(priceExpLO);
		}

		if (doSizeExp) {
			message.setSizeExponent(sizeExpLO);
		}

	}

	/**
	 * updates have different : market id, price, size
	 * 
	 * updates should have similar : trade date, time stamp
	 */
	private static void packUpdate(final MarketMessage.Builder message,
			final List<MarketEntry.Builder> entryList) {

		final CalcInt calcTradeDate = new CalcInt();
		final CalcLong calcTimeStamp = new CalcLong();

		for (final MarketEntry.Builder entry : entryList) {

			if (entry.hasTradeDate()) {
				calcTradeDate.apply(entry.getTradeDate());
			}

			if (entry.hasTimeStamp()) {
				calcTimeStamp.apply(entry.getTimeStamp());
			}

		}

		final boolean doTradeDate = calcTradeDate.shouldCompress(100); // days
		final boolean doTimeStamp = calcTimeStamp.shouldCompress(1000); // millis

		for (final MarketEntry.Builder entry : entryList) {

			if (doTradeDate && entry.hasTradeDate()) {
				entry.setTradeDate(calcTradeDate.offLo(entry.getTradeDate()));
			}

			if (doTimeStamp && entry.hasTimeStamp()) {
				entry.setTimeStamp(calcTimeStamp.offLo(entry.getTimeStamp()));
			}

		}

		if (doTradeDate) {
			message.setTradeDate(calcTradeDate.getLo());
		}

		if (doTimeStamp) {
			message.setTimeStamp(calcTimeStamp.getLo());
		}

	}

}
