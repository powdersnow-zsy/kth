package cn.kth;

import cn.kth.common.bean.Topic;
import cn.kth.common.conf.ConfProperties;
import cn.kth.writer.HdfsWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.*;

@Slf4j
public class KTH {

    public void start(Topic topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", ConfProperties.getStringValue("kafka.server"));
        props.put("group.id", "logGroup");

        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        Consumer<String, String> consu = new KafkaConsumer<String, String>(
                props);
        Collection<String> topics = Arrays.asList(topic.getTopicName());
        //消费者订阅topic
        consu.subscribe(topics);
        ConsumerRecords<String, String> consumerRecords = null;

        while (true) {

            consumerRecords = consu.poll(1000);

            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {

                Object value = consumerRecord.value();
                HdfsWriter.INS.write((String) value, topic);
            }
        }
    }

}
