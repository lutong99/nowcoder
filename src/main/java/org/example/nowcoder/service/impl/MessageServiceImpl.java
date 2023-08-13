package org.example.nowcoder.service.impl;

import org.example.nowcoder.entity.Message;
import org.example.nowcoder.entity.MessageExample;
import org.example.nowcoder.mapper.MessageMapper;
import org.example.nowcoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageMapper messageMapper;

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public List<Message> getConversations(Integer userId) {

        List<Integer> messageIds = messageMapper.getNewestMessageIds(userId);
        if (messageIds == null || messageIds.isEmpty()) {
            return new ArrayList<>();
        } else {
            MessageExample messageExample = new MessageExample();
            messageExample.setOrderByClause("id desc");
            messageExample.createCriteria().andIdIn(messageIds);
            return messageMapper.selectByExample(messageExample);
        }

    }


    @Override
    public int getConversationCount(Integer userId) {
        List<Integer> newestMessageIds = messageMapper.getNewestMessageIds(userId);
        return newestMessageIds.size();
    }

    @Override
    public List<Message> getMessagesByConversationId(String conversationId) {
        MessageExample messageExample = new MessageExample();
        messageExample.setOrderByClause("id desc");
        messageExample.createCriteria().andStatusNotEqualTo(STATUS_DELETED).andFromIdNotEqualTo(FROM_ID_SYSTEM)
                .andConversationIdEqualTo(conversationId);
        return messageMapper.selectByExample(messageExample);
    }

    @Override
    public int getMessageCount(String conversationId) {
        return getMessagesByConversationId(conversationId).size();
    }

    @Override
    public int getMessageUnreadCount(Integer userId, String conversationId) {
        MessageExample messageExample = new MessageExample();
        MessageExample.Criteria criteria = messageExample.createCriteria();
        criteria.andStatusEqualTo(STATUS_UNREAD).andFromIdNotEqualTo(FROM_ID_SYSTEM)
                .andToIdEqualTo(userId);
        if (conversationId != null) {
            criteria.andConversationIdEqualTo(conversationId);
        }
        return messageMapper.countByExample(messageExample);
    }
}
