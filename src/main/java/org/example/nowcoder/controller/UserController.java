package org.example.nowcoder.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.nowcoder.annotation.LoginRequired;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.LikeService;
import org.example.nowcoder.service.UserService;
import org.example.nowcoder.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController implements CommunityConstant {

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private UserHostHolder userHostHolder;
    private UserService userService;
    private LikeService likeService;

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

    @GetMapping({"/profile/{userId}", "/profile"})
    public String profile(Model model, @PathVariable("userId") Integer userId) {
        User profileUser;
        if (userId == null) {
            profileUser = userHostHolder.getUser();
        } else {
            profileUser = userService.getById(userId);
        }
        int likeCount = likeService.likeUserCount(profileUser.getId());
        model.addAttribute("profile", profileUser);
        model.addAttribute("likeCount", likeCount);
        return "site/profile";

    }

}
