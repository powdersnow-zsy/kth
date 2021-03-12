package cn.kth.common.conf;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

@Slf4j
public class ConfReload {


    private static PropertiesConfiguration config;

    static {
        try {
            //实例化一个PropertiesConfiguration
            config = new PropertiesConfiguration(System.getProperty("user.dir") + "/conf/reloadConf.properties");
            //设置reload策略，这里用当文件被修改之后reload（默认5s中检测一次）
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException e) {
            log.error("e:",e);
        }
    }

    public static synchronized String getProperty(String key) {
        return StringUtils.join(config.getStringArray(key), ',');
    }

    public static synchronized ArrayList<String> getListProperty(String key) {
        return (ArrayList<String>) config.getProperty(key);
    }

}
