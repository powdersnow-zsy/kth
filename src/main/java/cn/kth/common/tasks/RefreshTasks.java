package cn.kth.common.tasks;

import cn.kth.common.bean.Table;
import cn.kth.common.bean.Topic;
import cn.kth.common.bean.Topics;
import cn.kth.common.conf.ConfProperties;
import cn.kth.common.conf.ConfReload;
import cn.kth.common.hive.HiveDataSource;
import cn.kth.common.util.DateUtil;
import cn.kth.common.util.HdfsUtils;
import cn.kth.common.util.JsonUtil;
import cn.kth.writer.HdfsWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import static cn.kth.StreamHandler.HDFS_PATH;

@Slf4j
public class RefreshTasks implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Connection conn = null;
        try {
            conn = HiveDataSource.getConnection();

            Topics topics = JsonUtil.fromString(ConfReload.getProperty("topics"), Topics.class);
            for (Topic topic : topics.getTopics()) {
                for (Table ta : topic.getTables()) {
                    for (int i = 0; i < ConfProperties.getIntegerValue("hours"); i++) {
                        log.info("hours:" + ConfProperties.getIntegerValue("hours") + ",caculate:" + (-(ConfProperties.getIntegerValue("hours") + i + 1)));
                        String time = DateUtil.format(DateUtil.addHour(new Date(), -(ConfProperties.getIntegerValue("hours") + i + 1)), "yyyyMMddHH");
                        String sql = "alter table ods.ods_" + ta.getDb() + "_" + ta.getTableName() + " add if not exists partition (pt='" + time + "')";
                        log.info(sql);
                        HiveDataSource.refresh(conn, sql);
                    }

                }
            }

        } catch (SQLException e) {
            log.error("e:", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("e:", e);
                }
            }
        }
    }

}
