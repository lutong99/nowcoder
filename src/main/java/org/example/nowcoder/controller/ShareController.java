package org.example.nowcoder.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.component.event.EventProducer;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.entity.Event;
import org.example.nowcoder.entity.vo.ApiResponse;
import org.example.nowcoder.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Controller
public class ShareController implements CommunityConstant {


    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.path.wk.image-path}")
    private String wkImagePath;

    private EventProducer eventProducer;

    @Value("${community.oss.share-prefix}")
    private String sharePrefix;

    @Value("${community.oss.endpoint}")
    private String endpoint;

    @Value("${community.oss.endpoint-without}")
    private String endpointWithoutHttps;

    @Value("${community.oss.bucket-name}")
    private String bucketName;

    @Autowired
    public void setEventProducer(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @GetMapping("/share")
    @ResponseBody
    public ApiResponse shareImage(String htmlUrl) {
        String fileName = CommunityUtil.generateUUID();

        Event event = new Event()
                .setTopic(TOPIC_SHARE)
                .setData("htmlUrl", htmlUrl)
                .setData("fileName", fileName)
                .setData("suffix", ".png");
        eventProducer.fireEvent(event);


        return ApiResponse.success().data("shareUrl", "https://" + bucketName + "." + endpointWithoutHttps + "/" + sharePrefix + fileName + ".png");

    }

    @GetMapping("/share/image/{fileName}")
    @Deprecated
    public void getShareImage(@PathVariable("fileName") String fileName, HttpServletResponse response) {

        if (fileName == null) {
            throw new IllegalArgumentException("输入的图像路径有误, 文件名不能为空");
        }

        response.setContentType("image/png");

        File file = new File(wkImagePath + "/" + fileName + ".png");

        try (ServletOutputStream outputStream = response.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file)) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.error("获取图片失败: {}", e.getMessage());
        }


    }

}
