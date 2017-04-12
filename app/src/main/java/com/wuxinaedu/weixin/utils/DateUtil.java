package com.wuxinaedu.weixin.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author Administrator
 *
 */
public class DateUtil {
	
	public static final DateFormat SDF = new SimpleDateFormat("HH:mm");
	public static final DateFormat DF = new SimpleDateFormat("MM月dd日");
	public static final DateFormat DATE_SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static final DateFormat DATE_LONG = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");

	public static String dateToString(DateFormat df, Date date) {
		return df.format(date);
	}
	
	/**
	 * 将长整形时间转换为字符串日期
	 * @param longDate
	 * @param str
	 * @return
	 */
	public static String parseLongToString(long longDate, DateFormat str) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(longDate);
		return str.format(calendar.getTime());
	}
	
	/**
	 * 解析 字符串为日期
	 * @param format
	 * @param time
	 * @return
	 */
	public static Date parseStringDate(DateFormat format,String time) {
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 将当期日期转为字符串
	 * @return
	 */
	public static String parseDateToString() {
		return DATE_SDF.format(new Date());
	}
	
	/**
	 * 转换日期
	 * @param timesamp
	 * @return
	 */
	public static String getDay(Date timesamp) {
		if(timesamp == null){
			return "未";
		}
		String result = "未";
		SimpleDateFormat sdf = new SimpleDateFormat("DD");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = timesamp;
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天 " + dateToString(SDF,timesamp);
			break;
		case 1:
			result = "昨天 "+ dateToString(SDF,timesamp);
			break;
		case 2:
			result = "前天 "+ dateToString(SDF,timesamp);
			break;

		default:
			result = temp + "天前 "+ dateToString(SDF,timesamp);
			break;
		}
		return result;
	}


	
}
