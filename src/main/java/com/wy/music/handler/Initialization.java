package com.wy.music.handler;

import com.wy.music.common.util.YmlUtil;
import com.wy.music.common.vo.ConfValEntityVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * spring容器初始化完成后进行一些其他初始化操作
 *
 * @Author:wy
 * @Date: 2023/9/22  16:09
 * @Version 1.0
 */
@Slf4j
@Component
public class Initialization implements ApplicationRunner {

    private static ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
    private static String profile;


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        scheduleUpdateConf();
    }

    private void scheduleUpdateConf() {
        try {
            Map lhm = YmlUtil.loadYaml("application.yml");
            profile = (String) YmlUtil.getValByKey(lhm, "spring.profiles.active");
        } catch (Exception e) {
            log.error("加载配置文件application.yml异常");
        }

        //开启定时刷新内存中配置文件内容
        log.info("refresh config file start");
        exec.scheduleAtFixedRate(Initialization::updateConfVal, 0, 10, TimeUnit.SECONDS);
        log.info("refresh config file end");
    }

    /**
     * 更新配置文件值
     */
    private static void updateConfVal() {
        try {
            Map lhm = YmlUtil.loadYaml("application_" + profile + ".yml");
            log.info("profile=" + "application_-" + profile + ".yml");

            String myTestConf = (String) YmlUtil.getValByKey(lhm, "myTestConf");
            ConfValEntityVo.myTestConf = myTestConf;
            String myTestConf2 = (String) YmlUtil.getValByKey(lhm, "myTestConf2");
            ConfValEntityVo.myTestConf2 = myTestConf2;
            String show =  YmlUtil.getValByKey(lhm, "canSee.show").toString();
            ConfValEntityVo.show = show;
            String fi = YmlUtil.getValByKey(lhm, "canSee.fi").toString();
            ConfValEntityVo.fi = fi;
            log.info("实时配置myTestConf==" + myTestConf + "，myTestConf2==" + myTestConf2 + "，show==" + show + "，fi==" + fi);
        } catch (Exception e) {
            log.error("更新配置文件值异常: ", e);
        }
    }


}
