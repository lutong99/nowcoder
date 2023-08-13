package org.example.nowcoder.service.impl;

import org.example.nowcoder.component.SensitiveFilter;
import org.example.nowcoder.entity.Message;
import org.example.nowcoder.entity.MessageExample;
import org.example.nowcoder.mapper.MessageMapper;
import org.example.nowcoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageMapper messageMapper;

    private SensitiveFilter sensitiveFilter;

    @Autowired
    public void setSensitiveFilter(SensitiveFilter sensitiveFilter) {
        this.sensitiveFilter = sensitiveFilter;
    }

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public List<Message> getConversations(Integer userId) {
        return messageMapper.getNewestConversationsList(userId);
    }


    @Deprecated
    public List<Message> getConversationsDeprecated(Integer userId) {

        List<Integer> messageIds = messageMapper.getNewestMessageIds(userId);
        if (messageIds == null || messageIds.isEmpty()) {
            return new ArrayList<>();
        } else {
            MessageExample messageExample = new MessageExample();
            messageExample.setOrderByClause("create_time desc");
            messageExample.createCriteria().andIdIn(messageIds);
            return messageMapper.selectByExample(messageExample);
        }

    }

    public int getConversationCount(Integer userId) {
        return getConversations(userId).size();
    }


    @Deprecated
    public int getConversationCountDeprecated(Integer userId) {
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

    @Override
    public int updateStatus(List<Integer> ids, int status) {
        Message message = new Message();
        message.setStatus(status);

        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andIdIn(ids);
        return messageMapper.updateByExampleSelective(message, messageExample);
    }

    @Override
    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        message.setStatus(STATUS_UNREAD);
        return messageMapper.insert(message);
    }

    @Override
    public int readMessage(List<Integer> ids) {
        return updateStatus(ids, STATUS_READ);
    }

}
