package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.beans.AdminExample.*;
import com.cdut.studypro.exceptions.MaxUploadSizeExceedException;
import com.cdut.studypro.exceptions.NotExistException;
import com.cdut.studypro.services.AdminService;
import com.cdut.studypro.utils.MD5Util;
import com.cdut.studypro.utils.POIUtil;
import com.cdut.studypro.utils.RequestResult;
import com.cdut.studypro.validates.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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

    //异常处理
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public RequestResult handException(RuntimeException e, HttpServletRequest request) {
        return RequestResult.failure(e.getMessage());
    }

    //异常处理
    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceedException.class)
    public RequestResult handMaxUploadSizeExceedException(MaxUploadSizeExceedException e) {
        return RequestResult.failure(e.getMessage());
    }

    @ExceptionHandler(NotExistException.class)
    public String handNotExistException(NotExistException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return "error";
    }

    @ExceptionHandler(IOException.class)
    public String handIOException(IOException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return "error";
    }

    @ResponseBody
    @PostMapping(value = "/login")
    public RequestResult login(@RequestBody Map<String, String> map, HttpSession session) {
        AdminExample adminExample = new AdminExample();
        Criteria criteria = adminExample.createCriteria();
        String message = null;
        if ("phone".equals(map.get("type"))) {
            criteria.andTelephoneEqualTo(map.get("username"));
            message = "手机号不存在，请稍后重试";
        }
        if ("email".equals(map.get("type"))) {
            criteria.andEmailEqualTo(map.get("username"));
            message = "邮箱不存在，请稍后重试";
        }
        if ("account".equals(map.get("type"))) {
            criteria.andAccountEqualTo(map.get("username"));
            message = "帐号不存在，请稍后重试";
        }
        if (message == null) {
            return RequestResult.failure("登录失败，请稍后再试");
        }
        List<Admin> admins = adminService.selectAdminByExample(adminExample);
        if (admins != null && admins.size() != 0) {
            if (admins.size() != 1) {
                return RequestResult.failure("登录异常，请选择其他方式登录");
            }
            Admin admin = admins.get(0);
            String password = map.get("password");
            if (password.equals(MD5Util.stringToMD5(admin.getPassword()))) {
                //登录成功后将管理员信息保存在session中，并设置角色
                session.setAttribute("user", admin);
                session.setAttribute("role", "admin");
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

    @RequestMapping("/createStudent")
    public String createStudent(Map<String, Object> map) {
        List<College> colleges = adminService.getAllColleges();
        map.put("colleges", colleges);
        return "admin/createStudent";
    }

    @ResponseBody
    @PostMapping(value = "/saveStudent")
    public RequestResult saveStudent(@Validated({StudentSequence.class}) Student student, BindingResult result) {
        if (result.getErrorCount() > 0) {//后端验证错误
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        if (student == null) {
            return RequestResult.failure("新增学生失败，请稍后再试");
        }
        //在保存学生信息之前先查看数据库中是否有重复的数据
        StudentExample studentExample = new StudentExample();
        //1、查看学号是否存在
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andNumberEqualTo(student.getNumber());
        boolean exists = adminService.isStudentExistsByExample(studentExample);
        System.out.println(exists);
        if (exists) {
            return RequestResult.failure("该学号已经存在");
        }
        //2、查看联系电话是否存在
        studentExample.clear();
        criteria = studentExample.createCriteria();
        criteria.andTelephoneEqualTo(student.getTelephone());
        exists = adminService.isStudentExistsByExample(studentExample);
        if (exists) {
            return RequestResult.failure("该电话已经存在");
        }
        //3、身份证号码是否存在
        studentExample.clear();
        criteria = studentExample.createCriteria();
        criteria.andIdCardNoEqualTo(student.getIdCardNo());
        exists = adminService.isStudentExistsByExample(studentExample);
        if (exists) {
            return RequestResult.failure("该身份证号码已经存在");
        }
        //4、查看登录账号是否存在
        studentExample.clear();
        criteria = studentExample.createCriteria();
        criteria.andAccountEqualTo(student.getAccount());
        exists = adminService.isStudentExistsByExample(studentExample);
        if (exists) {
            return RequestResult.failure("该账号已经存在");
        }
        //5、查看邮箱是否存在
        studentExample.clear();
        criteria = studentExample.createCriteria();
        criteria.andEmailEqualTo(student.getEmail());
        exists = adminService.isStudentExistsByExample(studentExample);
        if (exists) {
            return RequestResult.failure("该邮箱已经存在");
        }
        //所有内容都不存在，则符合添加条件
        boolean b = adminService.insertStudentSelective(student);
        if (!b) {
            return RequestResult.failure("学生添加失败");
        }
        return RequestResult.success();
    }

    @RequestMapping("/createStudentBatch")
    public String createStudentBatch(Map<String, Object> map) {
        map.put("colleges", adminService.getAllColleges());
        return "admin/createStudentBatch";
    }

    //下载学生模板
    @RequestMapping("/downloadStudentTemplate")
    public ResponseEntity<byte[]> downloadStudentTemplate(HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/excels/");
        String fileName = "Student_Template.xlsx";
        File file = new File(path + fileName);

        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        // 将对文件做的特殊处理还原
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new NotExistException("你所访问的文件不存在");
        }
    }


    //批量上传学生信息

    /**
     * @description: 1、POI 提供了对2003版本的Excel的支持 ---- HSSFWorkbook(.xls格式)
     * 2、POI 提供了对2007版本以及更高版本的支持 ---- XSSFWorkbook(.xlsx格式)
     */
    @ResponseBody
    @PostMapping("/studentDataImport")
    public RequestResult studentDataImport(@RequestParam(value = "file") MultipartFile file, @RequestParam("collegeId") Integer collegeId) {
        String phone = "((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}";
        String idCardNo = "[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]";
        String email = "(\\w+\\.)*\\w+\\@+[0-9a-zA-Z]+\\.(com|com.cn|edu|hk|cn|net)";
        String fileName = file.getOriginalFilename();
        List<Student> students = new ArrayList<>();
        //批量导入学生需要保证手机号是唯一的
        List<String> phones = new ArrayList<>();
        List<String> idCardNos = new ArrayList<>();
        List<String> accounts = new ArrayList<>();
        List<String> emails = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        //获取到已注册学生的所有手机号码
        List<String> telephones = adminService.getAllStudentTelephone();
        //原表头顺序：姓名、学号、电话、性别、身份证号码、账号、密码、邮箱；使用数组来比较
        String[] original_title = new String[]{"姓名", "学号", "电话", "性别", "身份证号码", "账号", "密码", "邮箱"};
        String[] current_title = new String[original_title.length];
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
                    //获取总行数
                    int totalRows = POIUtil.getExcelRealRowHSSF(hssfSheet);
                    //默认第一行是标题,index=0
                    HSSFRow titleRow = hssfSheet.getRow(0);
                    //判断表头是否被修改或换位置
                    for (int cellIndex = 0; cellIndex < current_title.length; cellIndex++) {
                        current_title[cellIndex] = titleRow.getCell(cellIndex).getStringCellValue();
                    }
                    //判断两个标题数组的内容是否一致
                    boolean equals = Arrays.equals(original_title, current_title);
                    if (!equals) {
                        return RequestResult.failure("导入失败，请检查模板中的表头是否被修改");
                    }
                    //从第二行开始循环读取数据
                    for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                        //获取行对象
                        HSSFRow hssfRow = hssfSheet.getRow(rowIndex);
                        if (hssfRow == null) {
                            continue;
                        }
                        //读取列，从第一列开始
                        Student student = new Student();
                        HSSFCell cell;
                        CellType cellType;
                        String data;
                        for (int i = 0; i < original_title.length; i++) {
                            cell = hssfRow.getCell(i);
                            cellType = cell.getCellType();
                            if ("性别".equals(original_title[i])) {
                                if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的性别不是文本类型");
                                }
                                student.setGender(0);
                                if (cellType == CellType.STRING) {
                                    data = cell.getStringCellValue().trim();
                                    if ("男".equals(data)) {
                                        student.setGender(0);
                                    } else if ("女".equals(data)) {
                                        student.setGender(1);
                                    } else {
                                        return RequestResult.failure("导入失败，请检查第" + (rowIndex + 1) + "行同学的性别是否正确");
                                    }
                                }
                                continue;
                            }
                            if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不是文本类型");
                            }
                            data = "";
                            if (cellType == CellType.STRING) {
                                data = cell.getStringCellValue().trim();
                            }
                            if (data.contains(" ")) {//包含空格
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "中包含空格");
                            }
                            if (i == 0) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不能为空");
                                }
                                student.setName(data);
                            }
                            if (i == 1) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不能为空");
                                }
                                if (numbers.contains(data)) {//表中的学号有重复
                                    int j = numbers.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "和第" + (j + 2) + "行同学的重复了");
                                }
                                numbers.add(data);
                                student.setNumber(data);
                            }
                            if (i == 2) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不能为空");
                                }
                                if (!data.matches(phone)) {//判断号码是否符合格式
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "格式错误");
                                }
                                if (phones.contains(data)) {
                                    int j = phones.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "和第" + (j + 2) + "行同学的重复了");
                                }
                                phones.add(data);
                                if (telephones.contains(data)) {//判断号码是否已经被注册
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "已经被注册");
                                }
                                student.setTelephone(data);

                            }
                            if (i == 4) {
                                if (!"".equals(data)) {//身份证号码不为空时
                                    //判断身份证号码格式
                                    if (!data.matches(idCardNo)) {
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "格式错误");
                                    }
                                    if (idCardNos.contains(data)) {//表中的身份证号码有重复
                                        int j = idCardNos.indexOf(data);
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "和第" + (j + 2) + "行同学的重复了");
                                    }
                                    idCardNos.add(data);
                                }
                                student.setIdCardNo(data);
                            }
                            if (i == 5) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不能为空");
                                }
                                if (accounts.contains(data)) {//表中的账户有重复
                                    int j = accounts.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "和第" + (j + 2) + "行同学的重复了");
                                }
                                accounts.add(data);
                                student.setAccount(data);
                            }
                            if (i == 6) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不能为空");
                                }
                                student.setPassword(data);
                            }
                            if (i == 7) {
                                if (!"".equals(data)) {
                                    if (!data.matches(email)) {
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "格式错误");
                                    }
                                    if (emails.contains(data)) {//表中的邮箱有重复
                                        int j = emails.indexOf(data);
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "和第" + (j + 2) + "行同学的重复了");
                                    }
                                    emails.add(data);
                                }
                                student.setEmail(data);
                            }
                        }
                        student.setCollegeId(collegeId);
                        System.out.println(student);
                        students.add(student);
                    }
                    boolean success = adminService.insertStudentBatch(students);
                    if (!success) {
                        return RequestResult.failure("批量导入失败，请稍后再试");
                    }
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
                    //获取总行数
                    int totalRows = POIUtil.getExcelRealRowXSSF(xssfSheet);
                    //默认第一行是标题,index=0
                    XSSFRow titleRow = xssfSheet.getRow(0);
                    //判断表头是否被修改或换位置
                    for (int cellIndex = 0; cellIndex < current_title.length; cellIndex++) {
                        current_title[cellIndex] = titleRow.getCell(cellIndex).getStringCellValue();
                    }
                    //判断两个标题数组的内容是否一致
                    boolean equals = Arrays.equals(original_title, current_title);
                    if (!equals) {
                        return RequestResult.failure("导入失败，请检查模板中的表头是否被修改");
                    }
                    //从第二行开始循环读取数据
                    for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                        //获取行对象
                        XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
                        if (xssfRow == null) {
                            continue;
                        }
                        //读取列，从第一列开始
                        Student student = new Student();
                        XSSFCell cell;
                        CellType cellType;
                        String data;
                        for (int i = 0; i < original_title.length; i++) {
                            cell = xssfRow.getCell(i);
                            cellType = cell.getCellType();
                            if ("性别".equals(original_title[i])) {
                                if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的性别不是文本类型");
                                }
                                student.setGender(0);
                                if (cellType == CellType.STRING) {
                                    data = cell.getStringCellValue().trim();
                                    if ("男".equals(data)) {
                                        student.setGender(0);
                                    } else if ("女".equals(data)) {
                                        student.setGender(1);
                                    } else {
                                        return RequestResult.failure("导入失败，请检查第" + (rowIndex + 1) + "行同学的性别是否正确");
                                    }
                                }
                                continue;
                            }
                            if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不是文本类型");
                            }
                            data = "";
                            if (cellType == CellType.STRING) {
                                data = cell.getStringCellValue().trim();
                            }
                            if (data.contains(" ")) {//包含空格
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "中包含空格");
                            }
                            if (i == 0) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不能为空");
                                }
                                student.setName(data);
                            }
                            if (i == 1) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不能为空");
                                }
                                if (numbers.contains(data)) {//表中的学号有重复
                                    int j = numbers.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "和第" + (j + 2) + "行同学的重复了");
                                }
                                numbers.add(data);
                                student.setNumber(data);
                            }
                            if (i == 2) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不能为空");
                                }
                                if (!data.matches(phone)) {//判断号码是否符合格式
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "格式错误");
                                }
                                if (phones.contains(data)) {
                                    int j = phones.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "和第" + (j + 2) + "行同学的重复了");
                                }
                                phones.add(data);
                                if (telephones.contains(data)) {//判断号码是否已经被注册
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "已经被注册");
                                }
                                student.setTelephone(data);

                            }
                            if (i == 4) {
                                if (!"".equals(data)) {//身份证号码不为空时
                                    //判断身份证号码格式
                                    if (!data.matches(idCardNo)) {
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "格式错误");
                                    }
                                    if (idCardNos.contains(data)) {//表中的身份证号码有重复
                                        int j = idCardNos.indexOf(data);
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "和第" + (j + 2) + "行同学的重复了");
                                    }
                                    idCardNos.add(data);
                                }
                                student.setIdCardNo(data);
                            }
                            if (i == 5) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不能为空");
                                }
                                if (accounts.contains(data)) {//表中的账户有重复
                                    int j = accounts.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "和第" + (j + 2) + "行同学的重复了");
                                }
                                accounts.add(data);
                                student.setAccount(data);
                            }
                            if (i == 6) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "不能为空");
                                }
                                student.setPassword(data);
                            }
                            if (i == 7) {
                                if (!"".equals(data)) {
                                    if (!data.matches(email)) {
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "格式错误");
                                    }
                                    if (emails.contains(data)) {//表中的身份证号码有重复
                                        int j = emails.indexOf(data);
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行同学的" + original_title[i] + "和第" + (j + 2) + "行同学的重复了");
                                    }
                                    emails.add(data);
                                }
                                student.setEmail(data);
                            }
                        }
                        student.setCollegeId(collegeId);
                        System.out.println(student);
                        students.add(student);
                    }
                    boolean success = adminService.insertStudentBatch(students);
                    if (!success) {
                        return RequestResult.failure("批量导入失败，请稍后再试");
                    }
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
    public String searchStudent(Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, HttpSession session) {
        StudentExample studentExample = (StudentExample) session.getAttribute("studentExample");
        if (studentExample == null) {
            studentExample = new StudentExample();
            studentExample.setOrderByClause("number asc");
        }
        map.put("colleges", adminService.getAllColleges());
        //加入PageHelper分页插件，在查询之前只需要调用startPage方法
        //传入页码以及每页显示数据条数
        PageHelper.startPage(pageNum, 20);
        //后面的操作就是分页查询
        List<Student> students = adminService.getAllStudentsWithCollegeByExampleWithoutPassword(studentExample);
        //使用PageInfo包装查询结果，PageInfo包含了非常全面的分页属性，只需要将PageInfo交给页面就可以了
        //navigatePages：连续显示多少页
        PageInfo<Student> page = new PageInfo(students, 10);
        map.put("pageInfo", page);
        return "admin/searchStudent";
    }

    @ResponseBody
    @PostMapping("/deleteStudent")
    public RequestResult deleteStudent(@RequestParam("id") Integer id) {
        if (id == null) {
            return RequestResult.failure("学生删除失败，请稍后再试");
        }
        //删除学生信息
        boolean i = adminService.deleteStudentById(id);
        if (!i) {
            return RequestResult.failure("学生删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteStudentBatch")
    public RequestResult deleteStudentBatch(@RequestParam("ids") String id) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> studentIds = new ArrayList<>();
        for (String s : ids) {
            studentIds.add(Integer.parseInt(s));
        }
        boolean b = adminService.deleteStudentByIdBatch(studentIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/searchStudentByTerm")
    public RequestResult searchStudentByTerm(@RequestParam("number") String number, @RequestParam("collegeId") Integer collegeId, HttpSession session) {
        //将查询条件存放到session中，以回显查询条件
        Map<String, Object> map = new HashMap<>();
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        if (collegeId != 0) {
            criteria.andCollegeIdEqualTo(collegeId);
            map.put("collegeId", collegeId);
        }
        if (!"".equals(number.trim())) {
            criteria.andNumberLike("%" + number.trim() + "%");
            map.put("number", number.trim());
        }
        studentExample.setOrderByClause("number asc");
        session.setAttribute("studentExample", studentExample);
        session.setAttribute("studentQueryCriteria", map);
        return RequestResult.success();
    }

    @RequestMapping("/updateStudent/{id}")
    public String updateStudent(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        //判断id是否存在
        Student student = adminService.getStudentByPrimaryKey(id);
        if (student == null) {
            throw new NotExistException("该id对应的学生不存在");
        }
        map.put("colleges", adminService.getAllColleges());
        map.put("student", student);
        map.put("pageNum", pageNum);
        return "admin/updateStudent";
    }

    @ResponseBody
    @PostMapping("/editStudent/{id}")
    public RequestResult editStudent(@PathVariable("id") Integer id, @Validated({StudentSequence.class}) Student student, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        Student student1 = adminService.getStudentByPrimaryKey(id);
        if (student1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        if (student1.equals(student)) {
            return RequestResult.failure("未修改任何基本数据");
        }
        //在保存学生信息之前先查看数据库中是否有重复的数据
        StudentExample studentExample = new StudentExample();
        //1、查看学号是否存在
        StudentExample.Criteria criteria = studentExample.createCriteria();
        if (!student1.getNumber().equals(student.getNumber())) {
            criteria.andNumberEqualTo(student.getNumber());
            boolean exists = adminService.isStudentExistsByExample(studentExample);
            if (exists) {
                return RequestResult.failure("该学号已经存在");
            }
        } else {
            student.setNumber(null);
        }
        //2、查看联系电话是否存在
        if (!student1.getTelephone().equals(student.getTelephone())) {
            studentExample.clear();
            criteria = studentExample.createCriteria();
            criteria.andTelephoneEqualTo(student.getTelephone());
            boolean exists = adminService.isStudentExistsByExample(studentExample);
            if (exists) {
                return RequestResult.failure("该电话已经存在");
            }
        } else {
            student.setTelephone(null);
        }

        //3、身份证号码是否存在
        if (!student1.getIdCardNo().equals(student.getIdCardNo())) {
            studentExample.clear();
            criteria = studentExample.createCriteria();
            criteria.andIdCardNoEqualTo(student.getIdCardNo());
            boolean exists = adminService.isStudentExistsByExample(studentExample);
            if (exists) {
                return RequestResult.failure("该身份证号码已经存在");
            }
        } else {
            student.setIdCardNo(null);
        }

        //4、查看登录账号是否存在
        if (!student1.getAccount().equals(student.getAccount())) {
            studentExample.clear();
            criteria = studentExample.createCriteria();
            criteria.andAccountEqualTo(student.getAccount());
            boolean exists = adminService.isStudentExistsByExample(studentExample);
            if (exists) {
                return RequestResult.failure("该账号已经存在");
            }
        } else {
            student.setAccount(null);
        }

        //5、查看邮箱是否存在
        if (!student1.getEmail().equals(student.getEmail())) {
            studentExample.clear();
            criteria = studentExample.createCriteria();
            criteria.andEmailEqualTo(student.getEmail());
            boolean exists = adminService.isStudentExistsByExample(studentExample);
            if (exists) {
                return RequestResult.failure("该邮箱已经存在");
            }
        } else {
            student.setEmail(null);
        }
        if (student1.getName().equals(student.getName())) {
            student.setName(null);
        }
        if (student1.getCollegeId().equals(student.getCollegeId())) {
            student.setCollegeId(null);
        }
        if (student1.getGender().equals(student.getGender())) {
            student.setGender(null);
        }
        if (student1.getPassword().equals(student.getPassword())) {
            student.setPassword(null);
        }
        boolean b = adminService.updateStudentByPrimaryKeySelective(student);
        if (!b) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/createTeacher")
    public String createTeacher(Map<String, Object> map) {
        List<College> colleges = adminService.getAllColleges();
        List<Course> courses = adminService.getAllCourses();
        map.put("colleges", colleges);
        /*map.put("courses", courses);*/
        return "admin/createTeacher";
    }

    @ResponseBody
    @PostMapping("/saveTeacher")
    public RequestResult saveTeacher(@Validated({TeacherSequence.class}) Teacher teacher, BindingResult result) {
        if (result.getErrorCount() > 0) {//后端验证错误
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        if (teacher == null) {
            return RequestResult.failure("新增教师失败，请稍后再试");
        }
        //在保存教师信息之前先查看数据库中是否有重复的数据
        TeacherExample teacherExample = new TeacherExample();
        //1、查看教师编号是否存在
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
        criteria.andNumberEqualTo(teacher.getNumber());
        boolean exists = adminService.isTeacherExistsByExample(teacherExample);
        if (exists) {
            return RequestResult.failure("该教师编号已经存在");
        }
        //2、查看联系电话是否存在
        teacherExample.clear();
        criteria = teacherExample.createCriteria();
        criteria.andTelephoneEqualTo(teacher.getTelephone());
        exists = adminService.isTeacherExistsByExample(teacherExample);
        if (exists) {
            return RequestResult.failure("该电话已经存在");
        }
        //3、身份证号码是否存在
        teacherExample.clear();
        criteria = teacherExample.createCriteria();
        criteria.andIdCardNoEqualTo(teacher.getIdCardNo());
        exists = adminService.isTeacherExistsByExample(teacherExample);
        if (exists) {
            return RequestResult.failure("该身份证号码已经存在");
        }
        //4、查看登录账号是否存在
        teacherExample.clear();
        criteria = teacherExample.createCriteria();
        criteria.andAccountEqualTo(teacher.getAccount());
        exists = adminService.isTeacherExistsByExample(teacherExample);
        if (exists) {
            return RequestResult.failure("该账号已经存在");
        }
        //5、查看邮箱是否存在
        teacherExample.clear();
        criteria = teacherExample.createCriteria();
        criteria.andEmailEqualTo(teacher.getEmail());
        exists = adminService.isTeacherExistsByExample(teacherExample);
        if (exists) {
            return RequestResult.failure("该邮箱已经存在");
        }

        //所有内容都不存在，则符合添加条件
        boolean b = adminService.insertTeacherSelective(teacher);
        if (!b) {
            return RequestResult.failure("新增教师失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/createTeacherBatch")
    public String createTeacherBatch(Map<String, Object> map) {
        map.put("colleges", adminService.getAllColleges());
        return "admin/createTeacherBatch";
    }

    @ResponseBody
    @PostMapping("/teacherDataImport")
    public RequestResult teacherDataImport(@RequestParam(value = "file") MultipartFile file, @RequestParam("collegeId") Integer collegeId) {
        String phone = "((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}";
        String idCardNo = "[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]";
        String email = "(\\w+\\.)*\\w+\\@+[0-9a-zA-Z]+\\.(com|com.cn|edu|hk|cn|net)";
        String fileName = file.getOriginalFilename();
        List<Teacher> teachers = new ArrayList<>();
        //批量导入教师需要保证手机号是唯一的
        List<String> phones = new ArrayList<>();
        List<String> idCardNos = new ArrayList<>();
        List<String> accounts = new ArrayList<>();
        List<String> emails = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        //获取到已注册学生的所有手机号码
        List<String> telephones = adminService.getAllTeacherTelephone();
        //原表头顺序：姓名、编号、电话、性别、身份证号码、账号、密码、邮箱；使用数组来比较
        String[] original_title = new String[]{"姓名", "编号", "电话", "性别", "身份证号码", "账号", "密码", "邮箱"};
        String[] current_title = new String[original_title.length];
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
                    //获取总行数
                    int totalRows = POIUtil.getExcelRealRowHSSF(hssfSheet);
                    //默认第一行是标题,index=0
                    HSSFRow titleRow = hssfSheet.getRow(0);
                    //判断表头是否被修改或换位置
                    for (int cellIndex = 0; cellIndex < current_title.length; cellIndex++) {
                        current_title[cellIndex] = titleRow.getCell(cellIndex).getStringCellValue();
                    }
                    //判断两个标题数组的内容是否一致
                    boolean equals = Arrays.equals(original_title, current_title);
                    if (!equals) {
                        return RequestResult.failure("导入失败，请检查模板中的表头是否被修改");
                    }
                    //从第二行开始循环读取数据
                    for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                        //获取行对象
                        HSSFRow hssfRow = hssfSheet.getRow(rowIndex);
                        if (hssfRow == null) {
                            continue;
                        }
                        //读取列，从第一列开始
                        Teacher teacher = new Teacher();
                        HSSFCell cell;
                        CellType cellType;
                        String data;
                        for (int i = 0; i < original_title.length; i++) {
                            cell = hssfRow.getCell(i);
                            cellType = cell.getCellType();
                            if ("性别".equals(original_title[i])) {
                                if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的性别不是文本类型");
                                }
                                teacher.setGender(0);
                                if (cellType == CellType.STRING) {
                                    data = cell.getStringCellValue().trim();
                                    if ("男".equals(data)) {
                                        teacher.setGender(0);
                                    } else if ("女".equals(data)) {
                                        teacher.setGender(1);
                                    } else {
                                        return RequestResult.failure("导入失败，请检查第" + (rowIndex + 1) + "行老师的性别是否正确");
                                    }
                                }
                                continue;
                            }
                            if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不是文本类型");
                            }
                            data = "";
                            if (cellType == CellType.STRING) {
                                data = cell.getStringCellValue().trim();
                            }
                            if (data.contains(" ")) {//包含空格
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "中包含空格");
                            }
                            if (i == 0) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不能为空");
                                }
                                teacher.setName(data);
                            }
                            if (i == 1) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不能为空");
                                }
                                if (numbers.contains(data)) {//表中的学号有重复
                                    int j = numbers.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                                }
                                numbers.add(data);
                                teacher.setNumber(data);
                            }
                            if (i == 2) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不能为空");
                                }
                                if (!data.matches(phone)) {//判断号码是否符合格式
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "格式错误");
                                }
                                if (phones.contains(data)) {
                                    int j = phones.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                                }
                                phones.add(data);
                                if (telephones.contains(data)) {//判断号码是否已经被注册
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "已经被注册");
                                }
                                teacher.setTelephone(data);

                            }
                            if (i == 4) {
                                if (!"".equals(data)) {//身份证号码不为空时
                                    //判断身份证号码格式
                                    if (!data.matches(idCardNo)) {
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "格式错误");
                                    }
                                    if (idCardNos.contains(data)) {//表中的身份证号码有重复
                                        int j = idCardNos.indexOf(data);
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                                    }
                                    idCardNos.add(data);
                                }
                                teacher.setIdCardNo(data);
                            }
                            if (i == 5) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不能为空");
                                }
                                if (accounts.contains(data)) {//表中的账户有重复
                                    int j = accounts.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                                }
                                accounts.add(data);
                                teacher.setAccount(data);
                            }
                            if (i == 6) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不能为空");
                                }
                                teacher.setPassword(data);
                            }
                            if (i == 7) {
                                if (!"".equals(data)) {
                                    if (!data.matches(email)) {
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "格式错误");
                                    }
                                    if (emails.contains(data)) {//表中的邮箱有重复
                                        int j = emails.indexOf(data);
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                                    }
                                    emails.add(data);
                                }
                                teacher.setEmail(data);
                            }
                        }
                        teacher.setCollegeId(collegeId);
                        System.out.println(teacher);
                        teachers.add(teacher);
                    }
                    boolean i = adminService.insertTeacherBatch(teachers);
                    if (!i) {
                        return RequestResult.failure("批量导入失败，请稍后再试");
                    }
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
                    for (int cellIndex = 0; cellIndex < current_title.length; cellIndex++) {
                        current_title[cellIndex] = titleRow.getCell(cellIndex).getStringCellValue();
                    }
                    //判断两个标题数组的内容是否一致
                    boolean equals = Arrays.equals(original_title, current_title);
                    if (!equals) {
                        return RequestResult.failure("导入失败，请检查模板中的表头是否被修改");
                    }
                    //获取总行数
                    int totalRows = POIUtil.getExcelRealRowXSSF(xssfSheet);
                    //从第二行开始循环读取数据
                    for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                        //获取行对象
                        XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
                        if (xssfRow == null) {
                            continue;
                        }
                        //读取列，从第一列开始
                        Teacher teacher = new Teacher();
                        XSSFCell cell;
                        CellType cellType;
                        String data;
                        for (int i = 0; i < original_title.length; i++) {
                            cell = xssfRow.getCell(i);
                            cellType = cell.getCellType();
                            if ("性别".equals(original_title[i])) {
                                if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的性别不是文本类型");
                                }
                                teacher.setGender(0);
                                if (cellType == CellType.STRING) {
                                    data = cell.getStringCellValue().trim();
                                    if ("男".equals(data)) {
                                        teacher.setGender(0);
                                    } else if ("女".equals(data)) {
                                        teacher.setGender(1);
                                    } else {
                                        return RequestResult.failure("导入失败，请检查第" + (rowIndex + 1) + "行老师的性别是否正确");
                                    }
                                }
                                continue;
                            }
                            if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不是文本类型");
                            }
                            data = "";
                            if (cellType == CellType.STRING) {
                                data = cell.getStringCellValue().trim();
                            }
                            if (data.contains(" ")) {//包含空格
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "中包含空格");
                            }
                            if (i == 0) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不能为空");
                                }
                                teacher.setName(data);
                            }
                            if (i == 1) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不能为空");
                                }
                                if (numbers.contains(data)) {//表中的学号有重复
                                    int j = numbers.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                                }
                                numbers.add(data);
                                teacher.setNumber(data);
                            }
                            if (i == 2) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不能为空");
                                }
                                if (!data.matches(phone)) {//判断号码是否符合格式
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "格式错误");
                                }
                                if (phones.contains(data)) {
                                    int j = phones.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                                }
                                phones.add(data);
                                if (telephones.contains(data)) {//判断号码是否已经被注册
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "已经被注册");
                                }
                                teacher.setTelephone(data);

                            }
                            if (i == 4) {
                                if (!"".equals(data)) {//身份证号码不为空时
                                    //判断身份证号码格式
                                    if (!data.matches(idCardNo)) {
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "格式错误");
                                    }
                                    if (idCardNos.contains(data)) {//表中的身份证号码有重复
                                        int j = idCardNos.indexOf(data);
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                                    }
                                    idCardNos.add(data);
                                }
                                teacher.setIdCardNo(data);
                            }
                            if (i == 5) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不能为空");
                                }
                                if (accounts.contains(data)) {//表中的账户有重复
                                    int j = accounts.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                                }
                                accounts.add(data);
                                teacher.setAccount(data);
                            }
                            if (i == 6) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "不能为空");
                                }
                                teacher.setPassword(data);
                            }
                            if (i == 7) {
                                if (!"".equals(data)) {
                                    if (!data.matches(email)) {
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "格式错误");
                                    }
                                    if (emails.contains(data)) {//表中的邮箱有重复
                                        int j = emails.indexOf(data);
                                        return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行老师的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                                    }
                                    emails.add(data);
                                }
                                teacher.setEmail(data);
                            }
                        }
                        teacher.setCollegeId(collegeId);
                        System.out.println(teacher);
                        teachers.add(teacher);
                    }
                    boolean i = adminService.insertTeacherBatch(teachers);
                    if (!i) {
                        return RequestResult.failure("批量导入失败，请稍后再试");
                    }
                }
            } catch (IOException e) {
                return RequestResult.failure("批量导入失败，请稍后再试");
            }
        } else {
            return RequestResult.failure("请上传EXCEL文档");
        }
        return RequestResult.success();
    }

    //下载教师模板
    @RequestMapping("/downloadTeacherTemplate")
    public ResponseEntity<byte[]> downloadTeacherTemplate(HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/excels/");
        String fileName = "Teacher_Template.xlsx";
        File file = new File(path + fileName);
        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        // 将对文件做的特殊处理还原
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new NotExistException("你所访问的文件不存在");
        }
    }

    @RequestMapping("/searchTeacher")
    public String searchTeacher(Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, HttpSession session) {
        TeacherExample teacherExample = (TeacherExample) session.getAttribute("teacherExample");
        if (teacherExample == null) {
            teacherExample = new TeacherExample();
            teacherExample.setOrderByClause("number asc");
        }
        map.put("colleges", adminService.getAllColleges());
        //加入PageHelper分页插件，在查询之前只需要调用startPage方法
        //传入页码以及每页显示数据条数
        PageHelper.startPage(pageNum, 15);
        //后面的操作就是分页查询
        List<Teacher> teachers = adminService.getAllTeachersWithCollegeAndCourseByExample(teacherExample);
        //使用PageInfo包装查询结果，PageInfo包含了非常全面的分页属性，只需要将PageInfo交给页面就可以了
        //navigatePages：连续显示多少页
        PageInfo<Teacher> page = new PageInfo(teachers, 10);
        map.put("pageInfo", page);
        return "admin/searchTeacher";
    }


    @ResponseBody
    @PostMapping("/deleteTeacher")
    public RequestResult deleteTeacher(@RequestParam("id") Integer id) {
        if (id == null) {
            return RequestResult.failure("教师删除失败，请稍后再试");
        }
        //删除教师信息
        boolean i = adminService.deleteTeacherById(id);
        if (!i) {
            return RequestResult.failure("教师删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteTeacherBatch")
    public RequestResult deleteTeacherBatch(@RequestParam("ids") String id) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> teacherIds = new ArrayList<>();
        for (String s : ids) {
            teacherIds.add(Integer.parseInt(s));
        }
        boolean b = adminService.deleteTeacherByIdBatch(teacherIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/searchTeacherByTerm")
    public RequestResult searchTeacherByTerm(@RequestParam("name") String name,
                                             @RequestParam("collegeId") Integer collegeId,
                                             @RequestParam("course") String course,
                                             @RequestParam("number") String number,
                                             HttpSession session) {
        //将查询条件存放进session中，以回显查询条件
        Map<String, Object> map = new HashMap<>();
        map.put("name", null);
        map.put("collegeId", null);
        map.put("course", null);
        map.put("number", null);
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
        if (!"".equals(name.trim())) {
            map.put("name", name.trim());
            criteria.andNameLike("%" + name.trim() + "%");
        }
        if (!"".equals(number.trim())) {
            map.put("number", number.trim());
            criteria.andNumberLike("%" + number.trim() + "%");
        }
        if (collegeId != 0) {
            map.put("collegeId", collegeId);
            criteria.andCollegeIdEqualTo(collegeId);
        }
        if (!"".equals(course.trim())) {
            map.put("course", course.trim());
            CourseExample courseExample = new CourseExample();
            CourseExample.Criteria criteria1 = courseExample.createCriteria();
            criteria1.andNameLike("%" + course.trim() + "%");
            courseExample.setDistinct(true);
            List<Integer> teacherId = adminService.getTeacherIdByCourseExample(courseExample);
            criteria.andIdIn(teacherId);

        }
        teacherExample.setOrderByClause("number asc");
        session.setAttribute("teacherQueryCriteria", map);
        session.setAttribute("teacherExample", teacherExample);
        return RequestResult.success();
    }

    @RequestMapping("/updateTeacher/{id}")
    public String updateTeacher(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        //判断id是否存在
        Teacher teacher = adminService.getTeacherByPrimaryKeyWithCourse(id);
        if (teacher == null) {
            throw new NotExistException("该id对应的老师不存在");
        }
        map.put("colleges", adminService.getAllColleges());
        map.put("teacher", teacher);
        map.put("pageNum", pageNum);
        return "admin/updateTeacher";
    }

    @ResponseBody
    @PostMapping("/unbindCourse")
    public RequestResult unbindCourse(@RequestParam("courseId") Integer courseId) {
        Course course = new Course();
        course.setId(courseId);
        course.setTeacherId(0);
        boolean b = adminService.unbindCourse(course);
        if (!b) {
            return RequestResult.failure("解除绑定失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/editTeacher/{id}")
    public RequestResult editTeacher(@PathVariable("id") Integer id, @Validated({TeacherSequence.class}) Teacher teacher, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        //判断是否修改数据
        Teacher teacher1 = adminService.getTeacherByPrimaryKeyWithoutCourses(id);
        if (teacher1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        if (teacher1.equals(teacher)) {
            return RequestResult.failure("未修改任何基本数据");
        }
        //在保存老师信息之前先查看数据库中是否有重复的数据
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
        //1、查看编号是否存在
        if (!teacher1.getNumber().equals(teacher.getNumber())) {//表单的编号已经修改
            criteria.andNumberEqualTo(teacher.getNumber());
            boolean exists = adminService.isTeacherExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该教师编号已经存在");
            }
        } else {
            teacher.setNumber(null);
        }
        //2、查看联系电话是否存在
        if (!teacher1.getTelephone().equals(teacher.getTelephone())) {
            teacherExample.clear();
            criteria = teacherExample.createCriteria();
            criteria.andTelephoneEqualTo(teacher.getTelephone());
            boolean exists = adminService.isTeacherExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该电话已经存在");
            }
        } else {
            teacher.setTelephone(null);
        }

        //3、身份证号码是否存在
        if (!teacher1.getIdCardNo().equals(teacher.getIdCardNo())) {
            teacherExample.clear();
            criteria = teacherExample.createCriteria();
            criteria.andIdCardNoEqualTo(teacher.getIdCardNo());
            boolean exists = adminService.isTeacherExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该身份证号码已经存在");
            }
        } else {
            teacher.setIdCardNo(null);
        }
        //4、查看登录账号是否存在
        if (!teacher1.getAccount().equals(teacher.getAccount())) {
            teacherExample.clear();
            criteria = teacherExample.createCriteria();
            criteria.andAccountEqualTo(teacher.getAccount());
            boolean exists = adminService.isTeacherExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该账号已经存在");
            }
        } else {
            teacher.setAccount(null);
        }

        //5、查看邮箱是否存在
        if (!teacher1.getEmail().equals(teacher.getEmail())) {
            teacherExample.clear();
            criteria = teacherExample.createCriteria();
            criteria.andEmailEqualTo(teacher.getEmail());
            boolean exists = adminService.isTeacherExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该邮箱已经存在");
            }
        } else {
            teacher.setEmail(null);
        }
        if (teacher1.getName().equals(teacher.getName())) {
            teacher.setName(null);
        }
        if (teacher1.getGender().equals(teacher.getGender())) {
            teacher.setGender(null);
        }
        if (teacher1.getCollegeId().equals(teacher.getCollegeId())) {
            teacher.setCollegeId(null);
        }
        if (teacher1.getPassword().equals(teacher.getPassword())) {
            teacher.setPassword(null);
        }
        boolean b = adminService.updateTeacherByPrimaryKeySelective(teacher);
        if (!b) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        return RequestResult.success();
    }


    @RequestMapping("/createCollege")
    private String createCollege() {
        return "admin/createCollege";
    }

    @ResponseBody
    @PostMapping("/saveCollege")
    private RequestResult saveCollege(@Validated({CollegeSequence.class}) College college, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        if (college == null) {
            return RequestResult.failure("新增学院失败，请稍后再试");
        }
        //先判断该学院名称是否已经存在了
        CollegeExample collegeExample = new CollegeExample();
        CollegeExample.Criteria criteria = collegeExample.createCriteria();
        criteria.andNameEqualTo(college.getName());
        boolean exists = adminService.isCollegeExistsByExample(collegeExample);
        if (exists) {
            return RequestResult.failure("该学院已经存在了");
        }
        boolean b = adminService.insertCollegeSelective(college);
        if (!b) {
            return RequestResult.failure("学院添加失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/createCollegeBatch")
    public String createCollegeBatch() {
        return "admin/createCollegeBatch";
    }

    //下载学院模板
    @RequestMapping("/downloadCollegeTemplate")
    public ResponseEntity<byte[]> downloadCollegeTemplate(HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/excels/");
        String fileName = "College_Template.xlsx";
        File file = new File(path + fileName);

        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        // 将对文件做的特殊处理还原
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new NotExistException("你所访问的文件不存在");
        }
    }

    @ResponseBody
    @PostMapping("/collegeDataImport")
    public RequestResult collegeDataImport(@RequestParam(value = "file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        List<College> colleges = new ArrayList<>();
        //批量导入学院需要保证学院名称是唯一的
        List<String> names = new ArrayList<>();
        //获取到已注册学院的所有学院名称
        List<String> exist_names = adminService.getAllCollegeName();
        //原表头顺序：学院名称、学院介绍；使用数组来比较
        String[] original_title = new String[]{"学院名称", "学院介绍"};
        String[] current_title = new String[original_title.length];
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
                    //获取总行数
                    int totalRows = POIUtil.getExcelRealRowHSSF(hssfSheet);
                    //默认第一行是标题,index=0
                    HSSFRow titleRow = hssfSheet.getRow(0);
                    //判断表头是否被修改或换位置
                    for (int cellIndex = 0; cellIndex < current_title.length; cellIndex++) {
                        current_title[cellIndex] = titleRow.getCell(cellIndex).getStringCellValue();
                    }
                    //判断两个标题数组的内容是否一致
                    boolean equals = Arrays.equals(original_title, current_title);
                    if (!equals) {
                        return RequestResult.failure("导入失败，请检查模板中的表头是否被修改");
                    }
                    //从第二行开始循环读取数据
                    for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                        //获取行对象
                        HSSFRow hssfRow = hssfSheet.getRow(rowIndex);
                        if (hssfRow == null) {
                            continue;
                        }
                        //读取列，从第一列开始
                        College college = new College();
                        HSSFCell cell;
                        CellType cellType;
                        String data;
                        for (int i = 0; i < original_title.length; i++) {
                            cell = hssfRow.getCell(i);
                            cellType = cell.getCellType();
                            if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "不是文本类型");
                            }
                            data = "";
                            if (cellType == CellType.STRING) {
                                data = cell.getStringCellValue().trim();
                            }
                            if (i == 0) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "不能为空");
                                }
                                /*if (data.contains(" ")) {//包含空格
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "中包含空格");
                                }*/
                                if (names.contains(data)) {
                                    int j = names.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "和第" + (j + 2) + "行学院的重复了");
                                }
                                names.add(data);
                                if (exist_names.contains(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "已经被注册");
                                }
                                college.setName(data);
                            }
                            if (i == 1) {
                                /*if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "不能为空");
                                }*/
                                college.setIntro(data);
                            }
                        }
                        System.out.println(college);
                        colleges.add(college);
                    }
                    boolean i = adminService.insertCollegeBatch(colleges);
                    if (!i) {
                        return RequestResult.failure("批量导入失败，请稍后再试");
                    }
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
                    for (int cellIndex = 0; cellIndex < current_title.length; cellIndex++) {
                        current_title[cellIndex] = titleRow.getCell(cellIndex).getStringCellValue();
                    }
                    //判断两个标题数组的内容是否一致
                    boolean equals = Arrays.equals(original_title, current_title);
                    if (!equals) {
                        return RequestResult.failure("导入失败，请检查模板中的表头是否被修改");
                    }
                    //获取总行数
                    int totalRows = POIUtil.getExcelRealRowXSSF(xssfSheet);
                    //从第二行开始循环读取数据
                    for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                        //获取行对象
                        XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
                        if (xssfRow == null) {
                            continue;
                        }
                        //读取列，从第一列开始
                        College college = new College();
                        XSSFCell cell;
                        CellType cellType;
                        String data;
                        for (int i = 0; i < original_title.length; i++) {
                            cell = xssfRow.getCell(i);
                            cellType = cell.getCellType();
                            if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "不是文本类型");
                            }
                            data = "";
                            if (cellType == CellType.STRING) {
                                data = cell.getStringCellValue().trim();
                            }
                            if (i == 0) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "不能为空");
                                }
                                /*if (data.contains(" ")) {//包含空格
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "中包含空格");
                                }*/
                                if (names.contains(data)) {
                                    int j = names.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "和第" + (j + 2) + "行学院的重复了");
                                }
                                names.add(data);
                                if (exist_names.contains(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "已经被注册");
                                }
                                college.setName(data);
                            }
                            if (i == 1) {
                                /*if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行学院的" + original_title[i] + "不能为空");
                                }*/
                                college.setIntro(data);
                            }
                        }
                        System.out.println(college);
                        colleges.add(college);
                    }
                    boolean i = adminService.insertCollegeBatch(colleges);
                    if (!i) {
                        return RequestResult.failure("批量导入失败，请稍后再试");
                    }
                }
            } catch (IOException e) {
                return RequestResult.failure("批量导入失败，请稍后再试");
            }
        } else {
            return RequestResult.failure("请上传EXCEL文档");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchCollege")
    private String searchCollege(Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, HttpSession session) {
        CollegeExample collegeExample = (CollegeExample) session.getAttribute("collegeExample");
        if (collegeExample == null) {
            collegeExample = new CollegeExample();
            collegeExample.setOrderByClause("CONVERT(name using gbk) asc");
        }
        PageHelper.startPage(pageNum, 15);
        List<College> colleges = adminService.getAllCollegesWithBLOBsByExample(collegeExample);
        PageInfo<College> page = new PageInfo(colleges, 10);
        map.put("pageInfo", page);
        return "admin/searchCollege";
    }

    @ResponseBody
    @PostMapping("/deleteCollege")
    public RequestResult deleteCollege(@RequestParam("id") Integer id) {
        if (id == null) {
            return RequestResult.failure("删除学院信息失败，请稍后再试");
        }
        //删除学院信息
        boolean i = adminService.deleteCollegeById(id);
        if (!i) {
            return RequestResult.failure("删除学院信息失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteCollegeBatch")
    public RequestResult deleteCollegeBatch(@RequestParam("ids") String id) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> collegeIds = new ArrayList<>();
        for (String s : ids) {
            collegeIds.add(Integer.parseInt(s));
        }
        boolean b = adminService.deleteCollegeByIdBatch(collegeIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/searchCollegeByTerm")
    private RequestResult searchCollegeByTerm(@RequestParam("name") String name, HttpSession session) {
        //将查询条件存放进session中，以回显查询条件
        Map<String, Object> map = new HashMap<>();
        CollegeExample collegeExample = new CollegeExample();
        CollegeExample.Criteria criteria = collegeExample.createCriteria();
        if (!"".equals(name.trim())) {
            criteria.andNameLike("%" + name.trim() + "%");
            map.put("name", name.trim());
        }
        collegeExample.setOrderByClause("CONVERT(name using gbk) asc");
        session.setAttribute("collegeExample", collegeExample);
        session.setAttribute("collegeQueryCriteria", map);
        return RequestResult.success();
    }

    @RequestMapping("/updateCollege/{id}")
    public String updateCollege(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        //判断id是否存在
        College college = adminService.getCollegeByPrimaryKey(id);
        if (college == null) {
            throw new NotExistException("该id对应的学院不存在");
        }
        map.put("college", college);
        map.put("pageNum", pageNum);
        return "admin/updateCollege";
    }

    @ResponseBody
    @PostMapping("/editCollege/{id}")
    public RequestResult editCollege(@PathVariable("id") Integer id, @Validated({CollegeSequence.class}) College college, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        //判断是否修改数据
        College college1 = adminService.getCollegeByPrimaryKey(id);
        if (college1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        if (college1.equals(college)) {
            return RequestResult.failure("未修改任何基本数据");
        }
        //检查学院是否已经存在
        if (!college1.getName().equals(college.getName())) {
            CollegeExample collegeExample = new CollegeExample();
            CollegeExample.Criteria criteria = collegeExample.createCriteria();
            criteria.andNameEqualTo(college.getName());
            boolean exists = adminService.isCollegeExistsByExample(collegeExample);
            if (exists) {
                return RequestResult.failure("该学院名称已经存在");
            }
        } else {
            college.setName(null);
        }
        if (college1.getIntro().equals(college.getIntro())) {
            college.setIntro(null);
        }
        boolean b = adminService.updateCollegeByPrimaryKeySelective(college);
        if (!b) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/createCourse")
    private String createCourse(Map<String, Object> map) {
        map.put("colleges", adminService.getAllColleges());
        map.put("teachers", adminService.getAllTeachersWithIdNameNumberAndCollege(true));
        return "admin/createCourse";
    }

    @ResponseBody
    @PostMapping("/saveCourse")
    private RequestResult saveCourse(@Validated({CourseSequence.class}) Course course, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        //先判断该课程编号是否已经存在了
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andNumberEqualTo(course.getNumber());
        boolean exists = adminService.isCourseExistsByExample(courseExample);
        if (exists) {
            return RequestResult.failure("该课程编号已经存在了");
        }
        boolean b = adminService.insertCourseSelective(course);
        if (!b) {
            return RequestResult.failure("课程添加失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/createCourseBatch")
    public String createCourseBatch(HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/excels/");
        String fileName = "Course_Template.xlsx";
        File file = new File(path + fileName);
        //查询教师信息
        List<Teacher> teachers = adminService.getAllTeachersWithIdNameNumberAndCollege(false);
        List<String> teacherIds = new ArrayList<>();
        for (Teacher teacher : teachers) {
            teacherIds.add(String.valueOf(teacher.getId()));
        }
        request.getSession().setAttribute("teacherIds", teacherIds);
        //查询学院信息
        List<College> colleges = adminService.getAllColleges();
        List<String> collegeIds = new ArrayList<>();
        for (College college : colleges) {
            collegeIds.add(String.valueOf(college.getId()));
        }
        request.getSession().setAttribute("collegeIds", collegeIds);
        OutputStream os = null;
        try (InputStream is = new FileInputStream(file)) {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
            XSSFCellStyle cellColorStyle = xssfWorkbook.createCellStyle();
            //设置居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
            cellColorStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
            //设置字体样式
            XSSFFont font = xssfWorkbook.createFont();
            font.setFontName("宋体");
            cellStyle.setFont(font);
            cellColorStyle.setFont(font);
            cellColorStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            cellColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
            cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
            cellStyle.setBorderTop(BorderStyle.THIN);//上边框
            cellStyle.setBorderRight(BorderStyle.THIN);//右边框
            cellColorStyle.setBorderBottom(BorderStyle.THIN); //下边框
            cellColorStyle.setBorderLeft(BorderStyle.THIN);//左边框
            cellColorStyle.setBorderTop(BorderStyle.THIN);//上边框
            cellColorStyle.setBorderRight(BorderStyle.THIN);//右边框
            //获取"教师信息"表
            XSSFSheet teacherSheet = xssfWorkbook.getSheet("教师信息");
            //获取"学院信息"表
            XSSFSheet collegeSheet = xssfWorkbook.getSheet("学院信息");
            XSSFRow xssfRow;
            XSSFCell xssfCell;
            int i = 0;
            for (; i < teachers.size(); i++) {
                //从第二行开始写入
                xssfRow = teacherSheet.createRow(i + 1);
                //创建每个单元格Cell，即列的数据
                Teacher teacher = teachers.get(i);
                xssfCell = xssfRow.createCell(0, CellType.STRING);
                xssfCell.setCellStyle(cellColorStyle);
                xssfCell.setCellValue(String.valueOf(teacher.getId()));
                xssfCell = xssfRow.createCell(1, CellType.STRING);
                xssfCell.setCellStyle(cellStyle);
                xssfCell.setCellValue(teacher.getName());
                xssfCell = xssfRow.createCell(2, CellType.STRING);
                xssfCell.setCellStyle(cellStyle);
                if (teacher.getCollege() != null) {
                    xssfCell.setCellValue(teacher.getCollege().getName());
                } else {
                    xssfCell.setCellValue("未录入");
                }
                xssfCell = xssfRow.createCell(3, CellType.STRING);
                xssfCell.setCellStyle(cellStyle);
                xssfCell.setCellValue(teacher.getNumber());
            }
            while (true) {
                i++;
                xssfRow = teacherSheet.getRow(i);
                if (xssfRow != null) {
                    teacherSheet.removeRow(xssfRow);
                    continue;
                }
                break;
            }
            int j = 0;
            for (; j < colleges.size(); j++) {
                //从第二行开始写入
                xssfRow = collegeSheet.createRow(j + 1);
                //创建每个单元格Cell，即列的数据
                College college = colleges.get(j);
                xssfCell = xssfRow.createCell(0, CellType.STRING);
                xssfCell.setCellStyle(cellColorStyle);
                xssfCell.setCellValue(String.valueOf(college.getId()));
                xssfCell = xssfRow.createCell(1, CellType.STRING);
                xssfCell.setCellStyle(cellStyle);
                xssfCell.setCellValue(college.getName());
            }
            while (true) {
                j++;
                xssfRow = collegeSheet.getRow(j);
                if (xssfRow != null) {
                    collegeSheet.removeRow(xssfRow);
                    continue;
                }
                break;
            }
            os = new FileOutputStream(file);
            xssfWorkbook.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            //throw new NotExistException("跳转异常，请稍后再试");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "admin/createCourseBatch";
    }

    //下载课程模板
    @RequestMapping("/downloadCourseTemplate")
    public ResponseEntity<byte[]> downloadCourseTemplate(HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/excels/");
        String fileName = "Course_Template.xlsx";
        File file = new File(path + fileName);
        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        // 将对文件做的特殊处理还原
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new NotExistException("你所访问的文件不存在");
        }
    }

    @ResponseBody
    @PostMapping("/courseDataImport")
    public RequestResult courseDataImport(@RequestParam(value = "file") MultipartFile file, HttpSession session) {
        String fileName = file.getOriginalFilename();
        String number = "[a-zA-Z0-9]{6,18}";
        List<Course> courses = new ArrayList<>();
        //批量导入课程需要保证课程编号是唯一的
        List<String> numbers = new ArrayList<>();
        //获取到已注册课程的所有课程编号
        List<String> exist_numbers = adminService.getAllCourseNumber();
        List<String> teacherIds = (List<String>) session.getAttribute("teacherIds");
        List<String> collegeIds = (List<String>) session.getAttribute("collegeIds");
        //原表头顺序：课程名称、课程编号、所属学院、授课教师、课程介绍；使用数组来比较
        String[] original_title = new String[]{"课程名称", "课程编号", "所属学院", "授课教师", "课程介绍"};
        String[] current_title = new String[original_title.length];
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
                    //获取总行数
                    int totalRows = POIUtil.getExcelRealRowHSSF(hssfSheet);
                    //默认第一行是标题,index=0
                    HSSFRow titleRow = hssfSheet.getRow(0);
                    //判断表头是否被修改或换位置
                    for (int cellIndex = 0; cellIndex < current_title.length; cellIndex++) {
                        current_title[cellIndex] = titleRow.getCell(cellIndex).getStringCellValue();
                    }
                    //判断两个标题数组的内容是否一致
                    boolean equals = Arrays.equals(original_title, current_title);
                    if (!equals) {
                        return RequestResult.failure("导入失败，请检查模板中的表头是否被修改");
                    }
                    //从第二行开始循环读取数据
                    for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                        //获取行对象
                        HSSFRow hssfRow = hssfSheet.getRow(rowIndex);
                        if (hssfRow == null) {
                            continue;
                        }
                        //读取列，从第一列开始
                        Course course = new Course();
                        HSSFCell cell;
                        CellType cellType;
                        String data;
                        for (int i = 0; i < original_title.length; i++) {
                            cell = hssfRow.getCell(i);
                            cellType = cell.getCellType();
                            if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "不是文本类型");
                            }
                            data = "";
                            if (cellType == CellType.STRING) {
                                data = cell.getStringCellValue().trim();
                            }
                            if (data.contains(" ")) {//包含空格
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "中包含空格");
                            }
                            if (i == 0) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "不能为空");
                                }
                                course.setName(data);
                            }
                            if (i == 1) {
                                if ("".equals(data)) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "不能为空");
                                }
                                if (!data.matches(number)) {//判断课程编号是否符合格式
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "格式错误");
                                }
                                if (numbers.contains(data)) {//表中的编号有重复
                                    int j = numbers.indexOf(data);
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                                }
                                numbers.add(data);
                                if (exist_numbers.contains(data)) {//判断编号是否已经被注册
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "已经被注册");
                                }
                                course.setNumber(data);
                            }
                            if (i == 2) {
                                if ("".equals(data)) {
                                    course.setCollegeId(0);
                                } else {
                                    if (!collegeIds.contains(data)) {
                                        return RequestResult.failure("导入失败，请检查第" + (rowIndex + 1) + "行课程的" + original_title[i] + "是否在范围之中");
                                    }
                                    course.setCollegeId(Integer.parseInt(data));
                                }

                            }
                            if (i == 3) {
                                if ("".equals(data)) {
                                    course.setTeacherId(0);
                                } else {
                                    if (!teacherIds.contains(data)) {
                                        return RequestResult.failure("导入失败，请检查第" + (rowIndex + 1) + "行课程的" + original_title[i] + "是否在范围之中");
                                    }
                                    course.setTeacherId(Integer.parseInt(data));
                                }
                            }
                            if (i == 4) {
                                course.setIntro(data);
                            }
                        }
                        System.out.println(course);
                        courses.add(course);
                    }
                    boolean i = adminService.insertCourseBatch(courses);
                    if (!i) {
                        return RequestResult.failure("批量导入失败，请稍后再试");
                    }
                }
            } catch (IOException e) {
                return RequestResult.failure("批量导入失败，请稍后再试");
            }
        }
        //是".xlsx"格式
        /*else */
        if (fileName.endsWith(".xlsx")) {
            //定义工作簿，一个工作簿可以有多个并做表Sheet
            XSSFWorkbook xssfWorkbook = null;
            try (InputStream inputStream = file.getInputStream()) {
                xssfWorkbook = new XSSFWorkbook(inputStream);
                //获取到含有课程信息的工作表
                XSSFSheet xssfSheet = xssfWorkbook.getSheet("课程信息");
                if (xssfSheet == null) {
                    return RequestResult.failure("导入失败，请查看课程信息表表名是否被修改");
                }
                //获取行
                //默认第一行是标题,index=0
                XSSFRow titleRow = xssfSheet.getRow(0);
                //判断表头是否被修改或换位置
                for (int cellIndex = 0; cellIndex < current_title.length; cellIndex++) {
                    current_title[cellIndex] = titleRow.getCell(cellIndex).getStringCellValue();
                }
                //判断两个标题数组的内容是否一致
                boolean equals = Arrays.equals(original_title, current_title);
                if (!equals) {
                    return RequestResult.failure("导入失败，请检查模板中的表头是否被修改");
                }
                //获取总行数
                int totalRows = POIUtil.getExcelRealRowXSSF(xssfSheet);
                //从第二行开始循环读取数据
                for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                    //获取行对象
                    XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
                    if (xssfRow == null) {
                        continue;
                    }
                    //读取列，从第一列开始
                    Course course = new Course();
                    XSSFCell cell;
                    CellType cellType;
                    String data;
                    for (int i = 0; i < original_title.length; i++) {
                        cell = xssfRow.getCell(i);
                        cellType = cell.getCellType();
                        if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                            return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "不是文本类型");
                        }
                        data = "";
                        if (cellType == CellType.STRING) {
                            data = cell.getStringCellValue().trim();
                        }
                        if (data.contains(" ")) {//包含空格
                            return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "中包含空格");
                        }
                        if (i == 0) {
                            if ("".equals(data)) {
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "不能为空");
                            }
                            course.setName(data);
                        }
                        if (i == 1) {
                            if ("".equals(data)) {
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "不能为空");
                            }
                            if (!data.matches(number)) {//判断课程编号是否符合格式
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "格式错误");
                            }
                            if (numbers.contains(data)) {//表中的编号有重复
                                int j = numbers.indexOf(data);
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "和第" + (j + 2) + "行老师的重复了");
                            }
                            numbers.add(data);
                            if (exist_numbers.contains(data)) {//判断编号是否已经被注册
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行课程的" + original_title[i] + "已经被注册");
                            }
                            course.setNumber(data);
                        }
                        if (i == 2) {
                            if ("".equals(data)) {
                                course.setCollegeId(0);
                            } else {
                                if (!collegeIds.contains(data)) {
                                    return RequestResult.failure("导入失败，请检查第" + (rowIndex + 1) + "行课程的" + original_title[i] + "是否在范围之中");
                                }
                                course.setCollegeId(Integer.parseInt(data));
                            }

                        }
                        if (i == 3) {
                            if ("".equals(data)) {
                                course.setTeacherId(0);
                            } else {
                                if (!teacherIds.contains(data)) {
                                    return RequestResult.failure("导入失败，请检查第" + (rowIndex + 1) + "行课程的" + original_title[i] + "是否在范围之中");
                                }
                                course.setTeacherId(Integer.parseInt(data));
                            }
                        }
                        if (i == 4) {
                            course.setIntro(data);
                        }
                    }
                    System.out.println(course);
                    courses.add(course);
                }
                boolean i = adminService.insertCourseBatch(courses);
                if (!i) {
                    return RequestResult.failure("批量导入失败，请稍后再试");
                }
            } catch (IOException e) {
                return RequestResult.failure("批量导入失败，请稍后再试");
            }
        } else {
            return RequestResult.failure("请上传EXCEL文档");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchCourse")
    private String searchCourses(Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, HttpSession session) {
        CourseExample courseExample = (CourseExample) session.getAttribute("courseExample");
        if (courseExample == null) {
            courseExample = new CourseExample();
            courseExample.setOrderByClause("teacher_id asc,college_id asc");
        }
        PageHelper.startPage(pageNum, 15);
        List<Course> courses = adminService.getAllCoursesWithBLOBsCollegeAndTeacherByExample(courseExample);
        PageInfo<Course> page = new PageInfo(courses, 10);
        map.put("pageInfo", page);
        map.put("colleges", adminService.getAllColleges());
//        map.put("courses", courses);
        return "admin/searchCourse";
    }

    @ResponseBody
    @PostMapping("/deleteCourse")
    public RequestResult deleteCourse(@RequestParam("id") Integer id, HttpServletRequest request) {
        if (id == null) {
            return RequestResult.failure("删除课程信息失败，请稍后再试");
        }
        //删除课程信息
        boolean i = adminService.deleteCourseById(id, request);
        if (!i) {
            return RequestResult.failure("删除课程信息失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteCourseBatch")
    public RequestResult deleteCourseBatch(@RequestParam("ids") String id, HttpServletRequest request) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> courseIds = new ArrayList<>();
        for (String s : ids) {
            courseIds.add(Integer.parseInt(s));
        }
        boolean b = adminService.deleteCourseByIdBatch(courseIds, request);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/searchCourseByTerm")
    private RequestResult searchCourseByTerm(@RequestParam("name") String name,
                                             @RequestParam("collegeId") Integer collegeId,
                                             @RequestParam("teacher") String teacher,
                                             HttpSession session,
                                             @RequestParam("number") String number) {
        //将查询条件存放进session中，以回显查询条件
        Map<String, Object> map = new HashMap<>();
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        if (!"".equals(name.trim())) {
            criteria.andNameLike("%" + name.trim() + "%");
            map.put("name", name.trim());
        }
        if (!"".equals(teacher.trim())) {
            TeacherExample teacherExample = new TeacherExample();
            TeacherExample.Criteria criteria1 = teacherExample.createCriteria();
            criteria1.andNameLike("%" + teacher.trim() + "%");
            List<Integer> ids = adminService.getTeacherIdByTeacherExample(teacherExample);
            if (ids.size() == 0) {
                ids.add(0);
            }
            criteria.andTeacherIdIn(ids);
            map.put("teacher", teacher.trim());
        }
        if (collegeId != 0) {
            criteria.andCollegeIdEqualTo(collegeId);
            map.put("collegeId", collegeId);
        }
        if (!"".equals(number.trim())) {
            criteria.andNumberLike("%" + number.trim() + "%");
            map.put("number", number.trim());
        }
        courseExample.setOrderByClause("teacher_id asc,college_id asc");
        session.setAttribute("courseExample", courseExample);
        session.setAttribute("courseQueryCriteria", map);
        return RequestResult.success();
    }

    @RequestMapping("/updateCourse/{id}")
    public String updateCourse(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        //判断id是否存在
        Course course = adminService.getCourseByPrimaryKey(id);
        if (course == null) {
            throw new NotExistException("该id对应的课程不存在");
        }
        map.put("course", course);
        map.put("pageNum", pageNum);
        map.put("colleges", adminService.getAllColleges());
        map.put("teachers", adminService.getAllTeachersWithIdNameNumberAndCollege(true));
        return "admin/updateCourse";
    }

    @ResponseBody
    @PostMapping("/editCourse/{id}")
    public RequestResult editCourse(@PathVariable("id") Integer id, @Validated({CourseSequence.class}) Course course, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        //判断是否修改数据
        Course course1 = adminService.getCourseByPrimaryKeyWithoutTeacherAndCollege(id);
        if (course1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        if (course1.equals(course)) {
            return RequestResult.failure("未修改任何基本数据");
        }
        //检查课程编号是否已经存在
        if (!course1.getNumber().equals(course.getNumber())) {
            CourseExample courseExample = new CourseExample();
            CourseExample.Criteria criteria = courseExample.createCriteria();
            criteria.andNameEqualTo(course1.getNumber());
            boolean exists = adminService.isCourseExistsByExample(courseExample);
            if (exists) {
                return RequestResult.failure("该课程编号已经存在");
            }
        } else {
            course.setNumber(null);
        }
        if (course1.getIntro().equals(course.getIntro())) {
            course.setIntro(null);
        }
        if (course1.getName().equals(course.getName())) {
            course.setName(null);
        }
        if (course1.getCollegeId().equals(course.getCollegeId())) {
            course.setCollegeId(null);
        }
        if (course1.getTeacherId().equals(course.getTeacherId())) {
            course.setTeacherId(null);
        }
        boolean b = adminService.updateCourseByPrimaryKeySelective(course);
        if (!b) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchDiscuss")
    public String searchDiscussInfo(Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, HttpSession session) {
        DiscussExample discussExample = (DiscussExample) session.getAttribute("discussExample");
        if (discussExample == null) {
            discussExample = new DiscussExample();
            discussExample.setOrderByClause("course_id asc,record_time asc");
        }
        PageHelper.startPage(pageNum, 15);
        List<Discuss> discusses = adminService.getAllDiscussWithBLOBsAndTeacherAndCourseByExample(discussExample);
        PageInfo<Discuss> page = new PageInfo(discusses, 10);
        map.put("pageInfo", page);
        return "admin/searchDiscuss";
    }

    @ResponseBody
    @PostMapping("/searchDiscussByTerm")
    public RequestResult searchDiscussInfoByTerm(@RequestParam("course") String course, @RequestParam("teacher") String teacher, @RequestParam("title") String title, HttpSession session) {
        //将查询条件存放进session中，以回显查询条件
        Map<String, Object> map = new HashMap<>();
        map.put("course", null);
        map.put("teacher", null);
        map.put("title", null);
        DiscussExample discussExample = new DiscussExample();
        DiscussExample.Criteria discussCriteria = discussExample.createCriteria();
        if (!"".equals(course.trim())) {
            CourseExample courseExample = new CourseExample();
            CourseExample.Criteria courseCriteria = courseExample.createCriteria();
            courseCriteria.andNameLike("%" + course.trim() + "%");
            List<Integer> id = adminService.getCourseIdByCourseExample(courseExample);
            discussCriteria.andCourseIdIn(id);
            map.put("course", course.trim());
        }
        if (!"".equals(teacher.trim())) {
            TeacherExample teacherExample = new TeacherExample();
            TeacherExample.Criteria teacherCriteria = teacherExample.createCriteria();
            teacherCriteria.andNameLike("%" + teacher.trim() + "%");
            List<Integer> id = adminService.getTeacherIdByTeacherExample(teacherExample);
            CourseExample courseExample = new CourseExample();
            CourseExample.Criteria courseCriteria = courseExample.createCriteria();
            courseCriteria.andTeacherIdIn(id);
            List<Integer> ids = adminService.getCourseIdByCourseExample(courseExample);
            discussCriteria.andCourseIdIn(ids);
            map.put("teacher", teacher.trim());
        }
        if (!"".equals(title.trim())) {
            discussCriteria.andTitleLike("%" + title.trim() + "%");
            map.put("title", title.trim());
        }
        discussExample.setOrderByClause("course_id asc,record_time asc");
        session.setAttribute("discussExample", discussExample);
        session.setAttribute("discussQueryCriteria", map);
        return RequestResult.success();
    }

    @RequestMapping("/updateDiscuss/{id}")
    public String updateDiscuss(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        //判断id是否存在
        Discuss discuss = adminService.getDiscussByPrimaryKey(id);
        if (discuss == null) {
            throw new NotExistException("该id对应的讨论不存在");
        }
        map.put("courses", adminService.getAllCoursesWithWithCollegeAndTeacher());
        map.put("discuss", discuss);
        map.put("pageNum", pageNum);
        return "admin/updateDiscuss";
    }

    @ResponseBody
    @PostMapping("/editDiscuss/{id}")
    public RequestResult editDiscuss(@PathVariable("id") Integer id, @Validated({DiscussSequence.class}) Discuss discuss, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        //判断是否修改数据
        Discuss discuss1 = adminService.getDiscussByPrimaryKey(id);
        if (discuss1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        if (discuss1.equals(discuss)) {
            return RequestResult.failure("未修改任何基本数据");
        }
        if (discuss1.getTitle().equals(discuss.getTitle())) {
            discuss.setTitle(null);
        }
        if (discuss1.getContent().equals(discuss.getContent())) {
            discuss.setContent(null);
        }
        if (discuss1.getCourseId().equals(discuss.getCourseId())) {
            discuss.setCourseId(null);
        }
        boolean b = adminService.updateDiscussByPrimaryKeySelective(discuss);
        if (!b) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteDiscussBatch")
    public RequestResult deleteDiscussBatch(@RequestParam("ids") String id) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> discussIds = new ArrayList<>();
        for (String s : ids) {
            discussIds.add(Integer.parseInt(s));
        }
        //删除讨论的同时删除对应的讨论内容
        boolean b = adminService.deleteDiscussByIdBatch(discussIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        adminService.deleteDiscussPostByDiscussIds(discussIds);
        return RequestResult.success();
    }

    @RequestMapping("/createNotice")
    public String createNotice() {
        return "admin/createNotice";
    }

    @ResponseBody
    @PostMapping("/saveNotice")
    public RequestResult saveNotice(@Validated({NoticeSequence.class}) Notice notice, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        if (notice == null) {
            return RequestResult.failure("添加公告失败，请稍后再试");
        }
        notice.setRecordTime(new Date());
        boolean b = adminService.insertNoticeSelective(notice);
        if (!b) {
            return RequestResult.failure("添加公告失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchNotice")
    public String searchNotice(Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, HttpSession session) {
        NoticeExample noticeExample = (NoticeExample) session.getAttribute("noticeExample");
        if (noticeExample == null) {
            noticeExample = new NoticeExample();
            noticeExample.setOrderByClause("record_time desc");
        }
        PageHelper.startPage(pageNum, 15);
        List<Notice> notices = adminService.getAllNoticesByExample(noticeExample);
        PageInfo<Notice> page = new PageInfo(notices, 10);
        map.put("pageInfo", page);
        return "admin/searchNotice";
    }

    @ResponseBody
    @PostMapping("/deleteNotice")
    public RequestResult deleteNotice(@RequestParam("id") Integer id) {
        if (id == null) {
            return RequestResult.failure("删除公告信息失败，请稍后再试");
        }
        //删除公告信息
        boolean i = adminService.deleteNoticeById(id);
        if (!i) {
            return RequestResult.failure("删除课程信息失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteNoticeBatch")
    public RequestResult deleteNoticeBatch(@RequestParam("ids") String id) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> noticeIds = new ArrayList<>();
        for (String s : ids) {
            noticeIds.add(Integer.parseInt(s));
        }
        boolean b = adminService.deleteNoticeByIdBatch(noticeIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/searchNoticeByTerm")
    public RequestResult searchNoticeByTerm(@RequestParam("title") String title, @RequestParam("minTime") String minTime, @RequestParam("maxTime") String maxTime, HttpSession session) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //将查询条件存放进session中，以回显查询条件
            Map<String, Object> map = new HashMap<>();
            map.put("title", null);
            map.put("minTime", null);
            map.put("maxTime", null);
            NoticeExample noticeExample = new NoticeExample();
            NoticeExample.Criteria criteria = noticeExample.createCriteria();
            if (!"".equals(title.trim())) {
                criteria.andTitleLike("%" + title.trim() + "%");
                map.put("title", title.trim());
            }
            if (!"".equals(minTime.trim())) {
                Date min = simpleDateFormat.parse(minTime);
                criteria.andRecordTimeGreaterThanOrEqualTo(min);
                map.put("minTime", minTime);
            }
            if (!"".equals(maxTime.trim())) {
                Date max = simpleDateFormat.parse(maxTime);
                criteria.andRecordTimeLessThanOrEqualTo(max);
                map.put("maxTime", maxTime);
            }
            noticeExample.setOrderByClause("record_time desc");
            session.setAttribute("noticeExample", noticeExample);
            session.setAttribute("noticeQueryCriteria", map);
        } catch (Exception e) {
            return RequestResult.failure("查询失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/updateNotice/{id}")
    public String updateNotice(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        //判断id是否存在
        Notice notice = adminService.getNoticeByPrimaryKey(id);
        if (notice == null) {
            throw new NotExistException("该id对应的公告不存在");
        }
        map.put("notice", notice);
        map.put("pageNum", pageNum);
        return "admin/updateNotice";
    }

    @ResponseBody
    @PostMapping("/editNotice/{id}")
    public RequestResult editNotice(@PathVariable("id") Integer id, @Validated({NoticeSequence.class}) Notice notice, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        //判断是否修改数据
        Notice notice1 = adminService.getNoticeByPrimaryKey(id);
        if (notice1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        if (notice1.equals(notice)) {
            return RequestResult.failure("未修改任何基本数据");
        }
        if (notice1.getTitle().equals(notice.getTitle())) {
            notice.setTitle(null);
        }
        if (notice1.getContent().equals(notice.getContent())) {
            notice.setContent(null);
        }
        boolean b = adminService.updateNoticeByPrimaryKeySelective(notice);
        if (!b) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/viewAdminInfo")
    public String viewAdminInfo(HttpSession session, Map<String, Object> map) {
        Admin admin = (Admin) session.getAttribute("user");
        map.put("admin", admin);
        return "admin/viewAdminInfo";
    }

    @RequestMapping("/editAdminInfo")
    public String updateAdminInfo(HttpSession session, Map<String, Object> map) {
        Admin admin = (Admin) session.getAttribute("user");
        map.put("admin", admin);
        return "admin/updateAdminInfo";
    }

    @ResponseBody
    @PostMapping("/updateAdminInfo")
    public RequestResult editAdminInfo(@Validated({AdminSequence.class}) Admin adm, BindingResult result, @RequestParam("code") String code, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("user");
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        if (admin == null || adm == null) {
            return RequestResult.failure("信息更新失败，请稍后再试");
        }
        JSONObject json = (JSONObject) session.getAttribute("UpdateInfoCode");
        if (json == null) {
            return RequestResult.failure("修改失败，还未获取验证码");
        }
        if (!json.getString("verifyCode").equals(code)) {
            return RequestResult.failure("验证码错误，请稍后重试");
        }
        if ((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5) {
            //删除session中的验证码
            session.removeAttribute("UpdateInfoCode");
            return RequestResult.failure("验证码已过期，请重新获取");
        }
        adm.setId(admin.getId());
        if (admin.equals(adm)) {
            return RequestResult.failure("未修改任何基本数据");
        }
        //判断手机号、邮箱、登录账户是否存在
        AdminExample adminExample = new AdminExample();
        Criteria criteria = adminExample.createCriteria();
        if (!adm.getTelephone().equals(admin.getTelephone())) {//修改了手机号
            criteria.andTelephoneEqualTo(adm.getTelephone());
            boolean exists = adminService.isExistsByExample(adminExample);
            if (exists) {
                return RequestResult.failure("该手机号已经存在");
            }
            admin.setTelephone(adm.getTelephone());
        } else {
            adm.setTelephone(null);
        }
        if (!adm.getEmail().equals(admin.getEmail())) {//修改了邮箱
            adminExample.clear();
            criteria = adminExample.createCriteria();
            criteria.andEmailEqualTo(adm.getEmail());
            boolean exists = adminService.isExistsByExample(adminExample);
            if (exists) {
                return RequestResult.failure("该邮箱已经存在");
            }
            admin.setEmail(adm.getEmail());
        } else {
            adm.setEmail(null);
        }
        if (!adm.getAccount().equals(admin.getAccount())) {//修改了登录账户
            adminExample.clear();
            criteria = adminExample.createCriteria();
            criteria.andAccountEqualTo(adm.getAccount());
            boolean exists = adminService.isExistsByExample(adminExample);
            if (exists) {
                return RequestResult.failure("该账号已经存在");
            }
            admin.setAccount(adm.getAccount());
        } else {
            adm.setAccount(null);
        }
        if (adm.getPassword().equals(admin.getPassword())) {
            adm.setPassword(null);
        } else {
            admin.setPassword(adm.getPassword());
        }
        if (adm.getName().equals(admin.getName())) {
            adm.setName(null);
        } else {
            admin.setName(adm.getName());
        }
        boolean b = adminService.updateAdminByPrimaryKeySelective(adm);
        if (!b) {
            return RequestResult.failure("信息更新失败，请稍后再试");
        }
        session.setAttribute("user", admin);
        session.removeAttribute("UpdateInfoCode");
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/logout")
    public RequestResult logout(HttpSession session) {
        //执行该方法后会让session失效，其中的内容就会删除，但是会立即重新创建一个新的session对象
        session.invalidate();
        return RequestResult.success();
    }
}