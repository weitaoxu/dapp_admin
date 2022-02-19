package com.qkbus.utils;

import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;

public class HelpUtils {
    //随机生成邀请码
    public static String createCode(int length) {
        String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        };
        StringBuilder buf = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            buf.append(str[random.nextInt(str.length)]);
        }
        return buf.toString();
    }

    /*
     * 验证是否是APP
     * */
    public static boolean isApp(String name) {
        String substring = name.substring(name.lastIndexOf(".") + 1);
        if (!StringUtils.isEmpty(substring)) {
            if (!substring.equals("wgt") && !substring.equals("ipa") && !substring.equals("apk")
                    && !substring.equals("plist")) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /*
     * 验证图片
     * */
    public static boolean isImage(InputStream inputStream) {
        if (inputStream == null) {
            return false;
        }
        Image img;
        try {
            img = ImageIO.read(inputStream);
            return !(img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0);
        } catch (Exception e) {
            return false;
        }
    }
}
