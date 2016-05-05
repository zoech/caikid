package com.imzoee.caikid.utils.misc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zoey on 2016/4/21.
 *
 * Interface related to encryption, such as password md5 encryption.
 */
public class EncryptUtils {

    public static String getMD5(String src){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
