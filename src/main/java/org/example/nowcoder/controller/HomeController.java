package org.example.nowcoder.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.DiscussPostService;
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
public class HomeController {

    private UserService userService;


    private DiscussPostService discussPostService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setDiscussPostService(DiscussPostService discussPostService) {
        this.discussPostService = discussPostService;
    }

    @GetMapping("/index")
    public String indexPage(Model model,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<DiscussPost> page = PageHelper.startPage(pageNum, pageSize);
        List<DiscussPost> discussPostList = discussPostService.getAll();
        List<Map<String, Object>> discussPostMapList = new ArrayList<>();
        if (discussPostList != null) {
            for (DiscussPost discussPost : discussPostList) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPost);
                User user = userService.getUser(discussPost.getUserId());
                map.put("user", user);
                discussPostMapList.add(map);
            }
        }

        PageInfo<DiscussPost> pageInfo = new PageInfo<>(page);
        model.addAttribute("discussPostList", discussPostMapList);
        model.addAttribute("page", pageInfo);
        return "/index";
    }

}
