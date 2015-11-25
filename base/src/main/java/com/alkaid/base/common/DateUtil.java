package com.alkaid.base.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间帮助类
 * @author chenbf
 */
public class DateUtil {

	// private Calendar calendar=Calendar.getInstance();

	/**
	 * 获取 是星期？ 系统的周期是从星期日开始，这里要转一下
	 * @return
	 */
	public static int getWeekDay(long timeInMils) 
	{
		Calendar nowCal = Calendar.getInstance();
		Date nowDate = new Date(timeInMils);
		nowCal.setTime(nowDate);
		int nowNum = nowCal.get(Calendar.DAY_OF_WEEK);
		if(nowNum-1==0){
			return 7;
		}else {
			return nowNum-1;
		}
	}

	public static Calendar getCurrentCalendar() {
		Calendar nowCal = Calendar.getInstance();
		Date nowDate = new Date(System.currentTimeMillis());
		nowCal.clear();
		nowCal.setTime(nowDate);
		return nowCal;
	}

	/**
	 * 得到当前的时间，时间格式yyyy-MM-dd
	 * @return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	
	/**
	 * @param strData
	 *            时间字串
	 * @param dateFormat
	 *            时间格式
	 * */
	public static Date converStringToDate(String strData, String dateFormat) {
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		try {
			Date date = df.parse(strData);
			return date;
		} catch (Exception ex) {
			LogUtil.e(ex);
			return null;
		}
	}
	  
    /**
     * 格式化时间字串
     */
    public static String formatDateString(Date date,String dateFormat){
    	SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);  
        try {   
            return sdf.format(date);
        } catch (Exception ex) {   
            ex.printStackTrace();
            return null;
        }
    	
    }
   
    /**
     * @param strData  时间字串
     * @param dateFormat 时间格式
     * */
    public static Calendar converStringToCalendar(String strData,String dateFormat){
    	 SimpleDateFormat df = new SimpleDateFormat(dateFormat);  
    	 Calendar calendar = Calendar.getInstance();
         try {   
             Date date = df.parse(strData);
             calendar.setTime(date);
             return calendar; 
         } catch (Exception ex) {   
             return null;
         }
    }
    
      
    /**
     * 得到时间字串
     */
    public static String converTimeMilsToFormatStr(long timeInMils,String dateFormat){
    	 SimpleDateFormat df = new SimpleDateFormat(dateFormat);
    	 Date date = new Date(timeInMils);
    	return df.format(date);
    }
    
    
    
    
    /**
     *  
     * @param differDays
     * @return
     */
    public static long getDifferMillis(int differDays)
    {
    	return differDays * 24 * 60 * 60 * 1000;
    }
    
    
    public static boolean setTimeBigNowTime(long setTime)
    {
    	if(setTime >= System.currentTimeMillis()){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**
     *  计算相隔天数
     * @param startday
     * @param endday
     * @return
     */
    public static int getIntervalDays(long startday,long endday){        
        if(startday<endday){ 
            long cal=startday; 
            startday=endday; 
            endday=cal; 
        }              
        long ei=startday-endday;           
        return (int)(ei/(1000*60*60*24)); 
    } 
    
    /**
     * 获得某个时间到最近下月份的某天的时间
     * @param n 1-31 之间的某天
     * @param setTime 某个时间
     * @return
     */
    public static Calendar getToNextMonthDay(int n,long setTime ){

		Calendar canlendar = Calendar.getInstance();
		canlendar.clear();
		canlendar.setTimeInMillis(setTime);
		canlendar.add(Calendar.MONTH, 1);
		int maxDay = canlendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (maxDay < n) {
			// canlendar.add(Calendar.MONTH, 1);
			getToNextMonthDay(n, canlendar.getTimeInMillis());
		} else {
			canlendar.set(Calendar.DAY_OF_MONTH, n);
		}
		return canlendar;
	}

	/*    *//**
	 * 获得某个日期到最近的某号的时间
	 * 
	 * @param n
	 *            1-31 之间的某号
	 * @param setTime
	 *            某个时间
	 * @return
	 */
	/*
	 * public static Calendar getToNearMonthDay(int n,long setTime ){ Calendar
	 * calendar = Calendar.getInstance(); calendar.clear();
	 * 
	 * calendar.setTimeInMillis(setTime); int day
	 * =calendar.get(Calendar.DAY_OF_MONTH); int maxDay =
	 * calendar.getActualMaximum(Calendar.DAY_OF_MONTH); if(n>day){
	 * if(n<maxDay){ calendar.set(Calendar.DAY_OF_MONTH, n); }else{
	 * getToNextMonthDay(n,setTime); } }else if(n<day) {
	 * getToNextMonthDay(n,setTime); } return calendar; }
	 */

	/**
	 * 把一串 1010001 类似的字串转换成 int {1,3,7}数字
	 * 
	 */
	public static int[] converStringToInt(String repeatStr) {

		int[] i1 = new int[repeatStr.length()];
		int count = 0, number = 0;
		for (int i = 0; i < repeatStr.length(); i++) {
			i1[i] = Integer.parseInt(repeatStr.substring(i, i + 1));
			if (i1[i] == 1) {
				count++;
			}
		}
		if (count == 0)
			return null;
		int dayOfNum[] = new int[count];
		for (int i = 0; i < repeatStr.length(); i++) {
			if (i1[i] * (i + 1) > 0) {
				dayOfNum[number] = i1[i] * (i + 1);
				number++;
			}
		}

		return dayOfNum;
	}

	/*
	 * 计算in数组中距离 nowDay最近的一天
	 */
	public static int getResultDifferDay(int[] in, int nowDay) {
		int result = 0;
		for (int i = 0; i < in.length; i++) {
			if (in[i] >= nowDay) {
				result = in[i];
				break;
			}
		}

		if (result == 0) {
			result = in[0];
		}
		return result;
	}

	/*
	 * 计算in数组中距离 nowDay最近的一天
	 */
	public static int getResultDifferDay1(int[] in, int nowDay) {
		int result = 0;
		for (int i = 0; i < in.length; i++) {
			if (in[i] > nowDay) {
				result = in[i];
				break;
			}
		}

		if (result == 0) {
			result = in[0];
		}
		return result;
	}

	// compare nowDay to nextDay
	public static int compareDayNowToNext(int nowDay, int nextDay) {
		if (nextDay > nowDay) {
			return (nextDay - nowDay);
		} else if (nextDay == nowDay) {
			return 0;
		} else {
			return (7 - (nowDay - nextDay));
		}
	}

	/*
	 * public static Calendar getToNextMonthDay(int n,long setTime ){ Calendar
	 * canlendar = Calendar.getInstance(); canlendar.clear();
	 * canlendar.setTimeInMillis(setTime); int month =
	 * canlendar.get(Calendar.MONTH); if(month==12){
	 * canlendar.add(Calendar.YEAR, 1); canlendar.set(Calendar.MONTH, 1);
	 * canlendar.set(Calendar.DAY_OF_MONTH, 1);
	 * getToNextMonthDay(n,canlendar.getTimeInMillis()); }else{
	 * canlendar.add(Calendar.MONTH, 1); } int maxDay =
	 * canlendar.getActualMaximum(Calendar.DAY_OF_MONTH); if(maxDay<n){
	 * getToNextMonthDay(n,canlendar.getTimeInMillis()); }else{
	 * canlendar.set(Calendar.DAY_OF_MONTH, n); } return canlendar; }
	 */

	// 改进精确计算相隔天数的方法
	public static int getDaysBetween(Calendar d1, Calendar d2) {
		if (d1.after(d2)) {
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		if (d1.get(Calendar.YEAR) != y2) {
			d1 = (Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	// 获得指定提醒时间
	public static long getSetTimeInMils(long setTime, int day, int hour,
			int minute, int second) {
		Calendar canlendar = Calendar.getInstance();
		canlendar.clear();
		canlendar.setTimeInMillis(setTime);
		canlendar.set(Calendar.DAY_OF_MONTH, day);
		canlendar.set(Calendar.HOUR_OF_DAY, hour);
		canlendar.set(Calendar.MINUTE, minute);
		canlendar.set(Calendar.SECOND, second);
		canlendar.set(Calendar.MILLISECOND, 0);
		return canlendar.getTimeInMillis();
	}

	// 获得指定提醒时间
	public static long getSetTimeInMils(long setTime, int hour, int minute,
			int second) {
		Calendar canlendar = Calendar.getInstance();
		canlendar.clear();
		canlendar.setTimeInMillis(setTime);
		canlendar.set(Calendar.HOUR_OF_DAY, hour);
		canlendar.set(Calendar.MINUTE, minute);
		canlendar.set(Calendar.SECOND, second);
		canlendar.set(Calendar.MILLISECOND, 0);
		return canlendar.getTimeInMillis();
	}

	/**
	 * 得到当前的时间,自定义时间格式 y 年 M 月 d 日 H 时 m 分 s 秒
	 * 
	 * @param dateFormat
	 *            输出显示的时间格式
	 * @return
	 */
	public static String getCurrentDate(String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(new Date());
	}

	/**
	 * 日期格式化，默认日期格式yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 日期格式化，自定义输出日期格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormatDate(Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	/**
	 * 返回当前日期的前一个时间日期，amount为正数 当前时间后的时间 为负数 当前时间前的时间 默认日期格式yyyy-MM-dd
	 * 
	 * @param field
	 *            日历字段 y 年 M 月 d 日 H 时 m 分 s 秒
	 * @param amount
	 *            数量
	 * @return 一个日期
	 */
	public String getPreDate(String field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		if (field != null && !field.equals("")) {
			if (field.equals("y")) {
				calendar.add(Calendar.YEAR, amount);
			} else if (field.equals("M")) {
				calendar.add(Calendar.MONTH, amount);
			} else if (field.equals("d")) {
				calendar.add(Calendar.DAY_OF_MONTH, amount);
			} else if (field.equals("H")) {
				calendar.add(Calendar.HOUR, amount);
			}
		} else {
			return null;
		}
		return getFormatDate(calendar.getTime());
	}

	/**
	 * 某一个日期的前一个日期
	 * 
	 * @param d
	 *            ,某一个日期
	 * @param field
	 *            日历字段 y 年 M 月 d 日 H 时 m 分 s 秒
	 * @param amount
	 *            数量
	 * @return 一个日期
	 */
	public String getPreDate(Date d, String field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		if (field != null && !field.equals("")) {
			if (field.equals("y")) {
				calendar.add(Calendar.YEAR, amount);
			} else if (field.equals("M")) {
				calendar.add(Calendar.MONTH, amount);
			} else if (field.equals("d")) {
				calendar.add(Calendar.DAY_OF_MONTH, amount);
			} else if (field.equals("H")) {
				calendar.add(Calendar.HOUR, amount);
			}
		} else {
			return null;
		}
		return getFormatDate(calendar.getTime());
	}

	/**
	 * 某一个时间的前一个时间
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public String getPreDate(String date) throws ParseException {
		Date d = new SimpleDateFormat().parse(date);
		String preD = getPreDate(d, "d", 1);
		Date preDate = new SimpleDateFormat().parse(preD);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(preDate);
	}

	/**
	 * 计算两个日期相隔天数 (to-from)
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static int getDiffDays(Date from, Date to) {
		// java.text.SimpleDateFormat format = new
		// java.text.SimpleDateFormat("yyyy-MM-dd");
		// java.util.Date beginDate= format.parse(from);
		// java.util.Date endDate= format.parse(to);
		int day = (int) ((to.getTime() - from.getTime()) / (24 * 3600 * 1000));
		return day;
	}

	/**
	 * 计算两个日期相隔天数 (to-from)
	 * 
	 * @param from
	 * @param to
	 * @param format
	 *            时间格式
	 * @return
	 */
	public static int getDiffDays(String from, String to, String format) {
		Date beginDate = converStringToDate(from, format);
		Date endDate = converStringToDate(to, format);
		return getDiffDays(beginDate, endDate);
	}

	/**
	 * 时间格式为yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static String getFormatDate(Calendar c) {
		return getFormatDate(c.getTime(),"yyyy-MM-dd hh:mm:ss");
	}

    /**
     * 根据给定日期和相差天数计算日期
     * @param from	日期
     * @param differDays 相差天数
     * @return
     */
    public static Date getDiffDate(Date from,int differDays){
    	return new Date(from.getTime()+getDifferMillis(differDays));
    }
    /**
     * 根据给定日期和相差天数计算日期
     * @param from	日期
     * @param differDays 相差天数
     * @param format 时间格式
     * @return
     */
    public static String getDiffDate(String from,int differDays,String format){
    	Date fromDate=converStringToDate(from, format);
    	Date toDate=getDiffDate(fromDate, differDays);
    	return getFormatDate(toDate, format);
    }
    
    
    
    
}
