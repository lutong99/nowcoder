package org.example.nowcoder.controller;

import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.entity.vo.ApiResponse;
import org.example.nowcoder.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    private DiscussPostService discussPostService;

    private UserHostHolder userHostHolder;

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @Autowired
    public void setDiscussPostService(DiscussPostService discussPostService) {
        this.discussPostService = discussPostService;
    }

    @PostMapping("/add")
    @ResponseBody
    public ApiResponse addDiscussPost(String title, String content) {
        User user = userHostHolder.getUser();
        if (user == null) {
            return ApiResponse.failure("还没有登录，请先登录");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setUserId(user.getId());
        discussPost.setCreateTime(new Date());

        discussPostService.addDiscussPost(discussPost);

        return ApiResponse.success("发布成功");
    }
}
