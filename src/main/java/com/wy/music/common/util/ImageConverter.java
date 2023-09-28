package com.wy.music.common.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 去掉水印的工具类
 */
public class ImageConverter {


    private static List<File> fileList = new ArrayList<File>();


    public static void main(String[] args) {
        File file = new File("C:\\Users\\17529\\Desktop\\姐\\解析\\in");
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File dirFile : files) {
                String name = dirFile.getName();
                String outDirPath = "C:\\Users\\17529\\Desktop\\姐\\解析\\out\\";
                outDirPath = outDirPath + name;
                System.out.println(outDirPath);
                convertAllImages(dirFile.getAbsolutePath(), outDirPath); //支持批量去除图片水印
            }
        }
    }

    private static void convertAllImages(String dir, String saveDir) {
        File dirFile = new File(dir);
        File saveDirFile = new File(saveDir);
        dir = dirFile.getAbsolutePath();
        saveDir = saveDirFile.getAbsolutePath();
        fileList.clear();
        loadImages(new File(dir));
        for (File file : fileList) {
            String filePath = file.getAbsolutePath();

            String dstPath = saveDir + filePath.substring(filePath.indexOf(dir) + dir.length(), filePath.length());
            System.out.println("converting: " + filePath);
            replaceColor(file.getAbsolutePath(), dstPath);
        }
    }

    public static void loadImages(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                File[] fileArray = f.listFiles();
                if (fileArray != null) {
                    for (int i = 0; i < fileArray.length; i++) {
//递归调用
                        loadImages(fileArray[i]);
                    }
                }
            } else {
                String name = f.getName();
                if (name.endsWith("png") || name.endsWith("jpg")) {
                    fileList.add(f);
                }
            }
        }
    }

    private static void replaceFolderImages(String dir) {
        File dirFile = new File(dir);
        File[] files = dirFile.listFiles(new FileFilter() {
            public boolean accept(File file) {
                String name = file.getName();
                if (name.endsWith("png") || name.endsWith("jpg")) {
                    return true;
                }
                return false;
            }
        });
        for (File img : files) {
            replaceColor(img.getAbsolutePath(), img.getAbsolutePath());
        }
    }

    private static void replaceColor(String srcFile, String dstFile) {
        try {
            Color color = new Color(255, 195, 195);
            replaceImageColor(srcFile, dstFile, color, Color.WHITE);
        } catch (IOException e) {
            System.out.println("error" + e);
        }
    }

    public static void replaceImageColor(String file, String dstFile, Color srcColor, Color targetColor) throws IOException {
        URL http;
        if (file.trim().startsWith("https")) {
            http = new URL(file);
            HttpsURLConnection conn = (HttpsURLConnection) http.openConnection();
            conn.setRequestMethod("GET");
        } else if (file.trim().startsWith("http")) {
            http = new URL(file);
            HttpURLConnection conn = (HttpURLConnection) http.openConnection();
            conn.setRequestMethod("GET");
        } else {
            http = new File(file).toURI().toURL();
        }
        BufferedImage bi = ImageIO.read(http.openStream());
        if (bi == null) {
            return;
        }

//Color wColor = new Color(255, 255, 255);//白色
        Color wColor = new Color(238, 243, 249);//浅灰色
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
//System.out.println(bi.getRGB(i, j));
                int color = bi.getRGB(i, j);
                Color oriColor = new Color(color);
                int red = oriColor.getRed();
                int greed = oriColor.getGreen();
                int blue = oriColor.getBlue();
//粉色
                if (greed < 190 || blue < 190) {

                } else {
//                    去掉粉色水印(粉色替换为白色)
                    if (red == 255 && greed > 180 && blue > 180) {
                        bi.setRGB(i, j, wColor.getRGB());
                    }
//                    去掉灰色水印（灰色替换为白色）
                    if (red == 229 && greed == 229 && blue == 229) {
                        bi.setRGB(i, j, wColor.getRGB());
                    }
//                    去掉浅灰色水印（灰色替换为白色或替换为浅灰色）
                    if (red > 170 && greed > 170 && blue > 170) {
                        bi.setRGB(i, j, wColor.getRGB());
                    }
                }
            }
        }
        String type = file.substring(file.lastIndexOf(".") + 1, file.length());
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(type);
        ImageWriter writer = it.next();
        File f = new File(dstFile);
        f.getParentFile().mkdirs();
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);
        writer.write(bi);
        bi.flush();
        ios.flush();
        ios.close();
    }
}