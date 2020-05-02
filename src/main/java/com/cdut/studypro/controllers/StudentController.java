package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.beans.StudentExample.*;
import com.cdut.studypro.exceptions.FileIsNotExistException;
import com.cdut.studypro.services.StudentService;
import com.cdut.studypro.utils.MD5Util;
import com.cdut.studypro.utils.RequestResult;
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
import org.springframework.web.bind.annotation.*;

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

    private static final String APIURL = "https://sms_developer.zhenzikj.com";
    private static final String APPID = "105088";
    private static final String APPSERCRET = "bd4deaea-c9df-4076-a7b2-0f540c1c8e0a";

    @Autowired
    private StudentService studentService;

    @ResponseBody
    @PostMapping(value = "/login")
    public RequestResult login(@RequestBody Map<String, String> map, HttpSession session) {
        System.out.println(map);
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
                //登录成功将学生信息保存在session中，并保存角色
                session.setAttribute("role","student");
                session.setAttribute("user", student);
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
        if (b) {
            //注册成功，删除session中的验证码
            request.getSession().removeAttribute("RegisterCode");
            return RequestResult.success();
        }
        //删除session中的验证码
        request.getSession().removeAttribute("RegisterCode");
        return RequestResult.failure("注册失败，请稍后重试");
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


    //异常处理（运行时异常）
    @ExceptionHandler(RuntimeException.class)
    public String handRuntimeException(RuntimeException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return "error";
    }

    @ExceptionHandler(FileIsNotExistException.class)
    public String handFileIsNotExistException(FileIsNotExistException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return "error";
    }

    @RequestMapping("/searchCourseInfo")
    public String searchCourseInfo(Map<String, Object> map, HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        //根据学生所属的学院，查询该学院所有的课程
        /*Student student = (Student) session.getAttribute("user");
        map.put("courses", studentService.getAllCourseWithBLOBsAndTeacherByCollegeId(student.getCollegeId()));*/

        CourseExample courseExample = (CourseExample) session.getAttribute("courseExample");
        if (courseExample == null) {
            courseExample = new CourseExample();
            CourseExample.Criteria criteria = courseExample.createCriteria();
            criteria.andCollegeIdEqualTo(8);
            courseExample.setOrderByClause("CONVERT(name using gbk) asc");
        }
        CourseExample.Criteria criteria = courseExample.getOredCriteria().get(0);
        //根据student的id获取到学生已经加入的课程id
        List<Integer> courseIds = studentService.getJoinCourseIdByStudentId(26);
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        criteria.andIdNotIn(courseIds);
        PageHelper.startPage(pageNum, 10);
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
        criteria.andCollegeIdEqualTo(8);
        courseExample.setOrderByClause("CONVERT(name using gbk) asc");
        session.setAttribute("courseExample", courseExample);
        session.setAttribute("courseQueryCriteria", map);
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/joinCourse")
    public RequestResult joinCourse(@RequestParam("id") Integer id, HttpSession session) {
        /*Student student = (Student) session.getAttribute("user");
        Integer studentId = student.getId();*/
        Collect collect = new Collect();
        collect.setCourseId(id);
        collect.setStudentId(26);
        boolean success = studentService.joinCourse(collect);
        if (!success) {
            return RequestResult.failure("加入课程失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/joinCourseBatch")
    public RequestResult joinCourseBatch(@RequestParam("ids") String id, HttpSession session) {
        /*Student student = (Student) session.getAttribute("user");
        Integer studentId = student.getId();*/
        List<Collect> collects = new ArrayList<>();
        String[] ids = id.split("-");
        for (String courseId : ids) {
            Collect collect = new Collect();
            collect.setCourseId(Integer.parseInt(courseId));
            collect.setStudentId(26);
            collects.add(collect);
        }
        boolean success = studentService.joinCourseBatch(collects);
        return RequestResult.success();
    }

    @RequestMapping("/searchMyCourseInfo")
    public String searchMyCourseInfo(HttpSession session, Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Student student = (Student) session.getAttribute("user");
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        //根据student的id获取到学生已经加入的课程id
        List<Integer> courseIds = studentService.getJoinCourseIdByStudentId(26);
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        criteria.andIdIn(courseIds);
        courseExample.setOrderByClause("CONVERT(name using gbk) asc");
        PageHelper.startPage(pageNum, 10);
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
        boolean success = studentService.removeCourseBatch(courseIds, 26);
        if (!success) {
            return RequestResult.failure("批量移除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/removeCourse")
    public RequestResult removeCourse(@RequestParam("id") Integer id, HttpSession session) {
        Student student = (Student) session.getAttribute("user");
        if (id == null) {
            return RequestResult.failure("课程移除失败，请稍后再试");
        }
        boolean success = studentService.removeCourse(id, 26);
        if (!success) {
            return RequestResult.failure("课程移除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchChapter")
    public String searchChapter(Map<String, Object> map, HttpSession session, @RequestParam("courseId") Integer courseId, @RequestParam("pageNum") Integer pageNum) {
        Student student = (Student) session.getAttribute("user");
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
    public ResponseEntity<byte[]> downloadCourseFile(@PathVariable("id") Integer id, HttpServletRequest request) throws IOException {
        String path = request.getServletContext().getRealPath("/file/");
        CourseFile courseFile = studentService.getCourseFileById(id);
        String fileName = courseFile.getPath().substring(courseFile.getPath().indexOf("_") + 1);
        File file = new File(path + courseFile.getPath());
        if (!file.exists()) {
            throw new FileIsNotExistException("下载失败，当前文件不存在");
        }
        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        // 转码，避免文件名显示不出中文
        String tempStr = UUID.randomUUID().toString();
        fileName = fileName.replace(" ", tempStr);
        fileName = URLEncoder.encode(fileName, "UTF-8");
        fileName = fileName.replace(tempStr, " ");
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
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
        //根据student的id获取到学生已经加入的课程id
        List<Integer> courseIds = studentService.getJoinCourseIdByStudentId(26);
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        if (courseId == 0) {
            courseId = courseIds.get(0);
        }
        criteria.andIdEqualTo(courseId);
        map.put("courseId", courseId);
        map.put("course", studentService.getCourseByCourseIdsWithNameAndTeacher(courseIds));
        map.put("courses", studentService.getCourseWithChapterAndVideoByExample(courseExample));
        return "student/searchCourseVideoInfo";
    }

    @RequestMapping("/downloadCourseVideo/{id}")
    public ResponseEntity<byte[]> downloadCourseVideo(@PathVariable("id") Integer id, HttpServletRequest request) throws IOException {
        String path = request.getServletContext().getRealPath("/video/");
        CourseVideo courseVideo = studentService.getCourseVideoById(id);
        String fileName = courseVideo.getPath().substring(courseVideo.getPath().indexOf("_") + 1);
        File file = new File(path + courseVideo.getPath());
        if (!file.exists()) {
            throw new FileIsNotExistException("下载失败，当前文件不存在");
        }
        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        // 转码，避免文件名显示不出中文
        String tempStr = UUID.randomUUID().toString();
        //替换空格
        fileName = fileName.replace(" ", tempStr);
        fileName = URLEncoder.encode(fileName, "UTF-8");
        fileName = fileName.replace(tempStr, " ");
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    @RequestMapping("/searchCourseFileInfo")
    public String searchCourseFileInfo(HttpSession session, Map<String, Object> map, @RequestParam(value = "courseId", defaultValue = "0") Integer courseId) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        //根据student的id获取到学生已经加入的课程id
        List<Integer> courseIds = studentService.getJoinCourseIdByStudentId(26);
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        if (courseId == 0) {
            courseId = courseIds.get(0);
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

        //根据student的id获取到学生已经加入的课程id
        List<Integer> courseIds = studentService.getJoinCourseIdByStudentId(26);
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        if (courseId == 0) {
            courseId = courseIds.get(0);
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
            parameterMap.put("content", null);
            parameterMap.put("minDate", null);
            parameterMap.put("maxDate", null);
            parameterMap.put("name", null);
        }
        parameterMap.put("id", id);
        PageHelper.startPage(pageNum, 10);
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
        if (id == null) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        if (content == null) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        if ("".equals(content)) {
            return RequestResult.failure("回复内容为空，请重新输入");
        }
        Student student = (Student) session.getAttribute("user");
        DiscussPost discussPost = new DiscussPost();
        discussPost.setRecordTime(new Date());
        discussPost.setStudentId(26);
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
    public String searchTaskInfo(Map<String, Object> map, HttpSession session, @RequestParam(value = "courseId", defaultValue = "0") Integer courseId) {
        //根据student的id获取到学生已经加入的课程id
        List<Integer> courseIds = studentService.getJoinCourseIdByStudentId(26);
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        if (courseId == 0) {
            courseId = courseIds.get(0);
        }
        map.put("course", studentService.getCourseWithChapterAndTeacherByCourseId(courseId));
        map.put("courses", studentService.getCourseByCourseIdsWithNameAndTeacher(courseIds));
        map.put("courseId", courseId);
        return "student/searchCourseChapterInfo";
    }

    @RequestMapping("searchTaskInfo/{id}")
    public String searchTaskInfo(@PathVariable("id") Integer id, Map<String, Object> map, HttpSession session, @RequestParam("courseId") Integer courseId) {
        Student student = (Student) session.getAttribute("user");
        List<OnlineTask> tasks = studentService.getAllTasksByChapterId(id);
        map.put("tasks", tasks);
        List<Integer> taskIds = new ArrayList<>();
        for (OnlineTask task : tasks) {
            taskIds.add(task.getId());
        }
        if (taskIds.size() == 0) {
            taskIds.add(0);
        }
        List<StudentOnlineTask> finishTask = studentService.getFinishTaskByStudentIdAndTaskIds(26, taskIds);
        Map<Integer, StudentOnlineTask> taskMap = new HashMap<>();
        for (StudentOnlineTask studentTask : finishTask) {
            taskMap.put(studentTask.getOnlineTaskId(), studentTask);
        }
        map.put("taskMap", taskMap);
        map.put("chapterId", id);
        map.put("courseId", courseId);
        return "student/searchTaskInfo";
    }

    @RequestMapping("/enterTask/{id}")
    public String enterTask(@PathVariable("id") Integer id,
                            Map<String, Object> map,
                            HttpSession session,
                            @RequestParam(value = "flag", defaultValue = "0") Integer flag,
                            @RequestParam(value = "chapterId", defaultValue = "0") Integer chapterId,
                            @RequestParam(value = "courseId", defaultValue = "0") Integer courseId) {
        //map.put("questions", studentService.getTaskQuestionsWithTitleAndItemByTaskId(id));
        session.setAttribute("questions", studentService.getTaskQuestionsByTaskId(id));
        map.put("finish", studentService.isTaskFinish(id));
        map.put("taskId", id);
        map.put("chapterId", chapterId);
        map.put("flag", flag);
        map.put("courseId", courseId);
        return "student/enterTask";
    }

    @ResponseBody
    @PostMapping("/saveStudentTask/{id}")
    public RequestResult saveStudentTask(@PathVariable("id") Integer taskId,
                                         @RequestParam Map<Integer, String> map,
                                         @RequestParam(value = "chapterId", defaultValue = "0") Integer chapterId,
                                         HttpSession session) {
        //学生提交之前先判断该学生是否已经完成改作业
        Student student = (Student) session.getAttribute("user");
        if (studentService.isTaskFinish(taskId)) {
            return RequestResult.failure("你已经完成了该作业，请勿重复提交");
        }
        if (map.containsValue("0")) {
            return RequestResult.failure("还有未做的题目，请完成后再提交");
        }
        //提交的答案
        List<String> answers = new ArrayList<>(map.values());
        //答案对应题目的id
        //List<Integer> ids = new ArrayList<>(map.keySet());
        //从数据库中获取到的题目
        List<OnlineTaskQuestion> questions = (List<OnlineTaskQuestion>) session.getAttribute("questions");
        if (questions == null) {
            return RequestResult.failure("作业提交失败，请稍后再试");
        }
        if (answers.size() != questions.size()) {
            return RequestResult.failure("作业提交失败，请稍后再试");
        }
        int score = 0;
        //将成绩和错题id记录在result中
        List<Integer> result = new ArrayList<>(answers.size() + 1);
        int len = questions.size();
        for (int i = 0; i < len; i++) {
            OnlineTaskQuestion question = questions.get(i);
            if (answers.get(i).equals(question.getAnswer())) {
                score += questions.get(i).getScore();
            } else {
                result.add(question.getId());
            }
        }

        StudentOnlineTask studentTask = new StudentOnlineTask();
        studentTask.setRecordTime(new Date());
        studentTask.setScore(score);
        studentTask.setStudentId(26);
        studentTask.setOnlineTaskId(taskId);

        boolean success = studentService.insertStudentTaskSelective(studentTask);
        if (!success) {
            return RequestResult.failure("作业提交失败，请稍后再试");
        }
        result.add(score);
        RequestResult requestResult = RequestResult.success();
        requestResult.setData(result);
        return requestResult;
    }

    @RequestMapping("/searchStudentTaskInfo")
    public String searchStudentTaskInfo(HttpSession session, Map<String, Object> map, @RequestParam(value = "courseId", defaultValue = "0") Integer courseId) {
        Student student = (Student) session.getAttribute("user");
        //根据student的id获取到学生已经加入的课程id
        List<Integer> courseIds = studentService.getJoinCourseIdByStudentId(26);
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        map.put("courses", studentService.getCourseByCourseIdsWithNameAndTeacher(courseIds));
        if (courseId == 0) {
            courseId = courseIds.get(0);
        }
        //查找某个课程下已完成的作业
        List<Integer> chapterIds = studentService.getChapterIdsByCourseId(courseId);
        if (chapterIds.size() == 0) {
            chapterIds.add(0);
        }
        //某课程下的所有作业id
        List<Integer> taskIds = studentService.getTaskIdsByChapterIds(chapterIds);
        if (taskIds.size() == 0) {
            taskIds.add(0);
        }
        //根据某课程的作业id和学生学号，找到某课程下已完成的作业
        List<StudentOnlineTask> finishTask = studentService.getFinishTaskByStudentIdAndTaskIds(26, taskIds);
        Map<Integer, StudentOnlineTask> taskMap = new HashMap<>();
        List<Integer> finishIds = new ArrayList<>();
        for (StudentOnlineTask studentTask : finishTask) {
            taskMap.put(studentTask.getOnlineTaskId(), studentTask);
            finishIds.add(studentTask.getOnlineTaskId());
        }
        if (finishIds.size() == 0) {
            finishIds.add(0);
        }
        map.put("taskMap", taskMap);
        map.put("tasks", studentService.getAllTasksWithChapterByTaskIds(finishIds));
        map.put("courseId", courseId);
        return "student/searchStudentTaskInfo";
    }

    @RequestMapping("editStudentInfo")
    public String editStudentInfo(HttpSession session, Map<String, Object> map) {
        //Student student = (Student) session.getAttribute("user");
        map.put("student", studentService.getStudentByStudentIdWithCollege(26));
        return "student/updateStudentInfo";
    }

    @ResponseBody
    @PostMapping("/updateStudentInfo")
    public RequestResult updateStudentInfo(Student stu, @RequestParam("code") String code, HttpSession session) {

        //Student student = (Student) session.getAttribute("user");

        Student student = studentService.getStudentByStudentId(26);
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
        stu.setId(26);
        stu.setCollegeId(student.getCollegeId());
        stu.setNumber(student.getNumber());
        stu.setPassword(stu.getPassword().trim());
        stu.setName(stu.getName().trim());
        stu.setEmail(stu.getEmail().trim());
        stu.setAccount(stu.getAccount().trim());
        stu.setIdCardNo(stu.getIdCardNo().trim());
        stu.setTelephone(stu.getTelephone().trim());
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
            stu.setIdCardNo(null);
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
    public RequestResult logout(HttpSession session) {
        session.invalidate();
        return RequestResult.success();
    }
}
