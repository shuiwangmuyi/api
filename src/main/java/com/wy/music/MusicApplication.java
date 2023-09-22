package com.wy.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author:wy
 * @Date: 2023/8/25  16:55
 * @Version 1.0
 */
@SpringBootApplication
@EnableAsync
public class MusicApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicApplication.class, args);
    }
}
