package org.example.nowcoder.service;

import org.example.nowcoder.constant.MessageConstant;
import org.example.nowcoder.entity.Message;

import java.util.List;

public interface MessageService extends MessageConstant {

    List<Message> getConversations(Integer userId);

    int getConversationCount(Integer userId);

    List<Message> getMessagesByConversationId(String conversationId);

    int getMessageCount(String conversationId);

    int getMessageUnreadCount(Integer userId, String conversationId);

}
