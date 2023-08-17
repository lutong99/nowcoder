package org.example.nowcoder.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import org.example.nowcoder.entity.vo.ApiResponse;
import org.example.nowcoder.service.CommentService;
import org.example.nowcoder.service.DiscussPostService;
import org.example.nowcoder.service.LikeService;
import org.example.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/discuss")
@Slf4j
public class DiscussPostController implements CommentConstant, CommunityConstant {

    private DiscussPostService discussPostService;

    private UserHostHolder userHostHolder;
    private UserService userService;

    private CommentService commentService;

    private LikeService likeService;

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
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

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
    @LoginRequired
    public ApiResponse addDiscussPost(String title, String content) {
        User user = userHostHolder.getUser();

        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setUserId(user.getId());
        discussPost.setCreateTime(new Date());

        discussPostService.addDiscussPost(discussPost);

        // 发布成功后存到ES中
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(discussPost.getId());
        eventProducer.fireEvent(event);

        return ApiResponse.success("发布成功");
    }

    @GetMapping("/detail/{postId}")
    public String postDetailPage(Model model, @PathVariable("postId") Integer postId,
                                 @RequestParam(value = "num", defaultValue = PAGE_OFFSET) Integer pageNum,
                                 @RequestParam(value = "size", defaultValue = PAGE_SIZE_COMMENT) Integer pageSize
    ) {
        if (postId == null) {
            throw new IllegalArgumentException("请求的参数不合法");
        }

        DiscussPost discussPost = discussPostService.getById(postId);
        User discussPostUser = userService.getById(discussPost.getUserId());
        model.addAttribute("post", discussPost);
        model.addAttribute("user", discussPostUser);

        Page<Comment> commentPage = PageHelper.startPage(pageNum, pageSize);
        List<Comment> postCommentList = commentService.getListByEntity(ENTITY_TYPE_POST, postId);
        Long likeCount = likeService.likeCount(ENTITY_TYPE_POST, postId);
        User user = userHostHolder.getUser();
        int likeStatus = user == null ? 0 : likeService.likeStatus(user.getId(), ENTITY_TYPE_POST, postId);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("likeStatus", likeStatus);

        List<Map<String, Object>> postCommentMapList = new ArrayList<>();
        if (postCommentList != null) {
            for (Comment postComment : postCommentList) {
                Map<String, Object> postCommentMap = new HashMap<>();
                postCommentMap.put("comment", postComment);
                postCommentMap.put("user", userService.getById(postComment.getUserId()));
//                User targetUser = postComment.getTargetId() != TARGET_ID_DEFAULT ? userService.getById(postComment.getUserId()) : null;
                int likeStatusComment = user == null ? 0 : likeService.likeStatus(user.getId(), ENTITY_TYPE_COMMENT, postComment.getId());
                Long likeCountComment = likeService.likeCount(ENTITY_TYPE_COMMENT, postComment.getId());
                postCommentMap.put("likeStatus", likeStatusComment);
                postCommentMap.put("likeCount", likeCountComment);
                List<Comment> replyCommentList = commentService.getListByEntity(ENTITY_TYPE_COMMENT, postComment.getId());
                List<Map<String, Object>> replyCommentMapList = new ArrayList<>();
                if (replyCommentList != null) {
                    for (Comment replyComment : replyCommentList) {
                        Map<String, Object> replyCommentMap = new HashMap<>();
                        User target = replyComment.getTargetId() != TARGET_ID_DEFAULT ? userService.getById(replyComment.getTargetId()) : null;
                        User replyUser = userService.getById(replyComment.getUserId());
                        int likeStatusReply = user == null ? 0 : likeService.likeStatus(user.getId(), ENTITY_TYPE_COMMENT, replyComment.getId());
                        Long likeCountReply = likeService.likeCount(ENTITY_TYPE_COMMENT, replyComment.getId());
                        replyCommentMap.put("likeStatus", likeStatusReply);
                        replyCommentMap.put("likeCount", likeCountReply);
                        replyCommentMap.put("comment", replyComment);
                        replyCommentMap.put("user", replyUser);
                        replyCommentMap.put("target", target);
                        replyCommentMapList.add(replyCommentMap);
                    }
                }
                postCommentMap.put("replyMapList", replyCommentMapList);
                int count = commentService.getCountByEntity(ENTITY_TYPE_COMMENT, postComment.getId());
                postCommentMap.put("count", count);
                postCommentMapList.add(postCommentMap);
            }
        }

        model.addAttribute("postCommentMapList", postCommentMapList);
        PageInfo<Comment> pageInfo = new PageInfo<>(commentPage);
        model.addAttribute("page", pageInfo);
        return "site/discuss-detail";

    }
}
