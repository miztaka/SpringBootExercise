package jp.honestyworks.demo.lightning_talk.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class ExtDateUtil {
	
	public static LocalDateTime toLocalDateTime(Date dt) {
		return LocalDateTime.ofInstant(dt.toInstant(), ZoneId.systemDefault());
	}
	
	public static Date toDate(LocalDateTime ldt) {
		ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
		return Date.from(zdt.toInstant());
	}
	
	public static Date toTruncatedDate(LocalDateTime ldt) {
		return DateUtils.truncate(toDate(ldt), Calendar.DATE);
	}

}
