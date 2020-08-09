package com.bsc.sso.authentication.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Objects;

public class FileUtil {

    public FileUtil() {
    }

    public static String getStringFromResource(String fileName) throws Exception {
        BufferedReader bufferedReader = null;

        try {
            ClassLoader classLoader = FileUtil.class.getClassLoader();
            String urlEncoded = Objects.requireNonNull(classLoader.getResource(fileName)).getFile();
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
            LOGGER.error("Error when get string form resource " + e.getMessage());
            throw e;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
}
