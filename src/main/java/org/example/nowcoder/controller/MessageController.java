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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
