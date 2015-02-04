package org.openfeed.util.prices;

import org.openfeed.InstrumentDefinition.Decimal;
import org.openfeed.InstrumentDefinition.PriceFormat;

public abstract class PriceFormatter {

	public static final PriceFormatter STANDARD = new StandardPriceFormatter();

	protected PriceFormatter() {

	}

	public abstract CompositePrice parse(String displayValue, PriceFormat format);

	public abstract String display(CompositePrice price);

	public final double parseToDouble(String displayValue, PriceFormat format) {
		CompositePrice price = parse(displayValue, format);
		return price.toDouble();
	}

	public final Decimal parseToDecimal(String displayPrice, PriceFormat format) {
		CompositePrice price = parse(displayPrice, format);
		return price.toDecimal();
	}

	public final String display(double value, PriceFormat format) {
		CompositePrice price = CompositePrice.fromDouble(value, format);
		return display(price);
	}

	public final String display(long mantissa, int exponent, PriceFormat format) {
		CompositePrice price = CompositePrice.fromBase10(mantissa, exponent, format);
		return display(price);
	}

	private static final class StandardPriceFormatter extends PriceFormatter {

		private final String MAIN_FRACTIONAL_SEPARATOR = "-";

		private final String MAIN_DECIMAL_SEPARATOR = ".";

		private final String SEPARATORS_REGEX = "-|\\.";

		@Override
		public String display(CompositePrice price) {
			StringBuilder builder = new StringBuilder();
			if (price.isNegative()) {
				builder.append("-");
			}
			builder.append(price.getWholeNumber());
			if (price.getMainPrecision() > 0) {
				if (price.isFractional()) {
					builder.append(MAIN_FRACTIONAL_SEPARATOR);
				} else {
					builder.append(MAIN_DECIMAL_SEPARATOR);
				}

				
				appendMainFraction(builder, price.getMainNumerator(), price.getMainPrecision());
				
				if (price.getSubDenominator() > 0) {
					appendExactDigits(builder, convertToDecimal(price.getSubNumerator(), price.getSubDenominator()), price.getSubPrecision());
				}
			}
			return builder.toString();
		}

		private void appendMainFraction(StringBuilder builder, final long mainNumerator, final int mainPrecision) {
			String str = Long.toString(mainNumerator);
			str = truncate(Long.toString(mainNumerator), mainPrecision);
			str = padLeftWithZeros(str, mainPrecision);
			builder.append(str);
		}

		private String truncate(String string, int mainPrecision) {
			return string.substring(0, Math.min(mainPrecision, string.length()));
		}

		private String padLeftWithZeros(String string, int length) {
			if (string.length() >= length) {
				return string;
			}
			StringBuilder builder = new StringBuilder(length);
			for (int i = string.length(); i < length; i++) {
				builder.append('0');
			}
			builder.append(string);
			return builder.toString();
		}

		private int convertToDecimal(int numerator, int denominator) {
			switch (denominator) {
			case 1: {
				switch (numerator) {
				case 0:
					return 0;
				}
			}
			case 2: {
				switch (numerator) {
				case 0:
					return 0;
				case 1:
					return 5;
				}
			}
			case 4: {
				switch (numerator) {
				case 0:
					return 0;
				case 1:
					return 25;
				case 2:
					return 50;
				case 3:
					return 75;
				}
			}
			case 8: {
				switch (numerator) {
				case 0:
					return 0;
				case 1:
					return 125;
				case 2:
					return 250;
				case 3:
					return 375;
				case 4:
					return 500;
				case 5:
					return 625;
				case 6:
					return 750;
				case 7:
					return 875;
				}
			}
			}
			throw new IllegalArgumentException("Cannot figure out decimal representation for " + numerator + " / " + denominator);
		}

		@Override
		public CompositePrice parse(String displayValue, PriceFormat format) {
			boolean isNegative = false;
			if (displayValue.startsWith("-")) {
				displayValue = displayValue.substring(1);
				isNegative = true;
			}
			String[] parts = displayValue.split(SEPARATORS_REGEX);
			final String fractionalString = parts.length > 1 ? parts[1] : "";
			long wholePart = Long.parseLong(parts[0]);
			int mainNumerator = getMainNumerator(fractionalString, format);
			int subNumerator = getSubNumerator(fractionalString, format);
			return new CompositePrice(wholePart, mainNumerator, subNumerator, isNegative, format);
		}

		private int getMainNumerator(String fractionalString, PriceFormat format) {
			int precision = format.getPrecision();
			if (precision == 0) {
				return 0;
			} else {
				String mainNumeratorString = fractionalString.substring(0, precision);
				return Integer.parseInt(mainNumeratorString);
			}
		}

		private int getSubNumerator(String fractionalString, PriceFormat format) {
			int precision = format.getSubPrecision();
			if (precision == 0) {
				return 0;
			} else {
				// Only decimal representation of sub format is supported
				int mainPrecession = format.getPrecision();
				String possiblyTruncatedDecimalSubNumeratorString = fractionalString.substring(mainPrecession);
				int subNumerator = findSubNumerator(format.getSubDenominator(), possiblyTruncatedDecimalSubNumeratorString);
				return subNumerator;
			}
		}

		private int findSubNumerator(int subDenominator, String possiblyTruncatedDecimalSubNumeratorString) {
			int numerator = 0;
			while (numerator < subDenominator) {
				String stringRepresentation = Double.toString(numerator / (double) subDenominator).substring(2);
				if (stringRepresentation.startsWith(possiblyTruncatedDecimalSubNumeratorString)) {
					return numerator;
				} else {
					numerator++;
				}
			}
			throw new IllegalStateException("Could not find numerator for denominator " + subDenominator + " string: " + possiblyTruncatedDecimalSubNumeratorString);
		}

		private void appendExactDigits(StringBuilder builder, long value, int precision) {
			String str = Long.toString(value);
			for (int i = 0; i < precision; i++) {
				if (i < str.length()) {
					builder.append(str.charAt(i));
				} else {
					builder.append('0');
				}
			}
		}

	}

}
