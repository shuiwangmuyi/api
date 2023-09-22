package com.wy.music.common.util;

import com.wy.music.common.config.RestTemplateConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.List;


/**
 * @Author:wy
 * @Date: 2023/9/1  16:58
 * @Version 1.0
 */
public class HttpUtil {

    public static ResponseEntity<String> getRestTemplateResponseData(RestTemplate restTemplate, String url) {


        ResponseEntity<String> responseStr = restTemplate.getForEntity(url, String.class);
        return responseStr;

    }

    public static   ResponseEntity<byte[]> downloadFile(RestTemplate restTemplate, String url) {
        ResponseEntity<byte[]> responseStr = restTemplate.getForEntity(url, byte[].class);
        return responseStr;
    }

    /**
     * from 表单提交
     *
     * @param restTemplate
     * @param url
     * @param idList
     * @return
     */
    public static ResponseEntity<String> postFromDataTemplateResponseData(RestTemplate restTemplate, String url,
                                                                          List<String> idList) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        String ids = idList.toString().replace("[", "")
                .replace("]", "");
        map.add("ids", ids);
        //构造实体对象
        HttpEntity<MultiValueMap<String, Object>> param = new HttpEntity<>(map);
        ResponseEntity<String> response = restTemplate.postForEntity(url, param, String.class);
        return response;
    }


}
