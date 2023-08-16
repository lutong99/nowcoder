package org.example.nowcoder.controller;

import org.example.nowcoder.annotation.LoginRequired;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.component.event.EventProducer;
import org.example.nowcoder.constant.CommentConstant;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.entity.Event;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.entity.vo.ApiResponse;
import org.example.nowcoder.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController implements CommunityConstant, CommentConstant {

    private LikeService likeService;

    private UserHostHolder userHostHolder;

    private EventProducer eventProducer;

    @Autowired
    public void setEventProducer(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @PostMapping("/like")
    @ResponseBody
    @LoginRequired
    public ApiResponse like(Integer entityType, Integer entityId, Integer entityUserId, Integer postId) {
        User user = userHostHolder.getUser();
        likeService.like(user.getId(), entityType, entityId, entityUserId);

        Long likeCount = likeService.likeCount(entityType, entityId);
        int likeStatus = likeService.likeStatus(user.getId(), entityType, entityId);

        ApiResponse data = ApiResponse.success().data("likeCount", likeCount).data("likeStatus", likeStatus);

        if (likeStatus == LIKE_STATUS_LIKED) {
            Event event = new Event();
            event.setTopic(TOPIC_LIKE)
                    .setEntityId(entityId)
                    .setEntityType(entityType)
                    .setEntityUserId(entityUserId)
                    .setUserId(user.getId())
                    .setData("postId", postId);

            eventProducer.fireEvent(event);
        }

        return data;
    }


}
