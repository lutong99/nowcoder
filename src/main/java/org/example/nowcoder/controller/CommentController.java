package org.example.nowcoder.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.annotation.LoginRequired;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.component.event.EventProducer;
import org.example.nowcoder.constant.CommentConstant;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.entity.Comment;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.Event;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.CommentService;
import org.example.nowcoder.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
@Slf4j
public class CommentController implements CommunityConstant, CommentConstant {

    private CommentService commentService;

    private UserHostHolder userHostHolder;

    private EventProducer eventProducer;

    private DiscussPostService discussPostService;

    @Autowired
    public void setDiscussPostService(DiscussPostService discussPostService) {
        this.discussPostService = discussPostService;
    }

    @Autowired
    public void setEventProducer(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add/{postId}")
    @LoginRequired
    public String addComment(@PathVariable("postId") Integer postId, Comment comment) {
        if (postId == null) {
            log.error("传入的参数不合法，postId");
            throw new IllegalArgumentException("传入的参数不合法");
        }
        User user = userHostHolder.getUser();
        comment.setUserId(user.getId());
        commentService.addComment(comment);

        Event event = new Event();
        event.setTopic(TOPIC_COMMENT)
                .setUserId(user.getId())
                .setEntityId(comment.getEntityId())
                .setEntityType(comment.getEntityType())
                .setData("postId", postId);

        if (comment.getEntityType().equals(ENTITY_TYPE_POST)) {
            DiscussPost target = discussPostService.getById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType().equals(ENTITY_TYPE_COMMENT)) {
            Comment target = commentService.getById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);

        if (comment.getEntityType().equals(ENTITY_TYPE_POST)) {
            Event publishEvent = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(user.getId())
                    .setEntityId(postId)
                    .setEntityType(ENTITY_TYPE_POST);
            eventProducer.fireEvent(publishEvent);
        }

        return "redirect:/discuss/detail/" + postId;
    }

}
