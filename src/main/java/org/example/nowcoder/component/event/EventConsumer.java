package org.example.nowcoder.component.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.constant.MessageConstant;
import org.example.nowcoder.entity.Event;
import org.example.nowcoder.entity.Message;
import org.example.nowcoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class EventConsumer implements CommunityConstant, MessageConstant {

    private MessageService messageService;

    
    private ObjectMapper objectMapper;

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

}
