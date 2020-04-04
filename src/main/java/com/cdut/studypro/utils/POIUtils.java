package com.cdut.studypro.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-04-03 21:56
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class POIUtils {

    /**
     * @param hssfCell 单元格
     * @return 字符串
     * @description: 把单元格的内容转为字符串
     * _NONE(-1),
     * NUMERIC(0),数值型
     * STRING(1),字符串型
     * FORMULA(2),公式型
     * BLANK(3),空值
     * BOOLEAN(4),布尔型
     * ERROR(5);错误
     */
    public static String getStringHSSF(HSSFCell hssfCell) {
        if (hssfCell == null) {
            return "";
        }
        if (hssfCell.getCellType() == CellType.NUMERIC) {//数字
            return String.valueOf(hssfCell.getNumericCellValue());
        } else if (hssfCell.getCellType() == CellType.BOOLEAN) {//Boolean
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == CellType.BLANK) {//空值
            return "";
        } else if (hssfCell.getCellType() == CellType.FORMULA) {//空值
            return String.valueOf(hssfCell.getCellFormula());
        }
        return hssfCell.getStringCellValue();
    }

    public static String getStringXSSF(XSSFCell xssfCell) {
        if (xssfCell == null) {
            return "";
        }
        if (xssfCell.getCellType() == CellType.NUMERIC) {//数字
            return String.valueOf(xssfCell.getNumericCellValue());
        } else if (xssfCell.getCellType() == CellType.BOOLEAN) {//Boolean
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == CellType.BLANK) {//空值
            return "";
        } else if (xssfCell.getCellType() == CellType.FORMULA) {//公式
            return String.valueOf(xssfCell.getCellFormula());
        }
        return xssfCell.getStringCellValue();
    }

    // 获取Excel表的真实行数
    public static int getExcelRealRowHSSF(HSSFSheet hssfSheet) {
        boolean flag = false;
        for (int i = 1; i <= hssfSheet.getLastRowNum(); ) {//从第二行开始，第一行是表头
            Row r = hssfSheet.getRow(i);
            if (r == null) {
                // 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
                hssfSheet.shiftRows(i + 1, hssfSheet.getLastRowNum(), -1);
                continue;
            }
            flag = false;
            for (Cell c : r) {
                if (c.getCellType() != CellType.BLANK) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                i++;
                continue;
            } else {
                // 如果是空白行（即可能没有数据，但是有一定格式）
                if (i == hssfSheet.getLastRowNum())// 如果到了最后一行，直接将那一行remove掉
                    hssfSheet.removeRow(r);
                else//如果还没到最后一行，则数据往上移一行
                    hssfSheet.shiftRows(i + 1, hssfSheet.getLastRowNum(), -1);
            }
        }
        return hssfSheet.getLastRowNum();
    }

    public static int getExcelRealRowXSSF(XSSFSheet xssfSheet) {
        boolean flag = false;
        for (int i = 1; i <= xssfSheet.getLastRowNum(); ) {//从第二行开始，第一行是表头
            Row r = xssfSheet.getRow(i);
            if (r == null) {
                // 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
                xssfSheet.shiftRows(i + 1, xssfSheet.getLastRowNum(), -1);
                continue;
            }
            flag = false;
            for (Cell c : r) {
                if (c.getCellType() != CellType.BLANK) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                i++;
                continue;
            } else {
                // 如果是空白行（即可能没有数据，但是有一定格式）
                if (i == xssfSheet.getLastRowNum())// 如果到了最后一行，直接将那一行remove掉
                    xssfSheet.removeRow(r);
                else//如果还没到最后一行，则数据往上移一行
                    xssfSheet.shiftRows(i + 1, xssfSheet.getLastRowNum(), -1);
            }
        }
        return xssfSheet.getLastRowNum();
    }
}
