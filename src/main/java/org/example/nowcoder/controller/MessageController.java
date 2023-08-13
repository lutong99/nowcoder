package org.example.nowcoder.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.entity.Message;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.MessageService;
import org.example.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@Slf4j
@RequestMapping("/message")

public class MessageController implements CommunityConstant {

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

        return "site/letter-detail";
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
