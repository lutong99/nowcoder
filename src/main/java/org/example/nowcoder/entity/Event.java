package org.example.nowcoder.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class Event {

    /**
     * 事件的主题
     */
    private String topic;

    /**
     * 触发事件的用户
     */
    private Integer userId;

    /**
     * 事件触发的 主体 类型
     */
    private Integer entityType;

    /**
     * 事件触发的 主体的 id
     */
    private Integer entityId;

    /**
     * 消息发送给哪个用户
     */
    private Integer entityUserId;

    private Map<String, Object> data;

    public Event() {
        data = new HashMap<>();
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Event setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Event setEntityType(Integer entityType) {
        this.entityType = entityType;
        return this;
    }

    public Event setEntityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }

    /**
     * @param entityType 事件要通知给的用户id
     */
    public Event setEntityUserId(Integer entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
