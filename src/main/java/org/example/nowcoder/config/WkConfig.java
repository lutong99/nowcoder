package org.example.nowcoder.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
@Slf4j
public class WkConfig {

    @Value("${community.path.wk.image-path")
    private String wkImagePath;


    @PostConstruct
    public void createWkFilePath() {
        File file = new File(wkImagePath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (mkdirs) {
                log.info("创建WK 图像目录成功");
            }
        }
    }

}
