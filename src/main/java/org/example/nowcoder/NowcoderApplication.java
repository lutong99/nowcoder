package org.example.nowcoder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.nowcoder.mapper")
public class NowcoderApplication {

    public static void main(String[] args) {
        SpringApplication.run(NowcoderApplication.class, args);
    }

}
