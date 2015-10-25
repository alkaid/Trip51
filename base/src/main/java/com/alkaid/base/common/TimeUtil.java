package com.alkaid.base.common;


public class TimeUtil {
	/**
	 * 格式化时间为   分：秒   格式
	 * @param timeMs 单位 milliseconds
	 * @return
	 */
	public static String formatTimeInmmss(int timeMs) {
		StringBuilder timeDisplay = new StringBuilder();
		int timeMin = timeMs / 60000;
		int timeRestSec = timeMs / 1000 % 60;
		if (timeMin <= 0) {
			timeDisplay.append("00");
		} else if (timeMin < 10) {
			timeDisplay.append("0");
			timeDisplay.append(timeMin);
		} else {
			timeDisplay.append(timeMin);
		}
		timeDisplay.append(":");
		if (timeRestSec <= 0) {
			timeDisplay.append("00");
		} else if (timeRestSec < 10) {
			timeDisplay.append("0");
			timeDisplay.append(timeRestSec);
		} else {
			timeDisplay.append(timeRestSec);
		}
		return timeDisplay.toString();
	}
	
	
	/**
	 * 根据字符串转为timeMillions
	 * @param mmss  mm:ss格式的字符串
	 * @return
	 */
	public static int getTime(String mmss){
		String seprator=":";
		if(!mmss.contains(seprator))
			seprator="：";
		String[] cell=mmss.split(":");
		int time=Integer.parseInt(cell[0])*60*1000;
		time+=Float.parseFloat(cell[1])*1000;
		return time;
	}
}
