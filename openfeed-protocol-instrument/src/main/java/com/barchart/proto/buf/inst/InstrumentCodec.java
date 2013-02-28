/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.buf.inst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

/**
 * Instrument definition message codec.
 */
public class InstrumentCodec {

	private static final Logger log = LoggerFactory
			.getLogger(InstrumentCodec.class);

	private InstrumentCodec() {

	}

	public static InstrumentDefinition decode(final ByteString data)
			throws Exception {

		return InstrumentDefinition.newBuilder().mergeFrom(data).build();

	}

	public static <MESSAGE extends Message> InstrumentDefinition.Builder encode(
			final MESSAGE message) {

		final InstrumentDefinition.Builder builder = InstrumentDefinition
				.newBuilder();

		if (message == null) {
			log.warn("missing message", new NullPointerException("message"));
			return builder;
		}

		return builder;

	}

}
