/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package org.openfeed.proto.inst;

import org.junit.Test;
import org.openfeed.proto.inst.BookLiquidity;
import org.openfeed.proto.inst.BookStructure;
import org.openfeed.proto.inst.InstrumentType;
import org.openfeed.proto.inst.OptionStyle;
import org.openfeed.proto.inst.OptionType;
import org.openfeed.proto.inst.SpreadType;

import util.EnumUtil;

public class TestUniqEnums {

	@Test
	public void testBookLiquidity() throws Exception {
		EnumUtil.testUnique(BookLiquidity.class);
	}

	@Test
	public void testBookStructure() throws Exception {
		EnumUtil.testUnique(BookStructure.class);
	}

	@Test
	public void testInstType() throws Exception {
		EnumUtil.testUnique(InstrumentType.class);
	}

	@Test
	public void testOptionStyle() throws Exception {
		EnumUtil.testUnique(OptionStyle.class);
	}

	@Test
	public void testOptionType() throws Exception {
		EnumUtil.testUnique(OptionType.class);
	}

	@Test
	public void testSpreadType() throws Exception {
		EnumUtil.testUnique(SpreadType.class);
	}

}
