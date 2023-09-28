package com.wy.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:wy
 * @Date: 2023/8/25  16:55
 * @Version 1.0
 */
@SpringBootApplication
@EnableAsync
public class MusicApplication {

    @ResponseBody
    @RequestMapping(value = "/impMem")
    public static void main(String[] args) {
        SpringApplication.run(MusicApplication.class, args);
    }
}
