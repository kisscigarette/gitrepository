package com.kisscigarette.app.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 字符串操作工具包
 */
public class StringUtils {
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private final static Pattern ipAddresser = Pattern.compile(IPADDRESS_PATTERN);
    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static final String _BR = "<br/>";
    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 字符串截取
     *
     * @param str
     * @param length
     * @return
     * @throws Exception
     */
    public static String subString(String str, int length) throws Exception {
        if (str == null) {
            return null;
        }
        byte[] bytes = str.getBytes("Unicode");
        int n = 0; // 表示当前的字节数
        int i = 2; // 要截取的字节数，从第3个字节开始
        for (; i < bytes.length && n < length; i++) {
            // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
            if (i % 2 == 1) {
                n++; // 在UCS2第二个字节时n加1
            } else {
                // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                if (bytes[i] != 0) {
                    n++;
                }
            }
        }
        // 如果i为奇数时，处理成偶数
        if (i % 2 == 1) {
            // 该UCS2字符是汉字时，去掉这个截一半的汉字
            if (bytes[i - 1] != 0)
                i = i - 1;
                // 该UCS2字符是字母或数字，则保留该字符
            else
                i = i + 1;
        }
        return new String(bytes, 0, i, "Unicode");
    }

    /**
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * byte to String
     *
     * @param arg
     * @return
     */
    public static String byteToString(byte[] arg) {
        try {
            if (arg != null) {
                return new String(arg, "utf-8");
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算微博内容的长度 1个汉字 == 两个英文字母所占的长度 标点符号区分英文和中文
     *
     * @param c 所要统计的字符序列
     * @return 返回字符序列计算的长度
     */
    public static long calculateWeiboLength(CharSequence c) {

        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int temp = (int) c.charAt(i);
            if (temp > 0 && temp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * 是否包含汉字
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        return pattern.matcher(str).find();
    }

    /**
     * 分割字符串
     *
     * @param str       String 原始字符串
     * @param splitsign String 分隔符
     * @return String[] 分割后的字符串数组
     */
    public static String[] split(String str, String splitsign) {
        int index;
        if (str == null || splitsign == null)
            return null;
        ArrayList<String> al = new ArrayList<String>();
        while ((index = str.indexOf(splitsign)) != -1) {
            al.add(str.substring(0, index));
            str = str.substring(index + splitsign.length());
        }
        al.add(str);
        return (String[]) al.toArray(new String[0]);
    }

    /**
     * 替换字符串
     *
     * @param from   String 原始字符串
     * @param to     String 目标字符串
     * @param source String 母字符串
     * @return String 替换后的字符串
     */
    public static String replace(String from, String to, String source) {
        if (source == null || from == null || to == null)
            return null;
        StringBuffer bf = new StringBuffer("");
        int index = -1;
        while ((index = source.indexOf(from)) != -1) {
            bf.append(source.substring(0, index) + to);
            source = source.substring(index + from.length());
            index = source.indexOf(from);
        }
        bf.append(source);
        return bf.toString();
    }

    /**
     * 替换字符串，能够在HTML页面上直接显示(替换双引号和小于号)
     *
     * @param str String 原始字符串
     * @return String 替换后的字符串
     */
    public static String htmlencode(String str) {
        if (str == null) {
            return null;
        }

        return replace("\"", "&quot;", replace("<", "&lt;", str));
    }

    /**
     * 替换字符串，将被编码的转换成原始码（替换成双引号和小于号）
     *
     * @param str String
     * @return String
     */
    public static String htmldecode(String str) {
        if (str == null) {
            return null;
        }

        return replace("&quot;", "\"", replace("&lt;", "<", str));
    }

    /**
     * 在页面上直接显示文本内容，替换小于号，空格，回车，TAB
     *
     * @param str String 原始字符串
     * @return String 替换后的字符串
     */
    public static String htmlshow(String str) {
        if (str == null) {
            return null;
        }

        str = replace("<", "&lt;", str);
        str = replace(" ", "&nbsp;", str);
        str = replace("\r\n", _BR, str);
        str = replace("\n", _BR, str);
        str = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", str);
        return str;
    }

    /**
     * 返回指定字节长度的字符串
     *
     * @param str    String 字符串
     * @param length int 指定长度
     * @return String 返回的字符串
     */
    public static String toLength(String str, int length) {
        if (str == null) {
            return null;
        }
        if (length <= 0) {
            return "";
        }
        try {
            if (str.getBytes("GBK").length <= length) {
                return str;
            }
        } catch (Exception ex) {
        }
        StringBuffer buff = new StringBuffer();

        int index = 0;
        char c;
        length -= 3;
        while (length > 0) {
            c = str.charAt(index);
            if (c < 128) {
                length--;
            } else {
                length--;
                length--;
            }
            buff.append(c);
            index++;
        }
        buff.append("...");
        return buff.toString();
    }

    /**
     * 获取url的后缀名
     *
     * @return
     */
    public static String getUrlFileName(String urlString) {
        String fileName = urlString.substring(urlString.lastIndexOf("/"));
        fileName = fileName.substring(1, fileName.length());
        if (fileName.equalsIgnoreCase("")) {
            Calendar c = Calendar.getInstance();
            fileName = c.get(Calendar.YEAR) + "" + c.get(Calendar.MONTH) + "" + c.get(Calendar.DAY_OF_MONTH) + "" + c.get(Calendar.MINUTE);

        }
        return fileName;
    }

    public static String replaceSomeString(String str) {
        String dest = "";
        try {
            if (str != null) {
                str = str.replaceAll("\r", "");
                str = str.replaceAll("&gt;", ">");
                str = str.replaceAll("&ldquo;", "“");
                str = str.replaceAll("&rdquo;", "”");
                str = str.replaceAll("&#39;", "'");
                str = str.replaceAll("&nbsp;", "");
                str = str.replaceAll("<br\\s*/>", "\n");
                str = str.replaceAll("&quot;", "\"");
                str = str.replaceAll("&lt;", "<");
                str = str.replaceAll("&lsquo;", "《");
                str = str.replaceAll("&rsquo;", "》");
                str = str.replaceAll("&middot;", "·");
                str = str.replace("&mdash;", "—");
                str = str.replace("&hellip;", "…");
                str = str.replace("&amp;", "×");
                str = str.replaceAll("\\s*", "");
                str = str.trim();
                str = str.replaceAll("<p>", "\n      ");
                str = str.replaceAll("</p>", "");
                str = str.replaceAll("<div.*?>", "\n      ");
                str = str.replaceAll("</div>", "");
                dest = str;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return dest;
    }

    /**
     * 清除文本里面的HTML标签
     *
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        Log.v("htmlStr", htmlStr);
        try {
            Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            Matcher m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            Matcher m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
        } catch (Exception e) {
            // TODO: handle exception
        }

        return htmlStr; // 返回文本字符串
    }

    public static String delSpace(String str) {
        if (str != null) {
            str = str.replaceAll("\r", "");
            str = str.replaceAll("\n", "");
            str = str.replace(" ", "");
        }
        return str;
    }

    /**
     * 检查字符串是否存在值，如果为true,
     *
     * @param str 待检验的字符串
     * @return 当 str 不为 null 或 "" 就返回 true
     */
    public static boolean isNotNull(String str) {
        return (str != null && !"".equalsIgnoreCase(str.trim()));
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static int getDaysFromToday(String sdate) {
        if (sdate.equals("")) {
            return 0;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date endDate = null;
        try {
            endDate = simpleDateFormat.parse(sdate);
            simpleDateFormat.format(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (endDate == null) {
            return 0;
        }

        Date startDate = new Date(); // 今天
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 日期字符串转化为时间戳
     *
     * @param sdate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long dateToTimestamp(String sdate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(sdate);
            simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 将秒转化为时间格式
     *
     * @param seconds
     * @return
     */
    public static String toTime(int seconds) {
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        day = (int) Math.floor(seconds / (24 * 60 * 60));
        hour = (int) (Math.floor(seconds / 3600) - day * 24);
        minute = (int) (Math.floor(seconds / 60) - hour * 60 - day * 24 * 60);
        second= (seconds  - hour * 3600 - day * 24 * 3600 - minute*60);


        //return day>0? + "天" + hour + "小时" + minute + "分钟";
        return (day > 0 ? (day + "天") : "") + (hour > 0 ? (hour + "小时") : "") + (minute > 0 ? (minute + "分钟") : "")   + (second > 0 ? (second + "秒") : "");
    }

    /**
     * 将B转化为KB,MB,GB
     *
     * @param seed 网速B/s
     * @return
     */
    public static int toGMB(float seed) {
        if (seed > (1024 * 1024 * 1024)) {
            //GB/s
            return (int) Math.floor(seed / (1024 * 1024 * 1024));
        } else if (seed > (1024 * 1024)) {
            //MB/s
            return (int) Math.floor(seed / (1024 * 1024));
        } else if (seed > 1024) {
            //KB.s
            return (int) Math.floor(seed / 1024);
        }
        return (int) seed;
    }

    /**
     * 获取网速单位
     *
     * @param seed 网速B/s
     * @return
     */
    public static String getSpeedUnit(float seed) {
        if (seed > (1024 * 1024 * 1024)) {
            //GB/s
            return "GB/s";
        } else if (seed > (1024 * 1024)) {
            //MB/s
            return "MB/s";
        } else if (seed > 1024) {
            //KB.s
            return "KB/s";
        }
        return "B/s";
    }

    /**
     * 将米的形式化为公里的形式，保留一位小数
     *
     * @param meters
     * @return
     */
    public static String metersToKm(float meters) {
        if (meters < 1000) {
            return (int) meters + "米";
        }
        DecimalFormat df = new DecimalFormat("#.0"); //保留一位小数
        return df.format(meters / 1000) + "公里";
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    public static String trimmy(String str) {
        String dest = "";
        if (str != null) {
            str = str.replaceAll("-", "");
            str = str.replaceAll("\\+", "");
            dest = str;
        }
        return dest;
    }

    public static String replaceSpecialChar(String str, String tag) {
        String dest = "";
        if (str != null) {
            str = str.replaceAll(";", "");
            dest = str;
        }
        return dest;
    }

    public static String replaceBlank(String str) {

        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\r");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 防止String空指针
     *
     * @param string
     * @return
     */
    public static String safeString(String string) {
        return string == null ? "" : string;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的Ip地址
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIpAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.trim().length() == 0) {
            return false;
        }
        return ipAddresser.matcher(ipAddress).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断是不是合法手机 handset 手机号码
     */
    public static boolean isHandset(String handset) {
        try {
            if (!handset.substring(0, 1).equals("1")) {
                return false;
            }
            if (handset == null || handset.length() != 11) {
                return false;
            }
            String check = "^[0123456789]+$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(handset);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * 判断输入的字符串是否为纯汉字
     *
     * @param str 传入的字符窜
     * @return 如果是纯汉字返回true, 否则返回false
     */
    public static boolean isChinese(String str) {
        Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为浮点数，包括double和float
     *
     * @param str 传入的字符串
     * @return 是浮点数返回true, 否则返回false
     */
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 是否为空白,包括null和""
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断是否是指定长度的字符串
     *
     * @param text   字符串
     * @param lenght 自定的长度
     * @return
     */
    public static boolean isLenghtStrLentht(String text, int lenght) {
        if (text.length() <= lenght)
            return true;
        else
            return false;
    }

    /**
     * 是否是短信的长度
     *
     * @param text
     * @return
     */
    public static boolean isSMSStrLength(String text) {
        if (text.length() <= 70)
            return true;
        else
            return false;
    }

    /**
     * 判断手机号码是否正确
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {

        String regMobileStr = "^[1][3-9][0-9]{9}$";
        phoneNumber = trimmy(phoneNumber);
        if (phoneNumber.matches(regMobileStr)) {
            return true;
        } else {
            return false;
        }

    }


    public static boolean isPhoneValid(String phoneNumber) {

        String regMobileStr = "^[1][3-8][0-9]{9}$";
        phoneNumber = trimmy(phoneNumber);
        if (phoneNumber.matches(regMobileStr)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 判断名称长度
     *
     * @param name
     * @return
     */
    public static boolean isLengthName(String name) {
        if (name.length() <= 16)
            return true;
        else
            return false;
    }

    // 判断是否为url
    public static boolean checkEmail(String email) {

        Pattern pattern = Pattern.compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 数字字母的组合
     *
     * @param text
     * @return
     */
    public static boolean isNumericAndChar(String text) {
        Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        return pattern.matcher(text).matches();
    }

    // 判断微博分享是否为是否为120个
    public static boolean isShareStrLenth(String text, int length) {
        if (text.length() <= 120)
            return true;
        else
            return false;
    }

    public static String getFileNameFromUrl(String url) {
        // 名字不能只用这个
        // 通过 ‘？’ 和 ‘/’ 判断文件名
//        String extName = "";
//        String filename;
//        int index = url.lastIndexOf('?');
//        if (index > 1) {
//            extName = url.substring(url.lastIndexOf('.') + 1, index);
//        } else {
//            extName = url.substring(url.lastIndexOf('.') + 1);
//        }
//        filename = hashKeyForDisk(url) + "." + extName;

        String[] filename = url.split("filename=");
        return filename[filename.length - 1];
        /*
         * int index = url.lastIndexOf('?'); String filename; if (index > 1) {
		 * filename = url.substring(url.lastIndexOf('/') + 1, index); } else {
		 * filename = url.substring(url.lastIndexOf('/') + 1); }
		 *
		 * if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
		 * filename = UUID.randomUUID() + ".apk";// 默认取一个文件名 } return filename;
		 */
    }

    /**
     * 一个散列方法,改变一个字符串(如URL)到一个散列适合使用作为一个磁盘文件名。
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }



    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel() {
        return Build.BRAND + " " + Build.MODEL;
    }

    /**
     * 获取连接的WiFi的SSID
     *
     * @param context
     * @return
     */
    public static String getWiFiSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID().toString().replace("\"", "");
    }

    /**
     * 获取当前连接WiFi的mac地址
     *
     * @param context
     * @return
     */
    public static String getWiFiMac(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getBSSID() == null ? "" : wifiInfo.getBSSID().toString();
    }

    /**
     * 从InputStream中获取字符串
     *
     * @param inputStream
     * @return
     */
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /*判断当前版本是否支持某一功能*/
    public static boolean judgeVersion(String currentVersion, String lowVersion) {

        String[] curVersion2 = currentVersion.split("T");
        String[] curVersion1 = curVersion2[0].split("\\.");

        String[] lowVersion2 = lowVersion.split("T");
        String[] lowVersion1 = lowVersion2[0].split("\\.");

        String[] curV = concat(curVersion1, curVersion2);
        String[] lowV = concat(lowVersion1, lowVersion2);

        if (lowVersion1.length != curVersion1.length) {
            return false;
        }

        if (lowVersion2.length != curVersion2.length) {
            return false;
        }
        if (curV.length != lowV.length) {
            return false;
        }
        if (Arrays.equals(curV, lowV)) {
            return true;
        }
        for (int i = 0; i < curV.length; i++) {
            if (Float.valueOf(curV[i]) > Float.valueOf(lowV[i])) {
                return true;
            }
            if (Float.valueOf(curV[i]) < Float.valueOf(lowV[i])) {
                return false;
            }
        }
        return true;
    }

    /*数组和并*/
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + 1);
        System.arraycopy(second, second.length - 1, result, first.length, 1);
        return result;
    }

    /**
     * 返回xml样式的数据
     *
     * @return String
     */
    public static String getWeekdays(String weekloopsetting) {

        String result = "";
        String tString = Integer.toBinaryString((Integer.valueOf(weekloopsetting) & 0xFF) + 0x100).substring(1);
        if (TextUtils.isEmpty(tString) || tString.length() != 8) {
            return null;
        }
        for (int i = tString.length() - 1; i > tString.length() - 1 - 7; i--) {
            char item = tString.charAt(i);
            if (item == '1') {
                result += "<font color=\"#FAA773\">" + getWeekDay(i) + "</font>";
            } else {
                result += "<font color=\"#ffc0c0c0\">" + getWeekDay(i) + "</font>";
            }
        }
        return result;
    }

    /**
     * 返回xml样式的数据（状态关）
     *
     * @return String
     */
    public static String getWeekdaysClose(String weekloopsetting) {

        String result = "";
        String tString = Integer.toBinaryString((Integer.valueOf(weekloopsetting) & 0xFF) + 0x100).substring(1);
        if (TextUtils.isEmpty(tString) || tString.length() != 8) {
            return null;
        }
        for (int i = tString.length() - 1; i > tString.length() - 1 - 7; i--) {
            char item = tString.charAt(i);
            if (item == '1') {
                result += "<font color=\"#ffc0c0c0\">" + getWeekDay(i) + "</font>";
            } else {
                result += "<font color=\"#DDDDDD\">" + getWeekDay(i) + "</font>";
            }
        }
        return result;
    }


    public static String getWeekDay(int i) {
        switch (8 - i) {
            case 1:
                return "一 ";
            case 2:
                return "二 ";
            case 3:
                return "三 ";
            case 4:
                return "四 ";
            case 5:
                return "五 ";
            case 6:
                return "六 ";
            case 7:
                return "日 ";
            default:
                return "";
        }
    }


    public static void setWeekday(String weekloopsetting, boolean[] weekdays) {

        String tString = Integer.toBinaryString((Integer.valueOf(weekloopsetting) & 0xFF) + 0x100).substring(1);
        if (TextUtils.isEmpty(tString) || tString.length() != 8) {
            return;
        }
        for (int i = tString.length() - 1; i > tString.length() - 1 - 7; i--) {
            char item = tString.charAt(i);
            if (item == '1') {
                weekdays[7 - i] = true;
            } else {
                weekdays[7 - i] = false;
            }
        }
    }


    public static String getMacString(String mac) {
        return mac.replace(":", "").toUpperCase();
    }


    public static byte[] string2byte(String result) {
        if (!isNumeric(result)) {
            return null;
        }
        byte[] resultbyte = new byte[result.length()];
        for (int i = 0; i < result.length(); i++) {
            int item = Integer.valueOf(result.substring(i, i + 1));
            resultbyte[i] = (byte) item;
        }
        return resultbyte;
    }


    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];

        targets[3] = (byte) (res & 0xff);// 最低位
        targets[2] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[1] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[0] = (byte) ((res >> 24) & 0xff);// 最高位,无符号右移。
        return targets;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }


    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    public static boolean isLockSN(String sn)
    {
        String sn0 = "^[Ss][AaLlRr][A-Za-z0-9][AaBbCcDd][A-Za-z0-9]{7}$";
        Pattern pattern = Pattern.compile(sn0);
        Matcher matcher = pattern.matcher(sn);
        return matcher.matches();
    }


    public static boolean isGateWaySN(String sn) {
        String sn0 = "^[Ss][Rr][A-Za-z0-9][AaBbCcDd][A-Za-z0-9]{7}$";
        Pattern pattern = Pattern.compile(sn0);
        Matcher matcher = pattern.matcher(sn);
        return matcher.matches();
    }




    public static String getErrmsg(int status) {
        String result = null;
        switch (status) {

            case 143:
                result = "升级文件校验失败，请重新升级";
                break;
            case 144:
                result = "升级文件校验失败，请重新升级";
                break;
            case 72:
                result = "网关正忙，请稍后再试";
                break;

            case 76:
                result = "上次升级未完成，请重新升级";
                break;
            case 31:
                result = "钥匙不匹配";
                break;
            case 32:
                result = "蓝牙钥匙已存在";
                break;
            case 33:
                result = "蓝牙钥匙列表已满";
                break;

            case 36:
                result = "NVDS写失败";
                break;
            case 37:
                result = "NVDS删除失败";
                break;
            case 38:
                result = "清空BLE钥匙出错";
                break;
            case 39:
                result = "重置的蓝牙钥匙不存在";
                break;
            case 40:
                result = "修改数字密码不存在";
                break;


            case 138:
                result = "非法用户ID";
                break;
            case 134:
                result = "开门模式设置失败";
                break;
            case 128:
                result = "其他蓝牙错误";
                break;
            case 127:
                result = "IC卡数量已满";
                break;
            case 126:
                result = "该IC卡已存在";
                break;
            case 121:
                result = "找不到该数字密码";
                break;
            case 120:
                result = "数字密码已满";
                break;

            case 125:
                result = "找不到该IC卡索引";
                break;
            case 119:
                result = "该数字密码已存在";
                break;
            case 114:
                result = "未找到该指纹";
                break;
            case 113:
                result = "指纹数量已满";
                break;
            case 115:
                result = "指纹已存在";
                break;
            case 112:
                result = "门锁内部出错";
                break;
            case 111:
                result = "网关主机数据出错";
                break;
            case 109:
                result = "门锁内部超时";
                break;
            case 110:
                result = "门锁正忙";
                break;
            case 75:

                result = "该锁已经复位，如想正常使用，请先解绑再绑定！";
                break;
            case 116:
                result = "指纹配置失败，请重试";
                break;
            case 30:
                result = "门已反锁，请联系管理员";
                break;

            case 151:
                result = "未配置有效数据";
                break;
            case 152:
                result = "卡或密码已满";
                break;
            case 153:
                result = "未找到钥匙";
                break;

            case 255:
                result = "暂不支持此功能";
                break;


            case -1:
                result = "未找到设备，请确保门锁在手机周围";
                break;

            case -2:
                result = "配置超时，请重新尝试";
                break;
        }
        return result;
    }


    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }
    public static String bytesToMacString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[bytes.length - i - 1]);
            if (i > 0) {
                sb.append(":");
            }
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


}
