package com.sf.kh.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat format = new SimpleDateFormat();
	/***
	 * 生成批次号
	 * @return
	 */
	public static long generateBatchCode(){
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHH");
	    Calendar c = Calendar.getInstance();
	    String dateStr = sf.format(c.getTime())+"000";
	    return Long.valueOf(dateStr);
	}
	
	public static String formatDate(Date date) {
        return format(date, "yyyy-MM-dd");
    }
	
	public static String formatDateYmDhMCh(Date date) {
        return format(date, "yyyy年MM月dd日 HH:mm");
    }
	
    public static String formatDateYmDhMs(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }
    
    private synchronized static String format(Date date, String pattern) {
        format.applyPattern(pattern);
        return format.format(date);
    }
}
