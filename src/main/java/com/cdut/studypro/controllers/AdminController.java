package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.beans.AdminExample.*;
import com.cdut.studypro.services.AdminService;
import com.cdut.studypro.services.CollegeService;
import com.cdut.studypro.services.StudentService;
import com.cdut.studypro.utils.MD5Util;
import com.cdut.studypro.utils.POIUtils;
import com.cdut.studypro.utils.RequestResult;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:50
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private CollegeService collegeService;
    @Autowired
    private StudentService studentService;

    @ResponseBody
    @PostMapping(value = "/login")
    public RequestResult login(@RequestBody Map<String, String> map, HttpServletRequest request) {
        System.out.println(map);
        AdminExample adminExample = new AdminExample();
        Criteria criteria = adminExample.createCriteria();
        String message = "";
        if ("phone".equals(map.get("type"))) {
            criteria.andTelephoneEqualTo(map.get("username"));
            message = "手机号不存在，请稍后重试";
        } else if ("email".equals(map.get("type"))) {
            criteria.andEmailEqualTo(map.get("username"));
            message = "邮箱不存在，请稍后重试";
        } else if ("account".equals(map.get("type"))) {
            criteria.andAccountEqualTo(map.get("username"));
            message = "帐号不存在，请稍后重试";
        }
//        criteria.andPasswordEqualTo(map.get("password"));
        List<Admin> admins = adminService.selectAdminByExample(adminExample);
        if (admins != null && admins.size() != 0) {
            Admin admin = admins.get(0);
            String password = map.get("password");
            if (password.equals(MD5Util.stringToMD5(admin.getPassword()))) {
                //登录成功后将管理员信息保存在session中
                request.getSession().setAttribute("user", admin);
                return RequestResult.success();
            } else {
                return RequestResult.failure("密码错误，请稍后重试");
            }
        }
        return RequestResult.failure(message);
    }

    @RequestMapping("/adminIndex")
    public String adminIndex() {
        return "admin/adminIndex";
    }

    /*@ModelAttribute("colleges")
    public List<College> getColleges() {
        return collegeService.getAllColleges();
    }*/

    @RequestMapping("/createStudent")
    public String createStudent(Map<String, Object> map) {
        List<College> colleges = collegeService.getAllColleges();
        map.put("colleges", colleges);
        return "admin/createStudent";
    }

    @ResponseBody
    @PostMapping(value = "/saveStudent")
    public RequestResult saveStudent(Student student) {

        //在保存学生信息之前先查看数据库中是否有重复的数据
        StudentExample studentExample = new StudentExample();
        //1、查看学号是否存在
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andNumberEqualTo(student.getNumber());
        boolean exists = studentService.isExistsByExample(studentExample);
        System.out.println(exists);
        if (exists) {
            return RequestResult.failure("该学号已经存在");
        }
        //2、查看联系电话是否存在
        studentExample.clear();
        criteria = studentExample.createCriteria();
        criteria.andTelephoneEqualTo(student.getTelephone());
        exists = studentService.isExistsByExample(studentExample);
        if (exists) {
            return RequestResult.failure("该电话已经存在");
        }
        //3、省份证号码是否存在
        studentExample.clear();
        criteria = studentExample.createCriteria();
        criteria.andIdCardNoEqualTo(student.getIdCardNo());
        exists = studentService.isExistsByExample(studentExample);
        if (exists) {
            return RequestResult.failure("该身份证号码已经存在");
        }
        //4、查看登录账号是否存在
        studentExample.clear();
        criteria = studentExample.createCriteria();
        criteria.andAccountEqualTo(student.getAccount());
        exists = studentService.isExistsByExample(studentExample);
        if (exists) {
            return RequestResult.failure("该账号已经存在");
        }
        //5、查看邮箱是否存在
        studentExample.clear();
        criteria = studentExample.createCriteria();
        criteria.andEmailEqualTo(student.getEmail());
        exists = studentService.isExistsByExample(studentExample);
        if (exists) {
            return RequestResult.failure("该邮箱已经存在");
        }

        //所有内容都不存在，则符合添加条件
        boolean b = studentService.insertStudentSelective(student);
        if (!b) {
            return RequestResult.failure("学生添加失败");
        }
        return RequestResult.success();
    }


    @RequestMapping("/createStudentBatch")
    public String createStudentBatch(Map<String, Object> map) {
        return "admin/createStudentBatch";
    }


    //下载学生模板
    @RequestMapping("/downloadTemplate")
    public ResponseEntity<byte[]> downloadTemplate(HttpServletRequest request) throws IOException {
        String path = request.getServletContext().getRealPath("/excels/");
        String fileName = "Student_Template.xlsx";
        File file = new File(path + fileName);

        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        // 将对文件做的特殊处理还原
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    //异常处理
    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public RequestResult handException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        return RequestResult.failure("文件超过了大小，上传失败！");
    }


    //批量上传学生信息

    /**
     * @description: 1、POI 提供了对2003版本的Excel的支持 ---- HSSFWorkbook(.xls格式)
     * 2、POI 提供了对2007版本以及更高版本的支持 ---- XSSFWorkbook(.xlsx格式)
     * @author: Mr.Young
     * @date: 2020/4/3 20:54
     * @param: file:
     * @param: request:
     * @return: com.cdut.studypro.utils.RequestResult:
     * @version: v1.0
     */
    @ResponseBody
    @PostMapping("/studentDataImport")
    public RequestResult studentDataImport(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
        String fileName = file.getOriginalFilename();
        List<Student> students = new ArrayList<>();
        //是".xls"格式
        if (fileName.endsWith(".xls")) {
            //定义工作簿，一个工作簿可以有多个并做表Sheet
            HSSFWorkbook hssfWorkbook = null;
            try (InputStream inputStream = file.getInputStream()) {
                hssfWorkbook = new HSSFWorkbook(inputStream);
                //遍历工作表
                for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
                    //获取到当前工作表
                    HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                    if (hssfSheet == null) {
                        continue;
                    }
                    //获取行
                    //默认第一行是标题,index=0
                    HSSFRow titleRow = hssfSheet.getRow(0);
                    //判断表头是否被修改或换位置
                    //原表头顺序：姓名、学号、电话、身份证号码、账号、密码、邮箱；使用数组来比较
                    String[] original_title = new String[]{"姓名", "学号", "电话", "身份证号码", "账号", "密码", "邮箱"};
                    String[] current_title = new String[7];
                    for (int cellIndex = 0; cellIndex < 7; cellIndex++) {
                        String cell = POIUtils.getStringHSSF(titleRow.getCell(cellIndex));
                        current_title[cellIndex] = cell;
                    }
                    //判断两个标题数组的内容是否一致
                    boolean equals = Arrays.equals(original_title, current_title);

                    if (!equals) {
                        return RequestResult.failure("导入失败，请检查模板中的表头是否被修改");
                    }
                    //获取总行数
                    int totalRows = POIUtils.getExcelRealRowHSSF(hssfSheet);
                    //从第二行开始循环读取数据
                    for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                        //获取行对象
                        HSSFRow hssfRow = hssfSheet.getRow(rowIndex);
                        if (hssfRow == null) {
                            continue;
                        }
                        //读取列，从第一列开始
                        Student student = new Student();
                        String data = POIUtils.getStringHSSF(hssfRow.getCell(0));
                        HSSFCell cell = hssfRow.getCell(0);
                        cell.setCellType(CellType.STRING);
                        student.setName(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(1));
                        student.setNumber(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(2));
                        student.setTelephone(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(3));
                        student.setIdCardNo(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(4));
                        student.setAccount(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(5));
                        student.setPassword(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(6));
                        student.setEmail(data);
                        System.out.println(student);
                        students.add(student);
                    }
                    studentService.insertStudentBatch(students);
                }
            } catch (IOException e) {
                return RequestResult.failure("批量导入失败，请稍后再试");
            }
        }
        //是".xlsx"格式
        else if (fileName.endsWith(".xlsx")) {
            //定义工作簿，一个工作簿可以有多个并做表Sheet
            XSSFWorkbook xssfWorkbook = null;
            try (InputStream inputStream = file.getInputStream()) {
                xssfWorkbook = new XSSFWorkbook(inputStream);
                //遍历工作表
                for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                    //获取到当前工作表
                    XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                    if (xssfSheet == null) {
                        continue;
                    }
                    //获取行
                    //默认第一行是标题,index=0
                    XSSFRow titleRow = xssfSheet.getRow(0);
                    //判断表头是否被修改或换位置
                    //原表头顺序：姓名、学号、电话、身份证号码、账号、密码、邮箱；使用数组来比较
                    String[] original_title = new String[]{"姓名", "学号", "电话", "身份证号码", "账号", "密码", "邮箱"};
                    String[] current_title = new String[7];
                    for (int cellIndex = 0; cellIndex < 7; cellIndex++) {
                        String cell = POIUtils.getStringXSSF(titleRow.getCell(cellIndex));
                        current_title[cellIndex] = cell;
                    }
                    //判断两个标题数组的内容是否一致
                    boolean equals = Arrays.equals(original_title, current_title);

                    if (!equals) {
                        return RequestResult.failure("导入失败，请检查模板中的表头是否被修改");
                    }
                    //获取总行数
                    int totalRows = POIUtils.getExcelRealRowXSSF(xssfSheet);
                    //从第二行开始循环读取数据
                    for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                        //获取行对象
                        XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
                        if (xssfRow == null) {
                            continue;
                        }
                        //读取列，从第一列开始
                        Student student = new Student();
                        String data = POIUtils.getStringXSSF(xssfRow.getCell(0));
                        XSSFCell cell = xssfRow.getCell(0);
                        cell.setCellType(CellType.STRING);
                        student.setName(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(1));
                        student.setNumber(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(2));
                        student.setTelephone(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(3));
                        student.setIdCardNo(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(4));
                        student.setAccount(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(5));
                        student.setPassword(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(6));
                        student.setEmail(data);
                        System.out.println(student);
                        students.add(student);
                    }
                    studentService.insertStudentBatch(students);
                }
            } catch (IOException e) {
                return RequestResult.failure("批量导入失败，请稍后再试");
            }
        } else {
            return RequestResult.failure("请上传EXCEL文档");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchStudent")
    public String searchStudent(Map<String, Object> map) {
        return "admin/searchStudent";
    }

}
