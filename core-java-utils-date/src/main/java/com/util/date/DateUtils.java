package com.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

public class DateUtils {
	public static final String DATE_PATTERN = "dd-MM-yyyy";
	public static final String DATE_PATTERN_CARD = "MM/yy";
	public static final String DATE_PATTERN_MONTH_YEAR = "MM-yyyy";
	public static final String DATE_ORACLE_PATTERN = "yyyy-MM-dd";
	public static final String DATE_TIME_ORACLE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm";
	public static final String DATE_TIME_PATTERN_SLASH = "dd/MM/yyyy HH:mm";
	public static final String DATE_TIME_INTERNATIONAL_PATTERN = "yyyyMMdd HHmm";
	public static final String DATE_INTERNATIONAL_PATTERN = "yyyyMMdd";
	public static final String DATE_PATTERN_SLASH = "dd/MM/yyyy";
	public static final String TIME = "HH:mm";
	public static final String TIME_WITH_SEC = "HH:mm:ss";
	public static final String TIME_NOSPACE = "HHmmss";
	public static final String DATE_TIME_SLASH_PATTERN = "dd/MM/yyyy HH:mm";
	public static final String ISO_DATE = "yyyy-MM-dd";
	public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	public DateUtils() {
	}

	public static boolean isDateNowInRange(LocalDate from, LocalDate to) {
		boolean result = true;
		LocalDate now = LocalDate.now();
		if (now.isBefore(from) || now.isAfter(to)) {
			result = false;
		}

		return result;
	}

	public static boolean is18years(LocalDate toCheckDate) {
		if (toCheckDate == null) {
			return false;
		} else {
			LocalDate today = LocalDate.now();
			Period p = Period.between(toCheckDate, today);
			return p.getYears() >= 18;
		}
	}

	public static boolean after(LocalDate date) {
		return date.isAfter(LocalDate.now());
	}

	public static LocalDateTime fromDateToLocalDateTime(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	public static LocalDate fromDateToLocalDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
	}

	public static Date fromLocalDateTimeToDate(LocalDateTime ldt) {
		Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

	public static Date fromLocalDateToDate(LocalDate ld) {
		Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

	public static Date fromLocalTimeToDate(LocalTime lt) {
		Instant instant = lt.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

	public static LocalDateTime resetTime(LocalDateTime ldt) {
		return ldt.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

	public static LocalDateTime resetTime(LocalDate ld) {
		return ld.atTime(0, 0, 0, 0);
	}

	public static LocalDateTime fillTime(LocalDateTime ldt) {
		return ldt.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
	}

	public static LocalDateTime fillTime(LocalDate ld) {
		return ld.atTime(23, 59, 59, 999999999);
	}

	public static String format(LocalDate ld, String pattern) {
		return ld.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String format(LocalDateTime ldt, String pattern) {
		return ldt.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String format(LocalTime lt, String pattern) {
		return lt.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String formatNow(String pattern) {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	public static LocalDateTime parseDateTime(String date, String patter) throws DateTimeParseException {
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(patter));
	}

	public static LocalDate parseDate(String date, String patter) throws DateTimeParseException {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern(patter));
	}

	public static LocalTime parseTime(String time, String patter) throws DateTimeParseException {
		return LocalTime.parse(time, DateTimeFormatter.ofPattern(patter));
	}

	public static LocalDateTime formatLocalDateTimeAtStartOfday(LocalDate ldt) {
		return ldt.atStartOfDay();
	}

	public static LocalDateTime formatLocalDateTimeAtEndOfday(LocalDate ldt) {
		LocalDateTime temp = ldt.atStartOfDay();
		return fillTime(temp);
	}

	public static boolean isLastDayOfMonth(LocalDateTime ldt) {
		try {
			LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth() + 1, 0, 0);
			return false;
		} catch (DateTimeException var2) {
			return true;
		}
	}

	public static long diffInMinutes(Temporal past, Temporal future) {
		return past.until(future, ChronoUnit.MINUTES);
	}

	public static long diffInDays(Temporal past, Temporal future) {
		return past.until(future, ChronoUnit.DAYS);
	}

	public static long diffInWeeks(Temporal past, Temporal future) {
		return past.until(future, ChronoUnit.WEEKS);
	}

	public static long diffInMonths(Temporal past, Temporal future) {
		return past.until(future, ChronoUnit.MONTHS);
	}

	public static long diffInYears(Temporal past, Temporal future) {
		return past.until(future, ChronoUnit.YEARS);
	}

	public static boolean isTodayLastDayOfMonth() {
		return isLastDayOfMonth(LocalDateTime.now());
	}

	public static boolean isTodayFirstDayOfMonth() {
		return LocalDate.now().getDayOfMonth() == 1;
	}

	public static LocalDate addDaysALocalDate(LocalDate statDate, int daysToAdd) {
		return statDate.plusDays((long) daysToAdd);
	}

	public static LocalDate addMonthsALocalDate(LocalDate statDate, int monthsToAdd) {
		return statDate.plusMonths((long) monthsToAdd);
	}

	public static LocalDate addYearlslALocalDate(LocalDate statDate, int yearlsToAdd) {
		return statDate.plusYears((long) yearlsToAdd);
	}

	public static LocalDateTime addDays(LocalDateTime statDate, int days) {
		return statDate.plusDays((long) days);
	}

	public static LocalDateTime addMonths(LocalDateTime statDate, int months) {
		return statDate.plusMonths((long) months);
	}

	public static LocalDateTime addYearls(LocalDateTime statDate, int yearls) {
		return statDate.plusYears((long) yearls);
	}

	public static LocalDate firstDayOfWeek(LocalDate date) {
		return date.with(DayOfWeek.MONDAY);
	}

	public static LocalDateTime firstDayOfWeek(LocalDateTime date) {
		return date.with(DayOfWeek.MONDAY);
	}

	public static LocalDate firstDayOfMonth(LocalDate date) {
		return date.withDayOfMonth(1);
	}

	public static LocalDate lastDayOfMonth(LocalDate date) {
		return date.withDayOfMonth(date.lengthOfMonth());
	}

	public static LocalDateTime firstDayOfMonth(LocalDateTime dateTime) {
		return formatLocalDateTimeAtStartOfday(dateTime.toLocalDate().withDayOfMonth(1));
	}

	public static LocalDateTime lastDayOfMonth(LocalDateTime dateTime) {
		return formatLocalDateTimeAtEndOfday(
				dateTime.toLocalDate().withDayOfMonth(dateTime.toLocalDate().lengthOfMonth()));
	}

	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			return formatter.format(date);
		}
	}

	public static Date parseOldDate(String dateStr, String pattern) throws ParseException {
		if (dateStr == null) {
			throw new ParseException("", 0);
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			formatter.setLenient(false);
			return formatter.parse(dateStr);
		}
	}

	public static String formatDateWithSystemLocale(Date date) {
		return formatDate(date, "dd-MM-yyyy");
	}

	public static String formatDateAndTimeWithSystemLocale(Date date) {
		return formatDate(date, "dd-MM-yyyy HH:mm");
	}

	public static String formatDateWithSystemLocale(String pattern, String dateString) {
		String result = "";

		try {
			Date date = parseOldDate(dateString, pattern);
			result = formatDate(date, "dd-MM-yyyy");
		} catch (Exception var4) {
			;
		}

		return result;
	}

	public static String formatForResponseBean(LocalDate ld) {
		return format(ld, "yyyy-MM-dd");
	}

	public static String formatForResponseBean(LocalDateTime ldt) {
		return format(ldt, "yyyy-MM-dd HH:mm:ss");
	}

	public static LocalDate parseDateFromRequestBean(String date) {
		return parseDate(date, "yyyy-MM-dd");
	}

	public static LocalDateTime parseDateTimeFromRequestBean(String dateTime) {
		return parseDateTime(dateTime, "yyyy-MM-dd HH:mm:ss");
	}
}
