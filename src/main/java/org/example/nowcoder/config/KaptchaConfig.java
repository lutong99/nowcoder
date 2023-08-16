package org.example.nowcoder.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Value("${community.kaptcha.image.width}")
    private String imageWidth;
    @Value("${community.kaptcha.image.height}")
    private String imageHeight;
    @Value("${community.kaptcha.textproducer.font.size}")
    private String fontSize;
    @Value("${community.kaptcha.textproducer.font.color}")
    private String fontColor;
    @Value("${community.kaptcha.textproducer.char.string}")
    private String charString;
    @Value("${community.kaptcha.textproducer.char.length}")
    private String charLength;
    @Value("${community.kaptcha.noise.impl}")
    private String noiseImpl;

    @Bean
    public Producer kaptchaProducer() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", imageWidth);
        properties.setProperty("kaptcha.image.height", imageHeight);
        properties.setProperty("kaptcha.textproducer.font.size", fontSize);
        properties.setProperty("kaptcha.textproducer.font.color", fontColor);
        properties.setProperty("kaptcha.textproducer.char.string", charString);
        properties.setProperty("kaptcha.textproducer.char.length", charLength);
        properties.setProperty("kaptcha.noise.impl", noiseImpl);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}
