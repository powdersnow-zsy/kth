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
import org.rapidoid.annotation.Service;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import static cn.kth.StreamHandler.HDFS_PATH;

@Slf4j
public class StreamTasks implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            Iterator<Map.Entry<String, FSDataOutputStream>> entries = HdfsWriter.INS.getStreams().entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, FSDataOutputStream> entry = entries.next();
                String key = entry.getKey();
                if (key.contains(DateUtil.format(DateUtil.addHour(new Date(), -ConfProperties.getIntegerValue("hours") + 1), "yyyyMMddHH"))) {
                    log.info("close stream:" + key);
                    entry.getValue().flush();
                    entry.getValue().close();
                    entries.remove();
                }
            }

            Topics topics = JsonUtil.fromString(ConfReload.getProperty("topics"), Topics.class);
            for (Topic topic : topics.getTopics()) {
                for (Table ta : topic.getTables()) {
                    String key = ta.getDb() + "_" + ta.getTableName() + "/pt=" + DateUtil.format(DateUtil.addHour(new Date(), 1), "yyyyMMddHH");
                    //开启流
                    if (HdfsWriter.INS.getStreams().get(key) == null) {
                        Configuration conf = new Configuration();
                        String path = HDFS_PATH + key + "/" + UUID.randomUUID().toString();
                        String dir = HdfsUtils.formatHdfsPath(path);
                        FileSystem fs = FileSystem.get(URI.create(dir), conf);
                        FSDataOutputStream outputStream = null;
                        if (HdfsUtils.getInstance().exist(path)) {
                            outputStream = fs.append(new Path(path));
                        } else {
                            outputStream = fs.create(new Path(path), false);

                            HdfsWriter.INS.getStreams().put(key, outputStream);
                            log.info("open stream:" + key);
                        }
                    }

                }
            }


        } catch (IOException e) {
            log.error("e:", e);
        }
    }

}
