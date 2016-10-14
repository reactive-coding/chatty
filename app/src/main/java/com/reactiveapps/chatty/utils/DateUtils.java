package com.reactiveapps.chatty.utils;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    private final static String TAG = DateUtils.class.getSimpleName();

    public static String getDateCN() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        String date = format.format(new Date(System.currentTimeMillis()));
        return date;// 2012年03月15日 23:41:31
    }

    public static String getDateEN() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(new Date(System.currentTimeMillis()));
        return date1;// 2012-10-03 23:41:31
    }

    public static String getDateEN(long ts) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(new Date(ts));
        return date1;// 2012-10-03 23:41:31
    }

    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String date = format.format(new Date(System.currentTimeMillis()));
        return date;
    }

    public static String getDateHHmmss(Long src) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String date = format.format(new Date(src));
        return date;
    }

    public static String getDateHHmm(Long src) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String date = format.format(new Date(src));
        return date;
    }

    public static String getDateDay() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = format1.format(new Date(System.currentTimeMillis()));
        return date1;// 2012-10-03 23:41:31
    }

    public static String getDateyyyyMMdd(Long src) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = format1.format(new Date(src));
        return date1;// 2012-10-03
    }

    public static String getDateHHmm(String src) {
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        String date1 =null;
        try{

            date1 = format1.format(format1.parse(src));
        }catch (Exception e){
            e.printStackTrace();
        }
        return date1;// 2012-10-03
    }

    public static String getDateyyyyMMdd(String src) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String date1 =null;
        try{

        date1 = format1.format(format1.parse(src));
        }catch (Exception e){
            e.printStackTrace();
        }
        return date1;// 2012-10-03
    }

    public static String getYear() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        String date1 = format1.format(new Date(System.currentTimeMillis()));
        return date1;// 2012
    }

    public static String getYear(String date) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");

        String date1 = null;
        try {
            date1 = format1.format(format1.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date1;// 2012
    }

    /**
     * 服务器发过来的时间格式: datetime: 2013-04-27 13:56:38
     *
     * @param date
     * @return
     */
    public static String convertDateTime2Msec(String date) {
        // Log.i(TAG, "covertDateTime2Msec ---> "+date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (TextUtils.isEmpty(date))
            return date;
        // 得到毫秒数
        try {
            long timeMsec = format.parse(date).getTime();
            // Log.i(TAG, "after convet ---> "+String.valueOf(timeMsec));
            date = String.valueOf(timeMsec);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        // Log.i(TAG, "return convet ---> "+date);
        return date;
    }

    /**
     * 服务器发过来的时间格式: datetime: 2013-04-27 13:56:38
     *
     * @param date
     * @return
     */
    public static String convertDateTime2MinuteMsec(String date) {
        // Log.i(TAG, "covertDateTime2Msec ---> "+date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (null == date)
            return null;
        // 得到毫秒数
        try {
            long timeMsec = format.parse(date).getTime();
            // Log.i(TAG, "after convet ---> "+String.valueOf(timeMsec));
            date = String.valueOf(timeMsec);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        // Log.i(TAG, "return convet ---> "+date);
        return date;
    }

    /**
     * 服务器发过来的时间格式: datetime: 2013-04-27 13:56:38
     *
     * @param date
     * @return
     */
    public static String convertDateTime2DateMsec(String date) {
        // Log.i(TAG, "covertDateTime2DateMsec ---> "+date);
        if (null == date)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // 得到毫秒数
        try {
            long timeMsec = format.parse(date).getTime();
            // Log.i(TAG, "after convet ---> "+String.valueOf(timeMsec));
            date = String.valueOf(timeMsec);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        // Log.i(TAG, "return convet ---> "+date);
        return date;
    }

    /**
     * 今天: 17:05 昨天的消息日期显示格式: "昨天17:05" 前天以后的消息日期格式: "2014-04-27"
     */
    @SuppressLint("SimpleDateFormat")
    public static String convertMsec2DateTime(String msec) {
        // Log.i(TAG, "convertMsec2DateTime ---> "+msec);
        if (null == msec)
            return null;
        long ONE_DAY_MSEC = 1000 * 60 * 60 * 24L;
        long TWO_DAY_MSEC = 1000 * 60 * 60 * 48L;
        long THREE_DAY_MSEC = 1000 * 60 * 60 * 72L;

        try {
            long currentMsec = System.currentTimeMillis();
            long targetMsec = Long.parseLong(msec);
            long DURATION = currentMsec - targetMsec;
            //
            // Log.d(TAG, "The current date msec:" + currentMsec);
            // Log.d(TAG, "The target date msec: " + targetMsec);
            // Log.d(TAG, "The ONE_DAY_MSEC:" + String.valueOf(ONE_DAY_MSEC));
            // Log.d(TAG, "The TWO_DAY_MSEC:" + String.valueOf(TWO_DAY_MSEC));
            // Log.d(TAG, "The current-target:" + String.valueOf(DURATION));

            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd HH:mm");

            // 当前的消息
            if (0 < DURATION && DURATION < ONE_DAY_MSEC) {
                msec = format.format(new Date(Long.parseLong(msec)));
                // 昨天的消息
            } else if (ONE_DAY_MSEC <= DURATION && DURATION < TWO_DAY_MSEC) {
                msec = format.format(new Date(Long.parseLong(msec)));
                msec = "昨天  " + msec;
                // 前天的消息
            } else if (TWO_DAY_MSEC <= DURATION && DURATION < THREE_DAY_MSEC) {
                msec = format.format(new Date(Long.parseLong(msec)));
                msec = "前天  " + msec;
                // 前天的消息
            } else if (THREE_DAY_MSEC <= DURATION) {
                msec = format1.format(new Date(Long.parseLong(msec)));
            }
        } catch (Exception e) {
            return "";
        }
        return msec;
    }

    /**
     * 今天： 上午（下午、晚上） 08:20 昨天： 昨天 08:20 更早： 3月1日 09:00 　
     * 两条对话在3分钟之内，不显示时间，超过3分钟算发起一次新对话，显示时间 　 连续超过10条对话显示时间
     */
    /**
     * 返回当前显示日期格式
     *
     * @param date
     * @return
     */
    public static String getTodayDateFormat(String date) {
        // Log.i(TAG, "getTodayDateFormat ---> "+date);
        if (null == date)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            // Log.i(TAG,"msec: "+
            // String.valueOf(Long.parseLong(MyDate.convertDateTime2Msec(date))));
            date = format.format(new Date(Long.parseLong(DateUtils.convertDateTime2Msec(date))));
        } catch (Exception e) {
            return "";
        }
        // Log.i(TAG, "getTodayDateFormat,return: "+date);
        return date;
    }

    /**
     * 返回昨天显示日期格式
     *
     * @param date
     * @return
     */
    public static String getYestodayDateFormat(String date) {
        // Log.i(TAG, "getYestodayDateFormat ---> "+date);
        if (null == date)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            // Log.i(TAG,
            // "msec: "+String.valueOf(Long.parseLong(MyDate.convertDateTime2Msec(date))));
            date = format.format(new Date(Long.parseLong(DateUtils
                    .convertDateTime2Msec(date))));
            date = "昨天  " + date;
        } catch (Exception e) {
            return "";
        }
        // Log.i(TAG, "getYestodayDateFormat,return: "+date);
        return date;
    }

    /**
     * 返回前天的显示日期格式
     *
     * @param date
     * @return
     */
    public static String getBeforeYestodayDateFormat(String date) {
        // Log.i(TAG, "getBeforeYestodayDateFormat,---> " + date);
        if (null == date)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            // Log.i(TAG,"msec: "+
            // String.valueOf(Long.parseLong(MyDate.convertDateTime2Msec(date))));
            date = format.format(new Date(Long.parseLong(DateUtils
                    .convertDateTime2Msec(date))));
            date = "前天" + date;
        } catch (Exception e) {
            return "";
        }
        // Log.i(TAG, "getBeforeYestodayDateFormat,return: " + date);
        return date;
    }

    /**
     * <p>
     * Title: getSevenDateFormat
     * </p>
     * <p>
     * Description: 显示一周内时间
     * </p>
     *
     * @param date
     * @return
     */
    public static String getSevenDateFormat(String date) {
        // Log.i(TAG, "getBeforeYestodayDateFormat,---> " + date);
        if (null == date)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            date = format.format(new Date(Long.parseLong(DateUtils
                    .convertDateTime2Msec(date))));
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date2 = format2.format(new Date(Long.parseLong(DateUtils
                    .convertDateTime2Msec(date))));
            date = "星期 " + dayForWeek(date2) + "" + date;
        } catch (Exception e) {
            return "";
        }
        // Log.i(TAG, "getBeforeYestodayDateFormat,return: " + date);
        return date;
    }


    /**
     * 返回会话列表前天以后的显示日期格式
     *
     * @param date
     * @return
     */
    public static String getBeforeBeforeYestodayDateFormatAtRecentChatListActivity(
            String date) {
        if (null == date)
            return null;
        SimpleDateFormat format;
        if (getYear().equals(getYear(date))) {
            format = new SimpleDateFormat("MM-dd");
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd");
        }
        try {
            date = format.format(new Date(Long.parseLong(DateUtils.convertDateTime2Msec(date))));
        } catch (Exception e) {
            return "";
        }
        return date;
    }

    /**
     * 返回非会话列表时间显示格式
     *
     * @param date
     * @return
     */
    public static String getBeforeBeforeYestodayDateFormat(
            String date) {
        if (null == date)
            return null;
        SimpleDateFormat format;
        if (getYear().equals(getYear(date))) {
            format = new SimpleDateFormat("MM-dd HH:mm");
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }

        try {
            // Log.i(TAG,"msec: "+
            // String.valueOf(Long.parseLong(MyDate.convertDateTime2Msec(date))));
            date = format.format(new Date(Long.parseLong(DateUtils
                    .convertDateTime2Msec(date))));
        } catch (Exception e) {
            return "";
        }
        return date;
    }

    /**
     * For ChatActivityAdatper
     *
     * @param date , 消息中的日期: 2013-05-03 17:59:50
     * @return 0, 表示当天并和当前本地时间的间隔在三分钟之内 1,表示当前的消息 2,表示昨天的消息 3,表示前天的消息 4,表示前天以前的消息
     * -1,表示null date
     */
    public static int isDisplayDatetime(String date) {
        // Log.i(TAG, "isDisplayDatetime --->,"+date);
        if (null == date)
            return -1;
        int isDisplay = -1;
        long FIVE_MINUTE = 1000 * 60 * 5L;
        long ONE_DAY_MSEC = 1000 * 60 * 60 * 24L;
        long TWO_DAY_MSEC = 1000 * 60 * 60 * 48L;
        long THREE_DAY_MSEC = 1000 * 60 * 60 * 72L;

        String currentMsec = convertDateTime2DateMsec(getServerTime());
        String targetMsec = convertDateTime2DateMsec(date);
        try {
            long DURATION = (Long.parseLong(currentMsec) - Long.parseLong(targetMsec));
            // Log.d(TAG, "The current date msec:" + currentMsec);
            // Log.d(TAG, "The target date msec: " + targetMsec);
            // Log.d(TAG, "The ONE_DAY_MSEC:" + String.valueOf(ONE_DAY_MSEC));
            // Log.d(TAG, "The TWO_DAY_MSEC:" + String.valueOf(TWO_DAY_MSEC));
            // Log.d(TAG, "The current-target:" + String.valueOf(DURATION));
            // 今天
            if (0 < DURATION && DURATION < FIVE_MINUTE) {
                isDisplay = 0;
            } else if (FIVE_MINUTE < DURATION && DURATION < ONE_DAY_MSEC) {
                isDisplay = 1;
                // 昨天的消息
            } else if (ONE_DAY_MSEC <= DURATION && DURATION < TWO_DAY_MSEC) {
                isDisplay = 2;
                // 前天的消息
            } else if (TWO_DAY_MSEC <= DURATION && DURATION < THREE_DAY_MSEC) {
                isDisplay = 3;
            } else if (THREE_DAY_MSEC <= DURATION) {
                isDisplay = 4;
            } else {// 当发送过来的消息时间大于本地时间则显示 10:42
                isDisplay = 1;
            }
        } catch (Exception e) {
            isDisplay = -2;
        }
        // Log.i(TAG, "isDisplay --->,"+isDisplay);
        return isDisplay;
    }

    public static long synchronizedTime(long date) {
        try {
            String currentMsec = convertDateTime2DateMsec(getServerTime());
            String targetMsec = convertDateTime2DateMsec(String.valueOf(date));
            long DURATION = (Long.parseLong(currentMsec) - Long
                    .parseLong(targetMsec));
            return date + DURATION;
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return date;
        }
    }

    /**
     * For Activity to display the date
     *
     * @param date , msg date eg, 2013-05-03 12:30:24
     * @param flag , true at chatactivity or recentchatlistactivity
     * @return 12:30<today> 昨天12:30<yestoday> 05-02 12:30<long long ago> at
     * chatactivity 2013-05-02<long long ago> at recentchatactivity
     */
    public static String getDisplayDateTime(String date, Boolean flag) {
        // Log.i(TAG, "getDisplayDateTime ---> "+date);
        if (null == date)
            return null;
        long ONE_DAY_MSEC = 1000 * 60 * 60 * 24L;// 今天
        long TWO_DAY_MSEC = 1000 * 60 * 60 * 48L;// 昨天
        long THREE_DAY_MSEC = 1000 * 60 * 60 * 24L;// 前天
        long SEVEN_DAY_MSEC = 1000 * 60 * 60 * 7 * 24L;// 7天

        String currentMsec = convertDateTime2DateMsec(getServerTime());
        String targetMsec = convertDateTime2DateMsec(date);
        try {
            long DURATION = (Long.parseLong(currentMsec) - Long.parseLong(targetMsec));

            // Log.d(TAG, "The current date msec:" + currentMsec);
            // Log.d(TAG, "The target date msec: " + targetMsec);
            // Log.d(TAG, "The ONE_DAY_MSEC:" + String.valueOf(ONE_DAY_MSEC));
            // Log.d(TAG, "The TWO_DAY_MSEC:" + String.valueOf(TWO_DAY_MSEC));
            // Log.d(TAG, "The current-target:" + String.valueOf(DURATION));
            if ((0 <= DURATION) && (DURATION < ONE_DAY_MSEC)) {
                return getTodayDateFormat(date);
            }
            if ((ONE_DAY_MSEC <= DURATION) && (DURATION < TWO_DAY_MSEC)) {
                return getYestodayDateFormat(date);
                // 昨天的消息
            }
            /**
             * 暂时屏蔽此逻辑，逻辑有错误
             */
            // else if ((TWO_DAY_MSEC <= DURATION) && (DURATION <
            // SEVEN_DAY_MSEC)) {
            // //7天内显示星期和时间
            // return getSevenDateFormat(date);
            // }
            else if (TWO_DAY_MSEC <= DURATION) { // 大于7天显示年月日
                return getBeforeBeforeYestodayDateFormat(date);
            } else {// 当发送过来的消息时间大于本地时间则显示 10:42
                return getTodayDateFormat(date);
            }
        } catch (Exception e) {
            return "";
        }

    }

    public static String getDisplayDateTimeAtChatList(String date, Boolean flag) {
        // Log.i(TAG, "getDisplayDateTime ---> "+date);
        if (null == date)
            return null;
        long ONE_DAY_MSEC = 1000 * 60 * 60 * 24L;// 今天
        long TWO_DAY_MSEC = 1000 * 60 * 60 * 48L;// 昨天
        long THREE_DAY_MSEC = 1000 * 60 * 60 * 24L;// 前天
        long SEVEN_DAY_MSEC = 1000 * 60 * 60 * 7 * 24L;// 7天

        String currentMsec = convertDateTime2DateMsec(getServerTime());
        String targetMsec = convertDateTime2DateMsec(date);
        try {
            long DURATION = (Long.parseLong(currentMsec) - Long.parseLong(targetMsec));
            // Log.d(TAG, "The current date msec:" + currentMsec);
            // Log.d(TAG, "The target date msec: " + targetMsec);
            // Log.d(TAG, "The ONE_DAY_MSEC:" + String.valueOf(ONE_DAY_MSEC));
            // Log.d(TAG, "The TWO_DAY_MSEC:" + String.valueOf(TWO_DAY_MSEC));
            // Log.d(TAG, "The current-target:" + String.valueOf(DURATION));
            if ((0 <= DURATION) && (DURATION < ONE_DAY_MSEC)) {
                return getTodayDateFormat(date);
            }
            if ((ONE_DAY_MSEC <= DURATION) && (DURATION < TWO_DAY_MSEC)) {
//				return getYestodayDateFormat(date);
                return "昨天";
                // 昨天的消息
            }
            /**
             * 暂时屏蔽此逻辑，逻辑有错误
             */
            // else if ((TWO_DAY_MSEC <= DURATION) && (DURATION <
            // SEVEN_DAY_MSEC)) {
            // //7天内显示星期和时间
            // return getSevenDateFormat(date);
            // }
            else if (TWO_DAY_MSEC <= DURATION) { // 大于7天显示年月日
                return getBeforeBeforeYestodayDateFormatAtRecentChatListActivity(date);
            } else {// 当发送过来的消息时间大于本地时间则显示 10:42
                return getTodayDateFormat(date);
            }
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * Title: compareDatetimeBetween5Minutes
     * Description:判断消息是否是5分钟以内
     *
     * @param curentDate
     * @param nextDate
     * @return
     * @date 2015-3-12
     */
    public static Boolean compareDatetimeBetween5Minutes(String curentDate,
                                                         String nextDate) {
        if ((null == curentDate || null == nextDate)
                || ("" == curentDate || "" == nextDate))
            return false;
        Boolean isDisplay = false;
        long FIVE_MINUTE = 1000 * 60 * 5L;

        String currentMsec = convertDateTime2MinuteMsec(curentDate);
        String nextMsec = convertDateTime2MinuteMsec(nextDate);
        try {
            long DURATION = (Long.parseLong(nextMsec) - Long.parseLong(currentMsec));
            if (0 <= DURATION && DURATION <= FIVE_MINUTE) {
                isDisplay = true;
            } else {
                isDisplay = false;
            }
        } catch (Exception e) {
            isDisplay = false;
            ;
        }
        return isDisplay;
    }

    // 本地时间
    private static long localTime = 0;

    // 服务器时间
    private static long serverTime = 0;

    /**
     * 同步时间
     */
    public static void synchrosizedServerTime(String date) {
        Date d = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = format.parse(date);
        } catch (ParseException e) {
            d = new Date();
            Log.i(TAG, "转换时间出错，获取本地时间");
        }
        serverTime = d.getTime();
        localTime = SystemClock.elapsedRealtime();
    }

    /**
     * 获取服务器的时间
     *
     * @return
     */
    public static String getServerTime() {
        Date d = new Date(serverTime + (SystemClock.elapsedRealtime() - localTime));
        if (d.getTime() == SystemClock.elapsedRealtime()) {//当杀了进程之后，再次发送消息时，由于服务器时间被清掉，导致显示时间出错
            d = new Date(System.currentTimeMillis());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(d);
    }


    // public static String getServerTime() {
    // Date d = new Date(serverTime);
    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // return format.format(d);
    // }

    public static String date2String(Date date) {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retStr = dateFormater.format(date);
        return retStr;
    }

    /**
     * @param @param  date
     * @param @return
     * @return int
     * @Methods: getWeekOfYear
     * @Description: TODO(返回当前时间的周数)
     * @author cui.xiaoliang
     * @date 2013-11-27 下午2:29:49
     * @version 1.0.0
     */
    public static int getWeekOfYear(String date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            // 这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
            cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
            cal.setMinimalDaysInFirstWeek(1); // 设置每周最少为1天
            cal.setTime(df.parse(date));
            return cal.get(Calendar.WEEK_OF_YEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param @param  date
     * @param @return
     * @return int
     * @Methods: getWeekOfYear
     * @Description: TODO(返回当前时间的周数)
     * @author cui.xiaoliang
     * @date 2013-11-27 下午2:29:13
     * @version 1.0.0
     */
    public static int getBeforeWeekOfYear(Date date) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
            cal.setMinimalDaysInFirstWeek(1); // 设置每周最少为1天
            cal.setTime(date);
            return cal.get(Calendar.WEEK_OF_YEAR) - 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断当前日期是星期几<br>
     * <br>
     *
     * @param date 要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
    public static int dayForWeek(String date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(date));
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    public static String getBeforeWeekOfYear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date date = new Date(System.currentTimeMillis());
        String week = format.format(date) + "-" + getBeforeWeekOfYear(date);
        return week;
    }

    public static List<String> getOperationStateMonths() {
        List<String> strings = new ArrayList<String>();
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        month++;// 月份从0开始
        if (month == 1) {// 如果当前是1月，则下一月是去年12月
            month = 12;
            year--;
        } else
            month--;
        Log.d(TAG, "上个month: " + month);
        Log.d(TAG, "year: " + year);
        for (int i = 0; i < 12; i++) {
            if (month == 0) {
                year--;
                month = 12;
            }
            String tmp = null;
            if (month < 10) {
                tmp = year + "-" + "0" + month;
            } else {
                tmp = year + "-" + month;
            }
            strings.add(tmp);
            month--;
        }
        return strings;
    }

    public static Calendar getLastDayCalendar() {
        Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.setTime(new Date()); // 设置时间为当前时间
        ca.add(Calendar.DAY_OF_MONTH, -1); // 日减1
        return ca;
    }

    public static Calendar getLastMonthCalendar() {
        Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.setTime(new Date()); // 设置时间为当前时间
        ca.add(Calendar.MONTH, -1); // 月减1
        return ca;
    }

    // 计算两时间差

    /**
     * <p>
     * Title: getHourInterval
     * </p>
     * <p>
     * Description:
     * </p>
     * Get the hour interval
     *
     * @param beginDate
     * @return
     * @throws ParseException
     */
    public static int getHourAndTestMinutesInterval(String beginDate) throws ParseException {
        String endDate = getServerTime();
        Log.i("开始时间 ： ", beginDate);
        Log.i("结束时间 ： ", endDate);
//		String endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
        return getHourAndTestMinutesInterval(beginDate, endDate);
    }

    /**
     * <p>
     * Title: getHourInterval
     * </p>
     * <p>
     * Description:
     * </p>
     * Get time interval by hour.
     *
     * @param beginDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getHourAndTestMinutesInterval(String beginDate,
                                                    String endDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date bDate = format.parse(beginDate);
        Date eDate = format.parse(endDate);
        int hour = -1;
        long lt = eDate.getTime() - bDate.getTime();
        if (lt > 0) {
            /**
             * 增加测试环境开关,测试环境下测试周期为15分钟
             */
//            hour = BuildConfig.DEBUG ? (int) (lt / (Constant.HOUR_TIME_T)) : (int) (lt / (Constant.HOUR_TIME));
            // hour = (int)(lt / (Constant.HOUR_TIME)); //超过24小时
            // hour = (int)(lt / (60*1000)); //test 15分钟
        }
        Log.i("link时间差  ：", String.valueOf(hour));
        return hour;

    }

    // 计算两时间差

    /**
     * <p>
     * Title: getHourInterval
     * </p>
     * <p>
     * Description:
     * </p>
     * Get the hour interval
     *
     * @param beginDate
     * @return
     * @throws ParseException
     */
    public static int getMinutesInterval(String beginDate) {
        String endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(System.currentTimeMillis()));
        try {
            return getMinutesInterval(beginDate, endDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * <p>
     * Title: getHourInterval
     * </p>
     * <p>
     * Description:
     * </p>
     * Get time interval by hour.
     *
     * @param beginDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getMinutesInterval(String beginDate, String endDate)
            throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date bDate = format.parse(beginDate);
        Date eDate = format.parse(endDate);
        int minute = -1;
        long lt = eDate.getTime() - bDate.getTime();
        if (lt > 0) {
            minute = (int) (lt / (60 * 1000)); // 超过24小时
        }
        return minute;
    }

    /**
     * <p>
     * Title: dateToLong
     * </p>
     * <p>
     * Description:时间转换为秒数
     * </p>
     *
     * @param in
     * @return
     */
    public static long dateToLong(String in) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = format.parse(in);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.getTimeInMillis();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }

    }

    public static String getMonthByDate(String date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = dateStr.parse(date);
            calendar.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return String.valueOf(calendar.get(Calendar.MONTH));
    }
}
