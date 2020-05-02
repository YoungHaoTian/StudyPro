package com.cdut.young.test;

import com.cdut.studypro.beans.Teacher;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-04-30 17:27
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class POITest {
    @Test
    public void testExcel() throws IOException {
//        File file = new File("test.xlsx");
        File file = new File("src/test/resources/Course_Template.xlsx");
        InputStream is = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
        //设置居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
        //设置字体样式
        XSSFFont font = xssfWorkbook.createFont();
        font.setFontName("宋体");
        cellStyle.setFont(font);
        //获取"教师信息"表
        XSSFSheet teacherSheet = xssfWorkbook.getSheet("教师信息");
        //读取第一行
        //XSSFRow firstRow = teacherSheet.getRow(0);
        //创建行
        XSSFRow xssfRow;
        //创建列，即单元格Cell
        XSSFCell xssfCell;
        //从第二行开始写入
        xssfRow = teacherSheet.createRow(1);
        //创建每个单元格Cell，即列的数据
        xssfCell = xssfRow.createCell(0, CellType.STRING);
        xssfCell.setCellStyle(cellStyle);
        xssfCell.setCellValue("1");
        xssfCell = xssfRow.createCell(1, CellType.STRING);
        xssfCell.setCellStyle(cellStyle);
        xssfCell.setCellValue("1");
        xssfCell = xssfRow.createCell(2, CellType.STRING);
        xssfCell.setCellStyle(cellStyle);
        xssfCell.setCellValue("1");
        xssfCell = xssfRow.createCell(3, CellType.STRING);
        xssfCell.setCellStyle(cellStyle);
        xssfCell.setCellValue("1");
        OutputStream os = new FileOutputStream(file);
        //用输出流写到excel
        try {
            xssfWorkbook.write(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteDir(){
        deleteDir("src/test/resources/test");
    }

    public static void deleteDir(String dirPath)
	{
		File file = new File(dirPath);
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
