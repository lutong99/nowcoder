package org.example.nowcoder.component.event;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.nowcoder.component.FileUploadClient;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.constant.MessageConstant;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.Event;
import org.example.nowcoder.entity.Message;
import org.example.nowcoder.service.DiscussPostService;
import org.example.nowcoder.service.ElasticsearchService;
import org.example.nowcoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class EventConsumer implements CommunityConstant, MessageConstant {

    private MessageService messageService;


    private ObjectMapper objectMapper;

    private DiscussPostService discussPostService;
    private ElasticsearchService<DiscussPost> elasticsearchService;

    @Value("${community.path.wk.command}")
    private String wkImageCommand;

    @Value("${community.path.wk.image-path}")
    private String wkImagePath;

    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private FileUploadClient fileUploadClient;

    @Autowired
    public void setFileUploadClient(FileUploadClient fileUploadClient) {
        this.fileUploadClient = fileUploadClient;
    }

    @Autowired
    public void setThreadPoolTaskScheduler(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @Autowired
    public void setDiscussPostService(DiscussPostService discussPostService) {
        this.discussPostService = discussPostService;
    }

    @Autowired
    public void setElasticsearchService(ElasticsearchService<DiscussPost> elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_FOLLOW, TOPIC_LIKE})
    @SuppressWarnings("rawtypes")
    public void handleMessage(ConsumerRecord consumerRecord) {
        if (consumerRecord == null) {
            log.error("传入的消息错误");
            return;
        }
        Object value = consumerRecord.value();
//        Event event = objectMapper.convertValue(value, Event.class);
        Event event = null;
        try {
            event = objectMapper.readValue(String.valueOf(value), Event.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (event == null) {
            log.error("消息转换事件错误");
            return;
        }
        Message message = new Message();
        message.setStatus(STATUS_DEFAULT);
        message.setCreateTime(new Date());
        message.setFromId(FROM_ID_SYSTEM);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("entityType", event.getEntityType());
        contentMap.put("entityId", event.getEntityId());
        contentMap.put("userId", event.getUserId());
        contentMap.putAll(event.getData());

        try {
            message.setContent(objectMapper.writeValueAsString(contentMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        messageService.addMessage(message);
        log.info("消息处理完成，处理的消息类型是：{}", event.getTopic());

    }

    @KafkaListener(topics = {TOPIC_PUBLISH, TOPIC_HIGHLIGHT, TOPIC_TOP})
    @SuppressWarnings("rawtypes")
    public void handlePublish(ConsumerRecord consumerRecord) {
        if (consumerRecord == null) {
            log.error("传入的消息错误");
            return;
        }
        Object value = consumerRecord.value();
        Event event = null;
        try {
            event = objectMapper.readValue(String.valueOf(value), Event.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (event == null) {
            log.error("消息转换事件错误");
            return;
        }
        DiscussPost discussPost = discussPostService.getById(event.getEntityId());
        elasticsearchService.save(discussPost);
    }

    @KafkaListener(topics = TOPIC_INVALID)
    @SuppressWarnings("rawtypes")
    public void handleDelete(ConsumerRecord consumerRecord) {
        if (consumerRecord == null) {
            log.error("传入的消息错误");
            return;
        }
        Object value = consumerRecord.value();
        Event event = null;
        try {
            event = objectMapper.readValue(String.valueOf(value), Event.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (event == null) {
            log.error("消息转换事件错误");
            return;
        }
        elasticsearchService.delete(event.getEntityId());
    }

    @KafkaListener(topics = TOPIC_SHARE)
    @SuppressWarnings("rawtypes")
    public void handleShare(ConsumerRecord consumerRecord) {
        if (consumerRecord == null) {
            log.error("传入的消息错误");
            return;
        }
        Object value = consumerRecord.value();
        Event event = null;
        try {
            event = objectMapper.readValue(String.valueOf(value), Event.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (event == null) {
            log.error("消息转换事件错误");
            return;
        }
        Map<String, Object> data = event.getData();


        String htmlUrl = (String) data.get("htmlUrl");
        String fileName = (String) data.get("fileName");
        String suffix = (String) data.get("suffix");
        String cmd = wkImageCommand + " --quality 75 "
                + htmlUrl + " " + wkImagePath + "/" + fileName + suffix;

        try {
            Runtime.getRuntime().exec(cmd);
            log.info("生成长图成功: " + cmd);
        } catch (IOException e) {
            log.error("生成长图失败: " + e.getMessage());
        }

        UploadShareImageTask uploadTask = new UploadShareImageTask(fileName, suffix);
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(uploadTask, 500);
        uploadTask.setFuture(scheduledFuture);

    }

    class UploadShareImageTask implements Runnable {

        private String fileName;

        private String suffix;

        private Future<?> future;

        private Long startTime;

        private long uploadTimes;

        public UploadShareImageTask(String fileName, String suffix) {
            this.fileName = fileName;
            this.suffix = suffix;
            this.startTime = System.currentTimeMillis();
        }

        public void setFuture(Future<?> future) {
            this.future = future;
        }

        @Override
        public void run() {
            if (System.currentTimeMillis() - startTime > 3000) {
                log.error("生成图片失败，生成时间过长，终止任务 fileName is {}", fileName + suffix);
                this.future.cancel(true);
                return;
            }
            if (uploadTimes >= 3) {
                log.error("上传次数过多，终止任务 fileName is {}", fileName + suffix);
                this.future.cancel(true);
                return;
            }

            String filePath = wkImagePath + "/" + fileName + suffix;
            File file = new File(filePath);
            if (file.exists()) {
                log.info("开始第 {} 次上传 {} ", ++uploadTimes, fileName);
                try {
                    PutObjectResult upload = fileUploadClient.uploadShare(file);
                    if (upload != null) {
                        log.error("上传失败");
                    } else {
                        log.info("第 {} 次上传 {} 成功 ！！", uploadTimes, fileName + suffix);
                        this.future.cancel(true);
                    }
                } catch (Exception e) {
                    if (e instanceof ClientException) {
                        log.error("捕获了一个 ClientException，这意味着客户端在尝试与 OSS 通信时遇到了严重的内部问题，比如无法访问网络。");
                        log.error("错误信息：{}", e.getMessage());
                    }
                    if (e instanceof OSSException) {
                        OSSException oe = (OSSException) e;
                        log.error("捕获了一个OSSException 异常，错误消息：{}，错误码：{}，请求ID：{}，主机ID：{}", oe.getErrorMessage(), oe.getErrorCode(), oe.getRequestId(), oe.getHostId());
                        log.error("捕获了一个OSSException 异常，这意味着您的请求已经传递到了 OSS（对象存储服务），但由于某种原因被拒绝并返回了一个错误响应");
                    }
                    if (e instanceof FileNotFoundException) {
                        log.error("捕获了一个 FileNotFoundException，错误消息：{}", e.getMessage());
                    }
                    log.error("第{}次上传文件{} 失败", uploadTimes, fileName + suffix);
                }
            } else {
                log.error("等待图片生成 {}", fileName + suffix);
            }


        }
    }

}
