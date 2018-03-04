package com.capton.md5;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static String fileToMD5(String path){
        try {
            FileInputStream fis = new FileInputStream(path);
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            fis.close();
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return  bigInt.toString(16);
        } catch (IOException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }
    public static void main(String []arg){
        System.out.println("MD5Util.main "+fileToMD5("/users/capton/downloads/linuxdeploy.apk"));
    }
}
