package org.example.nowcoder.controller;

import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.entity.vo.ApiResponse;
import org.example.nowcoder.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    private LikeService likeService;

    private UserHostHolder userHostHolder;

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
    public ApiResponse like(Integer entityType, Integer entityId, Integer entityUserId) {
        User user = userHostHolder.getUser();
        likeService.like(user.getId(), entityType, entityId, entityUserId);

        Long likeCount = likeService.likeCount(entityType, entityId);
        int likeStatus = likeService.likeStatus(user.getId(), entityType, entityId);

        return ApiResponse.success().data("likeCount", likeCount).data("likeStatus", likeStatus);
    }


}
