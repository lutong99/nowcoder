package org.example.nowcoder.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.annotation.LoginRequired;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.constant.MessageConstant;
import org.example.nowcoder.entity.Message;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.entity.vo.ApiResponse;
import org.example.nowcoder.service.MessageService;
import org.example.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@Slf4j
@RequestMapping("/message")
public class MessageController implements CommunityConstant, MessageConstant {

    private MessageService messageService;
    private UserHostHolder userHostHolder;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/list")
    public String messageList(Model model,
                              @RequestParam(value = "num", defaultValue = PAGE_OFFSET) Integer pageNum,
                              @RequestParam(value = "size", defaultValue = PAGE_SIZE_MESSAGE) Integer pageSize) {
        User user = userHostHolder.getUser();
        Page<Message> pageHelper = PageHelper.startPage(pageNum, pageSize);
        List<Message> conversationList = messageService.getConversations(user.getId());
        List<Map<String, Object>> conversationMapList = new ArrayList<>();
        if (conversationList != null) {
            for (Message conversation : conversationList) {
                Map<String, Object> conversationMap = new HashMap<>();
                int messageUnread = messageService.getMessageUnreadCount(user.getId(), conversation.getConversationId());
                conversationMap.put("messageUnreadCount", messageUnread);
                conversationMap.put("conversation", conversation);
                int messageCount = messageService.getMessageCount(conversation.getConversationId());
                conversationMap.put("messageCount", messageCount);
                Integer targetId = conversation.getFromId().equals(user.getId()) ? conversation.getToId() : conversation.getFromId();
                User target = userService.getById(targetId);
                conversationMap.put("target", target);
                conversationMapList.add(conversationMap);
            }
        }

        PageInfo<Message> pageInfo = new PageInfo<>(pageHelper);
        model.addAttribute("conversationMapList", conversationMapList);
        int messageUnreadCount = messageService.getMessageUnreadCount(user.getId(), null);
        model.addAttribute("messageUnreadCount", messageUnreadCount);
        model.addAttribute("page", pageInfo);
        model.addAttribute("tab", "message");
        return "site/letter";

    }

    @LoginRequired
    @GetMapping("/conversation/{conversationId}")
    public String conversationDetail(Model model,
                                     @PathVariable("conversationId") String conversationId,
                                     @RequestParam(value = "num", defaultValue = PAGE_OFFSET) Integer pageNum,
                                     @RequestParam(value = "size", defaultValue = PAGE_SIZE_MESSAGE) Integer pageSize
    ) {

        List<Map<String, Object>> messageMapList = new ArrayList<>();
        Page<Message> messagePageHelper = PageHelper.startPage(pageNum, pageSize);
        List<Message> messageList = messageService.getMessagesByConversationId(conversationId);
        if (messageList != null) {
            for (Message message : messageList) {
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("message", message);
                messageMap.put("from", userService.getById(message.getFromId()));
                messageMapList.add(messageMap);
            }
        }

        PageInfo<Message> messagePageInfo = new PageInfo<>(messagePageHelper);
        model.addAttribute("messageMapList", messageMapList);
        User targetUser = getTargetUserByConversation(conversationId);
        model.addAttribute("target", targetUser);
        model.addAttribute("page", messagePageInfo);

        List<Integer> unReadIds = getUnreadMessageIds(messageList);
        if (!unReadIds.isEmpty()) {
            messageService.readMessage(unReadIds);
        }

        return "site/letter-detail";
    }

    @PostMapping("/send")
    @ResponseBody
    public ApiResponse sendMessage(String toName, String content) {
        User user = userHostHolder.getUser();
        User toUser = userService.getByUsername(toName);
        if (toUser == null) {
            return ApiResponse.failure("您要发送的用户【" + toName + "】不存在");
        }

        Message message = new Message();
        message.setContent(content);
        message.setToId(toUser.getId());
        message.setFromId(user.getId());
        message.setContent(content);
        if (message.getToId() < message.getFromId()) {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        } else {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        }
        message.setCreateTime(new Date());

        messageService.addMessage(message);
        return ApiResponse.success("发送私信成功");
    }

    @PostMapping("/hide")
    @ResponseBody
    public ApiResponse deleteMessage(Integer messageId) {
        if (messageId == null) {
            log.info("messageId 不合法，messageId为空");
            throw new IllegalArgumentException("请求的参数不合法");
        }
        messageService.deleteMessage(messageId);
        return ApiResponse.success("删除私信成功！");
    }

    /**
     * 获取当前conversation中的未读消息的所有id
     *
     * @param messageList 当前会话中的所有消息
     * @return 会话中未读消息的ids
     */
    private List<Integer> getUnreadMessageIds(List<Message> messageList) {
        List<Integer> ids = new ArrayList<>();
        User user = userHostHolder.getUser();
        Integer userId = user.getId();
        if (messageList != null) {
            for (Message message : messageList) {
                if (message.getToId().equals(userId) && message.getStatus().equals(STATUS_UNREAD)) {
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    private User getTargetUserByConversation(String conversationId) {
        User user = userHostHolder.getUser();
        String[] userId = conversationId.split("_");
        Integer userId0 = Integer.parseInt(userId[0]);
        Integer userId1 = Integer.parseInt(userId[1]);

        if (Objects.equals(user.getId(), userId0)) {
            return userService.getById(userId1);
        } else {
            return userService.getById(userId0);
        }

    }

}
