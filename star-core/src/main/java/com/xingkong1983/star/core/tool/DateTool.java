package com.xingkong1983.star.core.tool;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public final class DateTool {

	public static class Format {

		/**
		 * 缺省的时间格式
		 */
		public static final String DEFAULT_DATETIME = "yyyy-MM-dd HH:mm:ss";
		public static final String DEFAULT_DATE = "yyyy-MM-dd";
		public static final String DEFAULT_TIME = "HH:mm:ss";

		/**
		 * 没有连字符和空格的时间格式
		 */
		public static final String NOSPACE_DATETIME = "yyyyMMddHHmmss";
		public static final String NOSPACE_DATE = "yyyyMMdd";
		public static final String NOSPACE_TIME = "hhmmss";

		/**
		 * 中国的时间格式
		 */
		public static final String CHINESE_DATETIME = "yyyy年MM月dd日 HH点mm分ss秒";
		public static final String CHINESE_DATETIME_NO_SECOND = "yyyy年MM月dd日 HH点mm分";
		public static final String CHINESE_DATE = "yyyy年MM月dd日";
		public static final String CHINESE_DATE_NO_YEAR = "MM月dd日";
		public static final String CHINESE_TIME = "HH点mm分ss秒";

	}

	/**
	 * Description: 获取格式化后的时间字符串
	 * 
	 * @param longTime 时间戳
	 * @return
	 */
	public static LocalDateTime getDate(String longTime) {
		Long time = 0L;
		try {
			time = Long.parseLong(longTime);
		} catch (Exception e) {
			OsTool.print(e);
		}
		LocalDateTime date = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
		return date;
	}

	/**
	 * Description: 获取格式化后的时间字符串
	 * 
	 * @param longTime   时间戳
	 * @param formatText 格式化字符串
	 * @return
	 */
	public static String getString(long longTime, String formatText) {
		LocalDateTime date = Instant.ofEpochMilli(longTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
		return date.format(DateTimeFormatter.ofPattern(formatText));
	}

	/**
	 * Description: 获取格式化后的时间字符串
	 * 
	 * @param date       日期
	 * @param formatText 格式化字符串
	 * @return
	 */
	public static String getString(LocalDateTime date, String formatText) {
		if (date == null || StringUtils.isBlank(formatText)) {
			return null;
		}
		return date.format(DateTimeFormatter.ofPattern(formatText));
	}

	/**
	 * 获取当前日期与时间
	 */
	public static String getCurrentDateTime() {
		LocalDateTime now = LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern(Format.DEFAULT_DATETIME));
	}

	/**
	 * 获取现在是哪一年
	 */
	public static int getCurrentDateYear() {
		LocalDateTime date = LocalDateTime.now();
		return date.getYear();
	}

	/**
	 * 获取当前是几点
	 */
	public static int getCurrentDateHour() {
		LocalDateTime date = LocalDateTime.now();
		return date.getHour();
	}

	/**
	 * 获取当前是几点
	 */
	public static int getCurrentDateHour(Long time) {
		LocalDateTime date = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
		return date.getHour();
	}

	/**
	 * convent method to get days after or before 根据天数偏移量计算日期
	 *
	 * @param date
	 * @param days
	 * @return
	 */
	public static LocalDateTime getDateAfter(LocalDateTime date, int days) {
		if (date == null) {
			return null;
		}
		return date.plusDays(days);
	}

	/**
	 * 返回从当前时间开始的24小时制小时数组
	 * 
	 * @return
	 */
	public static ArrayList<String> getCurrentDateBeforeHour(int intervals) {
		ArrayList<String> pastHoursList = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < intervals; i++) {
			pastHoursList.add(String.valueOf(now.plusHours(i).getHour()));
		}
		return pastHoursList;
	}

	/**
	 * 获取过去 任意天内的日期数组
	 * 
	 * @param intervals  intervals天内
	 * @param formatText 格式化字符串
	 * @return 日期数组
	 */
	public static ArrayList<String> getCurrentDateBeforeDate(int intervals, String formatText) {
		ArrayList<String> pastDaysList = new ArrayList<>();
		for (int i = 0; i < intervals; i++) {
			pastDaysList.add(getPastDate(i, formatText));
		}
		return pastDaysList;
	}

	/**
	 * 获取过去第几天的日期
	 *
	 * @param past
	 * @param formatText 格式化字符串
	 * @return
	 */
	public static String getPastDate(int past, String formatText) {
		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime = localDateTime.minusDays(past);
		return localDateTime.format(DateTimeFormatter.ofPattern(formatText));
	}

	/**
	 * 获取当前日期与时间的字符串形式 使用DateTimeFormatter格式化时间
	 * 
	 * @return
	 */
	public static String getCurrentDateStr() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime.format(DateTimeFormatter.ofPattern(Format.DEFAULT_DATETIME));
	}

	/**
	 * 获得当前日期字符串
	 * 
	 * @return
	 */
	public static String getNospaceDateStr() {
		LocalDateTime date = LocalDateTime.now();
		return date.format(DateTimeFormatter.ofPattern(DateTool.Format.NOSPACE_DATE));
	}

	/**
	 * 将str转换为date
	 * 
	 * @return
	 */
	public static LocalDateTime getDateByStr(String dateStr) {
		return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(Format.DEFAULT_DATETIME));
	}

	/**
	 * getMonthsBetween(查询两个日期相隔的月份 )
	 *
	 * @param startDate 订单开始日期1 (格式yyyy-MM-dd)
	 * @param endDate   截止日期2 (格式yyyy-MM-dd)
	 * @return
	 */
	public static long getMonthsBetween(LocalDateTime startDate, LocalDateTime endDate) {
		long month = startDate.until(endDate, ChronoUnit.MONTHS);
		return Math.abs(month);
	}

	/**
	 * getDateBetween(查询两个日期相隔的年月日 )
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static String getDateBetween(LocalDateTime beginDate, LocalDateTime endDate) {
		String data = "";
		int day = endDate.getDayOfMonth() - beginDate.getDayOfMonth();
		int month = endDate.getMonthValue() - beginDate.getMonthValue();
		int year = endDate.getYear() - beginDate.getYear();
		// 按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
		if (day < 0) {
			month -= 1;
			endDate = endDate.minusHours(1);// 得到上一个月，用来得到上个月的天数。
			day = day + endDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
		}
		if (month < 0) {
			month = (month + 12) % 12;
			year--;
		}
		if (year < 1) {
			data = month + "月" + day + "天";
		} else {
			data = year + "年" + month + "月" + day + "天";
		}
		return data;
	}

	/**
	 * 获取不带符号的当前时间 yyyyMMdd
	 * 
	 * @return
	 */
	public static String getCurrentDateNoSpce() {
		LocalDateTime now = LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern(DateTool.Format.NOSPACE_DATE));
	}

	/**
	 * 获取带符号的当前时间 yyyy-MM-dd
	 *
	 * @return
	 */
	public static String getSignedCurrentTime() {
		LocalDateTime now = LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern(DateTool.Format.DEFAULT_DATE));
	}

	/**
	 * 字符串时间转Date
	 * 
	 * @param strTime
	 * @param pattern
	 * @return
	 */
	public static LocalDateTime parse(String strTime, String pattern) {
		return LocalDateTime.parse(strTime, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 获取指定日期当天的开始时间
	 * 
	 * @param day
	 * @return
	 */
	public static LocalDateTime getStartOfDay(LocalDateTime date) {
		if (date == null) {
			return null;
		}
		return date.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

	/**
	 * 获取指定日期当天的开始时间
	 * 
	 * @param day
	 * @return
	 */
	public static LocalDateTime getStartOfDay(LocalDate date) {
		if (date == null) {
			return null;
		}
		return date.atTime(0, 0, 0, 0);
	}

	/**
	 * 获取指定日期当天的最后时间
	 * 
	 * @param day
	 * @return
	 */
	public static LocalDateTime getEndOfDay(LocalDateTime date) {
		if (date == null) {
			return null;
		}
		return date.withHour(23).withMinute(59).withSecond(59).withNano(0);
	}

	/**
	 * 获取指定日期当天的最后时间
	 * 
	 * @param day
	 * @return
	 */
	public static LocalDateTime getEndOfDay(LocalDate date) {
		if (date == null) {
			return null;
		}
		return date.atTime(23, 59, 59, 0);
	}

	public static LocalDateTime date2LocalDateTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();

		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
		return localDateTime;
	}
}
