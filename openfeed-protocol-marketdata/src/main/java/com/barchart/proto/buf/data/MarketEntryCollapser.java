/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.buf.data;

import java.util.ArrayList;
import java.util.List;

import com.barchart.proto.buf.data.MarketEntry;
import com.barchart.proto.buf.data.MarketMessage;

public class MarketEntryCollapser {

	public MarketEntryCollapser() {

	}

	public List<MarketEntry> collapse(MarketMessage marketMessage) {
		List<MarketEntry> list = new ArrayList<MarketEntry>(marketMessage.getEntryCount());
		MarketEntry defaultValues = makeDefaultValuesForMarketEntries(marketMessage);
		for (MarketEntry entry : marketMessage.getEntryList()) {
			MarketEntry.Builder newBuilder = MarketEntry.newBuilder(entry);
			newBuilder.mergeFrom(defaultValues);
			list.add(newBuilder.build());
		}
		return list;
	}

	private MarketEntry makeDefaultValuesForMarketEntries(MarketMessage marketMessage) {

		MarketEntry.Builder newBuilder = MarketEntry.newBuilder();

		if (marketMessage.hasMarketId()) {
			newBuilder.setMarketId(marketMessage.getMarketId());
		}

		if (marketMessage.hasPriceExponent()) {
			newBuilder.setPriceExponent(marketMessage.getPriceExponent());
		}

		if (marketMessage.hasSizeExponent()) {
			newBuilder.setSizeExponent(marketMessage.getSizeExponent());
		}

		if (marketMessage.hasTradeDate()) {
			newBuilder.setTradeDate(marketMessage.getTradeDate());
		}

		if (marketMessage.hasTimeStamp()) {
			newBuilder.setTimeStamp(marketMessage.getTimeStamp());
		}

		if (marketMessage.hasSequence()) {
			newBuilder.setSequence(marketMessage.getSequence());
		}

		return newBuilder.build();

	}

}
