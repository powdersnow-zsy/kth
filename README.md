[![License](https://camo.githubusercontent.com/266dbf1c12e7e7f112914eb37138734857bde343e4fca6e01dfeb40553cf209a/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c6963656e73652d417061636865253230322d3445423142412e737667)](https://www.apache.org/licenses/LICENSE-2.0.html)

中文 | [English](https://github.com/powdersnow-zsy/kth/blob/main/README_EN.MD)
# 简介

mysql到hdfs的实时同步工具，

消费canal发送到kafka的mysql binlog数据，写入hdfs并定时转成hive表

# 特点

- 支持多topic，消息过滤
- 动态配置，无需重启
- 可配置kafka消费起点时间
- 自动定时刷新分区

# 架构

![](https://github.com/powdersnow-zsy/kth/blob/main/4.png)

- kth消费来自kafka的mysql binlog数据，该数据由canal格式化成json后发送给kafka
- kth将数据格式化成hdfs文件的一行，并且添加时间戳，写入hdfs
- kth定时add partition，使得hive对应的hive表可查

# 快速开始

1、修改conf-env.properties配置

![](https://github.com/powdersnow-zsy/kth/blob/main/1.png)

2、修改config-env.yml配置

![](https://github.com/powdersnow-zsy/kth/blob/main/2.png)

3、添加hive-site.xml 

![](https://github.com/powdersnow-zsy/kth/blob/main/3.png)

4、按照指定格式配置reloadConf.properties文件：

topics={"topics":[{"tables":[{"db":"db1","tableName":"t1"},{"db":"db2","tableName":"t2"},{"db":"db3","tableName":"t3"}],"topicName":"topic1"},{"tables":[{"db":"db4","tableName":"t4"}],"topicName":"topic2"}]}

此配置项必须进行json压缩。


5、打包：mvn clean install

将./target/kth上传到服务器

6、启动：nohup bash bin/start.sh profiles=production &

# 联系方式

QQ：346224832

# License

kth is under the Apache 2.0 license. See the [LICENSE](http://www.apache.org/licenses/LICENSE-2.0) file for details.
