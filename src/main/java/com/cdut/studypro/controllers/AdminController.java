package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.beans.AdminExample.*;
import com.cdut.studypro.exceptions.NotExistException;
import com.cdut.studypro.services.AdminService;
import com.cdut.studypro.utils.MD5Util;
import com.cdut.studypro.utils.POIUtils;
import com.cdut.studypro.utils.RequestResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
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

    @RequestMapping("/createStudent")
    public String createStudent(Map<String, Object> map) {
        List<College> colleges = adminService.getAllColleges();
        map.put("colleges", colleges);
        return "admin/createStudent";
    }

    @ResponseBody
    @PostMapping(value = "/saveStudent")
    public RequestResult saveStudent(Student student) {
        if (student == null) {
            return RequestResult.failure("新增学生失败，请稍后再试");
        }
        if (student.getPassword() == null || student.getAccount() == null ||
                student.getNumber() == null || student.getEmail() == null ||
                student.getIdCardNo() == null) {
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
        //3、省份证号码是否存在
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
    public ResponseEntity<byte[]> downloadStudentTemplate(HttpServletRequest request) throws IOException {
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


    @ExceptionHandler(NotExistException.class)
    public String notExistException(NotExistException e, Map<String, Object> map) {
        map.put("exception", e);
        return "error";
    }


    //批量上传学生信息

    /**
     * @description: 1、POI 提供了对2003版本的Excel的支持 ---- HSSFWorkbook(.xls格式)
     * 2、POI 提供了对2007版本以及更高版本的支持 ---- XSSFWorkbook(.xlsx格式)
     */
    @ResponseBody
    @PostMapping("/studentDataImport")
    public RequestResult studentDataImport(@RequestParam(value = "file") MultipartFile file, @RequestParam("collegeId") Integer collegeId) {
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
                        if (collegeId != 0) {//选择了学院
                            student.setCollegeId(collegeId);
                        }
                        System.out.println(student);
                        students.add(student);
                    }
                    boolean i = adminService.insertStudentBatch(students);
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
                        if (collegeId != 0) {//选择了学院
                            student.setCollegeId(collegeId);
                        }
                        System.out.println(student);
                        students.add(student);
                    }
                    boolean i = adminService.insertStudentBatch(students);
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

    @RequestMapping("/searchStudent")
    public String searchStudent(Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, HttpSession session) {

        StudentExample studentExample = (StudentExample) session.getAttribute("studentExample");
        map.put("colleges", adminService.getAllColleges());
        //加入PageHelper分页插件，在查询之前只需要调用startPage方法
        //传入页码以及每页显示数据条数
        PageHelper.startPage(pageNum, 10);
        //后面的操作就是分页查询
        List<Student> students = adminService.getAllStudentsWithCollegeByExample(studentExample);
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
        session.setAttribute("studentExample", studentExample);
        session.setAttribute("studentQueryCriteria", map);
        return RequestResult.success();
    }

    @RequestMapping("/updateStudent/{id}")
    public String updateStudent(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        System.out.println(pageNum);
        //判断id是否存在
        Student student = adminService.getStudentByPrimaryKey(id);
        if (student == null) {
            map.put("exception", new NotExistException("该id对应的学生不存在"));
            return "error";
        }
        map.put("colleges", adminService.getAllColleges());
        map.put("student", student);
        map.put("pageNum", pageNum);
        return "admin/updateStudent";
    }

    @ResponseBody
    @PostMapping("/editStudent/{id}")
    public RequestResult editStudent(@PathVariable("id") Integer id, Student student) {
        if (id == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        Student student1 = adminService.getStudentByPrimaryKey(id);
        if (student1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        student.setId(id);
        if (student1.equals(student)) {
            return RequestResult.failure("未修改任何数据");
        }
        //在保存学生信息之前先查看数据库中是否有重复的数据
        StudentExample studentExample = new StudentExample();
        //1、查看学号是否存在
        StudentExample.Criteria criteria = studentExample.createCriteria();
        if (!student1.getNumber().equals(student.getNumber())) {
            criteria.andNumberEqualTo(student.getNumber());
            boolean exists = adminService.isStudentExistsByExample(studentExample);
            System.out.println(exists);
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
    public RequestResult saveTeacher(Teacher teacher) {
        if (teacher == null) {
            return RequestResult.failure("新增教师失败，请稍后再试");
        }
        if (teacher.getPassword() == null || teacher.getAccount() == null ||
                teacher.getNumber() == null || teacher.getEmail() == null ||
                teacher.getIdCardNo() == null) {
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
        //3、省份证号码是否存在
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
        String fileName = file.getOriginalFilename();
        List<Teacher> teachers = new ArrayList<>();
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
                    //原表头顺序：姓名、编号、电话、身份证号码、账号、密码、邮箱；使用数组来比较
                    String[] original_title = new String[]{"姓名", "编号", "电话", "身份证号码", "账号", "密码", "邮箱"};
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
                        Teacher teacher = new Teacher();
                        String data = POIUtils.getStringHSSF(hssfRow.getCell(0));
                        HSSFCell cell = hssfRow.getCell(0);
                        cell.setCellType(CellType.STRING);
                        teacher.setName(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(1));
                        teacher.setNumber(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(2));
                        teacher.setTelephone(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(3));
                        teacher.setIdCardNo(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(4));
                        teacher.setAccount(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(5));
                        teacher.setPassword(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(6));
                        teacher.setEmail(data);
                        if (collegeId != 0) {//选择了学院
                            teacher.setCollegeId(collegeId);
                        }
                        System.out.println(teacher);
                        teachers.add(teacher);
                    }
//                     System.out.println(teachers);
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
                    //原表头顺序：姓名、编号、电话、身份证号码、账号、密码、邮箱；使用数组来比较
                    String[] original_title = new String[]{"姓名", "编号", "电话", "身份证号码", "账号", "密码", "邮箱"};
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
                        Teacher teacher = new Teacher();
                        String data = POIUtils.getStringXSSF(xssfRow.getCell(0));
                        XSSFCell cell = xssfRow.getCell(0);
                        cell.setCellType(CellType.STRING);
                        teacher.setName(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(1));
                        teacher.setNumber(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(2));
                        teacher.setTelephone(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(3));
                        teacher.setIdCardNo(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(4));
                        teacher.setAccount(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(5));
                        teacher.setPassword(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(6));
                        teacher.setEmail(data);
                        if (collegeId != 0) {
                            teacher.setCollegeId(collegeId);
                        }
                        System.out.println(teacher);
                        teachers.add(teacher);
                    }
//                    System.out.println(teachers);
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
    public ResponseEntity<byte[]> downloadTeacherTemplate(HttpServletRequest request) throws IOException {
        String path = request.getServletContext().getRealPath("/excels/");
        String fileName = "Teacher_Template.xlsx";
        File file = new File(path + fileName);

        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        // 将对文件做的特殊处理还原
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    @RequestMapping("/searchTeacher")
    public String searchTeacher(Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, HttpSession session) {

        TeacherExample teacherExample = (TeacherExample) session.getAttribute("teacherExample");
        map.put("colleges", adminService.getAllColleges());
        //加入PageHelper分页插件，在查询之前只需要调用startPage方法
        //传入页码以及每页显示数据条数
        PageHelper.startPage(pageNum, 10);
        //后面的操作就是分页查询
        List<Teacher> teachers = adminService.getAllTeachersWithCollegeAndCourseByExample(teacherExample);
        //使用PageInfo包装查询结果，PageInfo包含了非常全面的分页属性，只需要将PageInfo交给页面就可以了
        //navigatePages：连续显示多少页
        PageInfo<Student> page = new PageInfo(teachers, 10);
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
                                             HttpSession session) {
        //将查询条件存放进session中，以回显查询条件
        Map<String, Object> map = new HashMap<>();
        map.put("name", null);
        map.put("collegeId", null);
        map.put("course", null);
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
        if (!"".equals(name.trim())) {
            map.put("name", name.trim());
            criteria.andNameLike("%" + name.trim() + "%");
        }
        if (collegeId != 0) {
            map.put("collegeId", collegeId);
            criteria.andCollegeIdEqualTo(collegeId);
        }
        if (!"".equals(course.trim())) {
            map.put("course", course);
            CourseExample courseExample = new CourseExample();
            CourseExample.Criteria criteria1 = courseExample.createCriteria();
            criteria1.andNameLike("%" + course.trim() + "%");
            courseExample.setDistinct(true);
            List<Integer> teacherId = adminService.getTeacherIdByCourseExample(courseExample);
            criteria.andIdIn(teacherId);

        }
        session.setAttribute("teacherQueryCriteria", map);
        session.setAttribute("teacherExample", teacherExample);
        return RequestResult.success();
    }

    @RequestMapping("/updateTeacher/{id}")
    public String updateTeacher(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        //判断id是否存在
        Teacher teacher = adminService.getTeacherByPrimaryKey(id);
        if (teacher == null) {
            map.put("exception", new NotExistException("该id对应的老师不存在"));
            return "error";
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
    public RequestResult editTeacher(@PathVariable("id") Integer id, Teacher teacher) {
        System.out.println(teacher);
        if (id == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        //判断是否修改数据
        Teacher teacher1 = adminService.getTeacherByPrimaryKey(id);
        if (teacher1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        teacher.setId(id);
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
    private RequestResult saveCollege(College college) {
        if (college == null) {
            return RequestResult.failure("新增学院失败，请稍后再试");
        }
        if (college.getName() == null || college.getIntro() == null) {
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

    @RequestMapping("/searchCollege")
    private String searchCollege(Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum, HttpSession session) {

        CollegeExample collegeExample = (CollegeExample) session.getAttribute("collegeExample");
        PageHelper.startPage(pageNum, 10);
        List<College> colleges = adminService.getAllCollegesWithBLOBsByExample(collegeExample);
        PageInfo<Student> page = new PageInfo(colleges, 10);
        map.put("pageInfo", page);
        return "admin/searchCollege";
    }

    @ResponseBody
    @PostMapping("/deleteCollege")
    public RequestResult deleteCollege(@RequestParam("id") Integer id) {
        if (id == null) {
            return RequestResult.failure("删除学院信息失败，请稍后再试");
        }
        //删除教师信息
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
        session.setAttribute("collegeExample", collegeExample);
        session.setAttribute("collegeQueryCriteria", map);
        return RequestResult.success();
    }

    @RequestMapping("/updateCollege/{id}")
    public String updateCollege(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        //判断id是否存在
        College college = adminService.getCollegeByPrimaryKey(id);
        if (college == null) {
            map.put("exception", new NotExistException("该id对应的学院不存在"));
            return "error";
        }
        map.put("college", college);
        map.put("pageNum", pageNum);
        return "admin/updateCollege";
    }

    @ResponseBody
    @PostMapping("/editCollege/{id}")
    public RequestResult editCollege(@PathVariable("id") Integer id, College college) {
        if (id == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        //判断是否修改数据
        College college1 = adminService.getCollegeByPrimaryKey(id);
        if (college1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        college.setId(id);
        college.setIntro(college.getIntro().trim());
        college.setName(college.getName().trim());
        if (college1.equals(college)) {
            return RequestResult.failure("未修改任何数据");
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
        map.put("teachers", adminService.getAllTeachersWithIdNameAndCollege());
        return "admin/createCourse";
    }

    @ResponseBody
    @PostMapping("/saveCourse")
    private RequestResult saveCourse(Course course) {
        if (course == null) {
            return RequestResult.failure("新增课程失败，请稍后再试");
        }
        if (course.getName() == null || course.getIntro() == null || course.getCollegeId() == null || course.getNumber() == null) {
            return RequestResult.failure("新增课程失败，请稍后再试");
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


    @RequestMapping("/searchCourse")
    private String searchCourses(Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, HttpSession session) {
        CourseExample courseExample = (CourseExample) session.getAttribute("courseExample");
        PageHelper.startPage(pageNum, 10);
        List<Course> courses = adminService.getAllCoursesWithBLOBsCollegeAndTeacherByExample(courseExample);
        PageInfo<Student> page = new PageInfo(courses, 10);
        map.put("pageInfo", page);
        map.put("colleges", adminService.getAllColleges());
        map.put("courses", courses);
        return "admin/searchCourse";
    }

    @ResponseBody
    @PostMapping("/deleteCourse")
    public RequestResult deleteCourse(@RequestParam("id") Integer id) {
        if (id == null) {
            return RequestResult.failure("删除课程信息失败，请稍后再试");
        }
        //删除课程信息
        boolean i = adminService.deleteCourseById(id);
        if (!i) {
            return RequestResult.failure("删除课程信息失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteCourseBatch")
    public RequestResult deleteCourseBatch(@RequestParam("ids") String id) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> courseIds = new ArrayList<>();
        for (String s : ids) {
            courseIds.add(Integer.parseInt(s));
        }
        boolean b = adminService.deleteCourseByIdBatch(courseIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/searchCourseByTerm")
    private RequestResult searchCourseByTerm(@RequestParam("name") String name, @RequestParam("collegeId") Integer collegeId, @RequestParam("teacher") String teacher, HttpSession session) {
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
            criteria.andTeacherIdIn(ids);
            map.put("teacher", teacher.trim());
        }
        if (collegeId != 0) {
            criteria.andCollegeIdEqualTo(collegeId);
            map.put("collegeId", collegeId);
        }
        session.setAttribute("courseExample", courseExample);
        session.setAttribute("courseQueryCriteria", map);
        return RequestResult.success();
    }

    @RequestMapping("/updateCourse/{id}")
    public String updateCourse(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        //判断id是否存在
        Course course = adminService.getCourseByPrimaryKey(id);
        if (course == null) {
            map.put("exception", new NotExistException("该id对应的课程不存在"));
            return "error";
        }
        map.put("course", course);
        map.put("colleges", adminService.getAllColleges());
        map.put("teachers", adminService.getAllTeachersWithIdNameAndCollege());
        map.put("pageNum", pageNum);
        return "admin/updateCourse";
    }

    @ResponseBody
    @PostMapping("/editCourse/{id}")
    public RequestResult editCourse(@PathVariable("id") Integer id, Course course) {
        if (id == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        if (course == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        course.setName(course.getName().trim());
        course.setNumber(course.getNumber().trim());
        course.setIntro(course.getIntro().trim());
        //判断是否修改数据
        Course course1 = adminService.getCourseByPrimaryKeyWithoutTeacherAndCollege(id);
        if (course1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        course.setId(id);
        if (course1.equals(course)) {
            return RequestResult.failure("未修改任何数据");
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
        PageHelper.startPage(pageNum, 10);
        List<Discuss> discusses = adminService.getAllDiscussWithBLOBsAndTeacherAndCourseByExample(discussExample);
        PageInfo<Student> page = new PageInfo(discusses, 10);
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
        session.setAttribute("discussExample", discussExample);
        session.setAttribute("discussQueryCriteria", map);
        return RequestResult.success();
    }

    @RequestMapping("/updateDiscuss/{id}")
    public String updateDiscuss(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        //判断id是否存在
        Discuss discuss = adminService.getDiscussByPrimaryKey(id);
        if (discuss == null) {
            map.put("exception", new NotExistException("该id对应的讨论不存在"));
            return "error";
        }
        map.put("courses", adminService.getAllCoursesWithWithCollegeAndTeacher());
        map.put("discuss", discuss);
        map.put("pageNum", pageNum);
        return "admin/updateDiscuss";
    }

    @ResponseBody
    @PostMapping("/editDiscuss/{id}")
    public RequestResult editDiscuss(@PathVariable("id") Integer id, Discuss discuss) {
        if (id == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        if (discuss == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        discuss.setContent(discuss.getContent().trim());
        discuss.setTitle(discuss.getTitle().trim());
        //判断是否修改数据
        Discuss discuss1 = adminService.getDiscussByPrimaryKey(id);
        System.out.println(discuss);
        System.out.println(discuss1);
        if (discuss1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        discuss.setId(id);
        if (discuss1.equals(discuss)) {
            return RequestResult.failure("未修改任何数据");
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
        boolean b = adminService.deleteDiscussByIdBatch(discussIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();

    }


    @RequestMapping("/createNotice")
    public String createNotice() {
        return "admin/createNotice";
    }

    @ResponseBody
    @PostMapping("/saveNotice")
    public RequestResult saveNotice(Notice notice) {
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
        PageHelper.startPage(pageNum, 10);
        List<Notice> notices = adminService.getAllNoticesByExample(noticeExample);
        PageInfo<Student> page = new PageInfo(notices, 10);
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
                map.put("minTime", maxTime);
            }
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
            map.put("exception", new NotExistException("该id对应的公告不存在"));
            return "error";
        }
        map.put("notice", notice);
        map.put("pageNum", pageNum);
        return "admin/updateNotice";
    }

    @ResponseBody
    @PostMapping("/editNotice/{id}")
    public RequestResult editNotice(@PathVariable("id") Integer id, Notice notice) {
        if (id == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        if (notice == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        notice.setContent(notice.getContent().trim());
        notice.setTitle(notice.getTitle().trim());
        //判断是否修改数据
        Notice notice1 = adminService.getNoticeByPrimaryKey(id);
        System.out.println(notice);
        System.out.println(notice1);
        if (notice1 == null) {
            return RequestResult.failure("修改失败，请稍后再试");
        }
        notice.setId(id);
        if (notice1.equals(notice)) {
            return RequestResult.failure("未修改任何数据");
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
        /*Admin admin = (Admin) session.getAttribute("user");
        map.put("admin",adminService.getAdminById(admin.getId()));*/
        map.put("admin", adminService.getAdminById(1));
        return "admin/viewAdminInfo";
    }

    @RequestMapping("/updateAdminInfo")
    public String updateAdminInfo(HttpSession session, Map<String, Object> map) {
        /*Admin admin = (Admin) session.getAttribute("user");
        map.put("admin",adminService.getAdminById(admin.getId()));*/
        map.put("admin", adminService.getAdminById(1));
        return "admin/updateAdminInfo";
    }

    @ResponseBody
    @PostMapping("/editAdminInfo/{id}")
    public RequestResult editAdminInfo(@PathVariable("id") Integer id, Admin admin) {
        if (id == null) {
            return RequestResult.failure("信息更新失败，请稍后再试");
        }
        if (admin == null) {
            return RequestResult.failure("信息更新失败，请稍后再试");
        }
        Admin admin1 = adminService.getAdminById(id);
        System.out.println(admin);
        System.out.println(admin1);
        if (admin1 == null) {
            return RequestResult.failure("信息更新失败，请稍后再试");
        }
        admin.setAccount(admin.getAccount().trim());
        admin.setEmail(admin.getEmail().trim());
        admin.setName(admin.getName().trim());
        admin.setPassword(admin.getPassword().trim());
        admin.setTelephone(admin.getTelephone().trim());
        admin.setId(id);
        if (admin1.equals(admin)) {
            return RequestResult.failure("未修改任何信息");
        }
        //判断手机号、邮箱、登录账户是否存在
        AdminExample adminExample = new AdminExample();
        Criteria criteria = adminExample.createCriteria();
        if (!admin.getTelephone().equals(admin1.getTelephone())) {//修改了手机号
            criteria.andTelephoneEqualTo(admin.getTelephone());
            boolean exists = adminService.isExistsByExample(adminExample);
            if (exists) {
                return RequestResult.failure("该手机号已经存在");
            }
        } else {
            admin.setTelephone(null);
        }
        if (!admin.getEmail().equals(admin1.getEmail())) {//修改了邮箱
            adminExample.clear();
            criteria = adminExample.createCriteria();
            criteria.andEmailEqualTo(admin.getEmail());
            boolean exists = adminService.isExistsByExample(adminExample);
            if (exists) {
                return RequestResult.failure("该邮箱已经存在");
            }
        } else {
            admin.setEmail(null);
        }
        if (!admin.getAccount().equals(admin1.getAccount())) {//修改了登录账户
            adminExample.clear();
            criteria = adminExample.createCriteria();
            criteria.andAccountEqualTo(admin.getAccount());
            boolean exists = adminService.isExistsByExample(adminExample);
            if (exists) {
                return RequestResult.failure("该账号已经存在");
            }
        } else {
            admin.setAccount(null);
        }
        if (admin.getPassword().equals(admin1.getPassword())) {
            admin.setPassword(null);
        }
        if (admin.getName().equals(admin1.getName())) {
            admin.setName(null);
        }
        boolean b = adminService.updateAdminByPrimaryKeySelective(admin);
        if (!b) {
            return RequestResult.failure("信息更新失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/logout")
    public RequestResult logout(HttpSession session) {
        session.invalidate();
        return RequestResult.success();
    }
}