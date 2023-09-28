package com.wy.music.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author:wy
 * @Date: 2023/9/22  16:05
 * @Version 1.0
 */
public class YmlUtil {

    public static LinkedHashMap loadYaml(String fileName)throws Exception{
        String path=System.getProperty("user.dir");

        File file=new File(path+"/config/"+fileName);
        InputStream in = null;
        System.out.println("The config file "+path+"/config/"+fileName+" is exist? "+file.exists());
        if (file.exists()) {
            in = new FileInputStream(path+"/config/" + fileName);
        } else {
            //如果没有config文件夹，则从项目的resources目录下找
            in = YmlUtil.class.getClassLoader().getResourceAsStream(fileName);
        }
        LinkedHashMap lhm= new Yaml().loadAs(in, LinkedHashMap.class);
        return lhm;
    }

    public static Object getValByKey(Map lhm,String key)throws Exception{
        String[] keys = key.split("[.]");
        Map ymlInfo = lhm;
        for (int i = 0; i < keys.length; i++) {
            Object value = ymlInfo.get(keys[i]);
            if (i < keys.length - 1) {
                ymlInfo = (Map) value;
            } else if (value == null) {
                throw new Exception("key不存在");
            } else {
                return value;
            }
        }
        throw new RuntimeException("不可能到这里的...");
    }

    /**
     * slappasswd -s 123456 转java 代码（SSHA） 公共方法
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String generateSSHAPwd(String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final int SALT_LENGTH = 4;
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(password.getBytes("utf-8"));
        crypt.update(salt);
        byte[] hash = crypt.digest();

        byte[] hashPlusSalt = new byte[hash.length + salt.length];
        System.arraycopy(hash, 0, hashPlusSalt, 0, hash.length);
        System.arraycopy(salt, 0, hashPlusSalt, hash.length, salt.length);

        return new StringBuilder().append("{SSHA}")
                .append(new String(Base64.encodeBase64(hashPlusSalt), Charset.forName("utf-8")))
                .toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String str =RandomStringUtils.randomAlphanumeric(8);
        String s = generateSSHAPwd("123456");

        if (true)
           return;


        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] saltBytes = "random_salt".getBytes(StandardCharsets.UTF_8);
            byte[] passwordBytes = "123456".getBytes(StandardCharsets.UTF_8);

            messageDigest.reset();
            messageDigest.update(saltBytes);
            byte[] hashedBytes = messageDigest.digest(passwordBytes);

            StringBuilder stringBuilder = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                stringBuilder.append(Integer.toString((hashedByte & 0xff) + 0x100, 16).substring(1));
            }

            System.out.println(stringBuilder.toString());
        } catch (NoSuchAlgorithmException e) {

            System.out.println(  e.getLocalizedMessage());
        }
    }
}
