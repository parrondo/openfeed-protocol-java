package org.openfeed.util.prices;

import org.junit.Assert;
import org.junit.Test;
import org.openfeed.InstrumentDefinition.PriceFormat;
import org.openfeed.InstrumentDefinition.PriceFormat.SubFormat;
import org.openfeed.util.prices.PriceFormatter;

public class PriceFormatterTest {

	private static final PriceFormatter formatter = PriceFormatter.STANDARD;

	private static final double DELTA = 0.0000000001;

	@Test
	public void testZeroDecimalPlaces() {
		PriceFormat format = newPriceFormat(false, 0, 0, 0, 0);
		check(format, "98765", 98765);
	}

	@Test
	public void testZeroDecimalPlacesNegative() {
		PriceFormat format = newPriceFormat(false, 0, 0, 0, 0);
		check(format, "-98765", -98765);
	}

	@Test
	public void testZeroDecimalPlacesZero() {
		PriceFormat format = newPriceFormat(false, 0, 0, 0, 0);
		check(format, "0", 0.0);
	}

	@Test
	public void testOneDecimalPlace() {
		PriceFormat format = newPriceFormat(false, 10, 1, 0, 0);
		check(format, "1.1", 1.1);
	}

	@Test
	public void testOneDecimalPlaceNegative() {
		PriceFormat format = newPriceFormat(false, 10, 1, 0, 0);
		check(format, "-1.1", -1.1);
	}

	@Test
	public void testOneDecimalPlaceZero() {
		PriceFormat format = newPriceFormat(false, 10, 1, 0, 0);
		check(format, "0.0", 0);
	}

	@Test
	public void testTwoDecimalPlaces() {
		PriceFormat format = newPriceFormat(false, 100, 2, 0, 0);
		check(format, "25.12", 25.12);
	}

	@Test
	public void testTwoDecimalPlacesNegative() {
		PriceFormat format = newPriceFormat(false, 100, 2, 0, 0);
		check(format, "-25.12", -25.12);
	}

	@Test
	public void testTwoDecimalPlacesZero() {
		PriceFormat format = newPriceFormat(false, 100, 2, 0, 0);
		check(format, "0.00", 0.0);
	}

	@Test
	public void testThreeDecimalPlaces() {
		PriceFormat format = newPriceFormat(false, 1000, 3, 0, 0);
		check(format, "199.125", 199.125);
	}

	@Test
	public void testThreeDecimalPlacesNegative() {
		PriceFormat format = newPriceFormat(false, 1000, 3, 0, 0);
		check(format, "-199.125", -199.125);
	}

	@Test
	public void testThreeDecimalPlacesZero() {
		PriceFormat format = newPriceFormat(false, 1000, 3, 0, 0);
		check(format, "0.000", 0.0);
	}

	@Test
	public void testFourDecimalPlaces() {
		PriceFormat format = newPriceFormat(false, 10000, 4, 0, 0);
		check(format, "199.1250", 199.125);
	}

	@Test
	public void testFiveDecimalPlaces() {
		PriceFormat format = newPriceFormat(false, 100000, 5, 0, 0);
		check(format, "199.12591", 199.12591);
	}

	@Test
	public void testSixDecimalPlaces() {
		PriceFormat format = newPriceFormat(false, 1000000, 6, 0, 0);
		check(format, "9.123456", 9.123456);
	}

	@Test
	public void testSevenDecimalPlaces() {
		PriceFormat format = newPriceFormat(false, 10000000, 7, 0, 0);
		check(format, "9.1234567", 9.1234567);
	}

	@Test
	public void testEightDecimalPlaces() {
		PriceFormat format = newPriceFormat(false, 100000000, 8, 0, 0);
		check(format, "9.12345678", 9.12345678);
	}

	@Test
	public void testLessThan1() {
		PriceFormat format = newPriceFormat(false, 1000, 3, 0, 0);
		check(format, "0.875", .875);
	}

	@Test
	public void testLessThan1Negative() {
		PriceFormat format = newPriceFormat(false, 1000, 3, 0, 0);
		check(format, "-0.875", -.875);
	}

	@Test
	public void test0Over32() {
		PriceFormat format = newPriceFormat(true, 32, 2, 2, 1);
		check(format, "109-000", 109.0);
	}

	@Test
	public void test1Over32() {
		PriceFormat format = newPriceFormat(true, 32, 2, 2, 1);
		check(format, "109-010", 109.03125);
	}

	@Test
	public void test16Over32() {
		PriceFormat format = newPriceFormat(true, 32, 2, 2, 1);
		check(format, "109-160", 109.5);
	}

	@Test
	public void test16Over32Negative() {
		PriceFormat format = newPriceFormat(true, 32, 2, 2, 1);
		check(format, "-109-160", -109.5);
	}

	@Test
	public void test16Point5Over32() {
		PriceFormat format = newPriceFormat(true, 32, 2, 2, 1);
		check(format, "109-165", 109.515625);
	}

	@Test
	public void test0Over8() {
		PriceFormat format = newPriceFormat(true, 8, 1, 0, 0);
		check(format, "383-0", 383.0);
	}

	@Test
	public void test1Over8() {
		PriceFormat format = newPriceFormat(true, 8, 1, 0, 0);
		check(format, "383-1", 383.125);
	}

	@Test
	public void test2Over8() {
		PriceFormat format = newPriceFormat(true, 8, 1, 0, 0);
		check(format, "383-2", 383.25);
	}

	@Test
	public void test3Over8() {
		PriceFormat format = newPriceFormat(true, 8, 1, 0, 0);
		check(format, "383-3", 383.375);
	}

	@Test
	public void test4Over8() {
		PriceFormat format = newPriceFormat(true, 8, 1, 0, 0);
		check(format, "383-4", 383.5);
	}

	@Test
	public void test5Over8() {
		PriceFormat format = newPriceFormat(true, 8, 1, 0, 0);
		check(format, "383-5", 383.625);
	}

	@Test
	public void test6Over8() {
		PriceFormat format = newPriceFormat(true, 8, 1, 0, 0);
		check(format, "383-6", 383.75);
	}

	@Test
	public void test7Over8() {
		PriceFormat format = newPriceFormat(true, 8, 1, 0, 0);
		check(format, "383-7", 383.875);
	}

	private void check(PriceFormat format, String displayValue, double decimalValue) {
		checkParse(format, displayValue, decimalValue);
		checkDisplay(format, displayValue, decimalValue);
	}

	private void checkParse(PriceFormat format, String displayValue, double expected) {
		double actual = formatter.parseToDouble(displayValue, format);
		Assert.assertEquals(expected, actual, DELTA);
	}

	private void checkDisplay(PriceFormat format, String expected, double decimalValue) {
		String actual = formatter.display(decimalValue, format);
		Assert.assertEquals(expected, actual);
	}

	private PriceFormat newPriceFormat(boolean isFractional, int denominator, int precision, int subDenominator, int subPrecission) {
		return PriceFormat.newBuilder() //
				.setIsFractional(isFractional) //
				.setDenominator(denominator) //
				.setPrecision(precision) //
				.setSubFormat(SubFormat.DECIMAL) //
				.setSubDenominator(subDenominator) //
				.setSubPrecision(subPrecission) //
				.build();
	}
}
