package org.example.nowcoder.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.entity.Comment;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    private CommentService commentService;

    private UserHostHolder userHostHolder;

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add/{postId}")
    public String addComment(@PathVariable("postId") Integer postId, Comment comment) {
        if (postId == null) {
            log.error("传入的参数不合法，postId");
            throw new IllegalArgumentException("传入的参数不合法");
        }
        User user = userHostHolder.getUser();
        comment.setUserId(user.getId());
        commentService.addComment(comment);
        return "redirect:/discuss/detail/" + postId;
    }

}
