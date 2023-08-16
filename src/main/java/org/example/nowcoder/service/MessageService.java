package org.example.nowcoder.service;

import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.constant.MessageConstant;
import org.example.nowcoder.entity.Message;

import java.util.List;

public interface MessageService extends MessageConstant, CommunityConstant {

    List<Message> getConversations(Integer userId);

    int getConversationCount(Integer userId);

    List<Message> getMessagesByConversationId(String conversationId);

    int getMessageCount(String conversationId);

    int getMessageUnreadCount(Integer userId, String conversationId);

    /**
     * 修改消息的状态
     */
    int updateStatus(List<Integer> ids, int status);

    int addMessage(Message message);

    int readMessage(List<Integer> ids);

    int readMessage(Integer id);

    int deleteMessage(Integer messageId);


    Message getNewestNoticeByTopic(Integer userId, String topic);

    int getNoticeCountByTopic(Integer userId, String topic);

    int getNoticeUnreadCountByTopic(Integer userId, String topic);

    int getNoticeUnreadCount(Integer userId);

    List<Message> getNoticesByTopic(Integer userId, String topic);

    int getMessageUnreadCount(Integer userId);

}
