package org.example.nowcoder.controller;

import org.example.nowcoder.annotation.LoginRequired;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.component.event.EventProducer;
import org.example.nowcoder.constant.CommentConstant;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.entity.Event;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.entity.vo.ApiResponse;
import org.example.nowcoder.service.FollowService;
import org.example.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements CommentConstant, CommunityConstant {

    private UserHostHolder userHostHolder;

    private FollowService followService;

    private UserService userService;

    private EventProducer eventProducer;

    @Autowired
    public void setEventProducer(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setFollowService(FollowService followService) {
        this.followService = followService;
    }

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @PostMapping("/follow")
    @ResponseBody
    @LoginRequired
    public ApiResponse follow(Integer entityType, Integer entityId) {
        User user = userHostHolder.getUser();
        boolean status = followService.followStatus(user.getId(), entityType, entityId);
        if (status) {
            followService.unfollow(user.getId(), entityType, entityId);
            return ApiResponse.failure("取消关注成功");
        } else {
            followService.follow(user.getId(), entityType, entityId);
            Event event = new Event();
            event.setTopic(TOPIC_FOLLOW)
                    .setUserId(user.getId())
                    .setEntityId(entityId)
                    .setEntityType(entityType)
                    .setEntityUserId(entityId);
            eventProducer.fireEvent(event);
            return ApiResponse.success();
        }
    }

    @GetMapping("/followees/{userId}")
    public String getFollowees(Model model, @PathVariable("userId") Integer userId) {

        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user", user);

        List<Map<String, Object>> followeeMapList = followService.getFollowees(userId);
        if (followeeMapList != null) {
            for (Map<String, Object> followeeMap : followeeMapList) {
                User followee = (User) followeeMap.get("user");
                followeeMap.put("isFollow", isFollow(followee.getId()));
            }
        }
        model.addAttribute("followeeMapList", followeeMapList);
        model.addAttribute("tab", "followee");

        return "site/followee";
    }

    private boolean isFollow(Integer userId) {
        if (userHostHolder.getUser() == null) {
            return false;
        }
        return followService.followStatus(userHostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
    }


    @GetMapping("/followers/{userId}")
    public String getFollowers(Model model, @PathVariable("userId") Integer userId) {

        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user", user);
        List<Map<String, Object>> followerMapList = followService.getFollowers(userId);
        if (followerMapList != null) {
            for (Map<String, Object> followeeMap : followerMapList) {
                User followee = (User) followeeMap.get("user");
                followeeMap.put("isFollow", isFollow(followee.getId()));
            }
        }

        model.addAttribute("followerMapList", followerMapList);
        model.addAttribute("tab", "follower");
        return "site/follower";
    }

}
