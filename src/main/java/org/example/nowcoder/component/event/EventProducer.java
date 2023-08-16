package org.example.nowcoder.component.event;


import com.google.gson.Gson;
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

    private Gson gson;

    @Autowired
    public void setGson(Gson gson) {
        this.gson = gson;
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
        String data = gson.toJson(event);

        kafkaTemplate.send(topic, data);
    }


}
