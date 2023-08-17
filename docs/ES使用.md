# ES(ElasticSearch) 使用

## 下载

去ElasticSearch的官方下载[https://www.elastic.co/downloads](https://www.elastic.co/downloads)

**注意,ElasticSearch的版本要和SpringBoot中的父pom中的版本一致避免冲突**

## 配置

在`${elasticsearch Home}/config` 下的 `elasticsearch.yml` 文件中进行相关配置

## 中文分词插件

使用 IK 来进行中文分词 [elasticsearch-analysis-ik](https://github.com/medcl/elasticsearch-analysis-ik)

解压到 `${elasticsearch Home}/plugins/ik/`

插件使用完成

## 启动 

```shell
${elasticsearch Home}/bin/elasticsearch
```

