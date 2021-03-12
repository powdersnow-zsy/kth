package cn.kth.writer;

import cn.kth.common.bean.BinlogBean;
import cn.kth.common.bean.Topic;
import cn.kth.common.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FSDataOutputStream;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public enum HdfsWriter {
    INS;
    private ConcurrentHashMap<String, FSDataOutputStream> streams = new ConcurrentHashMap<>();

    public void write(String d, Topic topic) {

        BinlogBean record = JSON
                .parseObject(d, BinlogBean.class, Feature.OrderedField);
        if (streams.get(record.getDatabase() + "_" + record.getTable() + "/pt=" + DateUtil.format(new Date(record.getTs()), "yyyyMMddHH")) != null) {


            if (StringUtils.isEmpty(record.getDatabase())
                    || StringUtils.isEmpty(record.getTable())
                    || record.getData() == null

            ) {
                log.info("record:" + d);
                return;
            }
            JSONArray data = record.getData();

            Long ts = record.getTs();

            for (int i = 0; i < data.size(); i++) {
                JSONObject obj = data.getJSONObject(i);
                if (obj != null) {
                    StringBuilder fieldsBuilder = new StringBuilder();
                    Set<Map.Entry<String, Object>> s = obj
                            .entrySet();
                    Iterator<Map.Entry<String, Object>> it = s
                            .iterator();
                    for (int j = 0; j < s.size(); j++) {

                        fieldsBuilder.append(it.next()
                                .getValue());
                        fieldsBuilder
                                .append("\001");
                    }
                    fieldsBuilder.append(ts);
                    fieldsBuilder.append("\n");
                    FSDataOutputStream os = null;
                    String key = record.getDatabase() + "_" + record.getTable() + "/pt=" + DateUtil.format(new Date(record.getTs()), "yyyyMMddHH");

                    try {
                        os = streams.get(key);
                        String msg = fieldsBuilder.toString();
                        log.debug("key:" + key + ",msg:" + msg);
                        os.write(msg.getBytes());
                    } catch (IOException e) {
                        log.error("e:", e);

                    }
                }
            }
        }
    }

    public ConcurrentHashMap<String, FSDataOutputStream> getStreams() {
        return streams;
    }

    public void setStreams(ConcurrentHashMap<String, FSDataOutputStream> streams) {
        this.streams = streams;
    }
}
