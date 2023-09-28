package com.wy.music.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import com.wy.music.service.MusicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @Author:wy
 * @Date: 2023/8/28  10:55
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("music")
public class MusicController {


    @Resource
    private MusicService service;

    @PostMapping("impMem")
    public String test11() {
        System.out.println("fafafaf");
        return "sfaffafkaf";
    }

    @PostMapping("test")
    public Map test() {

        return service.test();


    }

    @PostMapping("down")
    public void downMusic(String name, HttpServerRequest request, HttpServerResponse response) throws IOException {

        service.down(name, request, response);


    }
}
