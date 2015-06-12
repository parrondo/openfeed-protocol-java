package org.openfeed.util.prices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.openfeed.InstrumentDefinition.PriceFormat;
import org.openfeed.InstrumentDefinition.PriceFormat.SubFormat;

public class CompositePriceTest {

	private static CompositePrice price;

	@Test
	public void fromBase10Decimal2RoudUp() {

		PriceFormat pf = PriceFormat.newBuilder() //
				.setIsFractional(false) //
				.setDenominator(100) //
				.setPrecision(2) //
				.setSubFormat(SubFormat.FLAT) //
				.setSubDenominator(1) //
				.setSubPrecision(0) //
				.build();

		price = CompositePrice.fromBase10(4909789L, -4, pf);

		assertEquals(490, price.getWholeNumber());
		assertEquals("Rounding should occur here for the PriceFormat precision.", 98, price.getMainNumerator());
		assertEquals("PriceFormat.getDeominator", 100, price.getMainDenominator());
		assertEquals("Not used", 0, price.getSubNumerator());
		assertFalse(price.isNegative());

	}

	@Test
	public void fromBase10Decimal2RoundDown() {

		PriceFormat pf = PriceFormat.newBuilder() //
				.setIsFractional(false) //
				.setDenominator(100) //
				.setPrecision(2) //
				.setSubFormat(SubFormat.FLAT) //
				.setSubDenominator(1) //
				.setSubPrecision(0) //
				.build();

		price = CompositePrice.fromBase10(4909729L, -4, pf);
		assertEquals(490, price.getWholeNumber());
		assertEquals("Rounding should occur here for the PriceFormat precision.", 97, price.getMainNumerator());
	}

	@Test
	public void fromBase10Decimal2NoRounding() {

		PriceFormat pf = PriceFormat.newBuilder() //
				.setIsFractional(false) //
				.setDenominator(100) //
				.setPrecision(2) //
				.setSubFormat(SubFormat.FLAT) //
				.setSubDenominator(1) //
				.setSubPrecision(0) //
				.build();

		price = CompositePrice.fromBase10(4909700L, -4, pf);
		assertEquals(490, price.getWholeNumber());
		assertEquals("No rounding.", 97, price.getMainNumerator());
		price = CompositePrice.fromBase10(4909701L, -4, pf);
		assertEquals(490, price.getWholeNumber());
		assertEquals("No rounding.", 97, price.getMainNumerator());
	}

	@Test
	public void fromBase10Decimal2RoundUpHalfWay() {

		PriceFormat pf = PriceFormat.newBuilder() //
				.setIsFractional(false) //
				.setDenominator(100) //
				.setPrecision(2) //
				.setSubFormat(SubFormat.FLAT) //
				.setSubDenominator(1) //
				.setSubPrecision(0) //
				.build();

		price = CompositePrice.fromBase10(4909750L, -4, pf);
		assertEquals(490, price.getWholeNumber());
		assertEquals("5 are rounded up", 98, price.getMainNumerator());
		price = CompositePrice.fromBase10(4909701L, -4, pf);
	}

}
