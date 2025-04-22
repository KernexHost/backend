package com.twoweeksmc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    public static String fileContent(String path) {
        String content = "";
        try {
            content = Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }
}
