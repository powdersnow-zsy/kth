package cn.kth;


import cn.kth.common.bean.Table;
import cn.kth.common.bean.Topic;
import cn.kth.common.bean.Topics;
import cn.kth.common.conf.ConfProperties;
import cn.kth.common.conf.ConfReload;
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
import org.rapidoid.annotation.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class StreamHandler {
    public static final String HDFS_PATH = "/apps/hive/warehouse/ods.db/ods_";

    public static void init() {
        try {
            Topics topics = JsonUtil.fromString(ConfReload.getProperty("topics"), Topics.class);
            for (Topic topic : topics.getTopics()) {
                for (Table ta : topic.getTables()) {
                    for (int i = 0; i < ConfProperties.getIntegerValue("hours"); i++) {
                        //开启流
                        String key = ta.getDb() + "_" + ta.getTableName() + "/pt=" + DateUtil.format(DateUtil.addHour(new Date(), -i + 1), "yyyyMMddHH");

                        Configuration conf = new Configuration();
                        String path = HDFS_PATH + key + "/" + UUID.randomUUID().toString();
                        String dir = HdfsUtils.formatHdfsPath(path);
                        FileSystem fs = FileSystem.get(URI.create(dir), conf);
                        FSDataOutputStream outputStream = null;
                        if (HdfsUtils.getInstance().exist(path)) {
                            outputStream = fs.append(new Path(path));
                        } else {
                            outputStream = fs.create(new Path(path), false);
                        }

                        log.info("init stream:" + key);
                        HdfsWriter.INS.getStreams().put(key, outputStream);

                    }
                }
            }
        } catch (Exception e) {
            log.error("e:", e);
        }
    }
}
