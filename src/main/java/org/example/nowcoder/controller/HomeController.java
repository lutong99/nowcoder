package org.example.nowcoder.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.constant.CommentConstant;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.DiscussPostService;
import org.example.nowcoder.service.LikeService;
import org.example.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@Slf4j
public class HomeController implements CommunityConstant, CommentConstant {

    private UserService userService;


    private DiscussPostService discussPostService;

    private LikeService likeService;

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setDiscussPostService(DiscussPostService discussPostService) {
        this.discussPostService = discussPostService;
    }

    @GetMapping({"/index", "/"})
    public String indexPage(Model model,
                            @RequestParam(value = "pageNum", defaultValue = PAGE_OFFSET) Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE_POST) Integer pageSize
    ) {
        log.debug("{} pageNum = {}", log.getName(), pageNum);
        log.debug("{} pageSize = {}", log.getName(), pageSize);
        Page<DiscussPost> page = PageHelper.startPage(pageNum, pageSize);
        List<DiscussPost> discussPostList = discussPostService.getAllByTimeDesc();
        return getString(model, page, discussPostList, "newest");
    }

    @GetMapping("/hot")
    public String indexHotPage(Model model,
                               @RequestParam(value = "pageNum", defaultValue = PAGE_OFFSET) Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE_POST) Integer pageSize
    ) {
        log.debug("{} pageNum = {}", log.getName(), pageNum);
        log.debug("{} pageSize = {}", log.getName(), pageSize);
        Page<DiscussPost> page = PageHelper.startPage(pageNum, pageSize);
        List<DiscussPost> discussPostList = discussPostService.getAllByHotDesc();
        return getString(model, page, discussPostList, "hot");
    }

    private String getString(Model model, Page<DiscussPost> page, List<DiscussPost> discussPostList, String tabName) {
        List<Map<String, Object>> discussPostMapList = new ArrayList<>();
        if (discussPostList != null) {
            for (DiscussPost discussPost : discussPostList) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPost);
                User user = userService.getById(discussPost.getUserId());
                map.put("user", user);
                Long likeCount = likeService.likeCount(ENTITY_TYPE_POST, discussPost.getId());
                map.put("likeCount", likeCount);

                discussPostMapList.add(map);
            }
        }
        model.addAttribute("tab", tabName);
        PageInfo<DiscussPost> pageInfo = new PageInfo<>(page);
        model.addAttribute("discussPostList", discussPostMapList);
        model.addAttribute("page", pageInfo);
        return "index";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error/500";
    }

    @GetMapping("/denied")
    public String deniedPage() {
        return "error/404";
    }

}
