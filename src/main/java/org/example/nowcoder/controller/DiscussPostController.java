package org.example.nowcoder.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.entity.vo.ApiResponse;
import org.example.nowcoder.service.DiscussPostService;
import org.example.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
@Slf4j
public class DiscussPostController {

    private DiscussPostService discussPostService;

    private UserHostHolder userHostHolder;
    private UserService userService;

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @Autowired
    public void setDiscussPostService(DiscussPostService discussPostService) {
        this.discussPostService = discussPostService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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

    @GetMapping("/detail/{postId}")
    public String postDetailPage(Model model, @PathVariable("postId") Integer postId) {
        if (postId == null) {
            throw new IllegalArgumentException("请求的参数不合法");
        }

        DiscussPost discussPost = discussPostService.getById(postId);
        User discussPostUser = userService.getById(discussPost.getUserId());
        model.addAttribute("post", discussPost);
        model.addAttribute("user", discussPostUser);

        return "site/discuss-detail";

    }
}
