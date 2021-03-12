package cn.kth.common.conf;

import cn.kth.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Slf4j
public class ConfProperties {

    private static Properties props = null;

    private static boolean isInit = false;

    private static Logger logger = LoggerFactory.getLogger(ConfProperties.class);

    public static void init() {
        if (!isInit) {
            try {

                FileInputStream in = new FileInputStream(System.getProperty("user.dir") + "/conf/conf-"
                        + Env.environment + ".properties");
                Reader f = new InputStreamReader(in);
                props = new Properties();
                props.load(f);


            } catch (IOException e) {
                log.error("e:",e);
            }
            isInit = true;
        }

    }

    public static String getStringValue(String key) {
        ConfProperties.init();
        return props.getProperty(key);
    }

    public static Integer getIntegerValue(String key) {
        String stringValue = getStringValue(key);
        if (StringUtil.isEmpty(stringValue)) {
            return null;
        }
        return Integer.parseInt(stringValue);
    }

    public static Long getLongValue(String key) {
        String stringValue = getStringValue(key);
        if (StringUtil.isEmpty(stringValue)) {
            return null;
        }
        return Long.parseLong(stringValue);
    }

    public static Boolean getBooleanValue(String key) {
        return Boolean.parseBoolean(getStringValue(key));
    }

    public static Date getDateValue(SimpleDateFormat format, String key) throws ParseException {
        return format.parse(getStringValue(key));
    }

    public static Properties getAll() {
        return props;
    }

}
