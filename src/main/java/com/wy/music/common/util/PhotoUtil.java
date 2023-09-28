package com.wy.music.common.util;

import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.render.*;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.*;

/**
 * @Author:wy
 * @Date: 2023/9/27  13:22
 * @Version 1.0
 */
public class PhotoUtil {
    final static File path = new File("/Users/mac/Downloads/压缩前.jpeg");

    final static File outPath = new File("/Users/mac/Downloads/压缩后.jpeg");
    final static File rpath = new File("/Users/mac/Downloads/加水印.jpeg");
    final static Font FONT = new Font("黑体", Font.PLAIN, 20);

    public static void main(String[] args) throws Exception {

        FixDrawTextItem item = new FixDrawTextItem("保住头发别秃！", Color.BLACK, Color.BLACK,
                FONT, 10, FixDrawTextItem.Position.TOP_LEFT, 1f);
        doDrawImageText(item);

    }

    /**
     * 压缩
     *
     * @param items
     */
    public static void scaleParameter(DrawTextItem... items) {
        ScaleParameter scaleParameter = new ScaleParameter(178, 178, ScaleParameter.Algorithm.AUTO);
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        ImageRender wr = null;

        try {
            fileInputStream = new FileInputStream(path);
            fileInputStream = new FileInputStream(outPath);
            ImageRender rr = new ReadRender(fileInputStream);
            ImageRender rs = new ScaleRender(rr, scaleParameter);
            wr = new WriteRender(rs, fileOutputStream);
            wr.render();
        } catch (FileNotFoundException | SimpleImageException e) {
            System.out.println(e);
        }


    }

    /**
     * 加水印
     *
     * @param items
     * @throws Exception
     */
    public static void doDrawImageText(DrawTextItem... items) throws Exception {
        InputStream in = null;
        ImageRender fr = null;
        try {
            in = new FileInputStream(path);
            ImageRender rr = new ReadRender(in);

            DrawTextParameter dp = new DrawTextParameter();

            if (items != null) {
                for (DrawTextItem itm : items) {
                    dp.addTextInfo(itm);
                }
            }
            DrawTextRender dtr = new DrawTextRender(rr, dp);
            fr = new WriteRender(dtr, rpath);
            fr.render();
        } finally {
            IOUtils.closeQuietly(in);
            if (fr != null) {
                fr.dispose();
            }
        }
    }
}
