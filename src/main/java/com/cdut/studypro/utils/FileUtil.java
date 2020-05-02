package com.cdut.studypro.utils;

import java.io.File;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-05-02 14:15
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class FileUtil {
    public static void deleteDir(String dirPath) {
        File file = new File(dirPath);
        if (file.exists()) {
            if (!file.isFile()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        deleteDir(files[i].getAbsolutePath());
                    }
                }
            }
            file.delete();
        }
    }
}
