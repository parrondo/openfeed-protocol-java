/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.buf.data;

import com.barchart.proto.buf.data.MarketEntryOrBuilder;
import com.barchart.proto.buf.data.MarketMessageOrBuilder;

/**
 * special contract on message/entry relationship
 * 
 * helps additional compression and program flow
 */
public final class MessageRules {

	/** entry overrides message; 0 is default */
	public static long getMarketId(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		if (entry.hasMarketId()) {
			return entry.getMarketId();
		}

		if (message.hasMarketId()) {
			return message.getMarketId();
		}

		return 0;

	}

	/** entry overrides message; 0 is default */
	public static int getPriceExponent(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		if (entry.hasPriceExponent()) {
			return entry.getPriceExponent();
		}

		if (message.hasPriceExponent()) {
			return message.getPriceExponent();
		}

		return 0;

	}

	/** entry is offset to message; 0 is default */
	public static long getSequence(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		return message.getSequence() + entry.getSequence();

	}

	/** entry overrides message; 0 is default */
	public static int getSizeExponent(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		if (entry.hasSizeExponent()) {
			return entry.getSizeExponent();
		}

		if (message.hasSizeExponent()) {
			return message.getSizeExponent();
		}

		return 0;

	}

	/** entry is offset to message; 0 is default */
	public static long getTimeStamp(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		return message.getTimeStamp() + entry.getTimeStamp();

	}

	/** entry is offset to message; 0 is default */
	public static int getTradeDate(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		return message.getTradeDate() + entry.getTradeDate();

	}

	/** message or entry market id must be present */
	public static boolean hasMarketId(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		return message.hasMarketId() || entry.hasMarketId();

	}

	/** entry price mantissa must be present */
	public static boolean hasPrice(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		return entry.hasPriceMantissa();

	}

	/** message or entry market trade date must be present */
	public static boolean hasSequence(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		return message.hasSequence() || entry.hasSequence();

	}

	/** entry size mantissa must be present */
	public static boolean hasSize(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		return entry.hasSizeMantissa();

	}

	/** message or entry market time stamp must be present */
	public static boolean hasTimeStamp(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		return message.hasTimeStamp() || entry.hasTimeStamp();

	}

	/** message or entry market trade date must be present */
	public static boolean hasTradeDate(final MarketMessageOrBuilder message,
			final MarketEntryOrBuilder entry) {

		return message.hasTradeDate() || entry.hasTradeDate();

	}

	private MessageRules() {
	}

}
