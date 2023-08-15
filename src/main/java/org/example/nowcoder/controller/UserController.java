package org.example.nowcoder.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.nowcoder.annotation.LoginRequired;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.constant.CommentConstant;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.DiscussPostService;
import org.example.nowcoder.service.FollowService;
import org.example.nowcoder.service.LikeService;
import org.example.nowcoder.service.UserService;
import org.example.nowcoder.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController implements CommunityConstant, CommentConstant {

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private UserHostHolder userHostHolder;
    private UserService userService;
    private LikeService likeService;
    private FollowService followService;

    private DiscussPostService discussPostService;

    @Autowired
    public void setDiscussPostService(DiscussPostService discussPostService) {
        this.discussPostService = discussPostService;
    }

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    @LoginRequired
    @GetMapping("/setting")
    public String settingPage() {
        return "site/setting";
    }

    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            log.error("请上传图片");
            throw new RuntimeException("请上传图片再进行操作");
        }

        String originalFilename = headerImage.getOriginalFilename();
        if (!StringUtils.isBlank(originalFilename)) {
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String storeFileName = CommunityUtil.generateUUID() + extension;
            File imageFile = new File(uploadPath + File.separator, storeFileName);
            try {
                headerImage.transferTo(imageFile);
                String headerUrl = domain + contextPath + "/user/header/" + storeFileName;
                User user = userHostHolder.getUser();
                userService.updateHeader(user.getId(), headerUrl);
                return "redirect:/index";
            } catch (IOException e) {
                log.error("文件上传失败: {}", e.getMessage());
                throw new RuntimeException(e.getMessage(), e);
            }
        } else {
            model.addAttribute("error", "服务器未能读取成功文件");
            return "site/setting";
        }

    }

    @GetMapping("/header/{imageName}")
    public void getHeaderImage(@PathVariable("imageName") String imageName, HttpServletResponse response) {
        if (imageName != null) {
            imageName = uploadPath + File.separator + imageName;
            String extension = imageName.substring(imageName.lastIndexOf("."));
            response.setContentType("image/" + extension);
            try (ServletOutputStream outputStream = response.getOutputStream(); FileInputStream fileInputStream = new FileInputStream(imageName);) {
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
            } catch (IOException e) {
                log.error("显示头像失败 {}", e.getMessage());
                throw new RuntimeException("显示头像失败");
            }
        } else {
            log.error("请求的头像文件名为空");
            throw new RuntimeException("请求的文件名为空");
        }
    }

    @LoginRequired
    @PostMapping("/updatePassword")
    public String updatePassword(Model model, String password, String confirm) {
        if (StringUtils.isBlank(password) || StringUtils.isBlank(confirm)) {
            log.error("请求参数错误, password is {}, confirm is {}", password, confirm);
            throw new IllegalArgumentException("请求参数错误，为空");
        }
        User user = userHostHolder.getUser();
        if (!user.getPassword().equals(CommunityUtil.md5(password + user.getSalt()))) {
            model.addAttribute("error", "原来的密码不正确");
            return "site/setting";
        }

        String newPassword = CommunityUtil.md5(confirm + user.getSalt());
        userService.updateUserPass(user.getId(), newPassword);
        model.addAttribute(OPERATE_RESULT_MSG, "密码更新成功");
        model.addAttribute(OPERATE_RESULT_TARGET, "/logout");
        return "site/operate-result";
    }

    @GetMapping("/profile/{userId}")
    @LoginRequired
    public String profile(Model model, @PathVariable("userId") Integer userId) {
        User profileUser;
        if (userId == null) {
            profileUser = userHostHolder.getUser();
        } else {
            profileUser = userService.getById(userId);
        }
        int likeCount = likeService.likeUserCount(profileUser.getId());
        Long followerCount = followService.followerCount(ENTITY_TYPE_USER, userId);
        Long followeeCount = followService.followeeCount(userId, ENTITY_TYPE_USER);
        boolean status = userHostHolder.getUser() != null && followService.followStatus(userHostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("followeeCount", followeeCount);
        model.addAttribute("followStatus", status);

        model.addAttribute("profile", profileUser);
        model.addAttribute("likeCount", likeCount);
        return "site/profile";

    }

    @Autowired
    public void setFollowService(FollowService followService) {
        this.followService = followService;
    }


    @GetMapping("/post/{userId}")
    public String myPost(Model model, @PathVariable("userId") Integer userId,
                         @RequestParam(value = "num", defaultValue = PAGE_OFFSET) Integer pageNum,
                         @RequestParam(value = "size", defaultValue = PAGE_SIZE_POST) Integer pageSize
    ) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("查询的用户不存在");
        }

        List<Map<String, Object>> discussPostMapList = new ArrayList<>();
        Page<DiscussPost> discussPostPage = PageHelper.startPage(pageNum, pageSize);
        List<DiscussPost> postList = discussPostService.getAllByUserId(userId);
        int postCount = discussPostService.getPostCountByUserId(userId);
        if (postList != null) {
            for (DiscussPost discussPost : postList) {
                Map<String, Object> discussPostMap = new HashMap<>();
                discussPostMap.put("post", discussPost);
                Long likeCount = likeService.likeCount(ENTITY_TYPE_POST, discussPost.getId());
                discussPostMap.put("likeCount", likeCount);

                discussPostMapList.add(discussPostMap);
            }
        }
        PageInfo<DiscussPost> pageInfo = new PageInfo<>(discussPostPage);

        model.addAttribute("discussPostMapList", discussPostMapList);
        model.addAttribute("profile", user);
        model.addAttribute("postCount", postCount);
        model.addAttribute("page", pageInfo);
        return "site/my-post";
    }


}
