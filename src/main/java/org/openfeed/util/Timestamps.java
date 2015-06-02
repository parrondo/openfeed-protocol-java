package org.openfeed.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openfeed.util.datetime.ProtoDateUtil;

public class Timestamps {
	
	public long currentDecimalDateTime() {
		return toDecimalDateTime(DateTime.now());
	}

	public long toDecimalDateTime(DateTime dateTime) {
		return ProtoDateUtil.fromJodaDateTimeToDecimalDateTime(dateTime);
	}
	
	public DateTime fromDecimalDateTime(long decimalDateTime) {
		return fromDecimalDateTime(decimalDateTime, DateTimeZone.UTC);
	}
	
	public DateTime fromDecimalDateTime(long decimalDateTime, DateTimeZone zone) {
		return ProtoDateUtil.fromDecimalDateTimeToJoda(decimalDateTime, zone);
	}
	
}
