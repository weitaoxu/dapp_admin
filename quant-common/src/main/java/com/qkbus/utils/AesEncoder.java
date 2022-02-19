package com.qkbus.utils;

import com.qkbus.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/*
 * AES对称加密和解密
 */
@Slf4j
public class AesEncoder {


    private static final String charset = "utf-8";
    private static final String keyGenerator = "AES";

    private static final String SYS_ENCODE_KEY = RedisConstant.SYSTEM_NAME + "ApPasswi0rd@!!";

    /*
     * 加密
     */
    private static String excuteEncode(String content, String rule) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance(keyGenerator);
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组

            //防止linux下 随机生成key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(rule.getBytes(charset));
            keygen.init(128, secureRandom);

            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey secretKey = new SecretKeySpec(raw, keyGenerator);
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(keyGenerator);
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            String substring = content.substring(5, 7);
            String substring1 = content.substring(8, 10);
            StringBuilder sb = new StringBuilder(content);
            sb.replace(5, 7, substring1);
            sb.replace(8, 10, substring);
            content = sb.toString();


            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = content.getBytes(charset);
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(byte_encode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String AES_encode = new String(new BASE64Encoder().encode(byte_AES));
            //11.将字符串返回
            return AES_encode;
        } catch (Exception ex) {
            log.error("加密失败", ex);
        }
        //如果有错就返加nulll
        return null;
    }

    /*
     * 解密
     */
    private static String excuteDecode(String content, String rule) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance(keyGenerator);
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(rule.getBytes(charset));
            keygen.init(128, secureRandom);
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, keyGenerator);
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(keyGenerator);
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
            /*
             * 解密
             */
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, charset);

            String substring = AES_decode.substring(5, 7);
            String substring1 = AES_decode.substring(8, 10);
            StringBuilder sb = new StringBuilder(AES_decode);
            sb.replace(5, 7, substring1);
            sb.replace(8, 10, substring);
            AES_decode = sb.toString();
            return AES_decode;
        } catch (Exception ex) {
            log.error("解密失败：{}", content);
        }
        return null;
    }

    /*
     * 加密
     */
    public static String excuteApiEncode(String content, String key) {
        return excuteEncode(content, SYS_ENCODE_KEY + key);
    }

    /*
     * 解密
     */
    public static String excuteApiDecode(String content, String key) {
        return excuteDecode(content, SYS_ENCODE_KEY + key);
    }

    public static void main(String[] args) {
        System.out.println(excuteEncode("qz5c4v5b6n-18439331-60f6a1a2-afbb7", SYS_ENCODE_KEY));
        System.out.println(excuteDecode("J1Dcy8VvxTCO7y7cCiU4viSQWlnmdMs0MihmRP/Eo4pIlraUokNQPO+gTnjOF0Bs", SYS_ENCODE_KEY));
    }
}