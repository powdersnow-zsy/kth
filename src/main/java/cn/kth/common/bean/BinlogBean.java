package cn.kth.common.bean;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class BinlogBean {
    private JSONArray data;
    private String database;
    private Long es;
    private Long id;
    private boolean isDdl;
    private String old;
    private String pkNames;
    private String sql;
    private String table;
    private Long ts;
    private String type;

}
