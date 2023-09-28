package com.wy.music.service;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * @Author:wy
 * @Date: 2023/8/30  16:30
 * @Version 1.0
 */
public interface MusicService {
    void down(String name ,HttpServerRequest request, HttpServerResponse response) throws IOException;

    Map test();
}
