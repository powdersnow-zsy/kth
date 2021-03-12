package cn.kth.common.util;import java.text.ParseException;import java.text.SimpleDateFormat;import java.util.Calendar;import java.util.Date;import java.util.GregorianCalendar;public class DateUtil {    /**     * 计算两个时间之间相差多少秒(无视顺序)     *     * @param date1     * @param date2     * @return     */    public static long diffSec(Date date1, Date date2) {        Calendar cal = Calendar.getInstance();        cal.setTime(date1);        long time1 = cal.getTimeInMillis();        cal.setTime(date2);        long time2 = cal.getTimeInMillis();        long diff = time2 - time1;        if (diff < 0) {            diff = diff * -1;        }        long sec = diff / (1000);        return sec;    }    // 获得当天0点的时间戳    public static int getTimesmorning() {        Calendar cal = Calendar.getInstance();        cal.set(Calendar.HOUR_OF_DAY, 0);        cal.set(Calendar.SECOND, 0);        cal.set(Calendar.MINUTE, 0);        cal.set(Calendar.MILLISECOND, 0);        return (int) (cal.getTimeInMillis() / 1000);    }    // 获得当天24点的时间戳    public static int getTimesnight() {        Calendar cal = Calendar.getInstance();        cal.set(Calendar.HOUR_OF_DAY, 24);        cal.set(Calendar.SECOND, 0);        cal.set(Calendar.MINUTE, 0);        cal.set(Calendar.MILLISECOND, 0);        return (int) (cal.getTimeInMillis() / 1000);    }    // 获得本周一0点时间    public static int getTimesWeekmorning() {        Calendar cal = Calendar.getInstance();        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);        return (int) (cal.getTimeInMillis() / 1000);    }    // 获得本周日24点时间    public static int getTimesWeeknight() {        Calendar cal = Calendar.getInstance();        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);        return (int) ((cal.getTime().getTime() + (7 * 24 * 60 * 60 * 1000)) / 1000);    }    // 将当天0点的时间戳转换成yyyy-MM-dd HH:mm:ss字符串    public static String formatmorningTime() {        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        String date = sdf.format(new Date(getTimesmorning() * 1000L));        return date;    }    // 获得当天24点的时间戳转换成yyyy-MM-dd HH:mm:ss字符串    public static String formatnightTime() {        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        String date = sdf.format(new Date(getTimesnight() * 1000L));        return date;    }    // 获得周一0点的时间戳转换成yyyy-MM-dd HH:mm:ss字符串    public static String formatMondayTime() {        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        String date = sdf.format(new Date(getTimesWeekmorning() * 1000L));        return date;    }    // 获得周日24点的时间戳转换成yyyy-MM-dd HH:mm:ss字符串    public static String formatWeekdayTime() {        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        String date = sdf.format(new Date(getTimesWeeknight() * 1000L));        return date;    }    /**     * 获取当前日期(默认格式："yyyyMMdd" 即20150728)     *     * @return     */    public static String formatCurrentDate(String dateFormat) {        String result = null;        if (StringUtil.isEmpty(dateFormat)) {            dateFormat = "yyyyMMdd";        }        Date date = new Date();        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);        result = sdf.format(date);        return result;    }    /**     * 获取前一天的日期(默认格式："yyyyMMdd" 即20150728)     *     * @return     */    public static String formatYesterdayDate(String dateFormat) {        String result = null;        if (StringUtil.isEmpty(dateFormat)) {            dateFormat = "yyyyMMdd";        }        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);        Date beginDate = new Date();        Calendar date = Calendar.getInstance();        date.setTime(beginDate);        date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);        Date endDate;        try {            endDate = sdf.parse(sdf.format(date.getTime()));            result = sdf.format(endDate);        } catch (ParseException e) {            e.printStackTrace();        }        return result;    }    /**     * @param str     * @return     */    public static Date StringToDate(String str) {        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");        Date date = null;        try {            date = format.parse(str);        } catch (ParseException e) {            e.printStackTrace();        }        return date;    }    /**     * 按照指定的格式把字符串转换成时间，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS     *     * @param str     字符串     * @param pattern 格式     * @return 时间     */    public static Date StringToDate(String str, String pattern) {        if (StringUtil.isEmpty(str)) {            return null;        }        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);        Date date = null;        try {            date = dateFormat.parse(str);        } catch (ParseException e) {        }        return date;    }    // 获得当天0点的时间    public static Long getStartTime() {        Calendar todayStart = Calendar.getInstance();        todayStart.set(Calendar.HOUR, 0);        todayStart.set(Calendar.MINUTE, 0);        todayStart.set(Calendar.SECOND, 0);        todayStart.set(Calendar.MILLISECOND, 0);        return todayStart.getTime().getTime();    }    // 获得当天24点的时间    public static Long getEndTime() {        Calendar todayEnd = Calendar.getInstance();        todayEnd.set(Calendar.HOUR, 23);        todayEnd.set(Calendar.MINUTE, 59);        todayEnd.set(Calendar.SECOND, 59);        todayEnd.set(Calendar.MILLISECOND, 999);        return todayEnd.getTime().getTime();    }    /**     * 获取某天的起始时间, e.g. 2005-10-01 00:00:00.000     *     * @param date 日期对象     * @return 该天的起始时间     */    public static Date getStartDate(Date date) {        if (date == null) {            return null;        }        Calendar cal = Calendar.getInstance();        cal.setTime(date);        cal.set(Calendar.HOUR_OF_DAY, 0);        cal.set(Calendar.MINUTE, 0);        cal.set(Calendar.SECOND, 0);        cal.set(Calendar.MILLISECOND, 0);        return cal.getTime();    }    /**     * 获取某天的结束时间, e.g. 2005-10-01 23:59:59.999     *     * @param date 日期对象     * @return 该天的结束时间     */    public static Date getEndDate(Date date) {        if (date == null) {            return null;        }        Calendar cal = Calendar.getInstance();        cal.setTime(date);        return new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59,                59);    }    /**     * 获取某月的起始时间, e.g. 2005-10-01 00:00:00.000     *     * @param date 日期对象     * @return 该天的起始时间     */    public static Date getStartMonth(Date date) {        Calendar cal = Calendar.getInstance();        cal.setTime(date);        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));        return getStartDate(cal.getTime());    }    public static Date getEndMonth(Date date) {        Calendar cal = Calendar.getInstance();        cal.setTime(date);        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));        return getEndDate(cal.getTime());    }    /**     * 时间转换     *     * @param date     * @param format 格式     * @return     */    public static String format(Date date, String format) {        SimpleDateFormat sf = new SimpleDateFormat(format);        return sf.format(date);    }    /**     * 获取指定格式日期的第二天的日期     *     * @param str(例：2015-09-22)     * @return (例 ： 2015 - 09 - 23)     */    public static String getTomorrowDate(String str) {        if (StringUtil.isEmpty(str)) {            return null;        }        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");        Date date = null;        try {            date = sdf.parse(str);        } catch (ParseException e) {            e.printStackTrace();        }        Calendar cal = Calendar.getInstance();        cal.setTime(date);        cal.add(Calendar.DATE, 1);        return sdf.format(cal.getTime());    }    /**     * 获取指定格式日期的前一天的日期     *     * @param str(例：2015-09-22)     * @return (例 ： 2015 - 09 - 23)     */    public static String getYesterdayDate(String str) {        if (StringUtil.isEmpty(str)) {            return null;        }        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");        Date date = null;        try {            date = sdf.parse(str);        } catch (ParseException e) {            e.printStackTrace();        }        Calendar cal = Calendar.getInstance();        cal.setTime(date);        cal.add(Calendar.DATE, -1);        return sdf.format(cal.getTime());    }    /**     * 得到当前时间＋addDay（天）的时间     *     * @param addDay     * @return     */    public static Date getDate(int addDay) {        Calendar cal = Calendar.getInstance();        cal.setTime(new Date());        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + addDay);        return cal.getTime();    }    /**     * 取得指定小时后的时间     *     * @param date      基准时间     * @param dayAmount 指定小时，允许为负数     * @return 指定天数后的时间     */    public static Date addHour(Date date, int dayAmount) {        if (date == null) {            return null;        }        Calendar calendar = Calendar.getInstance();        calendar.setTime(date);        calendar.add(Calendar.HOUR, dayAmount);        return calendar.getTime();    }    /**     * 取得指定天数后的时间     *     * @param date      基准时间     * @param dayAmount 指定天数，允许为负数     * @return 指定天数后的时间     */    public static Date addDay(Date date, int dayAmount) {        if (date == null) {            return null;        }        Calendar calendar = Calendar.getInstance();        calendar.setTime(date);        calendar.add(Calendar.DATE, dayAmount);        return calendar.getTime();    }    /**     * 得到当前时间＋addDay（月）的时间     *     * @param addDay     * @return     */    public static Date addMonth(Date date, int monthAmount) {        Calendar cal = Calendar.getInstance();        cal.setTime(date);        cal.add(Calendar.MONTH, monthAmount);        return cal.getTime();    }    /**     * 得到当前时间 + addYear(年) 的时间     *     * @param date     * @param monthAmount     * @return     */    public static Date addYear(Date date, int monthAmount) {        Calendar cal = Calendar.getInstance();        cal.setTime(date);        cal.add(Calendar.YEAR, monthAmount);        return cal.getTime();    }    /**     * 取得指定分钟数后的时间     *     * @param date         基准时间     * @param minuteAmount 指定分钟数，允许为负数     * @return 指定分钟数后的时间     */    public static Date addMinute(Date date, int minuteAmount) {        if (date == null) {            return null;        }        Calendar calendar = Calendar.getInstance();        calendar.setTime(date);        calendar.add(Calendar.MINUTE, minuteAmount);        return calendar.getTime();    }    /**     * 获取从当前时间到当日最后时刻的秒数     *     * @author simier     * @Date 2015 下午6:58:43     */    public static int get2TodayEndSeconds() {        Calendar c = Calendar.getInstance();        c.set(Calendar.HOUR_OF_DAY, 23);        c.set(Calendar.MINUTE, 59);        c.set(Calendar.SECOND, 59);        c.set(Calendar.MILLISECOND, 999);        long diff = c.getTimeInMillis() - System.currentTimeMillis();        return (int) (diff / 1000);    }    /**     * 获取从当前时间到本周末最后时刻的秒数     *     * @author simier     * @Date 2015 下午7:21:07     */    public static int get2ThisWeebEndSeconds() {        Calendar c = Calendar.getInstance();        c.set(Calendar.DAY_OF_WEEK, 7);        c.add(Calendar.DAY_OF_MONTH, 1);        c.set(Calendar.HOUR_OF_DAY, 23);        c.set(Calendar.MINUTE, 59);        c.set(Calendar.SECOND, 59);        c.set(Calendar.MILLISECOND, 999);        long diff = c.getTimeInMillis() - System.currentTimeMillis();        return (int) (diff / 1000);    }    public static int get30daysEndSeconds() {        Calendar c = Calendar.getInstance();        c.add(Calendar.MONTH, 1);        long diff = c.getTimeInMillis() - System.currentTimeMillis();        return (int) (diff / 1000);    }    /**     * 得到昨天的日期</br>     * 格式：20150110     *     * @return     */    public static String getYesterdayString() {        Calendar c = Calendar.getInstance();        c.add(Calendar.DAY_OF_MONTH, -1);        int year = c.get(Calendar.YEAR);        int month = c.get(Calendar.MONTH) + 1;        int day = c.get(Calendar.DAY_OF_MONTH);        String today = year + (month >= 10 ? month + "" : "0" + month) + (day >= 10 ? day + "" : "0" + day);        return today;    }    /**     * 得到两个日期之间的天数     *     * @param startDate     * @param endDate     * @return     * @throws ParseException     */    public static int daysBetween(Date startDate, Date endDate) {        try {            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");            startDate = sdf.parse(sdf.format(startDate));            endDate = sdf.parse(sdf.format(endDate));            Calendar cal = Calendar.getInstance();            cal.setTime(startDate);            long time1 = cal.getTimeInMillis();            cal.setTime(endDate);            long time2 = cal.getTimeInMillis();            long between_days = (time2 - time1) / (1000 * 3600 * 24);            return Integer.parseInt(String.valueOf(between_days)) + 1;        } catch (Exception e) {            e.printStackTrace();        }        return -1;    }    /**     * 得到指定时间是星期几     *     * @param date     * @return     */    public static int getWeek(Date date) {        Calendar c = Calendar.getInstance();        c.setTime(date);        return c.get(Calendar.DAY_OF_WEEK);    }    public static Date getClearTimeDate(Date date) {        Calendar c = Calendar.getInstance();        c.setTime(date);        c.set(Calendar.HOUR_OF_DAY, 0);        c.set(Calendar.MILLISECOND, 0);        c.set(Calendar.MINUTE, 0);        c.set(Calendar.SECOND, 0);        return c.getTime();    }    public static Date getClearTimeDateEndMonth(Date date) {        Calendar c = Calendar.getInstance();        c.setTime(date);        c.set(Calendar.DAY_OF_MONTH, 1);        c.set(Calendar.HOUR_OF_DAY, 0);        c.set(Calendar.MILLISECOND, 0);        c.set(Calendar.MINUTE, 0);        c.set(Calendar.SECOND, 0);        return c.getTime();    }    public static Date getClearTimeDateEndYear(Date date) {        Calendar c = Calendar.getInstance();        c.setTime(date);        c.set(Calendar.MONTH, 0);        c.set(Calendar.DAY_OF_MONTH, 1);        c.set(Calendar.HOUR_OF_DAY, 0);        c.set(Calendar.MILLISECOND, 0);        c.set(Calendar.MINUTE, 0);        c.set(Calendar.SECOND, 0);        return c.getTime();    }    private static int getMondayPlus() {        Calendar cd = Calendar.getInstance();        int dayOfWeek = cd.get(7) - 1;        if (dayOfWeek == 1) {            return 0;        }        return (1 - dayOfWeek);    }    /**     * 获取上周一日期     *     * @return     */    public static String getPreviousWeekday() {        int weeks = -1;        int mondayPlus = getMondayPlus();        GregorianCalendar currentDate = new GregorianCalendar();        currentDate.add(5, mondayPlus + 7 * weeks);        Date monday = currentDate.getTime();        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");        String preMonday = sdf.format(monday);        return preMonday;    }    // 获取昨天的日期    public static Date getDateYesterday() {        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");        String format = sdf.format(getDate(-1));        return StringToDate(format);    }    // 得到前天的日期    public static String getDateTheDayBeforeYesterday(String format) {        SimpleDateFormat formatter = new SimpleDateFormat(format);        return formatter.format(getDate(-2));    }    // 获取昨天对应的年月    public static String getMonthYesterday(String fomart) {        SimpleDateFormat formatter = new SimpleDateFormat(fomart);        return formatter.format(getDate(-1));    }    public static Date chooseDate(int i) // //获取前后日期 i为正数 向后推迟i天，负数时向前提前i天    {        Date dat = getDate(i);        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");        try {            return dformat.parse(dformat.format(dat));        } catch (ParseException e) {            e.printStackTrace();        }        return null;    }    public static String getLastWeek() {        Calendar cd = Calendar.getInstance();        String str = getPreviousWeekday();        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");        try {            cd.setTime(dformat.parse(str));            int year = cd.get(Calendar.YEAR);            int month = cd.get(Calendar.MONTH) + 1;            int week = cd.get(Calendar.WEEK_OF_YEAR);            if (month < 10) {                return new StringBuffer().append(year).append("0").append(month).append(week).toString();            }            return new StringBuffer().append(year).append(month).append(week).toString();        } catch (ParseException e) {            e.printStackTrace();        }        return null;    }    public static String getTheWeekBeforeLast() {        Date d = chooseDate(-14);        Calendar cd = Calendar.getInstance();        cd.setTime(d);        int year = cd.get(Calendar.YEAR);        int month = cd.get(Calendar.MONTH) + 1;        int week = cd.get(Calendar.WEEK_OF_YEAR);        if (month < 10) {            return new StringBuffer().append(year).append("0").append(month).append(week).toString();        }        return new StringBuffer().append(year).append(month).append(week).toString();    }    public static int getWeek() {        Calendar c = Calendar.getInstance();        int month = c.get(Calendar.MONTH) + 1;        if (month < 10) {            return Integer.valueOf(new StringBuffer().append(c.get(Calendar.YEAR)).append("0").append(month).append(c.get(Calendar.WEEK_OF_YEAR)).toString());        }        return Integer.valueOf(new StringBuffer().append(c.get(Calendar.YEAR)).append(month).append(c.get(Calendar.WEEK_OF_YEAR)).toString());    }    public static String getMonth(Date date) {        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");        Calendar cal = Calendar.getInstance();        cal.setTime(date);        return sdf.format(cal.getTime());    }    // 获取上周对应年月    public static int getMonthLastWeek() {        return Integer.valueOf(getMonth(getDate(-7)));    }    public static int getLastMonth() {        return Integer.valueOf(getMonth(getStartMonth(addMonth(new Date(), -1))));    }    public static String getFutureMonthLastTime(int month) {        Date date = getStartMonth(new Date());        Calendar cal = Calendar.getInstance();        cal.setTime(date);        cal.add(Calendar.MONTH, month);        cal.setTime(cal.getTime());        cal.add(Calendar.MILLISECOND, -1);        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        return sdf.format(cal.getTime());    }    public static void main(String[] args) {        System.out.println(getYesterdayString());    }}