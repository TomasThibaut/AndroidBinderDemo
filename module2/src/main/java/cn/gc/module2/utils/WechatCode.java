package cn.gc.module2.utils;


import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class WechatCode {

    //微信计算md5方法
    //wechat 7.0.6, com.tencent.mm.a.g
    public static String md5(byte[] bArr) {
        char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(bArr);
            byte[] digest = instance.digest();
            int length = digest.length;
            char[] cArr2 = new char[(length * 2)];
            int i = 0;
            int i2 = 0;
            while (i < length) {
                byte b = digest[i];
                int i3 = i2 + 1;
                cArr2[i2] = cArr[(b >>> 4) & 15];
                int i4 = i3 + 1;
                cArr2[i3] = cArr[b & 15];
                i++;
                i2 = i4;
            }
            return new String(cArr2);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 可以通过这个方式获取imei
     */
    ///data/data/com.tencent.mm/files/KeyInfo.bin
    //SecretKeySpec secretKeySpec = new SecretKeySpec("_wEcHAT_".getBytes(), "RC4");
    public static List<String> getImei() {
        List<String> imeiList = new ArrayList<>();
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec("_wEcHAT_".getBytes(), "RC4");
            Cipher instance = Cipher.getInstance("RC4");
            instance.init(2, secretKeySpec);
            BufferedReader bufferedReader = null;
            bufferedReader = new BufferedReader(new InputStreamReader(new CipherInputStream(new FileInputStream("/data/data/com.tencent.mm/files/KeyInfo.bin"), instance)));

            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    imeiList.add(readLine);
                } catch (Exception e2) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imeiList;
    }

}
