package org.example.nowcoder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.annotation.LoginRequired;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.constant.CommentConstant;
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
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notice")
@Slf4j
public class NoticeController implements CommunityConstant, CommentConstant {

    private MessageService messageService;
    private UserService userService;

    private UserHostHolder userHostHolder;

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    @LoginRequired
    public String listNotice(Model model) {
        User user = userHostHolder.getUser();

        // 点赞
        Map<String, Object> likeNoticeMap = getMapDataByTopic(user, TOPIC_LIKE);
        model.addAttribute("likeNoticeMap", likeNoticeMap);

        // 关注
        Map<String, Object> followNoticeMap = getMapDataByTopic(user, TOPIC_FOLLOW);
        model.addAttribute("followNoticeMap", followNoticeMap);

        // 评论
        Map<String, Object> commentNoticeMap = getMapDataByTopic(user, TOPIC_COMMENT);
        model.addAttribute("commentNoticeMap", commentNoticeMap);


        // 总的未读数量
        int noticeUnreadCount = messageService.getNoticeUnreadCount(user.getId());
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);
        int messageUnreadCount = messageService.getMessageUnreadCount(user.getId(), null);
        model.addAttribute("messageUnreadCount", messageUnreadCount);
        model.addAttribute("tab", "notice");
        return "site/notice";
    }

    @GetMapping("/detail/{topic}")
    @SuppressWarnings("unchecked")
    public String getNoticeDetail(Model model, @PathVariable("topic") String topic,
                                  @RequestParam(value = "num", defaultValue = PAGE_OFFSET) Integer pageNum,
                                  @RequestParam(value = "size", defaultValue = PAGE_SIZE_MESSAGE) Integer pageSize
    ) {
        User user = userHostHolder.getUser();
        Page<Message> noticePage = PageHelper.startPage(pageNum, pageSize);
        List<Message> noticesByTopic = messageService.getNoticesByTopic(user.getId(), topic);
        List<Map<String, Object>> noticeMapList = new ArrayList<>();
        if (noticesByTopic != null) {
            for (Message notice : noticesByTopic) {
                Map<String, Object> noticeMap = new HashMap<>();
                noticeMap.put("notice", notice);
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                HashMap<String, Object> contentMap = null;
                try {
                    contentMap = objectMapper.readValue(content, HashMap.class);
                } catch (JsonProcessingException e) {
                    log.error("HashMap<String, Object> 转换错误");
                    throw new RuntimeException(e);
                }
                noticeMap.put("user", userService.getById((Integer) contentMap.get("userId")));
                noticeMap.put("fromUser", userService.getById(notice.getFromId()));
                noticeMap.putAll(contentMap);
                noticeMapList.add(noticeMap);
                messageService.readMessage(notice.getId());
            }
        }
        PageInfo<Message> pageInfo = new PageInfo<>(noticePage);
        model.addAttribute("page", pageInfo);
        model.addAttribute("noticeMapList", noticeMapList);

        return "site/notice-detail";

    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMapDataByTopic(User user, String topicComment) {
        Message newestCommentNotice = messageService.getNewestNoticeByTopic(user.getId(), topicComment);
        Map<String, Object> commentNoticeMap = null;
        if (newestCommentNotice != null) {
            commentNoticeMap = new HashMap<>();
            commentNoticeMap.put("notice", newestCommentNotice);
            String content = HtmlUtils.htmlUnescape(newestCommentNotice.getContent());
            HashMap<String, Object> data = null;
            try {
                data = objectMapper.readValue(content, HashMap.class);
            } catch (JsonProcessingException e) {
                log.error("HashMap<String, Object> 转换错误");
                throw new RuntimeException(e);
            }

            commentNoticeMap.put("user", userService.getById((Integer) data.get("userId")));
            int followNoticeCount = messageService.getNoticeCountByTopic(user.getId(), topicComment);
            commentNoticeMap.put("count", followNoticeCount);
            int followNoticeUnreadCount = messageService.getNoticeUnreadCountByTopic(user.getId(), topicComment);
            commentNoticeMap.put("unreadCount", followNoticeUnreadCount);
            commentNoticeMap.putAll(data);
        }
        return commentNoticeMap;
    }

}
