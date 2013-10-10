/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package org.openfeed.proto.feed.session;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.CodedInputStream;

/**
 * 
 */
public class FeedSessionCodec {

	private static final Logger log = LoggerFactory
			.getLogger(FeedSessionCodec.class);

	/**
	 * 
	 */
	public static List<FeedSessionMessage> decodePacket(
			final CodedInputStream input) throws Exception {

		final FeedSessionPacket packet = FeedSessionPacket.PARSER
				.parseFrom(input);

		final PacketType type = packet.getType();

		switch (type) {
		case FEED_SESSION:
			break;
		default:
			log.error("Unexpected packet type {}", type);
			return Collections.emptyList();
		}

		return packet.getMessageList();

	}

	private FeedSessionCodec() {
	}

}
