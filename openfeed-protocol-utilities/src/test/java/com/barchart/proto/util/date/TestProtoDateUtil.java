/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.proto.util.date;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestProtoDateUtil {

	private final static Logger log = LoggerFactory
			.getLogger(TestProtoDateUtil.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEqualsDateOnly() {

		final DateOnlyValue source = new DateOnlyValue(2012, 12, 31);

		final DateOnlyValue target = new DateOnlyValue(2012, 12, 31);

		assertEquals(source, target);

	}

	@Test
	public void testEqualsDateTime() {

		final DateTimeValue source = new DateTimeValue(2012, 12, 31, 23, 59,
				59, 999);

		final DateTimeValue target = new DateTimeValue(2012, 12, 31, 23, 59,
				59, 999);

		assertEquals(source, target);

	}

	@Test
	public void testDecimalDateOnly() {

		final DateOnlyValue source = new DateOnlyValue(2012, 12, 31);

		log.debug("source : {}", source);

		final int encoded = ProtoDateUtil.intoDecimalDateOnly(source);

		final DateOnlyValue target = ProtoDateUtil.fromDecimalDateOnly(encoded);

		log.debug("target : {}", target);

		assertEquals(source, target);

	}

	@Test
	public void testDecimalDateTime() {

		final DateTimeValue source = new DateTimeValue(2012, 12, 31, 23, 59,
				59, 999);

		log.debug("source : {}", source);

		final long encoded = ProtoDateUtil.intoDecimalDateTime(source);

		final DateTimeValue target = ProtoDateUtil.fromDecimalDateTime(encoded);

		log.debug("target : {}", target);

		assertEquals(source, target);

	}

	@Test
	public void testBinaryDateTime() {

		final DateTimeValue source = new DateTimeValue(2012, 12, 31, 23, 59,
				59, 999);

		log.debug("source : {}", source);

		final long encoded = ProtoDateUtil.intoBinaryDateTime(source);

		final DateTimeValue target = ProtoDateUtil.fromBinaryDateTime(encoded);

		log.debug("target : {}", target);

		assertEquals(source, target);

	}

	@Test
	public void testBinaryDateOnly() {

		final DateOnlyValue source = new DateOnlyValue(2012, 12, 31);

		log.debug("source : {}", source);

		final int encoded = ProtoDateUtil.intoBinaryDateOnly(source);

		final DateOnlyValue target = ProtoDateUtil.fromBinaryDateOnly(encoded);

		log.debug("target : {}", target);

		assertEquals(source, target);

	}

	@Test
	public void testDateOnlySize() {

		final int fixDate = 20121231;

		final int fixBytes = ProtoDateUtil.countBytes(fixDate);

		log.debug("fixDate : {}", fixDate);
		log.debug("fixDate bytes : {}", fixBytes);

		assertEquals(fixBytes, 4);

		final int bufDate = ProtoDateUtil.intoBinaryDateOnly(new DateOnlyValue(
				2012, 12, 31));

		final int bufBytes = ProtoDateUtil.countBytes(bufDate);

		log.debug("bufDate : {}", bufDate);
		log.debug("bufDate bytes : {}", bufBytes);

		assertEquals(bufBytes, 3);

	}

	@Test
	public void testDateTimeSize() {

		final long fixDate = 20121231235959123L;

		final int fixBytes = ProtoDateUtil.countBytes(fixDate);

		log.debug("fixDate : {}", fixDate);
		log.debug("fixDate bytes : {}", fixBytes);

		assertEquals(fixBytes, 7);

		//

		final long bufDate = ProtoDateUtil
				.intoBinaryDateTime(new DateTimeValue(2012, 12, 31, 23, 59, 59,
						123));

		final int bufBytes = ProtoDateUtil.countBytes(bufDate);

		log.debug("bufDate : {}", bufDate);
		log.debug("bufDate bytes : {}", bufBytes);

		assertEquals(bufBytes, 6);

		//

		final long utcDate = System.currentTimeMillis();
		final int utcBytes = ProtoDateUtil.countBytes(utcDate);

		log.debug("utcDate : {}", utcDate);
		log.debug("utcBytes bytes : {}", utcBytes);

		assertEquals(utcBytes, 6);

	}

}
