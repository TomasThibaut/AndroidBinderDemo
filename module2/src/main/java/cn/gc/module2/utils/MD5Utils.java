package cn.gc.module2.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Qingchang Bai on 9/14/16.
 */
public class MD5Utils {
    private static final String TAG = MD5Utils.class.getSimpleName();

    private static MessageDigest MD5;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.d(TAG, "MessageDigest MD5 init failed: " + e.getMessage());
        }
    }

    public static String getMD5Digest(String source) {
        if (source == null) {
            return null;
        }

        if (MD5 == null) {
            Log.d(TAG, "getMD5Digest failed: MessageDigest MD5 is null.");
            return null;
        }

        byte[] hash = MD5.digest(source.getBytes());
        StringBuilder hexDigest = new StringBuilder();
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hexDigest.append("0");
            }
            hexDigest.append(Integer.toHexString(b & 0xFF));
        }

        return hexDigest.toString();
    }
}
