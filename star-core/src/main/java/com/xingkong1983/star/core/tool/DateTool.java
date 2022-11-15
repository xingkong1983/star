package com.xingkong1983.star.core.tool;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

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
	public static Date getDate( String longTime ) {
		Long time = 0L;
		try {
			time = Long.parseLong(longTime);
		} catch (Exception e) {
			OsTool.print(e);
		}
		Date date = new Date(time);

		return date;
	}

	/**
	 * Description: 获取格式化后的时间字符串
	 * 
	 * @param longTime   时间戳
	 * @param formatText 格式化字符串
	 * @return
	 */
	public static String getString( long longTime, String formatText ) {
		SimpleDateFormat ft = new SimpleDateFormat(formatText, Locale.CHINA);
		ft.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return ft.format(longTime);
	}

	/**
	 * Description: 获取格式化后的时间字符串
	 * 
	 * @param date       日期
	 * @param formatText 格式化字符串
	 * @return
	 */
	public static String getString( Date date, String formatText ) {
		SimpleDateFormat ft = new SimpleDateFormat(formatText, Locale.CHINA);
		ft.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return ft.format(date);
	}

	/**
	 * 获取当前日期与时间
	 */
	public static String getCurrentDateTime( ) {
		SimpleDateFormat ft = new SimpleDateFormat(Format.DEFAULT_DATETIME, Locale.CHINA);
		ft.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return ft.format(new Date());
	}

	/**
	 * 获取当前时间的年
	 */
	public static int getCurrentDateYear( ) {
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.YEAR);
	}

	/**
	 * 获取当前时间的小时
	 */
	public static int getCurrentDateHour( Long time ) {
		Date date = new Date(time);
		return date.getHours();
	}

	/**
	 * convent method to get days after or before 根据天数偏移量计算日期
	 *
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date getDateAfter( Date date, int days ) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(GregorianCalendar.DATE, days);
		return calendar.getTime();
	}

	/**
	 * 返回当前时间24小时 时数组
	 * 
	 * @return
	 */
	public static ArrayList<String> getCurrentDateBeforeHour( int intervals ) {
		ArrayList<String> pastHoursList = new ArrayList<>();
		for (int i = 0; i < intervals; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, i);
			pastHoursList.add(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
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
	public static ArrayList<String> getCurrentDateBeforeDate( int intervals, String formatText ) {
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
	public static String getPastDate( int past, String formatText ) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat(formatText);
		String result = format.format(today);
		return result;
	}

	/**
	 * 获取当前日期与时间的字符串形式 使用DateTimeFormatter格式化时间
	 * 
	 * @return
	 */
	public static String getCurrentDateStr( ) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Format.DEFAULT_DATETIME, Locale.CHINA);
		LocalDateTime localDateTime = LocalDateTime.now();
		String format1 = dateTimeFormatter.format(localDateTime);
		return format1;
	}

	/**
	 * 获得当前日期字符串
	 * 
	 * @return
	 */
	public static String getNospaceDateStr( ) {
		Date date = new Date();
		String formatStr = DateTool.getString(date.getTime(), DateTool.Format.NOSPACE_DATE);
		return formatStr;
	}

	/**
	 * 将str转换为date
	 * 
	 * @return
	 */
	public static Date getDateByStr( String dateStr ) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Format.DEFAULT_DATETIME, Locale.CHINA);
		LocalDateTime localDateTime = LocalDateTime.parse(dateStr, dateTimeFormatter);
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		Date date = Date.from(zdt.toInstant());
		return date;
	}

	/**
	 * getMonthsBetween(查询两个日期相隔的月份 )
	 *
	 * @param startDate 订单开始日期1 (格式yyyy-MM-dd)
	 * @param endDate   截止日期2 (格式yyyy-MM-dd)
	 * @return
	 */
	public static int getMonthsBetween( Date startDate, Date endDate ) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(startDate);
		c2.setTime(endDate);
		int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		int month = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		return Math.abs(year * 12 + month);
	}

	/**
	 * getDateBetween(查询两个日期相隔的年月日 )
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static String getDateBetween( Date beginDate, Date endDate ) {
		String data = "";
		Calendar begin = Calendar.getInstance();
		begin.setTime(beginDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		int day = end.get(Calendar.DAY_OF_MONTH) - begin.get(Calendar.DAY_OF_MONTH);
		int month = end.get(Calendar.MONTH) - begin.get(Calendar.MONTH);
		int year = end.get(Calendar.YEAR) - begin.get(Calendar.YEAR);
		// 按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
		if (day < 0) {
			month -= 1;
			end.add(Calendar.MONTH, -1);// 得到上一个月，用来得到上个月的天数。
			day = day + end.getActualMaximum(Calendar.DAY_OF_MONTH);
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
	public static String getCurrentDateNoSpce( ) {
		SimpleDateFormat ft = new SimpleDateFormat(DateTool.Format.NOSPACE_DATE);
		return ft.format(new Date());
	}

	/**
	 * 获取带符号的当前时间 yyyy-MM-dd
	 *
	 * @return
	 */
	public static String getSignedCurrentTime( ) {
		SimpleDateFormat ft = new SimpleDateFormat(DateTool.Format.DEFAULT_DATE);
		return ft.format(new Date());
	}
}
