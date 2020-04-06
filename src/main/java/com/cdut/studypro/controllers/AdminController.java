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
    public String studentNotExistException(NotExistException e, Map<String, Object> map) {
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
        map.put("courses", courses);
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
    public String createTeacherBatch() {
        return "admin/createTeacherBatch";
    }

    @ResponseBody
    @PostMapping("/teacherDataImport")
    public RequestResult teacherDataImport(@RequestParam(value = "file") MultipartFile file) {
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
        map.put("courses", adminService.getAllCourses());
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
    public RequestResult searchTeacherByTerm(@RequestParam("number") String number,
                                             @RequestParam("collegeId") Integer collegeId,
                                             @RequestParam("courseId") Integer courseId,
                                             HttpSession session) {
        //将查询条件存放进session中，以回显查询条件
        Map<String, Object> map = new HashMap<>();
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
        if (!"".equals(number.trim())) {
            criteria.andNumberLike("%" + number.trim() + "%");
            map.put("number", Integer.parseInt(number.trim()));
        }
        if (collegeId != 0) {
            criteria.andCollegeIdEqualTo(collegeId);
            map.put("collegeId", collegeId);
        }
        if (courseId != 0) {
            criteria.andCourseIdEqualTo(courseId);
            map.put("courseId", courseId);
        }
        session.setAttribute("teacherExample", teacherExample);
        session.setAttribute("teacherQueryCriteria", map);
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
        map.put("courses", adminService.getAllCourses());
        map.put("teacher", teacher);
        map.put("pageNum", pageNum);
        return "admin/updateTeacher";
    }

    @ResponseBody
    @PostMapping("/editTeacher/{id}")
    public RequestResult editTeacher(@PathVariable("id") Integer id, Teacher teacher) {
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
            return RequestResult.failure("未修改任何数据");
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
        if (teacher1.getCourseId().equals(teacher.getCourseId())) {
            teacher.setCourseId(null);
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
    private String createCourse(Map<String,Object> map){
        map.put("colleges",adminService.getAllColleges());
        return "admin/createCourse";
    }

    @ResponseBody
    @PostMapping("/saveCourse")
    private RequestResult saveCourse(Course course) {
        //先判断该课程编号是否已经存在了
        /*CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andNameEqualTo(course.getNum());
        boolean exists = adminService.isCollegeExistsByExample(collegeExample);
        if (exists) {
            return RequestResult.failure("该学院已经存在了");
        }
        boolean b = adminService.insertCollegeSelective(college);
        if (!b) {
            return RequestResult.failure("学院添加失败，请稍后再试");
        }*/
        return RequestResult.success();
    }



    @RequestMapping("/searchCourse")
    private String searchCourses() {
        return "admin/searchCourse";
    }


}
