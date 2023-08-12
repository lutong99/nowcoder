package org.example.nowcoder.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.constant.CommentConstant;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.entity.Comment;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.entity.vo.ApiResponse;
import org.example.nowcoder.service.CommentService;
import org.example.nowcoder.service.DiscussPostService;
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

        List<Map<String, Object>> postCommentMapList = new ArrayList<>();
        if (postCommentList != null) {
            for (Comment postComment : postCommentList) {
                Map<String, Object> postCommentMap = new HashMap<>();
                postCommentMap.put("comment", postComment);
                postCommentMap.put("user", userService.getById(postComment.getUserId()));
//                User targetUser = postComment.getTargetId() != TARGET_ID_DEFAULT ? userService.getById(postComment.getUserId()) : null;
                List<Comment> replyCommentList = commentService.getListByEntity(ENTITY_TYPE_COMMENT, postComment.getId());
                List<Map<String, Object>> replyCommentMapList = new ArrayList<>();
                if (replyCommentList != null) {
                    for (Comment replyComment : replyCommentList) {
                        Map<String, Object> replyCommentMap = new HashMap<>();
                        User target = replyComment.getTargetId() != TARGET_ID_DEFAULT ? userService.getById(replyComment.getTargetId()) : null;
                        User replyUser = userService.getById(replyComment.getUserId());
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
