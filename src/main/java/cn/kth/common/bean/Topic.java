package cn.kth.common.bean;

import cn.kth.common.util.JsonUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Topic {
    private List<Table> tables;
    private String topicName;

}
