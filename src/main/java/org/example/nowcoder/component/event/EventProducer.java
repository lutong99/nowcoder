package org.example.nowcoder.component.event;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
public class EventProducer {

    private KafkaTemplate kafkaTemplate;


    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void fireEvent(Event event) {
        if (event == null) {
            log.info("发送消息失败");
            return;
        }

        String topic = event.getTopic();
        String data = null;
        try {
            data = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(topic, data);
    }


}
