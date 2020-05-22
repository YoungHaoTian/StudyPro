package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.beans.StudentExample.*;
import com.cdut.studypro.exceptions.DownloadException;
import com.cdut.studypro.exceptions.MaxUploadSizeExceedException;
import com.cdut.studypro.services.StudentService;
import com.cdut.studypro.utils.MD5Util;
import com.cdut.studypro.utils.RequestResult;
import com.cdut.studypro.validates.UpdateStudentSequence;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
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
import java.io.File;
import java.io.IOException;

import java.net.URLEncoder;
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
@RequestMapping("/student")
public class StudentController {

    //单位：MB
    private final static long FILE_MAX_SIZE = 100;
    @Autowired
    private StudentService studentService;

    //异常处理（运行时异常）
    @ExceptionHandler(RuntimeException.class)
    public String handRuntimeException(RuntimeException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return "error";
    }

    //文件上传超过最大容量异常处理
    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceedException.class)
    public RequestResult handMaxUploadSizeExceedException(MaxUploadSizeExceedException e) {
        return RequestResult.failure(e.getMessage());
    }


    @ResponseBody
    @PostMapping(value = "/login")
    public RequestResult login(@RequestBody Map<String, String> map, HttpServletRequest request) {
        HttpSession session = request.getSession();
        /*ServletContext application = request.getServletContext();
        List<Integer> studentLogin = (List<Integer>) application.getAttribute("studentLogin");*/
        StudentExample studentExample = new StudentExample();
        Criteria criteria = studentExample.createCriteria();
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
        List<Student> students = studentService.selectStudentByExample(studentExample);
        if (students != null && students.size() != 0) {
            if (students.size() != 1) {
                return RequestResult.failure("登录异常，请选择其他方式登录");
            }
            Student student = students.get(0);
            String password = map.get("password");
            if (password.equals(MD5Util.stringToMD5(student.getPassword()))) {
                //同时只能登陆一个账户
                Object user = session.getAttribute("user");
                if (user != null) {
                    return RequestResult.failure("同时只能登陆一个账户");
                }
                //先查看当前学生是否已经登录
                /*if (studentLogin.contains(student.getId())) {
                    return RequestResult.failure("请勿重复登录");
                }*/
                //登录成功将学生信息保存在session中，并保存角色
                Student student1 = new Student();
                student1.setId(student.getId());
                student1.setCollegeId(student.getCollegeId());
                session.setAttribute("role", "student");
                session.setAttribute("user", student1);
                //登录成功后将该管理员id记录到studentLogin中
                //studentLogin.add(student.getId());
                //根据student的id获取到学生已经加入的课程id
                session.setAttribute("courseIds", studentService.getJoinCourseIdByStudentId(student.getId()));
                return RequestResult.success();
            } else {
                return RequestResult.failure("密码错误，请稍后重试");
            }
        }
        return RequestResult.failure(message);
    }

    @ResponseBody
    @PostMapping(value = "/register")
    public RequestResult register(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String code = map.get("code");
        String phone = map.get("phone");
        String password = map.get("password");

        //查询该手机号是否已经被注册
        Integer id = studentService.getIdByTelephone(phone);
        if (id != null) {
            return RequestResult.failure("该手机号已经被注册了");
        }
        JSONObject json = (JSONObject) request.getSession().getAttribute("RegisterCode");
        if (json == null) {
            return RequestResult.failure("注册失败，还未获取验证码");
        }
        if (!json.getString("verifyCode").equals(code)) {
            return RequestResult.failure("验证码错误，请稍后重试");
        }
        if ((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5) {
            //删除session中的验证码
            request.getSession().removeAttribute("RegisterCode");
            return RequestResult.failure("验证码已过期，请重新获取");
        }

        //将用户存入数据库
        Student student = new Student();
        student.setTelephone(phone);
        student.setPassword(password);

        boolean b = studentService.insertStudentSelective(student);
        if (!b) {
            //注册成功，删除session中的验证码
            request.getSession().removeAttribute("RegisterCode");
            return RequestResult.failure("注册失败，请稍后重试");

        }
        //删除session中的验证码
        request.getSession().removeAttribute("RegisterCode");
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping(value = "/findPassword")
    public RequestResult findPassword(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String code = map.get("code");
        String phone = map.get("phone");
        String password = map.get("password");
        //1、先验证验证码是否正确
        JSONObject json = (JSONObject) request.getSession().getAttribute("ForgetCode");
        if (json == null) {
            return RequestResult.failure("密码找回失败，还未获取验证码");
        }
        if (!json.getString("verifyCode").equals(code)) {
            return RequestResult.failure("验证码错误，请稍后重试");
        }
        if ((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5) {
            //删除session中的验证码
            request.getSession().removeAttribute("ForgetCode");
            return RequestResult.failure("验证码已过期，请重新获取");
        }

        //2、查询该手机号是否存在
        Integer id = studentService.getIdByTelephone(phone);
        System.out.println(id);
        //不存在
        if (id == null) {
            //删除session中的验证码
            request.getSession().removeAttribute("ForgetCode");
            return RequestResult.failure("该手机号不存在，请稍后再试");
        }
        //存在，修改学生密码
        Student student = new Student();
        student.setId(id);
        student.setPassword(password);
        boolean b = studentService.updateStudentByPrimaryKeySelective(student);
        if (!b) {
            //删除session中的验证码
            request.getSession().removeAttribute("ForgetCode");
            RequestResult.failure("密码找回失败，请稍后再试");
        }
        //删除session中的验证码
        request.getSession().removeAttribute("ForgetCode");
        return RequestResult.success();
    }

    @RequestMapping("/studentIndex")
    public String teacherIndex() {
        return "student/studentIndex";
    }


    @RequestMapping("/searchCourseInfo")
    public String searchCourseInfo(Map<String, Object> map, HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        //根据学生所属的学院，查询该学院所有的课程
        Student student = (Student) session.getAttribute("user");
        CourseExample courseExample = (CourseExample) session.getAttribute("courseExample");
        if (courseExample == null) {
            courseExample = new CourseExample();
            CourseExample.Criteria criteria = courseExample.createCriteria();
            criteria.andCollegeIdEqualTo(student.getCollegeId());
            courseExample.setOrderByClause("CONVERT(name using gbk) asc");
            List<Integer> courseIds = (List<Integer>) session.getAttribute("courseIds");
            if (courseIds.size() == 0) {
                courseIds.add(0);
            }
            //查询学生没有加入的课程
            criteria.andIdNotIn(courseIds);
        }
        PageHelper.startPage(pageNum, 20);
        List<Course> courses = studentService.getAllCourseWithBLOBsAndTeacherByExample(courseExample);
        PageInfo<Course> page = new PageInfo(courses, 10);
        map.put("pageInfo", page);
        return "student/searchCourseInfo";
    }

    @ResponseBody
    @PostMapping("/searchCourseInfoByTerm")
    private RequestResult searchCourseInfoByTerm(@RequestParam("name") String name,
                                                 @RequestParam("teacher") String teacher,
                                                 HttpSession session,
                                                 @RequestParam("number") String number) {
        Student student = (Student) session.getAttribute("user");
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
            List<Integer> ids = studentService.getTeacherIdByTeacherExample(teacherExample);
            if (ids.size() == 0) {
                ids.add(0);
            }
            criteria.andTeacherIdIn(ids);
            map.put("teacher", teacher.trim());
        }
        if (!"".equals(number.trim())) {
            criteria.andNumberLike("%" + number.trim() + "%");
            map.put("number", number.trim());
        }
        //根据student的id获取到学生已经加入的课程id
        List<Integer> courseIds = (List<Integer>) session.getAttribute("courseIds");
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        //查询学生没有加入的课程
        criteria.andIdNotIn(courseIds);
        criteria.andCollegeIdEqualTo(student.getCollegeId());
        courseExample.setOrderByClause("CONVERT(name using gbk) asc");
        session.setAttribute("courseExample", courseExample);
        session.setAttribute("courseQueryCriteria", map);
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/joinCourse")
    public RequestResult joinCourse(@RequestParam("id") Integer id, HttpSession session) {
        Student student = (Student) session.getAttribute("user");
        Collect collect = new Collect();
        collect.setCourseId(id);
        collect.setStudentId(student.getId());
        boolean success = studentService.joinCourse(collect);
        if (!success) {
            return RequestResult.failure("加入课程失败，请稍后再试");
        }
        List<Integer> course = (List<Integer>) session.getAttribute("courseIds");
        course.add(id);
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/joinCourseBatch")
    public RequestResult joinCourseBatch(@RequestParam("ids") String id, HttpSession session) {
        Student student = (Student) session.getAttribute("user");
        List<Collect> collects = new ArrayList<>();
        String[] ids = id.split("-");
        List<Integer> courseIds = new ArrayList<>();
        for (String courseId : ids) {
            Collect collect = new Collect();
            collect.setCourseId(Integer.parseInt(courseId));
            courseIds.add(Integer.parseInt(courseId));
            collect.setStudentId(student.getId());
            collects.add(collect);
        }
        boolean success = studentService.joinCourseBatch(collects);
        if (!success) {
            return RequestResult.failure("批量加入课程失败，请稍后再试");
        }
        List<Integer> course = (List<Integer>) session.getAttribute("courseIds");
        course.addAll(courseIds);
        return RequestResult.success();
    }

    @RequestMapping("/searchMyCourseInfo")
    public String searchMyCourseInfo(HttpSession session, Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Student student = (Student) session.getAttribute("user");
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        //根据student的id获取到学生已经加入的课程id
        List<Integer> courseIds = studentService.getJoinCourseIdByStudentId(student.getId());
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        criteria.andIdIn(courseIds);
        courseExample.setOrderByClause("CONVERT(name using gbk) asc");
        PageHelper.startPage(pageNum, 20);
        List<Course> courses = studentService.getAllCourseWithBLOBsAndTeacherByExample(courseExample);
        PageInfo<Course> page = new PageInfo(courses, 10);
        map.put("pageInfo", page);
        return "student/searchMyCourseInfo";
    }

    @ResponseBody
    @PostMapping("/removeCourseBatch")
    public RequestResult removeCourseBatch(@RequestParam("ids") String id, HttpSession session) {
        Student student = (Student) session.getAttribute("user");
        if (id == null) {
            return RequestResult.failure("批量移除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> courseIds = new ArrayList<>();
        for (String s : ids) {
            courseIds.add(Integer.parseInt(s));
        }
        boolean success = studentService.removeCourseBatch(courseIds, student.getId());
        if (!success) {
            return RequestResult.failure("批量移除失败，请稍后再试");
        }
        List<Integer> course = (List<Integer>) session.getAttribute("courseIds");
        course.removeAll(courseIds);
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/removeCourse")
    public RequestResult removeCourse(@RequestParam("id") Integer id, HttpSession session) {
        Student student = (Student) session.getAttribute("user");
        if (id == null) {
            return RequestResult.failure("课程移除失败，请稍后再试");
        }
        boolean success = studentService.removeCourse(id, student.getId());
        if (!success) {
            return RequestResult.failure("课程移除失败，请稍后再试");
        }
        List<Integer> course = (List<Integer>) session.getAttribute("courseIds");
        course.remove(id);
        return RequestResult.success();
    }

    @RequestMapping("/searchChapter")
    public String searchChapter(Map<String, Object> map, @RequestParam("courseId") Integer courseId, @RequestParam("pageNum") Integer pageNum) {
        CourseChapterExample courseChapterExample = new CourseChapterExample();
        CourseChapterExample.Criteria criteria = courseChapterExample.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        courseChapterExample.setOrderByClause("record_time asc");
        List<CourseChapter> chapters = studentService.getAllChapterWithBLOBsAndCourseByExample(courseChapterExample);
        map.put("chapters", chapters);
        map.put("courseId", courseId);
        map.put("pageNum", pageNum);
        return "student/searchChapter";
    }

    @RequestMapping("/viewChapterFiles/{id}")
    public String viewChapterFiles(@PathVariable("id") Integer id,
                                   Map<String, Object> map,
                                   @RequestParam(value = "minTime", defaultValue = "") String minTime,
                                   @RequestParam(value = "maxTime", defaultValue = "") String maxTime,
                                   @RequestParam("pageNum") Integer pageNum,
                                   @RequestParam("courseId") Integer courseId) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CourseVideoExample courseVideoExample = new CourseVideoExample();
        CourseVideoExample.Criteria videoExampleCriteria = courseVideoExample.createCriteria();
        videoExampleCriteria.andChapterIdEqualTo(id);
        CourseFileExample courseFileExample = new CourseFileExample();
        CourseFileExample.Criteria fileExampleCriteria = courseFileExample.createCriteria();
        fileExampleCriteria.andChapterIdEqualTo(id);
        if (!"".equals(minTime.trim())) {
            Date min = simpleDateFormat.parse(minTime.trim());
            videoExampleCriteria.andRecordTimeGreaterThanOrEqualTo(min);
            fileExampleCriteria.andRecordTimeGreaterThanOrEqualTo(min);
        }
        if (!"".equals(maxTime.trim())) {
            Date max = simpleDateFormat.parse(maxTime.trim());
            videoExampleCriteria.andRecordTimeLessThanOrEqualTo(max);
            fileExampleCriteria.andRecordTimeLessThanOrEqualTo(max);
        }
        courseVideoExample.setOrderByClause("record_time asc");
        courseFileExample.setOrderByClause("record_time asc");
        List<CourseVideo> videos = studentService.getCourseVideoByExample(courseVideoExample);
        List<CourseFile> files = studentService.getCourseFileByExample(courseFileExample);
        map.put("videos", videos);
        map.put("files", files);
        map.put("id", id);
        map.put("minTime", minTime);
        map.put("maxTime", maxTime);
        map.put("pageNum", pageNum);
        map.put("courseId", courseId);
        return "student/viewChapterFiles";
    }

    @RequestMapping("/downloadCourseFile/{id}")
    public ResponseEntity<byte[]> downloadCourseFile(@PathVariable("id") Integer id, HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/file/");
        CourseFile courseFile = studentService.getCourseFileById(id);
        String fileName = courseFile.getPath().split("_", 2)[1];
        File file = new File(path + courseFile.getPath());
        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        try {
            // 转码，避免文件名显示不出中文
            String tempStr = UUID.randomUUID().toString();
            fileName = fileName.replace(" ", tempStr);
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = fileName.replace(tempStr, " ");
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new DownloadException("下载异常，请稍后再试");
        }
    }

    @RequestMapping("/viewCourseVideo/{id}")
    public String viewCourseVideo(@PathVariable("id") Integer id, Map<String, Object> map) {
        CourseVideo video = studentService.getCourseVideoById(id);
        map.put("video", video);
        return "student/viewCourseVideo";
    }

    @RequestMapping("/searchCourseVideoInfo")
    public String searchCourseVideoInfo(HttpSession session, Map<String, Object> map, @RequestParam(value = "courseId", defaultValue = "0") Integer courseId) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();

        List<Integer> courseIds = (List<Integer>) session.getAttribute("courseIds");
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        if (courseId == 0) {
            Integer courseId1 = (Integer) session.getAttribute("courseId");
            if (courseId1 != null && courseIds.contains(courseId1)) {
                courseId = courseId1;
            } else {
                courseId = courseIds.get(0);
            }
        } else {
            session.setAttribute("courseId", courseId);
        }
        criteria.andIdEqualTo(courseId);
        map.put("courseId", courseId);
        map.put("course", studentService.getCourseByCourseIdsWithNameAndTeacher(courseIds));
        map.put("courses", studentService.getCourseWithChapterAndVideoByExample(courseExample));
        return "student/searchCourseVideoInfo";
    }

    @RequestMapping("/downloadCourseVideo/{id}")
    public ResponseEntity<byte[]> downloadCourseVideo(@PathVariable("id") Integer id, HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/video/");
        CourseVideo courseVideo = studentService.getCourseVideoById(id);
        String fileName = courseVideo.getPath().split("_", 2)[1];
        File file = new File(path + courseVideo.getPath());
        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        try {
            // 转码，避免文件名显示不出中文
            String tempStr = UUID.randomUUID().toString();
            //替换空格
            fileName = fileName.replace(" ", tempStr);
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = fileName.replace(tempStr, " ");
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new DownloadException("下载异常，请稍后再试");
        }
    }

    @RequestMapping("/searchCourseFileInfo")
    public String searchCourseFileInfo(HttpSession session, Map<String, Object> map, @RequestParam(value = "courseId", defaultValue = "0") Integer courseId) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();

        List<Integer> courseIds = (List<Integer>) session.getAttribute("courseIds");
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        if (courseId == 0) {
            Integer courseId1 = (Integer) session.getAttribute("courseId");
            if (courseId1 != null && courseIds.contains(courseId1)) {
                courseId = courseId1;
            } else {
                courseId = courseIds.get(0);
            }
        } else {
            session.setAttribute("courseId", courseId);
        }
        criteria.andIdEqualTo(courseId);
        map.put("courseId", courseId);
        map.put("course", studentService.getCourseByCourseIdsWithNameAndTeacher(courseIds));
        map.put("courses", studentService.getCourseWithChapterAndFileByExample(courseExample));
        return "student/searchCourseFileInfo";
    }

    @RequestMapping("/searchDiscussInfo")
    public String searchDiscussInfo(Map<String, Object> map, HttpSession session, @RequestParam(value = "courseId", defaultValue = "0") Integer courseId) {
        DiscussExample discussExample = new DiscussExample();
        DiscussExample.Criteria criteria = discussExample.createCriteria();

        List<Integer> courseIds = (List<Integer>) session.getAttribute("courseIds");
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        if (courseId == 0) {
            Integer courseId1 = (Integer) session.getAttribute("courseId");
            if (courseId1 != null && courseIds.contains(courseId1)) {
                courseId = courseId1;
            } else {
                courseId = courseIds.get(0);
            }
        } else {
            session.setAttribute("courseId", courseId);
        }
        criteria.andCourseIdEqualTo(courseId);
        map.put("courses", studentService.getCourseByCourseIdsWithNameAndTeacher(courseIds));
        map.put("discusses", studentService.getDiscussWithCourseByExample(discussExample));
        map.put("courseId", courseId);
        return "student/searchDiscussInfo";
    }

    @RequestMapping("/searchDiscussReply/{id}")
    public String searchDiscussReply(HttpSession session,
                                     @PathVariable("id") Integer id,
                                     Map<String, Object> map,
                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam("courseId") Integer courseId) {
        Map<String, Object> parameterMap = (Map<String, Object>) session.getAttribute("discussPostParameterMap");
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        parameterMap.put("id", id);
        PageHelper.startPage(pageNum, 30);
        List<DiscussPost> discussPosts = studentService.getAllDiscussPostByMapWithStudentName(parameterMap);
        PageInfo<DiscussPost> page = new PageInfo(discussPosts, 10);
        map.put("pageInfo", page);
        map.put("id", id);
        map.put("courseId", courseId);
        return "student/searchDiscussReply";
    }

    @ResponseBody
    @PostMapping("/searchDiscussPostByTerm/{id}")
    public RequestResult searchDiscussPostByTerm(@PathVariable("id") Integer id,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("minTime") String minTime,
                                                 @RequestParam("maxTime") String maxTime,
                                                 @RequestParam("content") String content,
                                                 HttpSession session) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //将查询条件存放进session中，以回显查询条件
            Map<String, Object> map = new HashMap<>();
            map.put("name", null);
            map.put("minTime", null);
            map.put("maxTime", null);
            map.put("content", null);
            if (!"".equals(name.trim())) {
                map.put("name", name.trim());
            }
            if (!"".equals(minTime.trim())) {
                Date min = simpleDateFormat.parse(minTime.trim());
                map.put("minTime", minTime.trim());
                map.put("minDate", min);
            }
            if (!"".equals(maxTime.trim())) {
                Date max = simpleDateFormat.parse(maxTime.trim());
                map.put("maxTime", maxTime.trim());
                map.put("maxDate", max);
            }
            if (!"".equals(content.trim())) {
                map.put("content", content.trim());
            }
            map.put("id", id);
            session.setAttribute("discussPostParameterMap", map);
        } catch (Exception e) {
            return RequestResult.failure("查询失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/saveDiscussReply/{id}")
    public RequestResult saveDiscussReply(@PathVariable("id") Integer id,
                                          HttpSession session,
                                          @RequestParam("content") String content) {
        if (content == null) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        if ("".equals(content)) {
            return RequestResult.failure("回复内容为空，请重新输入");
        }
        if (content.length() >= 100) {
            return RequestResult.failure("回复内容请控制在100字以内");
        }
        Student student = (Student) session.getAttribute("user");
        DiscussPost discussPost = new DiscussPost();
        discussPost.setRecordTime(new Date());
        discussPost.setStudentId(student.getId());
        discussPost.setDiscussId(id);
        discussPost.setContent(content);
        boolean success = studentService.insertDiscussPostSelective(discussPost);
        if (!success) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchNoticeInfo")
    public String searchNoticeInfo(Map<String, Object> map) {
        List<Notice> notices = studentService.getAllNotices();
        map.put("notices", notices);
        return "student/searchNoticeInfo";
    }

    @RequestMapping("/searchCourseChapterInfo")
    public String searchCourseChapterInfo(Map<String, Object> map, HttpSession session, @RequestParam(value = "courseId", defaultValue = "0") Integer courseId) {

        List<Integer> courseIds = (List<Integer>) session.getAttribute("courseIds");
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        if (courseId == 0) {
            Integer courseId1 = (Integer) session.getAttribute("courseId");
            if (courseId1 != null && courseIds.contains(courseId1)) {
                courseId = courseId1;
            } else {
                courseId = courseIds.get(0);
            }
        } else {
            session.setAttribute("courseId", courseId);
        }
        map.put("course", studentService.getCourseWithChapterAndTeacherByCourseId(courseId));
        map.put("courses", studentService.getCourseByCourseIdsWithNameAndTeacher(courseIds));
        map.put("courseId", courseId);
        return "student/searchCourseChapterInfo";
    }

    @RequestMapping("searchTaskInfo/{id}")
    public String searchTaskInfo(@PathVariable("id") Integer id,
                                 Map<String, Object> map,
                                 HttpSession session,
                                 @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                 @RequestParam("courseId") Integer courseId,
                                 @RequestParam(value = "taskType", defaultValue = "") String taskType) {
        Student student = (Student) session.getAttribute("user");
        String type = null;
        if ("".equals(taskType)) {
            type = (String) session.getAttribute("taskType");
        } else {
            type = taskType;
            session.setAttribute("taskType", type);
        }
        if (type == null || type.equals("online")) {
            List<OnlineTask> onlineTasks = studentService.getAllOnlineTasksByChapterId(id);
            map.put("onlineTasks", onlineTasks);
            List<Integer> taskIds = new ArrayList<>();
            for (OnlineTask task : onlineTasks) {
                taskIds.add(task.getId());
            }
            if (taskIds.size() == 0) {
                taskIds.add(0);
            }
            List<StudentOnlineTask> finishTask = studentService.getFinishOnlineTaskByStudentIdAndTaskIds(student.getId(), taskIds);
            Map<Integer, Integer> total = new HashMap<>();
            Map<Integer, StudentOnlineTask> onlineTasksMap = new HashMap<>();
            for (StudentOnlineTask studentTask : finishTask) {
                onlineTasksMap.put(studentTask.getOnlineTaskId(), studentTask);
                total.put(studentTask.getOnlineTaskId(), studentService.getOnlineTaskTotalScore(studentTask.getOnlineTaskId()));
            }
            map.put("onlineTasksMap", onlineTasksMap);
            map.put("total", total);
            if (type == null) {
                session.setAttribute("taskType", "online");
            }
        } else if (type.equals("offline")) {
            List<OfflineTask> offlineTasks = studentService.getAllOfflineTasksByChapterId(id);
            map.put("offlineTasks", offlineTasks);
            List<Integer> taskIds = new ArrayList<>();
            for (OfflineTask task : offlineTasks) {
                taskIds.add(task.getId());
            }
            if (taskIds.size() == 0) {
                taskIds.add(0);
            }
            List<StudentOfflineTask> finishTask = studentService.getFinishOfflineTaskByStudentIdAndTaskIds(student.getId(), taskIds);
            Map<Integer, StudentOfflineTask> offlineTasksMap = new HashMap<>();
            for (StudentOfflineTask studentTask : finishTask) {
                offlineTasksMap.put(studentTask.getOfflineTaskId(), studentTask);
            }
            map.put("offlineTasksMap", offlineTasksMap);
        } else {
            throw new RuntimeException("查看任务出现异常，请稍后再试");
        }
        map.put("chapterId", id);
        map.put("courseId", courseId);
        map.put("pageNum", pageNum);
        return "student/searchTaskInfo";
    }

    @ResponseBody
    @PostMapping("/uploadOfflineTask/{taskId}")
    public RequestResult uploadOfflineTask(@PathVariable("taskId") Integer taskId,
                                           @RequestParam(value = "file") MultipartFile file,
                                           @RequestParam("chapterId") Integer chapterId,
                                           @RequestParam("courseId") Integer courseId,
                                           HttpServletRequest request) {
        HttpSession session = request.getSession();
        Student student = (Student) session.getAttribute("user");
        long size = file.getSize();
        if (chapterId == 0 || courseId == 0) {
            return RequestResult.failure("上传作业文件失败，请稍后再试");
        }
        if (size > FILE_MAX_SIZE * 1024 * 1024) {
            throw new MaxUploadSizeExceedException("文件上传最大为：", FILE_MAX_SIZE);
        }
        String path = request.getServletContext().getRealPath("/offlineTask/");
        //线下作业文件存放路径：/offlineTask/课程id/章节id/任务id/上传时间_学生姓名_文件名
        String fileDir = courseId + "\\" + chapterId + "\\" + taskId + "\\";
        path += fileDir;
        File offlineTaskDir = new File(path);
        if (!offlineTaskDir.exists()) {//文件夹不存在，先创建出对应的文件夹
            boolean mkdirs = offlineTaskDir.mkdirs();
            if (!mkdirs) {
                return RequestResult.failure("上传作业文件失败，请稍后再试");
            }
        }
        Date date = new Date();
        String fileName = date.getTime() + "_" + student.getId() + "_" + file.getOriginalFilename();
        try {
            file.transferTo(new File(offlineTaskDir, fileName));
        } catch (IOException e) {
            return RequestResult.failure("上传作业文件失败，请稍后再试");
        }
        StudentOfflineTask studentOfflineTask = new StudentOfflineTask();
        studentOfflineTask.setOfflineTaskId(taskId);
        studentOfflineTask.setRecordTime(date);
        studentOfflineTask.setStudentId(student.getId());
//        studentOfflineTask.setStudentId(26);
        studentOfflineTask.setPath(fileDir + fileName);
        boolean success = studentService.uploadOfflineTaskFile(studentOfflineTask);
        if (!success) {
            return RequestResult.failure("上传作业文件失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/reUploadOfflineTask/{studentTaskId}")
    public RequestResult reUploadOfflineTask(@PathVariable("studentTaskId") Integer studentTaskId,
                                             @RequestParam(value = "file") MultipartFile file,
                                             @RequestParam("chapterId") Integer chapterId,
                                             @RequestParam("courseId") Integer courseId,
                                             @RequestParam("taskId") Integer taskId,
                                             HttpServletRequest request) {
        HttpSession session = request.getSession();
        Student student = (Student) session.getAttribute("user");
        long size = file.getSize();
        if (chapterId == 0 || courseId == 0) {
            return RequestResult.failure("更新作业文件失败，请稍后再试");
        }
        if (size > FILE_MAX_SIZE * 1024 * 1024) {
            throw new MaxUploadSizeExceedException("文件上传最大为：", FILE_MAX_SIZE);
        }
        String path = request.getServletContext().getRealPath("/offlineTask/");
        String fileDir = courseId + "\\" + chapterId + "\\" + taskId + "\\";
        //根据学生id和任务id，找出学生的任务记录
        StudentOfflineTask studentOfflineTask = studentService.getStudentOfflineTask(studentTaskId);
        System.out.println(studentOfflineTask);
        String filePath = studentOfflineTask.getPath();
        File oldFile = new File(path + filePath);
        if (oldFile.exists()) {
            boolean delete = oldFile.delete();
            if (!delete) {
                return RequestResult.failure("更新作业文件失败，请稍后再试");
            }
        }
        Date date = new Date();
        String fileName = date.getTime() + "_" + student.getId() + "_" + file.getOriginalFilename();
        try {
            file.transferTo(new File(path + fileDir + fileName));
        } catch (IOException e) {
            return RequestResult.failure("上传作业文件失败，请稍后再试");
        }
        studentOfflineTask.setOfflineTaskId(null);
        studentOfflineTask.setRecordTime(date);
        studentOfflineTask.setStudentId(null);
        studentOfflineTask.setPath(fileDir + fileName);
        boolean success = studentService.reUploadOfflineTaskFile(studentOfflineTask);
        if (!success) {
            return RequestResult.failure("更新作业文件失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/downloadOfflineTaskFile/{id}")
    public ResponseEntity<byte[]> downloadOfflineTaskFile(@PathVariable("id") Integer id, HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/offlineTask/");
        StudentOfflineTask studentOfflineTask = studentService.getStudentOfflineTask(id);
        String fileName = studentOfflineTask.getPath().split("_", 3)[2];
        File file = new File(path + studentOfflineTask.getPath());
        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        try {
            // 转码，避免文件名显示不出中文
            String tempStr = UUID.randomUUID().toString();
            //替换空格
            fileName = fileName.replace(" ", tempStr);
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = fileName.replace(tempStr, " ");
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new DownloadException("下载异常，请稍后再试");
        }
    }


    @RequestMapping("/enterTask/{id}")
    public String enterTask(@PathVariable("id") Integer id,
                            Map<String, Object> map,
                            HttpSession session,
                            @RequestParam(value = "pageNum", required = false) Integer pageNum,
                            @RequestParam(value = "flag", defaultValue = "0") Integer flag,
                            @RequestParam(value = "chapterId", defaultValue = "0") Integer chapterId,
                            @RequestParam(value = "courseId", defaultValue = "0") Integer courseId) {

        session.setAttribute("questions", studentService.getTaskQuestionsByTaskId(id));
        map.put("finish", studentService.isTaskFinish(id));
        map.put("taskId", id);
        map.put("total", studentService.getOnlineTaskTotalScore(id));
        map.put("onlineTask", studentService.getOnlineTask(id));
        map.put("chapterId", chapterId);
        map.put("flag", flag);
        map.put("courseId", courseId);
        map.put("pageNum", pageNum);
        return "student/enterTask";
    }

    @ResponseBody
    @PostMapping("/saveStudentTask/{id}")
    public RequestResult saveStudentTask(@PathVariable("id") Integer taskId,
                                         @RequestParam Map<String, String> map,
                                         HttpSession session) {
        //学生提交之前先判断该学生是否已经完成该作业
        Student student = (Student) session.getAttribute("user");
        if (studentService.isTaskFinish(taskId)) {
            return RequestResult.failure("你已经完成了该作业，请勿重复提交");
        }
        if (map.containsValue("0")) {
            return RequestResult.failure("还有未做的题目，请完成后再提交");
        }
        //从数据库中获取到的题目
        List<OnlineTaskQuestion> questions = (List<OnlineTaskQuestion>) session.getAttribute("questions");
        if (questions == null) {
            return RequestResult.failure("作业提交失败，请稍后再试");
        }
        if (map.size() != questions.size()) {
            return RequestResult.failure("作业提交失败，请稍后再试");
        }
        int score = 0;//得分
        int total = 0;//总分
        //将成绩和错题id记录在result中
        List<Integer> result = new ArrayList<>();
        int len = questions.size();
        for (int i = 0; i < len; i++) {
            OnlineTaskQuestion question = questions.get(i);
            if (map.get(String.valueOf(question.getId())).equals(question.getAnswer())) {
                score += question.getScore();
            } else {
                result.add(question.getId());
            }
            total += question.getScore();
        }
        StudentOnlineTask studentOnlineTask = new StudentOnlineTask();
        studentOnlineTask.setRecordTime(new Date());
        studentOnlineTask.setScore(score);
        studentOnlineTask.setStudentId(student.getId());
        studentOnlineTask.setOnlineTaskId(taskId);
        boolean success = studentService.insertStudentOnlineTaskSelective(studentOnlineTask);
        if (!success) {
            return RequestResult.failure("作业提交失败，请稍后再试");
        }
        //将所得分数添加到结果中
        result.add(score);
        result.add(total);
        RequestResult requestResult = RequestResult.success();
        requestResult.setData(result);
        //移除session中的题目信息
        session.removeAttribute("questions");
        return requestResult;
    }

    @RequestMapping("/searchStudentTaskInfo")
    public String searchStudentTaskInfo(HttpSession session,
                                        Map<String, Object> map,
                                        @RequestParam(value = "courseId", defaultValue = "0") Integer courseId,
                                        @RequestParam(value = "taskType", defaultValue = "") String taskType) {
        Student student = (Student) session.getAttribute("user");
        List<Integer> courseIds = (List<Integer>) session.getAttribute("courseIds");
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        map.put("courses", studentService.getCourseByCourseIdsWithNameAndTeacher(courseIds));
        if (courseId == 0) {
            Integer courseId1 = (Integer) session.getAttribute("courseId");
            if (courseId1 != null && courseIds.contains(courseId1)) {
                courseId = courseId1;
            } else {
                courseId = courseIds.get(0);
            }
        } else {
            session.setAttribute("courseId", courseId);
        }
        //查找某个课程下已完成的作业
        //课程下的所有章节
        List<Integer> chapterIds = studentService.getChapterIdsByCourseId(courseId);
        if (chapterIds.size() == 0) {
            chapterIds.add(0);
        }
        String type = null;
        if ("".equals(taskType)) {
            type = (String) session.getAttribute("taskType");
        } else {
            type = taskType;
            session.setAttribute("taskType", type);
        }
        if (type == null || type.equals("online")) {
            //章节下所有的在线作业id
            List<Integer> onlineTaskIds = studentService.getOnlineTaskIdsByChapterIds(chapterIds);
            if (onlineTaskIds.size() == 0) {
                onlineTaskIds.add(0);
            }
            //根据在线作业id和学生学号，找到某课程下已完成的作业
            List<StudentOnlineTask> finishTask = studentService.getFinishOnlineTaskByStudentIdAndTaskIds(student.getId(), onlineTaskIds);
            Map<Integer, StudentOnlineTask> onlineTasksMap = new HashMap<>();
            List<Integer> finishIds = new ArrayList<>();
            for (StudentOnlineTask studentTask : finishTask) {
                onlineTasksMap.put(studentTask.getOnlineTaskId(), studentTask);
                finishIds.add(studentTask.getOnlineTaskId());
            }
            if (finishIds.size() != 0) {
                Map<Integer, Integer> totalMap = new HashMap<>();
                for (Integer id : finishIds) {
                    Integer total = studentService.getOnlineTaskTotalScore(id);
                    totalMap.put(id, total);
                }
                map.put("totalMap", totalMap);
            }
            if (finishIds.size() == 0) {
                finishIds.add(0);
            }
            map.put("onlineTasksMap", onlineTasksMap);
            map.put("onlineTasks", studentService.getAllOnlineTasksWithChapterByTaskIds(finishIds));
            if (type == null) {
                session.setAttribute("taskType", "online");
            }
        } else if (type.equals("offline")) {
            //章节下所有的离线作业id
            List<Integer> offlineTaskIds = studentService.getOfflineTaskIdsByChapterIds(chapterIds);
            if (offlineTaskIds.size() == 0) {
                offlineTaskIds.add(0);
            }
            //根据离线作业的id和学生学号，找到某课程下已完成的作业
            List<StudentOfflineTask> finishTask = studentService.getFinishOfflineTaskByStudentIdAndTaskIds(student.getId(), offlineTaskIds);
            Map<Integer, StudentOfflineTask> offlineTasksMap = new HashMap<>();
            List<Integer> finishIds = new ArrayList<>();
            for (StudentOfflineTask studentTask : finishTask) {
                offlineTasksMap.put(studentTask.getOfflineTaskId(), studentTask);
                finishIds.add(studentTask.getOfflineTaskId());
            }
            if (finishIds.size() == 0) {
                finishIds.add(0);
            }
            map.put("offlineTasksMap", offlineTasksMap);
            map.put("offlineTasks", studentService.getAllOfflineTasksWithChapterByTaskIds(finishIds));
        }
        map.put("courseId", courseId);
        return "student/searchStudentTaskInfo";
    }

    @RequestMapping("editStudentInfo")
    public String editStudentInfo(HttpSession session, Map<String, Object> map) {
        Student student = (Student) session.getAttribute("user");
        map.put("student", studentService.getStudentByStudentIdWithCollege(student.getId()));
        return "student/updateStudentInfo";
    }

    @ResponseBody
    @PostMapping("/updateStudentInfo")
    public RequestResult updateStudentInfo(@Validated({UpdateStudentSequence.class}) Student stu, BindingResult result, @RequestParam("code") String code, HttpSession session) {
        Student student = (Student) session.getAttribute("user");
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        student = studentService.getStudentByStudentId(student.getId());
        if (stu == null || student == null) {
            return RequestResult.failure("个人信息修改失败，请稍后再试");
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
        stu.setId(student.getId());
        stu.setCollegeId(student.getCollegeId());
        stu.setNumber(student.getNumber());
        if (student.equals(stu)) {
            return RequestResult.failure("未修改任何信息");
        }
        stu.setCollegeId(null);
        stu.setNumber(null);
        StudentExample studentExample = new StudentExample();
        Criteria criteria = studentExample.createCriteria();
        if (!student.getTelephone().equals(stu.getTelephone())) {
            //修改电话，查看电话是否已经存在
            criteria.andTelephoneEqualTo(stu.getTelephone());
            boolean exists = studentService.isExistsByExample(studentExample);
            if (exists) {
                return RequestResult.failure("该手机号已经存在");
            }
        } else {
            stu.setTelephone(null);
        }
        if (!student.getIdCardNo().equals(stu.getIdCardNo())) {
            studentExample.clear();
            criteria = studentExample.createCriteria();
            criteria.andIdCardNoEqualTo(stu.getIdCardNo());
            boolean exists = studentService.isExistsByExample(studentExample);
            if (exists) {
                return RequestResult.failure("该身份证号已经存在");
            }
        } else {
            stu.setIdCardNo(null);
        }
        if (!student.getAccount().equals(stu.getAccount())) {
            studentExample.clear();
            criteria = studentExample.createCriteria();
            criteria.andAccountEqualTo(stu.getAccount());
            boolean exists = studentService.isExistsByExample(studentExample);
            if (exists) {
                return RequestResult.failure("该账户已经存在");
            }
        } else {
            stu.setAccount(null);
        }
        if (!student.getEmail().equals(stu.getEmail())) {
            studentExample.clear();
            criteria = studentExample.createCriteria();
            criteria.andEmailEqualTo(stu.getEmail());
            boolean exists = studentService.isExistsByExample(studentExample);
            if (exists) {
                return RequestResult.failure("该邮箱地址已经存在");
            }
        } else {
            stu.setEmail(null);
        }
        if (student.getName().equals(stu.getName())) {
            stu.setName(null);
        }
        if (student.getPassword().equals(stu.getPassword())) {
            stu.setPassword(null);
        }
        if (student.getGender().equals(stu.getGender())) {
            stu.setGender(null);
        }
        boolean b = studentService.updateStudentByPrimaryKeySelective(stu);
        if (!b) {
            return RequestResult.failure("更新信息失败，请稍后再试");
        }
        session.removeAttribute("UpdateInfoCode");
        return RequestResult.success();
    }

    @RequestMapping("/viewStudentInfo")
    public String viewStudentInfo(Map<String, Object> map, HttpSession session) {
        map.put("student", studentService.getStudentByStudentIdWithCollege(26));
        return "student/viewStudentInfo";
    }

    @ResponseBody
    @PostMapping("/logout")
    public RequestResult logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        /*Student student = (Student) session.getAttribute("user");
        ServletContext application = request.getServletContext();
        List<Integer> studentLogin = (List<Integer>) application.getAttribute("studentLogin");
        studentLogin.remove(student.getId());*/
        session.invalidate();
        return RequestResult.success();
    }
}
