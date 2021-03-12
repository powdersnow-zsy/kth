English|[Chinese](https://github.com/powdersnow-zsy/kth/edit/main/README.MD)
# Introduction

Real-time synchronization tool from mysql to hdfs,

Consume the mysql binlog data sent by canal to kafka, write it to hdfs and convert it to hive table regularly

# Feature

- Support multiple topics, message filtering
- Dynamic configuration, no need to restart
- Configurable Kafka consumption start time
- Automatically refresh partitions regularly

# Architecture

![](https://github.com/powdersnow-zsy/kth/blob/main/4.png)

- kth consumes mysql binlog data from kafka, which is formatted into json by canal and sent to kafka
- kth formats the data into one line of the hdfs file, adds a timestamp, and writes it to hdfs
- kth add partition regularly, so that the hive table corresponding to hive can be checked

# Quick Start

1、Modify configuration conf-env.properties

![](https://github.com/powdersnow-zsy/kth/blob/main/1.png)

2、Modify configuration config-env.yml

![](https://github.com/powdersnow-zsy/kth/blob/main/2.png)

3、Add hive--env-site.xml 

![](https://github.com/powdersnow-zsy/kth/blob/main/3.png)

4、Configure the reloadConf.properties file according to the specified format：

topics={"topics":[{"tables":[{"db":"db1","tableName":"t1"},{"db":"db2","tableName":"t2"},{"db":"db3","tableName":"t3"}],"topicName":"topic1"},{"tables":[{"db":"db4","tableName":"t4"}],"topicName":"topic2"}]}

This configuration item must be json compressed.


5、package：mvn clean install

load file folder ./target/kth to server

6、start ：nohup bash bin/start.sh profiles=production &

# Contact

QQ：346224832
EMAIL: zsyhhh123@gmail.com

# License

kth is under the Apache 2.0 license. See the [LICENSE](http://www.apache.org/licenses/LICENSE-2.0) file for details.
