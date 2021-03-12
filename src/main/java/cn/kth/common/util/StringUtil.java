package cn.kth.common.util;import org.apache.commons.lang.StringUtils;import javax.crypto.Cipher;import javax.crypto.SecretKey;import javax.crypto.SecretKeyFactory;import javax.crypto.spec.DESKeySpec;import javax.crypto.spec.IvParameterSpec;import javax.servlet.http.HttpServletRequest;import java.math.BigDecimal;import java.text.DecimalFormat;import java.text.NumberFormat;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import java.util.regex.Matcher;import java.util.regex.Pattern;public class StringUtil extends StringUtils {    // 一个空的字符串。    public static final String        EMPTY_STRING          = "";    // 逗号    public static final String        SYMBOL_COMMA          = ",";    // 等于号    public static final String        SYMBOL_EQUAL          = "=";    // 点号    public static final String        SYMBOL_DOT            = ".";    // 问号    public static final String        SYMBOL_QUESTION       = "?";    // 分号    public static final String        SYMBOL_SEMICOLON      = ";";    // 左括号    public static final String        SYMBOL_LEFT_BRACKETS  = "(";    // 右括号    public static final String        SYMBOL_RIGHT_BRACKETS = ")";    public static final String        SYMBOL_AND            = "&";    public static Map<String, String> contractMappingTable  = new HashMap<String, String>();    /**     * 判断一个传入的字符串是否包含英文字母。 ps: 传入null认为不包含英文字母。     *     * @param words 传入的源字符串。     * @return     */    public static boolean containEnglish(String word) {        if (word == null) {            return false;        }        for (int i = 0; i < word.length(); i++) {            if ((word.charAt(i) >= 'A' && word.charAt(i) <= 'Z') || (word.charAt(i) >= 'a' && word.charAt(i) <= 'z')) {                return true;            }        }        return false;    }    /**     * 如果传入的值是null，返回空字符串，如果不是null，返回本身。     *     * @param word 传入的源字符串。     * @return     */    public static String getNotNullValue(String word) {        return (word == null || word.equalsIgnoreCase("null")) ? "" : word;    }    /**     * 如果传入的值是null，返回空字符串，如果不是null，返回本身。     *     * @param word 传入的源字符串。     * @return     */    public static String getNotNullValue(String word, String defaultWord) {        return (word == null || word.equalsIgnoreCase("null")) ? defaultWord : word;    }    /**     * 根据分隔符从一段字符串拿到对应的列表。应用于以下场景。 2,3,4,5 ==> [2,3,4,5]     *     * @param originWord     * @param symbol     * @return     */    public static List<String> getSplitListFromString(String originWord, String symbol) {        List<String> result = new ArrayList<String>();        if (isBlank(originWord)) {            return result;        }        String[] splitData = originWord.split(symbol);        if (splitData == null || splitData.length == 0) {            return result;        }        for (String word : splitData) {            if (isNotBlank(word)) {                result.add(word);            }        }        return result;    }    /**     * 移除左边的0, eg：00000jakjdkf89000988000 转换之后变为 jakjdkf89000988000     *     * @param str     * @return     */    public static String removeLeftZero(String str) {        int start = 0;        if (isNotEmpty(str)) {            char[] chars = str.toCharArray();            for (int i = 0; i < chars.length; i++) {                if (chars[i] != '0') {                    start = i;                    break;                }            }            return str.substring(start);        }        return "";    }    /**     * 判断一个传入的字符串是数字和字母的混合 ps: 传入null返回false。     *     * @param words 传入的源字符串。     * @return     */    public static boolean isNumberAndLetter(String word) {        if (word == null) {            return false;        }        for (int i = 0; i < word.length(); i++) {            boolean isLetter = (word.charAt(i) >= 'A' && word.charAt(i) <= 'Z')                               || (word.charAt(i) >= 'a' && word.charAt(i) <= 'z');            boolean isNumber = word.charAt(i) >= '0' && word.charAt(i) <= '9';            if (!(isLetter || isNumber)) {                return false;            }        }        return true;    }    /**     * 判断一个传入的字符串是否全是英文 ps: 传入null返回false。     *     * @param words 传入的源字符串。     * @return     */    public static boolean isEnglishWord(String word) {        if (word == null) {            return false;        }        for (int i = 0; i < word.length(); i++) {            if (!Character.isLetter(word.charAt(i))) {                return false;            }        }        return true;    }    /**     * 判断一个传入的字符串是数字 ps: 传入null返回false。     *     * @param words 传入的源字符串。     * @return     */    public static boolean isNumber(String word) {        if (isBlank(word)) {            return false;        }        for (int i = 0; i < word.length(); i++) {            if (!Character.isDigit(word.charAt(i))) {                return false;            }        }        return true;    }    public static String getAuditStatus(String auditStatus) {        if (StringUtil.isNotBlank(auditStatus)) {            if (auditStatus.indexOf("_") != -1) {                return contractMappingTable.get(auditStatus.substring(auditStatus.indexOf("_")));            }        }        return "";    }    /**     * 格式化大数据为len个小数     *     * @param obj     * @param len     * @return     */    public static String formatLagerNumberToStr(Object obj, int len) {        if (obj != null) {            if (obj instanceof String) {                String str = String.valueOf(obj);                return str.substring(0, str.indexOf(".") + len + 1);            } else {                StringBuffer pattern = new StringBuffer("0");                for (int i = 0; i < len; i++) {                    if (i == 0) {                        pattern.append(".0");                        continue;                    }                    pattern.append("0");                }                return new DecimalFormat(pattern.toString()).format(obj);            }        }        return "";    }    /**     * DES加密，返回大写的16进制的串     *     * @param val 原始值     * @param key 密钥     * @return     * @throws Exception     */    public static String encryptToDesHexString(Object val, String key) {        String ret = "";        String src = String.valueOf(val);        if (isBlank(src) || isBlank(key)) {            return ret;        }        try {            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);            return toHexString(cipher.doFinal(src.getBytes("UTF-8"))).toUpperCase();        } catch (Exception e) {            e.printStackTrace();        }        return ret;    }    /**     * 转16进制     *     * @param src     * @return     */    public static String toHexString(byte src[]) {        StringBuffer res = new StringBuffer();        for (int i = 0; i < src.length; i++) {            String plainText = Integer.toHexString(0xff & src[i]);            if (plainText.length() < 2) plainText = "0" + plainText;            res.append(plainText);        }        return res.toString();    }    /**     * String数组转字符串，以逗号为分割符     *     * @param str     * @return     */    public static String arrayToString(String str[]) {        if (str == null) {            return null;        }        StringBuffer sb = new StringBuffer();        int length = str.length;        for (int i = 0; i < length; i++) {            sb.append(str[i]);            if (i < length - 1) {                sb.append(",");            }        }        return sb.toString();    }    /**     * 将数字类型的工号补全到六位     *     * @param src     * @return     */    public static String ifNumToSixLength(String work_no) {        if (work_no.length() > 5 || Character.isLetter(work_no.charAt(0))) return work_no;        int len = work_no.length();        StringBuffer res = new StringBuffer(work_no);        for (int i = len; i < 6; i++) {            res.insert(0, '0');        }        return res.toString();    }    /**     * 格式化数字。返回的数字字符串取消千分位输出。     * <p>     * setGroupingUsed(true)，如123456.55，输出1,233.55。<br>     * setGroupingUsed(false)，如1233.55，输出1233.55。     * </p>     *     * @param d     * @return     */    public static String cancelGroupingNumber(Double d) {        if (d == null) {            return "";        }        NumberFormat nf = NumberFormat.getInstance();        nf.setGroupingUsed(false);        return nf.format(d);    }    /**     * 格式化数字。返回的数字字符串含有千分位     *     * @param d     * @return     */    public static String formatGroupingNumber(Double d) {        if (d == null) {            return "";        }        NumberFormat nf = NumberFormat.getInstance();        return nf.format(d);    }    /**     * 格式化数字。返回的数字字符串含有千分位     *     * @param d     * @return     */    public static String formatGroupingNumber(String s) {        if (StringUtil.isBlank(s)) {            return "";        }        Double d = 0d;        try {            d = Double.valueOf(s);        } catch (Exception e) {            e.printStackTrace();        }        NumberFormat nf = NumberFormat.getInstance();        return nf.format(d);    }    /**     * * 将double格式化为指定小数位数的字符串。     * <p>     * 如precision=2,<br>     * 3.156返回3.16<br>     * 3.154返回3.15<br>     * 3.0返回3.00     * </p>     *     * @param number 需要格式化的数字     * @param precision 小数位数。不能大于10或者小于0     * @return     */    public static String formatDouble(Double number, int precision) {        if (number == null) {            return "";        }        if (precision < 0 || precision > 10) {            precision = 2;        }        return String.format("%." + precision + "f", number);    }    /**     * * 将double格式化为待2位小数数的字符串。     * <p>     * 3.156返回3.16<br>     * 3.154返回3.15<br>     * 3.0返回3.00     * </p>     *     * @param number 需要格式化的数字     * @return     */    public static String formatDouble(Double d) {        return formatDouble(d, 2);    }    /**     * 对double四舍五入，小数精度通过precision指定     *     * @param number     * @param precision     * @return     */    public static double round(Double number, int precision) {        if (number == null) {            return 0;        }        if (precision < 0) {            precision = 2;        }        BigDecimal decimal = new BigDecimal(Double.toString(number)).setScale(precision, BigDecimal.ROUND_HALF_UP);        return decimal.doubleValue();    }    /**     * 对double四舍五入，保留2位小数     *     * @param number     * @return     */    public static double round(Double number) {        return round(number, 2);    }    public static String formatToCurrency(Object o) {        return o == null ? null : String.format("%1$,.2f", o);    }    public static String formatFloat(Object number, int precision) {        if (number == null) {            return "";        }        if (precision < 0 || precision > 10) {            precision = 2;        }        return String.format("%." + precision + "f", number);    }    public static String getStringForObject(Object obj) {        if (null == obj) {            return null;        }        return obj.toString();    }    public static String getParameterByKey(HttpServletRequest request, String key) {        if (null == request || StringUtil.isEmpty(key)) {            return null;        }        String value = request.getParameter(key);        if (StringUtil.isEmpty(value)) {            return null;        }        return value;    }    /**     * 手机号验证     *     * @param str     * @return 验证通过返回true     */    public static boolean isMobile(String str) {        Pattern p = null;        Matcher m = null;        boolean b = false;        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号        m = p.matcher(str);        b = m.matches();        return b;    }    /**     * 判断Integer是否为空     *     * @param a     * @return 验证通过返回true     */    public static boolean isEmptyForIntger(Integer intger) {        boolean b = false;        if (intger == null || intger == 0) {            b = true;            return b;        }        return b;    }    /**     * 获取百分数 numerator/denominator:分子/分母     *     * @return     */    public static String getPercentum(int numerator, int denominator) {        if (denominator == 0 || numerator == 0) {            return "0";        }        NumberFormat format = NumberFormat.getPercentInstance();// 获取格式化类实例        format.setMinimumFractionDigits(2);// 设置小数位        String result = format.format(numerator / Double.valueOf(denominator));        return result;    }    /**     * 判断字符串是否为空串，空格，tab等     *     * @author simier     * @Date 2015 下午6:32:42     */    public static boolean isNotEmpty(String str) {        if (StringUtils.isNotBlank(str) && StringUtils.isNotEmpty(str)) {            return true;        } else {            return false;        }    }    /**     * 判断字符串是否为空串     *     * @author simier     * @Date 2015 下午5:07:29     */    public static boolean isEmpty(String str) {        if (StringUtils.isNotEmpty(str) && StringUtils.isNotEmpty(str.trim())) {            return false;        } else {            return true;        }    }}