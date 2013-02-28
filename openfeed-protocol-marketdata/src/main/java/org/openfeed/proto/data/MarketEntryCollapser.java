/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package org.openfeed.proto.data;

import java.util.ArrayList;
import java.util.List;

public class MarketEntryCollapser {

	public MarketEntryCollapser() {

	}

	public List<MarketEntry> collapse(final MarketMessage marketMessage) {
		final List<MarketEntry> list = new ArrayList<MarketEntry>(
				marketMessage.getEntryCount());
		final MarketEntry defaultValues = makeDefaultValuesForMarketEntries(marketMessage);
		for (final MarketEntry entry : marketMessage.getEntryList()) {
			final MarketEntry.Builder newBuilder = MarketEntry
					.newBuilder(entry);
			newBuilder.mergeFrom(defaultValues);
			list.add(newBuilder.build());
		}
		return list;
	}

	private MarketEntry makeDefaultValuesForMarketEntries(
			final MarketMessage marketMessage) {

		final MarketEntry.Builder newBuilder = MarketEntry.newBuilder();

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
