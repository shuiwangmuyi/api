package com.wy.music.service.impl;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import com.wy.music.common.util.HttpUtil;
import com.wy.music.common.util.PinUtil;
import com.wy.music.common.vo.ConfValEntityVo;
import com.wy.music.common.vo.DownVo;
import com.wy.music.common.vo.SingerVo;
import com.wy.music.service.MusicService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.wy.music.common.vo.ConfValEntityVo.*;

/**
 * @Author:wy
 * @Date: 2023/8/30  16:31
 * @Version 1.0
 */
@Service
@Slf4j
public class MusicServiceImpl implements MusicService {



    private final static String geshouSrcUrl = "https://www.9ku.com";
    private final static String GE_SHOU_URL = "https://www.9ku.com/geshou/all-";
    private final static String DOWN_URl = "https://www.9ku.com/playlist.php";
    @Resource
    private RestTemplate template;

    @Override
    public void down(String name, HttpServerRequest request, HttpServerResponse response) throws IOException {
        // 根据名字获取英文首字母

        String charName = PinUtil.getFirstSpell(name);
        log.info("name:{},firstName:{}", name, charName.substring(0, 1));
        charName = charName.substring(0, 1);
//        String url = "https://www.9ku.com/geshou/all-" + charName + "-all.htm";
        Integer num = 1;
        boolean isRead = false;
        SingerVo singerVo = new SingerVo();
        List<SingerVo> signs = new ArrayList<>();
        while (true) {
            String url = GE_SHOU_URL + charName + "-all/" + num + ".htm";
            // TODO: 2023/9/1   获取网页信息
            ResponseEntity<String> restTemplateResponseData = HttpUtil.getRestTemplateResponseData(template, url);
//            System.out.println(restTemplateResponseData);

            String body = restTemplateResponseData.getBody();
            if (ObjectUtils.isEmpty(body)) {
                break;
            }
            // TODO: 2023/9/1  解析网页
            Document document = Jsoup.parse(body, "utf-8");

            Elements signErDocument = document.select("li");
            Iterator<Element> signErIter = signErDocument.iterator();
            while (signErIter.hasNext()) {
                Element next = signErIter.next();
                Elements nextDocument = next.select("a");
                Iterator<Element> signUrlItem = nextDocument.iterator();

                //获取当前歌手地址
                while (signUrlItem.hasNext()) {
                    Element imgElement = signUrlItem.next();
                    Attributes attributes = imgElement.attributes();
                    //歌手地址
                    String href = geshouSrcUrl + imgElement.attr("href");
                    //图片地址
                    Node node = imgElement.childNode(1);
                    String imgSrc = node.attr("src");
                    String imgName = node.attr("alt");
                    SingerVo vo = new SingerVo();
                    signs.add(vo);
                    vo.setHref(href);
                    vo.setImgName(imgName);
                    vo.setImgSrc(imgSrc);
                    if (name.equals(imgName)) {
                        singerVo.setHref(href);
                        singerVo.setImgName(imgName);
                        singerVo.setImgSrc(imgSrc);
                        isRead = true;
                    }
                    break;
                }
                if (isRead) {
                    break;
                }

            }
            System.out.println("===========================================一组完成" + num);
            if (isRead) {
                break;
            }
            num++;
        }
        if (isRead && !ObjectUtils.isEmpty(singerVo)) {
            //知道了对应的信息

            //创建名称
            File file = new File("D:\\" + singerVo.getImgName());
            if (!file.exists()) {
                file.mkdir();
            }
            String savePath = file.getAbsolutePath();


            String href = singerVo.getHref();
            ResponseEntity<String> responseData = HttpUtil.getRestTemplateResponseData(template, href);
            String body = responseData.getBody();
            // TODO: 2023/9/1  解析网页
            Document document = Jsoup.parse(body, "utf-8");
            Elements select = document.select("#body .singerMusic");
            Iterator<Element> iterator = select.iterator();
            while (iterator.hasNext()) {
                Element next = iterator.next();
//                Elements select1 = document.select("#body .singerMusic").iterator().next().select(".singerMusic ol li");
                Elements select1 = next.select(".singerMusic ol li");
                Iterator<Element> iterator1 = select1.iterator();
                while (iterator1.hasNext()) {
                    Element next1 = iterator1.next();
                    Elements select2 = next1.select("li div a");
                    String[] hrefs = select2.attr("href").split("/");
                    String id = hrefs[hrefs.length - 1].split("\\.")[0];
                    System.out.println(id);
                    String liDivA = next1.select("li div a").text();

                    System.out.println(id + "==============" + liDivA);
                    List<String> idList = new ArrayList<>();
                    idList.add(id);
                    //开始下载
                    ResponseEntity<String> response1 = HttpUtil.postFromDataTemplateResponseData(template, DOWN_URl, idList);
                    String body1 = response1.getBody();
                    System.out.println(body1);

                    List<DownVo> downVos = JSON.parseArray(body1, DownVo.class);
                    System.out.println(downVos);
                    for (DownVo downVo : downVos) {
                        System.out.println(downVo);
                        String singName = downVo.getMname();
                        String signUrl = downVo.getWma();

                        //下载
                        ResponseEntity<byte[]> responseEntity = HttpUtil.downloadFile(template, signUrl);
                        System.out.println("文件下载请求结果状态码：" + responseEntity.getStatusCode());

                        // 将下载下来的文件内容保存到本地
                        String targetPath = savePath + "\\" + singName + ".mp3";
                        Files.write(Paths.get(targetPath), Objects.requireNonNull(responseEntity.getBody(),
                                "未获取到下载文件"));
                    }
                }
            }
        }
    }




    @Override
    public Map test() {

        Map<String,Object > map=new HashMap<>();
        map.put("show", ConfValEntityVo.show);
        map.put("fi",ConfValEntityVo.fi);
        map.put("myTestConf",myTestConf);
        map.put("myTestConf2",myTestConf2);


        return map;

    }
}
