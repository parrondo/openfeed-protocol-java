package org.openfeed.util.prices;

import org.openfeed.InstrumentDefinition.Decimal;
import org.openfeed.InstrumentDefinition.PriceFormat;

public final class CompositePrice {

	private static final long MAX_DECIMAL_DENOMINATOR = 1000000000;

	private static final int MAX_DECIMAL_DENOMINATOR_EXPONENT = 9;

	private static final Decimal DECIMAL_ZERO = Decimal.newBuilder().setMantissa(0).setExponent(0).build();

	private final long wholePart;

	private final int mainNumerator;

	private final int subNumerator;

	private final PriceFormat format;

	private final boolean isNegative;

	public CompositePrice(long wholePart, int mainNumerator, int subNumerator, boolean isNegative, PriceFormat format) {
		this.wholePart = wholePart;
		this.mainNumerator = mainNumerator;
		this.subNumerator = subNumerator;
		this.isNegative = isNegative;
		this.format = format;
	}

	public long getWholeNumber() {
		return wholePart;
	}

	public long getMainNumerator() {
		return mainNumerator;
	}

	public int getSubNumerator() {
		return subNumerator;
	}

	public int getMainDenominator() {
		return format.getDenominator();
	}

	public int getMainPrecision() {
		return format.getPrecision();
	}

	public int getSubPrecision() {
		return format.getSubPrecision();
	}

	public int getSubDenominator() {
		return format.getSubDenominator();
	}

	public boolean isFractional() {
		return format.getIsFractional();
	}

	public boolean isNegative() {
		return isNegative;
	}

	public Decimal toDecimal() {
		int mainDenominator = format.getDenominator() == 0 ? 1 : format.getDenominator();
		int subDenominator = format.getSubDenominator() == 0 ? 1 : format.getSubDenominator();

		long combinedNumerator = (wholePart * mainDenominator * subDenominator) + (mainNumerator * subDenominator) + subNumerator;

		long combinedDenominator = mainDenominator * subDenominator;

		long mantissa = (combinedNumerator * MAX_DECIMAL_DENOMINATOR) / combinedDenominator;
		int exponent = MAX_DECIMAL_DENOMINATOR_EXPONENT;
		while (mantissa % 10 == 0 & mantissa > 0) {
			mantissa /= 10;
			exponent--;
		}

		mantissa = isNegative ? -mantissa : mantissa;
		Decimal decimal = Decimal.newBuilder().setMantissa(mantissa).setExponent(-exponent).build();

		return decimal;
	}

	public double toDouble() {
		Decimal decimal = toDecimal();
		return decimal.getMantissa() * Math.pow(10, decimal.getExponent());
	}

	public long getBase10Mantissa() {
		return 0L;
	}

	public int getBase10Exponent() {
		return 1;
	}

	@Override
	public String toString() {
		String str = wholePart + " + (" + mainNumerator + " + (" + subNumerator + " / " + format.getSubDenominator() + ")) / " + format.getDenominator() + "";
		if (isNegative) {
			return "-(" + str + ")";
		} else {
			return str;
		}
	}

	public static CompositePrice fromBase10(long mantissa, int exponent, PriceFormat format) {
		while (exponent > 0) {
			mantissa = mantissa * 10;
			exponent--;
		}
		boolean isNegative = mantissa < 0;
		mantissa = Math.abs(mantissa);
		final long fractionMask = tenPow(-exponent);
		long wholePart = mantissa / fractionMask;

		long partialPart = Math.abs(mantissa % fractionMask);
		long extendedFraction = partialPart * format.getDenominator();

		int mainNumerator = (int) (extendedFraction / fractionMask);

		long secondaryPartial = extendedFraction % fractionMask;
		int subNumerator = (int) ((secondaryPartial * format.getSubDenominator()) / fractionMask);

		return new CompositePrice(wholePart, mainNumerator, subNumerator, isNegative, format);
	}

	/**
	 * extracts floating point parts from a double value in decimal
	 * representation;
	 * 
	 * approximation; works correct till about 14 decimal digits in mantissa;
	 * 
	 * suitable for double values parsed from string representations like
	 * MMMMMM.EEEEE which does not use "E" exponent notation and are safe for
	 * double round-trip conversions
	 * 
	 * @return floating point parts in decimal representation, normalized;
	 */
	public static CompositePrice fromDouble(double value, PriceFormat format) {
		/**
		 * Translate the double into sign, exponent and mantissa, according to
		 * the formulae in JLS, Section 20.10.22.
		 */

		if (value == 0.0) {
			return fromBase10(0, 0, format);
		}

		final long bits = Double.doubleToLongBits(value);

		final int sign = ((bits >> 63) == 0 ? 1 : -1);

		int binaryExponent = (int) ((bits >> 52) & 0x7ffL);

		final long binaryMantissa = (binaryExponent == 0 ? (bits & ((1L << 52) - 1)) << 1 : (bits & ((1L << 52) - 1)) | (1L << 52));

		binaryExponent -= 1075;

		/** value == sign * mantissa * 2 ^ exponent */

		/** uses in-place mantissa transform */

		long decimalMantissa = binaryMantissa;
		int decimalExponent = 0;

		/** replace binary with decimal */
		while (binaryExponent < 0) {
			if (decimalMantissa < Long.MAX_VALUE / 5) {
				/** divide by 2, multiply by 10 */
				decimalMantissa *= 5;
				decimalExponent -= 1;
				binaryExponent += 1;
			} else {
				/** divide by 2 */
				decimalMantissa >>= 1;
				binaryExponent += 1;
			}
		}

		/** replace binary with decimal */
		while (binaryExponent > 0) {
			if (decimalMantissa < Long.MAX_VALUE / 5) {
				/** multiply by 2 */
				decimalMantissa <<= 1;
				binaryExponent -= 1;
			} else {
				/** multiply by 2, divide by 10 */
				decimalMantissa /= 5;
				decimalExponent += 1;
				binaryExponent -= 1;
			}
		}

		assert binaryExponent == 0;

		/** round about decimal tails like ...00123 or ...99876 */

		final long truncate = (decimalMantissa / LIMIT) * LIMIT;
		final long distance = (decimalMantissa - truncate);

		assert distance >= 0;

		if (BASIS < distance && distance < BELOW) {
			decimalMantissa -= distance;
		}
		if (ABOVE < distance && distance < LIMIT) {
			decimalMantissa += LIMIT - distance;
		}

		/** normalize decimal representation */

		while (decimalMantissa % 10 == 0) {
			decimalMantissa /= 10;
			decimalExponent += 1;
		}

		/**  */

		if (sign < 0) {
			decimalMantissa = -decimalMantissa;
		}

		return fromBase10(decimalMantissa, decimalExponent, format);

	}

	private static long tenPow(int exponent) {
		switch (exponent) {
		case 0:
			return 1;
		case 1:
			return 10;
		case 2:
			return 100;
		case 3:
			return 1000;
		case 4:
			return 10000;
		case 5:
			return 100000;
		case 6:
			return 1000000;
		case 7:
			return 10000000;
		case 8:
			return 100000000;
		default:
			throw new IllegalArgumentException("Bad exponent: " + exponent);
		}
	}

	private static final long BASIS = 0L;
	private static final long LIMIT = 10000L;
	private static final long BELOW = LIMIT / 10L;
	private static final long ABOVE = LIMIT - LIMIT / 10L;

}
