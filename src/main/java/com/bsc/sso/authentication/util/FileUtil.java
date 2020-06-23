package com.bsc.sso.authentication.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.net.URLDecoder;

public class FileUtil {

    public FileUtil() {
    }

    public static String getStringFromResource(String fileName) throws Exception {
        BufferedReader bufferedReader = null;

        try {
            ClassLoader classLoader = new FileUtil().getClass().getClassLoader();
            String urlEncoded = classLoader.getResource(fileName).getFile();
            String urlDecoded = URLDecoder.decode(urlEncoded, "UTF-8");
            File file = new File(urlDecoded);
            bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

        }
    }
}
