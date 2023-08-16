# Kafka 使用

## 下载

从apache 官方网站[下载](https://kafka.apache.org/downloads) 并解压

## 配置

配置 `zookeeper.properties` 和 `server.properties` 

## 启动 Zookeeper 和 Kafka

以下命令均在 **kafka** 的 **home** 目录下进行

### Zookeeper

```shell
bin/zookeeper-server-start.sh config/zookeeper.properties
```

### Kafka 

```shell
bin/kafka-server-start.sh config/server.properties
```

## 创建主题

创建 `test` 主题

```shell
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
```

## 查看主题列表

```shell
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

## 向主题test发消息

```shell
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test
```

## 接收test主题的消息

```shell
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test
```