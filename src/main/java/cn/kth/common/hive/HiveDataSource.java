package cn.kth.common.hive;


import cn.kth.common.conf.ConfProperties;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.rapidoid.annotation.Service;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
public class HiveDataSource {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    private static Map<Integer, Connection> connectionMap = new HashMap<>();

    private static String zookeeperQuorum;

    private static String user;

    private static String passwd;

    static {
        user = ConfProperties.getStringValue("hive.user");
        passwd = ConfProperties.getStringValue("hive.passwd");
        zookeeperQuorum = ConfProperties.getStringValue("zk.full.host");

    }

    /**
     * 获取hive数据库链接
     *
     * @return
     * @throws SQLException
     */
    public synchronized static Connection getConnection() throws SQLException {
        Random random = new Random();
        int index = random.nextInt(5);
        Connection conn = connectionMap.get(index);
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName(driverName);
                conn = DriverManager.getConnection("jdbc:hive2://" + zookeeperQuorum + "/ods"
                                + ";serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2",
                        user, passwd);
                connectionMap.put(index, conn);
                return conn;
            } catch (Exception e) {
                log.error("e:", e);
            }
        }
        return conn;
    }


    public static void refresh(Connection conn, String sql) {


        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.execute();
        } catch (SQLException e) {
            log.error("e:", e);
        }

    }

}
