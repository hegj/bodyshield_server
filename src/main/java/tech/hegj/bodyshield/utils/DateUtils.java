package tech.hegj.bodyshield.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

 

/**
 * 日期工具类
 * @author abramlu
 *
 */
public class DateUtils {
	
	public static final long DAY_TIME_STAMP = 86400000;
	/**
	 * 格式时间  2012-11-16 16:50:23
	 * @param date
	 * @return
	 */
	public static String getDateFormat(long time){
		SimpleDateFormat sbf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sbf.format(new Date(time));
	}
	// sql date
	public static java.sql.Date getDateFormat(String time) throws ParseException{
		 
		   
		return java.sql.Date.valueOf(time);  //时间  
	}
	/**
	 * 自定义时间格式
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String getDateFormat(long time, String pattern){
		SimpleDateFormat sbf =new SimpleDateFormat(pattern);
		return sbf.format(new Date(time));
	} 
	
	public static String getDateFormat(){
		return getDateFormat(System.currentTimeMillis());
	}
	/**
	 * 计算剩余时间（48小时）
	 * @param createTime
	 * @return
	 */
	public static long getRemainTime(long createTime,long limitTime){
		long now = System.currentTimeMillis();
		long temp = now - createTime;
		long remainTime = limitTime - temp;
		return remainTime > 0 ? remainTime : 0;
	}
	
	/** 
     * 日期格式字符串转换成时间戳 
     * @param date 字符串日期 
     * @param format 如：yyyy-MM-dd HH:mm:ss 
     * @return 
     */  
    public static long date2TimeStamp(String date_str,String format){  
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat(format);  
            return sdf.parse(date_str).getTime();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return 0;
    } 
	/** 
     * 今天零点零分零秒的毫秒数
     */  
    public static long getTodayZeroTime(){
    	return (System.currentTimeMillis()/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset());
    	  
    }
    public static long getTomorrowZeroTime (){
    	return getTodayZeroTime()+DAY_TIME_STAMP;
    }
	/** 
     * 今天还有多少秒
     */ 
    public static int getTodayRemainSecond(){
    	 Calendar curDate = Calendar.getInstance();
 		Calendar tommorowDate = new GregorianCalendar(curDate
 				.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate
 				.get(Calendar.DATE) + 1, 0, 0, 0);
 	  return(int)(tommorowDate.getTimeInMillis() - curDate .getTimeInMillis()) / 1000;
    	
    }
}
