package com.cdut.young.test;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-05-04 22:35
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class TestZip {
    /**
     * 功能:压缩多个文件成一个zip文件
     *
     * @param srcfile：源文件列表
     * @param zipfile：压缩后的文件
     */
    public static void zipFiles(File[] srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            System.out.println("压缩完成.");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 功能:解压缩
     *
     * @param zipfile：需要解压缩的文件
     * @param descDir：解压后的目标目录
     */
    public static void unZipFiles(File zipfile, String descDir) {
        try {
            ZipFile zf = new ZipFile(zipfile);
            for (Enumeration entries = zf.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zf.getInputStream(entry);
                OutputStream out = new FileOutputStream(descDir + zipEntryName);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) < 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
                System.out.println("解压缩完成.");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 功能:
     *
     * @param args
     */
    public static void main(String[] args) {
        //2个源文件
        File file = new File("C:\\Users\\10237\\Desktop\\test");
        File[] srcfile = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {//只压缩目标文件夹符合条件的文件
                if (pathname.getName().contains("txt")) {//压缩txt文档
                    return true;
                }
                return false;
            }
        });

        //压缩后的文件
        File zipfile = new File("C:\\Users\\10237\\Desktop\\test.zip");
        TestZip.zipFiles(srcfile, zipfile);
        //需要解压缩的文件
        /*File file = new File("D:\\workspace\\flexTest\\src\\com\\biao\\test\\biao.zip");
        //解压后的目标目录
        String dir = "D:\\workspace\\flexTest\\src\\com\\biao\\test\\";
        TestZip.unZipFiles(file, dir);*/
    }
}
