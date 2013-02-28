package com.barchart.proto.buf.inst;


public class InstrumentUtil {

	public static Month getMonth(int monthOfYear) {
		switch (monthOfYear) {
			case 1:
				return Month.JANUARY_MONTH;
			case 2:
				return Month.FEBRUARY_MONTH;
			case 3:
				return Month.MARCH_MONTH;
			case 4:
				return Month.APRIL_MONTH;
			case 5:
				return Month.MAY_MONTH;
			case 6:
				return Month.JUNE_MONTH;
			case 7:
				return Month.JULY_MONTH;
			case 8:
				return Month.AUGUST_MONTH;
			case 9:
				return Month.SEPTEMBER_MONTH;
			case 10:
				return Month.OCTOBER_MONTH;
			case 11:
				return Month.NOVEMBER_MONTH;
			case 12:
				return Month.DECEMBER_MONTH;
			default:
				throw new IllegalArgumentException("Illegal monthOfYear: " + monthOfYear);

		}
	}

	public static int getMonthOfYear(Month month) {
		switch (month) {
			case JANUARY_MONTH:
				return 1;
			case FEBRUARY_MONTH:
				return 2;
			case MARCH_MONTH:
				return 3;
			case APRIL_MONTH:
				return 4;
			case MAY_MONTH:
				return 5;
			case JUNE_MONTH:
				return 6;
			case JULY_MONTH:
				return 7;
			case AUGUST_MONTH:
				return 8;
			case SEPTEMBER_MONTH:
				return 9;
			case OCTOBER_MONTH:
				return 10;
			case NOVEMBER_MONTH:
				return 11;
			case DECEMBER_MONTH:
				return 12;
			default:
				throw new IllegalArgumentException("Illegal month: " + month);
		}
	}

	public static String getMonthCode(Month month) {
		switch (month) {
			case JANUARY_MONTH:
				return "F";
			case FEBRUARY_MONTH:
				return "G";
			case MARCH_MONTH:
				return "H";
			case APRIL_MONTH:
				return "J";
			case MAY_MONTH:
				return "K";
			case JUNE_MONTH:
				return "M";
			case JULY_MONTH:
				return "N";
			case AUGUST_MONTH:
				return "Q";
			case SEPTEMBER_MONTH:
				return "U";
			case OCTOBER_MONTH:
				return "V";
			case NOVEMBER_MONTH:
				return "X";
			case DECEMBER_MONTH:
				return "Z";
			default:
				throw new IllegalArgumentException("Illegal month: " + month);
		}
	}

}
