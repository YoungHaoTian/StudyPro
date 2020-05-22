package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.exceptions.DownloadException;
import com.cdut.studypro.exceptions.MaxUploadSizeExceedException;
import com.cdut.studypro.services.TeacherService;
import com.cdut.studypro.utils.FileUtil;
import com.cdut.studypro.utils.MD5Util;
import com.cdut.studypro.utils.POIUtil;
import com.cdut.studypro.utils.RequestResult;
import com.cdut.studypro.validates.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
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
@RequestMapping("/teacher")
public class TeacherController {
    //单位：MB
    private final static long VIDEO_MAX_SIZE = 512;
    private final static long FILE_MAX_SIZE = 100;

    @Autowired
    private TeacherService teacherService;

    //运行时异常处理
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

    //操作文件时异常处理
    @ExceptionHandler(IOException.class)
    public String handIOException(IOException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return "error";
    }

    //下载时异常处理
    @ExceptionHandler(DownloadException.class)
    public String handDownloadException(DownloadException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return "error";
    }

    //类型转换异常处理
    @ExceptionHandler(ParseException.class)
    public String handParseException(ParseException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return "error";
    }

    @ResponseBody
    @PostMapping(value = "/login")
    public RequestResult login(@RequestBody Map<String, String> map, HttpServletRequest request) {
        HttpSession session = request.getSession();
        /*ServletContext application = request.getServletContext();
        List<Integer> teacherLogin = (List<Integer>) application.getAttribute("teacherLogin");*/
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
        String message = null;
        if ("phone".equals(map.get("type"))) {
            criteria.andTelephoneEqualTo(map.get("username"));
            message = "手机号不存在，请重新输入";
        } else if ("email".equals(map.get("type"))) {
            criteria.andEmailEqualTo(map.get("username"));
            message = "邮箱不存在，请重新输入";
        } else if ("account".equals(map.get("type"))) {
            criteria.andAccountEqualTo(map.get("username"));
            message = "帐号不存在，请重新输入";
        }
        if (message == null) {
            return RequestResult.failure("登录失败，请稍后重试");
        }
        List<Teacher> teachers = teacherService.selectTeacherByExample(teacherExample);
        if (teachers != null && teachers.size() > 0) {
            if (teachers.size() != 1) {
                return RequestResult.failure("登录异常，请选择其他方式登录");
            }
            Teacher teacher = teachers.get(0);
            String password = map.get("password");
            if (password.equals(MD5Util.stringToMD5(teacher.getPassword()))) {
                //同时只能登陆一个账户
                Object user = session.getAttribute("user");
                if (user != null) {
                    return RequestResult.failure("同时只能登陆一个账户");
                }
                //先查看当前教师是否已经登录
                /*if (teacherLogin.contains(teacher.getId())) {
                    return RequestResult.failure("请勿重复登录");
                }*/
                //登录成功后将教师信息保存在session中
                Teacher teacher1 = new Teacher();
                teacher1.setId(teacher.getId());
                session.setAttribute("role", "teacher");
                session.setAttribute("user", teacher1);
                //登录成功之后将该教师id记录到teacherLogin中
                //teacherLogin.add(teacher.getId());
                return RequestResult.success();
            } else {
                return RequestResult.failure("密码错误，请稍后重试");
            }
        }
        return RequestResult.failure(message);
    }

    @ResponseBody
    @PostMapping(value = "/findPassword")
    public RequestResult findPassword(@RequestBody Map<String, String> map, HttpSession session) {
        String code = map.get("code");
        String phone = map.get("phone");
        String password = map.get("password");
        //1、先验证验证码是否正确
        JSONObject json = (JSONObject) session.getAttribute("ForgetCode");
        if (json == null) {
            return RequestResult.failure("密码找回失败，还未获取验证码");
        }
        if (!json.getString("verifyCode").equals(code)) {
            return RequestResult.failure("验证码错误，请稍后重试");
        }
        if ((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5) {
            //删除session中的验证码
            session.removeAttribute("ForgetCode");
            return RequestResult.failure("验证码已过期，请重新获取");
        }
        //2、查询该手机号是否存在
        Integer id = teacherService.getIdByTelephone(phone);
        //不存在
        if (id == null) {
            return RequestResult.failure("该手机号不存在，请稍后再试");
        }
        //存在，修老师密码
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setPassword(password);
        boolean b = teacherService.updateTeacherByPrimaryKeySelective(teacher);
        if (!b) {
            return RequestResult.failure("密码找回失败，请稍后再试");
        }
        //删除session中的验证码
        session.removeAttribute("ForgetCode");
        return RequestResult.success();
    }

    @RequestMapping("/teacherIndex")
    public String teacherIndex() {
        return "teacher/teacherIndex";
    }

    @RequestMapping("/createCourseVideo")
    public String createCourseVideo(Map<String, Object> map, HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(teacher.getId()));
        return "teacher/createCourseVideo";
    }

    @ResponseBody
    @PostMapping("/saveCourseVideo")
    public RequestResult saveCourseVideo(@RequestParam(value = "file") MultipartFile file, @RequestParam("chapterId") Integer chapterId, @RequestParam("courseId") Integer courseId, HttpServletRequest request) {
        long size = file.getSize();
        if (courseId == 0 || chapterId == 0) {
            return RequestResult.failure("你还没有选择视频所属课程及章节");
        }
        if (!(file.getOriginalFilename().endsWith(".mp4") || file.getOriginalFilename().endsWith(".avi"))) {
            return RequestResult.failure("请上传mp4或avi格式的视频文件");
        }
        if (size > VIDEO_MAX_SIZE * 1024 * 1024) {
            throw new MaxUploadSizeExceedException("视频上传最大大小为：", VIDEO_MAX_SIZE);
        }
        String path = request.getServletContext().getRealPath("/video/");
        //System.out.println(request.getServletPath());//请求路径
        //System.out.println(request.getContextPath());//项目路径
        //视频存放路径：/video/课程id/章节id/上传时间_fileName
        String fileDir = courseId + "\\" + chapterId + "\\";
        path += fileDir;
        File file1 = new File(path);
        Date date = new Date();
        String fileName = date.getTime() + "_" + file.getOriginalFilename();
        if (!file1.exists()) {//文件夹不存在，先创建出对应的文件夹
            boolean mkdir = file1.mkdirs();
            if (!mkdir) {
                return RequestResult.failure("视频上传失败，请稍后再试");
            }
        }
        try {
            file.transferTo(new File(file1, fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return RequestResult.failure("视频上传失败，请稍后再试");
        }
        CourseVideo courseVideo = new CourseVideo();
        courseVideo.setChapterId(chapterId);
        courseVideo.setPath(fileDir + fileName);
        courseVideo.setRecordTime(date);
        boolean b = teacherService.saveCourseVideo(courseVideo);
        if (!b) {
            return RequestResult.failure("视频上传失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchCourseVideo")
    public String searchCourseVideo(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, Map<String, Object> map) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        CourseVideoExample courseVideoExample = (CourseVideoExample) session.getAttribute("courseVideoExample");
        if (courseVideoExample == null) {
            courseVideoExample = new CourseVideoExample();
            List<Integer> ids = teacherService.getCourseIdsByTeacherId(teacher.getId());
            if (ids.size() == 0) {
                ids.add(0);
            }
            ids = teacherService.getChapterIdsByCourseIds(ids);
            if (ids.size() == 0) {
                ids.add(0);
            }
            ids = teacherService.getVideoIdsByChapterIds(ids);
            if (ids.size() == 0) {
                ids.add(0);
            }
            CourseVideoExample.Criteria criteria = courseVideoExample.createCriteria();
            criteria.andIdIn(ids);
            courseVideoExample.setOrderByClause("record_time desc");
        }
        PageHelper.startPage(pageNum, 20);
        List<CourseVideo> videos = teacherService.searchCourseVideoByExampleWithCourseChapter(courseVideoExample);
        PageInfo<CourseVideo> page = new PageInfo(videos, 10);
        map.put("pageInfo", page);
        map.put("colleges", teacherService.getAllCollegesByTeacherId(teacher.getId()));
        return "teacher/searchCourseVideo";
    }

    @ResponseBody
    @PostMapping("/deleteCourseVideo")
    public RequestResult deleteCourseVideo(@RequestParam("id") Integer id, HttpServletRequest request) {
        if (id == null) {
            return RequestResult.failure("课程视频删除失败，请稍后再试");
        }
        CourseVideo courseVideo = teacherService.getCourseVideoById(id);
        if (courseVideo == null) {
            return RequestResult.failure("课程视频删除失败，请稍后再试");
        }
        //先删除数据库中的记录，再删除课程视频
        boolean success = teacherService.deleteCourseVideoById(id);
        if (!success) {
            return RequestResult.failure("课程视频删除失败，请稍后再试");
        }
        //在文件目录中删除文件
        String path = request.getServletContext().getRealPath("/video/") + courseVideo.getPath();
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        return RequestResult.success();
    }


    @ResponseBody
    @PostMapping("/deleteCourseVideoBatch")
    public RequestResult deleteCourseVideoBatch(@RequestParam("ids") String ids, HttpServletRequest request) {
        if (ids == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] id = ids.split("-");
        List<Integer> videoIds = new ArrayList<>();
        for (String s : id) {
            videoIds.add(Integer.parseInt(s));
        }
        List<CourseVideo> videos = teacherService.getCourseVideoByIds(videoIds);
        if (videos == null || videos.size() == 0 || videos.size() != videoIds.size()) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        videoIds.clear();
        for (CourseVideo video : videos) {
            videoIds.add(video.getId());
        }
        boolean success = teacherService.deleteCourseVideoByIdBatch(videoIds);
        if (!success) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        //在文件目录中删除文件
        for (CourseVideo video : videos) {
            String path = request.getServletContext().getRealPath("/video/") + video.getPath();
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        return RequestResult.success();
    }


    @ResponseBody
    @PostMapping("/searchCourseVideoByTerm")
    public RequestResult searchCourseVideoByTerm(@RequestParam("name") String name, @RequestParam("minTime") String minTime, @RequestParam("maxTime") String maxTime, @RequestParam("collegeId") Integer collegeId, HttpSession session) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> map = new HashMap<>();
            CourseVideoExample courseVideoExample = new CourseVideoExample();
            CourseVideoExample.Criteria criteria = courseVideoExample.createCriteria();
            Teacher teacher = (Teacher) session.getAttribute("user");
            map.put("name", null);
            map.put("minTime", null);
            map.put("maxTime", null);
            map.put("collegeId", null);
            if (!"".equals(name.trim())) {
                CourseExample courseExample = new CourseExample();
                CourseExample.Criteria courseExampleCriteria = courseExample.createCriteria();
                courseExampleCriteria.andNameLike("%" + name.trim() + "%").andTeacherIdEqualTo(teacher.getId());
                List<Integer> ids = teacherService.getCourseIdsByExample(courseExample);
                if (ids.size() == 0) {
                    ids.add(0);
                }
                CourseChapterExample courseChapterExample = new CourseChapterExample();
                CourseChapterExample.Criteria courseChapterExampleCriteria = courseChapterExample.createCriteria();
                courseChapterExampleCriteria.andCourseIdIn(ids);
                ids = teacherService.getChapterIdsByExample(courseChapterExample);
                if (ids.size() == 0) {
                    ids.add(0);
                }
                criteria.andChapterIdIn(ids);
                map.put("name", name);
            }
            if (collegeId != 0) {
                CourseExample courseExample = new CourseExample();
                CourseExample.Criteria courseExampleCriteria = courseExample.createCriteria();
                courseExampleCriteria.andCollegeIdEqualTo(collegeId).andTeacherIdEqualTo(teacher.getId());
                List<Integer> ids = teacherService.getCourseIdsByExample(courseExample);
                if (ids.size() == 0) {
                    ids.add(0);
                }
                CourseChapterExample courseChapterExample = new CourseChapterExample();
                CourseChapterExample.Criteria courseChapterExampleCriteria = courseChapterExample.createCriteria();
                courseChapterExampleCriteria.andCourseIdIn(ids);
                ids = teacherService.getChapterIdsByExample(courseChapterExample);
                if (ids.size() == 0) {
                    ids.add(0);
                }
                criteria.andChapterIdIn(ids);
                map.put("collegeId", collegeId);
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
            courseVideoExample.setOrderByClause("record_time desc");
            session.setAttribute("courseVideoExample", courseVideoExample);
            session.setAttribute("courseVideoQueryCriteria", map);
        } catch (Exception e) {
            return RequestResult.failure("查询失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/updateCourseVideo")
    public RequestResult updateCourseVideo(@RequestParam(value = "file") MultipartFile file, @RequestParam("courseVideoId") Integer courseVideoId, HttpServletRequest request) {
        if (file == null) {
            return RequestResult.failure("视频更新失败，请稍后再试");
        }
        if (!(file.getOriginalFilename().endsWith(".mp4") || file.getOriginalFilename().endsWith(".avi"))) {
            return RequestResult.failure("请上传mp4或avi格式的视频文件");
        }
        long size = file.getSize();
        if (size > VIDEO_MAX_SIZE * 1024 * 1024) {
            throw new MaxUploadSizeExceedException("视频上传最大大小为：", VIDEO_MAX_SIZE);
        }
        CourseVideo video = teacherService.getCourseVideoById(courseVideoId);
        if (video == null) {
            return RequestResult.failure("视频更新失败，请稍后再试");
        }
        String basePath = request.getServletContext().getRealPath("/video/");
        File baseFile = new File(basePath);
        if (!baseFile.exists()) {
            return RequestResult.failure("视频更新失败，请稍后再试");
        }
        String dirPath = video.getPath().substring(0, video.getPath().lastIndexOf("\\") + 1);
        //删除原来的文件
        File oldFile = new File(baseFile, video.getPath());
        if (oldFile.exists()) {
            boolean delete = oldFile.delete();
            if (!delete) {
                return RequestResult.failure("视频更新失败，请稍后再试");
            }
        }
        Date date = new Date();
        String fileName = date.getTime() + "_" + file.getOriginalFilename();
        //写入新的文件
        try {
            file.transferTo(new File(baseFile, dirPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return RequestResult.failure("视频更新失败，请稍后再试");
        }
        //修改文件信息
        video.setPath(dirPath + fileName);
        video.setRecordTime(date);
        video.setRecordTime(null);
        video.setChapterId(null);
        boolean success = teacherService.updateCourseVideoByPrimaryKeySelective(video);
        if (!success) {
            return RequestResult.failure("视频更新失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/viewCourseVideo/{id}")
    public String viewCourseVideo(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        CourseVideo video = teacherService.getCourseVideoById(id);
        map.put("video", video);
        map.put("pageNum", pageNum);
        return "teacher/viewCourseVideo";
    }

    @RequestMapping("/createCourseFile")
    public String createCourseFile(Map<String, Object> map, HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(teacher.getId()));
        return "teacher/createCourseFile";
    }

    @ResponseBody
    @PostMapping("/saveCourseFile")
    public RequestResult saveCourseFile(@RequestParam(value = "file") MultipartFile file, @RequestParam("chapterId") Integer chapterId, @RequestParam("courseId") Integer courseId, HttpServletRequest request) {
        if (courseId == 0 || chapterId == 0) {
            return RequestResult.failure("你还没有选择文档所属课程及章节");
        }
        long size = file.getSize();
        if (size > FILE_MAX_SIZE * 1024 * 1024) {
            throw new MaxUploadSizeExceedException(FILE_MAX_SIZE);
        }
        String path = request.getServletContext().getRealPath("/file/");
        //文档存放路径：//课程id/章节id/上传时间_fileName
        String fileDir = courseId + "\\" + chapterId + "\\";
        path += fileDir;
        File file1 = new File(path);
        Date date = new Date();
        String fileName = date.getTime() + "_" + file.getOriginalFilename();
        if (!file1.exists()) {
            boolean mkdir = file1.mkdirs();
            if (!mkdir) {
                return RequestResult.failure("文档上传失败，请稍后再试");
            }
        }
        try {
            file.transferTo(new File(file1, fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return RequestResult.failure("文档上传失败，请稍后再试");
        }
        CourseFile courseFile = new CourseFile();
        courseFile.setChapterId(chapterId);
        courseFile.setPath(fileDir + fileName);
        courseFile.setRecordTime(date);
        boolean b = teacherService.saveCourseFile(courseFile);
        if (!b) {
            return RequestResult.failure("文档上传失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchCourseFile")
    public String searchCourseFile(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, Map<String, Object> map) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        CourseFileExample courseFileExample = (CourseFileExample) session.getAttribute("courseFileExample");
        if (courseFileExample == null) {
            courseFileExample = new CourseFileExample();
            List<Integer> ids = teacherService.getCourseIdsByTeacherId(teacher.getId());
            if (ids.size() == 0) {
                ids.add(0);
            }
            ids = teacherService.getChapterIdsByCourseIds(ids);
            if (ids.size() == 0) {
                ids.add(0);
            }
            ids = teacherService.getFileIdsByChapterIds(ids);
            if (ids.size() == 0) {
                ids.add(0);
            }
            CourseFileExample.Criteria criteria = courseFileExample.createCriteria();
            criteria.andIdIn(ids);
            courseFileExample.setOrderByClause("record_time desc");
        }
        PageHelper.startPage(pageNum, 20);
        List<CourseFile> files = teacherService.searchCourseFileByExampleWithCourseChapter(courseFileExample);
        PageInfo<CourseFile> page = new PageInfo(files, 10);
        map.put("pageInfo", page);
        map.put("colleges", teacherService.getAllCollegesByTeacherId(teacher.getId()));
        return "teacher/searchCourseFile";
    }

    @ResponseBody
    @PostMapping("/deleteCourseFile")
    public RequestResult deleteCourseFile(@RequestParam("id") Integer id, HttpServletRequest request) {
        if (id == null) {
            return RequestResult.failure("课程文档删除失败，请稍后再试");
        }
        CourseFile courseFile = teacherService.getCourseFileById(id);
        if (courseFile == null) {
            return RequestResult.failure("课程文档删除失败，请稍后再试");
        }
        //删除课程视频在数据库中的记录
        boolean success = teacherService.deleteCourseFileById(id);
        if (!success) {
            return RequestResult.failure("课程文档删除失败，请稍后再试");
        }
        //在文件目录中删除文件
        String path = request.getServletContext().getRealPath("/file/") + courseFile.getPath();
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteCourseFileBatch")
    public RequestResult deleteCourseFileBatch(@RequestParam("ids") String ids, HttpServletRequest request) {
        if (ids == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] id = ids.split("-");
        List<Integer> fileIds = new ArrayList<>();
        for (String s : id) {
            fileIds.add(Integer.parseInt(s));
        }
        List<CourseFile> files = teacherService.getCourseFileByIds(fileIds);
        if (files == null || files.size() == 0 || files.size() != fileIds.size()) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        fileIds.clear();
        for (CourseFile file : files) {
            fileIds.add(file.getId());
        }
        boolean success = teacherService.deleteCourseFileByIdBatch(fileIds);
        if (!success) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        //在文件目录中删除文件
        for (CourseFile file : files) {
            String path = request.getServletContext().getRealPath("/file/") + file.getPath();
            File f = new File(path);
            if (f.exists()) {
                f.delete();
            }
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/searchCourseFileByTerm")
    public RequestResult searchCourseFileByTerm(@RequestParam("name") String name, @RequestParam("collegeId") Integer collegeId, @RequestParam("minTime") String minTime, @RequestParam("maxTime") String maxTime, HttpSession session) {
        try {
            Map<String, Object> map = new HashMap<>();
            CourseFileExample courseFileExample = new CourseFileExample();
            CourseFileExample.Criteria criteria = courseFileExample.createCriteria();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Teacher teacher = (Teacher) session.getAttribute("user");
            map.put("name", null);
            map.put("collegeId", null);
            map.put("minTime", null);
            map.put("maxTime", null);
            if (!"".equals(name.trim())) {
                CourseExample courseExample = new CourseExample();
                CourseExample.Criteria courseExampleCriteria = courseExample.createCriteria();
                courseExampleCriteria.andNameLike("%" + name.trim() + "%").andTeacherIdEqualTo(teacher.getId());
                List<Integer> ids = teacherService.getCourseIdsByExample(courseExample);
                if (ids.size() == 0) {
                    ids.add(0);
                }
                CourseChapterExample courseChapterExample = new CourseChapterExample();
                CourseChapterExample.Criteria courseChapterExampleCriteria = courseChapterExample.createCriteria();
                courseChapterExampleCriteria.andCourseIdIn(ids);
                ids = teacherService.getChapterIdsByExample(courseChapterExample);
                if (ids.size() == 0) {
                    ids.add(0);
                }
                criteria.andChapterIdIn(ids);
                map.put("name", name);
            }
            if (collegeId != 0) {
                CourseExample courseExample = new CourseExample();
                CourseExample.Criteria courseExampleCriteria = courseExample.createCriteria();
                courseExampleCriteria.andCollegeIdEqualTo(collegeId).andTeacherIdEqualTo(teacher.getId());
                List<Integer> ids = teacherService.getCourseIdsByExample(courseExample);
                if (ids.size() == 0) {
                    ids.add(0);
                }
                CourseChapterExample courseChapterExample = new CourseChapterExample();
                CourseChapterExample.Criteria courseChapterExampleCriteria = courseChapterExample.createCriteria();
                courseChapterExampleCriteria.andCourseIdIn(ids);
                ids = teacherService.getChapterIdsByExample(courseChapterExample);
                if (ids.size() == 0) {
                    ids.add(0);
                }
                criteria.andChapterIdIn(ids);
                map.put("collegeId", collegeId);
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
            courseFileExample.setOrderByClause("record_time desc");
            session.setAttribute("courseFileExample", courseFileExample);
            session.setAttribute("courseFileQueryCriteria", map);
        } catch (Exception e) {
            return RequestResult.failure("查询失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/updateCourseFile")
    public RequestResult updateCourseFile(@RequestParam(value = "file") MultipartFile file, @RequestParam("courseFileId") Integer courseFileId, HttpServletRequest request) {
        if (file == null) {
            return RequestResult.failure("文档更新失败，请稍后再试");
        }
        long size = file.getSize();
        if (size > FILE_MAX_SIZE * 1024 * 1024) {
            throw new MaxUploadSizeExceedException(FILE_MAX_SIZE);
        }
        CourseFile courseFile = teacherService.getCourseFileById(courseFileId);
        if (courseFile == null) {
            return RequestResult.failure("文档更新失败，请稍后再试");
        }
        String basePath = request.getServletContext().getRealPath("/file/");
        File baseFile = new File(basePath);
        if (!baseFile.exists()) {
            return RequestResult.failure("文档更新失败，请稍后再试");
        }
        String dirPath = courseFile.getPath().substring(0, courseFile.getPath().lastIndexOf("\\") + 1);
        //删除原来的文件
        File oldFile = new File(baseFile, courseFile.getPath());
        if (oldFile.exists()) {
            boolean delete = oldFile.delete();
            if (!delete) {
                return RequestResult.failure("文档更新失败，请稍后再试");
            }
        }
        Date date = new Date();
        String fileName = date.getTime() + "_" + file.getOriginalFilename();
        //写入新的文件
        try {
            file.transferTo(new File(baseFile, dirPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return RequestResult.failure("文档更新失败，请稍后再试");
        }
        courseFile.setPath(dirPath + fileName);
        courseFile.setRecordTime(date);
        courseFile.setChapterId(null);
        boolean success = teacherService.updateCourseFileByPrimaryKeySelective(courseFile);
        if (!success) {
            return RequestResult.failure("文档更新失败，请稍后再试");
        }
        return RequestResult.success();
    }


    @RequestMapping("/downloadCourseFile/{id}")
    public ResponseEntity<byte[]> downloadCourseFile(@PathVariable("id") Integer id, HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/file/");
        CourseFile courseFile = teacherService.getCourseFileById(id);
        String fileName = courseFile.getPath().split("_", 2)[1];
        File file = new File(path + courseFile.getPath());
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

    @RequestMapping("/downloadCourseVideo/{id}")
    public ResponseEntity<byte[]> downloadCourseVideo(@PathVariable("id") Integer id, HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/video/");
        CourseVideo courseVideo = teacherService.getCourseVideoById(id);
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

    @RequestMapping("/createDiscuss")
    public String createDiscuss(Map<String, Object> map, HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("courses", teacherService.getAllCoursesWithCollegeByTeacherId(teacher.getId()));
        return "teacher/createDiscuss";
    }

    @ResponseBody
    @PostMapping("/saveDiscuss")
    public RequestResult saveDiscuss(@Validated({DiscussSequence.class}) Discuss discuss, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        if (discuss.getCourseId() == 0) {
            return RequestResult.failure("请选择所属课程");
        }
        discuss.setRecordTime(new Date());
        boolean success = teacherService.saveDiscuss(discuss);
        if (!success) {
            return RequestResult.failure("新增讨论失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchDiscuss")
    public String searchDiscuss(Map<String, Object> map, HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        DiscussExample discussExample = (DiscussExample) session.getAttribute("discussExample");
        if (discussExample == null) {
            discussExample = new DiscussExample();
            DiscussExample.Criteria criteria = discussExample.createCriteria();
            List<Integer> ids = teacherService.getCourseIdsByTeacherId(teacher.getId());
            if (ids.size() == 0) {
                ids.add(0);
            }
            criteria.andCourseIdIn(ids);
            discussExample.setOrderByClause("record_time desc");
        }
        PageHelper.startPage(pageNum, 15);
        List<Discuss> discusses = teacherService.getAllDiscussWithBLOBsAndCourseByExample(discussExample);
        PageInfo<Discuss> page = new PageInfo(discusses, 10);
        map.put("pageInfo", page);
        map.put("courses", teacherService.getAllCoursesWithCollegeByTeacherId(15));
        return "teacher/searchDiscuss";
    }

    @ResponseBody
    @PostMapping("/searchDiscussByTerm")
    public RequestResult searchDiscussByTerm(@RequestParam("courseId") Integer courseId, @RequestParam("minTime") String minTime, @RequestParam("maxTime") String maxTime, @RequestParam("title") String title, HttpSession session) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("user");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //将查询条件存放进session中，以回显查询条件
            Map<String, Object> map = new HashMap<>();
            map.put("courseId", null);
            map.put("title", null);
            map.put("minTime", null);
            map.put("maxTime", null);
            DiscussExample discussExample = new DiscussExample();
            DiscussExample.Criteria criteria = discussExample.createCriteria();
            if (!"".equals(title.trim())) {
                map.put("title", title.trim());
                criteria.andTitleLike("%" + title.trim() + "%");
            }
            if (courseId != 0) {
                map.put("courseId", courseId);
                criteria.andCourseIdEqualTo(courseId);
            }
            List<Integer> ids = teacherService.getCourseIdsByTeacherId(teacher.getId());
            if (ids.size() == 0) {
                ids.add(0);
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
            criteria.andCourseIdIn(ids);
            discussExample.setOrderByClause("record_time desc");
            session.setAttribute("discussQueryCriteria", map);
            session.setAttribute("discussExample", discussExample);
        } catch (Exception e) {
            return RequestResult.failure("查询失败，请稍后再试");
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
        boolean b = teacherService.deleteDiscussByIdBatch(discussIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/editDiscuss/{id}")
    public String editDiscuss(@PathVariable("id") Integer id, @RequestParam("pageNumber") Integer pageNumber, Map<String, Object> map, HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("discuss", teacherService.getDiscussById(id));
        map.put("pageNumber", pageNumber);
        map.put("courses", teacherService.getAllCoursesWithCollegeByTeacherId(teacher.getId()));
        return "teacher/updateDiscuss";
    }

    @ResponseBody
    @PostMapping("/updateDiscuss/{id}")
    public RequestResult updateDiscuss(@PathVariable("id") Integer id, @Validated({DiscussSequence.class}) Discuss discuss, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        Discuss discuss1 = teacherService.getDiscussById(id);
        if (discuss1 == null) {
            return RequestResult.failure("讨论更新失败，请稍后再试");
        }
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
        boolean success = teacherService.updateDiscussByPrimaryKeySelective(discuss);
        if (!success) {
            return RequestResult.failure("讨论更新失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchDiscussReply/{id}")
    public String searchDiscussReply(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam("pageNumber") Integer pageNumber, HttpSession session) {
        Map<String, Object> map1 = (Map<String, Object>) session.getAttribute("discussPostQueryCriteria");
        if (map1 == null) {
            map1 = new HashMap<>();
        }
        map1.put("id", id);
        PageHelper.startPage(pageNum, 30);
        List<DiscussPost> discussPosts = teacherService.getAllDiscussPostByMapWithStudentName(map1);
        PageInfo<DiscussPost> page = new PageInfo(discussPosts, 10);
        map.put("pageInfo", page);
        map.put("pageNumber", pageNumber);
        map.put("id", id);
        return "teacher/searchDiscussReply";
    }

    @ResponseBody
    @PostMapping("/searchDiscussPostByTerm/{id}")
    public RequestResult searchDiscussPostByTerm(@PathVariable("id") Integer id, @RequestParam("minTime") String minTime, @RequestParam("maxTime") String maxTime, @RequestParam("name") String name, @RequestParam("content") String content, HttpSession session) {
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
                Date min = simpleDateFormat.parse(minTime);
                map.put("minTime", minTime);
                map.put("minDate", min);
            }
            if (!"".equals(maxTime.trim())) {
                Date max = simpleDateFormat.parse(maxTime);
                map.put("maxTime", maxTime);
                map.put("maxDate", max);
            }
            if (!"".equals(content.trim())) {
                map.put("content", content);
            }
            session.setAttribute("discussPostQueryCriteria", map);
        } catch (Exception e) {
            return RequestResult.failure("查询失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteDiscussPostBatch")
    public RequestResult deleteDiscussPostBatch(@RequestParam("ids") String id) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> postIds = new ArrayList<>();
        for (String s : ids) {
            postIds.add(Integer.parseInt(s));
        }
        boolean b = teacherService.deleteDiscussPostByIdBatch(postIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();

    }

    @ResponseBody
    @PostMapping("/saveDiscussReply/{id}")
    public RequestResult saveDiscussReply(@PathVariable("id") Integer id, @RequestParam("content") String content) {

        if (content == null) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        if ("".equals(content.trim())) {
            return RequestResult.failure("回复内容为空，请重新输入");
        }
        if (content.length() >= 100) {
            return RequestResult.failure("回复内容请控制在100字以内");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setContent(content);
        discussPost.setDiscussId(id);
        discussPost.setStudentId(0);
        discussPost.setRecordTime(new Date());
        boolean b = teacherService.insertDiscussPostSelective(discussPost);
        if (!b) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/createTask")
    public String createTask(Map<String, Object> map, @RequestParam("type") String type, HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(teacher.getId()));
        map.put("type", type);
        return "teacher/createTask";
    }

    @ResponseBody
    @PostMapping("/saveOnlineTask")
    public RequestResult saveOnlineTask(@Validated({OnlineTaskSequence.class}) OnlineTask task, BindingResult result, HttpSession session) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (task == null) {
            return RequestResult.failure("添加作业失败，请稍后再试");
        }
        task.setRecordTime(new Date());
        task.setTeacherId(teacher.getId());
        boolean success = teacherService.saveOnlineTask(task);
        if (!success) {
            return RequestResult.failure("添加作业失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/saveOfflineTask")
    public RequestResult saveOfflineTask(@Validated({OfflineTaskSequence.class}) OfflineTask task, BindingResult result, HttpSession session) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (task == null) {
            return RequestResult.failure("添加作业失败，请稍后再试");
        }
        task.setRecordTime(new Date());
        task.setTeacherId(teacher.getId());
        boolean success = teacherService.saveOfflineTask(task);
        if (!success) {
            return RequestResult.failure("添加作业失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchTask")
    public String searchTask(Map<String, Object> map, HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "taskType", defaultValue = "") String taskType) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        String type = null;
        if ("".equals(taskType)) {
            type = (String) session.getAttribute("taskType");
        } else {
            type = taskType;
            session.setAttribute("taskType", type);
        }
        if (type == null || type.equals("online")) {
            OnlineTaskExample onlineTaskExample = (OnlineTaskExample) session.getAttribute("onlineTaskExample");
            if (onlineTaskExample == null) {
                onlineTaskExample = new OnlineTaskExample();
                OnlineTaskExample.Criteria criteria = onlineTaskExample.createCriteria();
                criteria.andTeacherIdEqualTo(teacher.getId());
                onlineTaskExample.setOrderByClause("record_time desc");
            }
            PageHelper.startPage(pageNum, 15);
            List<OnlineTask> onlineTasks = teacherService.getAllOnlineTasksWithCourseAndChapterExample(onlineTaskExample);
            PageInfo<OnlineTask> page = new PageInfo(onlineTasks, 10);
            map.put("pageInfo", page);
            if (type == null) {
                session.setAttribute("taskType", "online");
            }
        } else if (type.equals("offline")) {
            OfflineTaskExample offlineTaskExample = (OfflineTaskExample) session.getAttribute("offlineTaskExample");
            if (offlineTaskExample == null) {
                offlineTaskExample = new OfflineTaskExample();
                OfflineTaskExample.Criteria criteria = offlineTaskExample.createCriteria();
                criteria.andTeacherIdEqualTo(teacher.getId());
                offlineTaskExample.setOrderByClause("record_time desc");
            }
            PageHelper.startPage(pageNum, 15);
            List<OfflineTask> offlineTasks = teacherService.getAllOfflineTasksWithCourseAndChapterExample(offlineTaskExample);
            PageInfo<OfflineTask> page = new PageInfo(offlineTasks, 10);
            map.put("pageInfo", page);
        } else {
            throw new RuntimeException("查看任务出现异常，请稍后再试");
        }
        map.put("courses", teacherService.getAllCoursesWithCollegeByTeacherId(15));
        return "teacher/searchTask";
    }

    @ResponseBody
    @PostMapping("/deleteOnlineTask")
    public RequestResult deleteOnlineTask(@RequestParam("id") Integer id) {
        if (id == null) {
            return RequestResult.failure("作业删除失败，请稍后再试");
        }
        boolean success = teacherService.deleteOnlineTaskById(id);
        if (!success) {
            return RequestResult.failure("作业删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteOfflineTask")
    public RequestResult deleteOfflineTask(@RequestParam("id") Integer id,
                                           HttpServletRequest request,
                                           @RequestParam("courseId") Integer courseId,
                                           @RequestParam("chapterId") Integer chapterId) {
        if (id == null) {
            return RequestResult.failure("作业删除失败，请稍后再试");
        }
        String path = request.getServletContext().getRealPath("/offlineTask/") + courseId + "\\" + chapterId + "\\" + id;
        boolean success = teacherService.deleteOfflineTaskById(id, path);
        if (!success) {
            return RequestResult.failure("作业删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/searchTaskByTerm")
    public RequestResult searchTaskByTerm(HttpSession session, @RequestParam("courseId") Integer courseId, @RequestParam("title") String title, @RequestParam("minTime") String minTime, @RequestParam("maxTime") String maxTime) {
        try {
            Teacher teacher = (Teacher) session.getAttribute("user");
            String type = (String) session.getAttribute("taskType");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //将查询条件存放进session中，以回显查询条件
            Map<String, Object> map = new HashMap<>();
            map.put("courseId", null);
            map.put("title", null);
            map.put("minTime", null);
            map.put("maxTime", null);
            OnlineTaskExample onlineTaskExample = new OnlineTaskExample();
            OnlineTaskExample.Criteria onlineTaskExampleCriteria = onlineTaskExample.createCriteria();
            OfflineTaskExample offlineTaskExample = new OfflineTaskExample();
            OfflineTaskExample.Criteria offlineTaskExampleCriteria = offlineTaskExample.createCriteria();
            if (type == null || type.equals("online")) {
                if (!"".equals(title.trim())) {
                    map.put("title", title.trim());
                    onlineTaskExampleCriteria.andTitleLike("%" + title.trim() + "%");
                }
                if (courseId != 0) {
                    map.put("courseId", courseId);
                    CourseChapterExample courseChapterExample = new CourseChapterExample();
                    CourseChapterExample.Criteria criteria1 = courseChapterExample.createCriteria();
                    criteria1.andCourseIdEqualTo(courseId);
                    List<Integer> ids = teacherService.getChapterIdsByExample(courseChapterExample);
                    if (ids.size() == 0) {
                        ids.add(0);
                    }
                    onlineTaskExampleCriteria.andChapterIdIn(ids);
                }
                if (!"".equals(minTime.trim())) {
                    Date min = simpleDateFormat.parse(minTime);
                    onlineTaskExampleCriteria.andRecordTimeGreaterThanOrEqualTo(min);
                    map.put("minTime", minTime);
                }
                if (!"".equals(maxTime.trim())) {
                    Date max = simpleDateFormat.parse(maxTime);
                    onlineTaskExampleCriteria.andRecordTimeLessThanOrEqualTo(max);
                    map.put("maxTime", maxTime);
                }
                onlineTaskExampleCriteria.andTeacherIdEqualTo(teacher.getId());
                onlineTaskExample.setOrderByClause("record_time desc");
                session.setAttribute("onlineTaskExample", onlineTaskExample);
                session.setAttribute("onlineTaskQueryCriteria", map);
            } else if (type.equals("offline")) {
                if (!"".equals(title.trim())) {
                    map.put("title", title.trim());
                    offlineTaskExampleCriteria.andTitleLike("%" + title.trim() + "%");
                }
                if (courseId != 0) {
                    map.put("courseId", courseId);
                    CourseChapterExample courseChapterExample = new CourseChapterExample();
                    CourseChapterExample.Criteria criteria1 = courseChapterExample.createCriteria();
                    criteria1.andCourseIdEqualTo(courseId);
                    List<Integer> ids = teacherService.getChapterIdsByExample(courseChapterExample);
                    if (ids.size() == 0) {
                        ids.add(0);
                    }
                    offlineTaskExampleCriteria.andChapterIdIn(ids);
                }
                if (!"".equals(minTime.trim())) {
                    Date min = simpleDateFormat.parse(minTime);
                    offlineTaskExampleCriteria.andRecordTimeGreaterThanOrEqualTo(min);
                    map.put("minTime", minTime);
                }
                if (!"".equals(maxTime.trim())) {
                    Date max = simpleDateFormat.parse(maxTime);
                    offlineTaskExampleCriteria.andRecordTimeLessThanOrEqualTo(max);
                    map.put("maxTime", maxTime);
                }
                offlineTaskExampleCriteria.andTeacherIdEqualTo(teacher.getId());
                offlineTaskExample.setOrderByClause("record_time desc");
                session.setAttribute("offlineTaskExample", offlineTaskExample);
                session.setAttribute("offlineTaskQueryCriteria", map);
            } else {
                return RequestResult.failure("查询失败，请稍后再试");
            }
        } catch (Exception e) {
            return RequestResult.failure("查询失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/editTask/{id}")
    public String updateOnlineTask(@PathVariable("id") Integer id,
                                   Map<String, Object> map,
                                   HttpSession session,
                                   @RequestParam(value = "type", defaultValue = "") String type,
                                   @RequestParam("pageNum") Integer pageNum,
                                   @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                   @RequestParam(value = "courseId", required = false) Integer courseId,
                                   @RequestParam(value = "chapterId", required = false) Integer chapterId) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        if ("online".equals(type)) {
            map.put("task", teacherService.getOnlineTaskWithCourseAndChapterById(id));
        } else {
            map.put("task", teacherService.getOfflineTaskWithCourseAndChapterById(id));
        }
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(teacher.getId()));
        map.put("pageNum", pageNum);
        map.put("pageNumber", pageNumber);
        map.put("courseId", courseId);
        map.put("chapterId", chapterId);
        map.put("taskId", id);
        map.put("type", type);
        return "teacher/updateTask";
    }

    @ResponseBody
    @PostMapping("/updateOnlineTask/{id}")
    public RequestResult updateOnlineTask(@PathVariable("id") Integer id, @Validated({OnlineTaskSequence.class}) OnlineTask task, BindingResult result, HttpSession session) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (id == null || task == null) {
            return RequestResult.failure("修改作业失败，请稍后再试");
        }
        task.setTeacherId(teacher.getId());
        OnlineTask task1 = teacherService.getOnlineTaskById(id);
        if (task1.equals(task)) {
            return RequestResult.failure("未修改任何数据");
        }
        if (task1.getChapterId().equals(task.getChapterId())) {
            task.setChapterId(null);
        }
        if (task1.getTitle().equals(task.getTitle())) {
            task.setTitle(null);
        }
        task.setTeacherId(null);
        boolean success = teacherService.updateOnlineTaskByPrimaryKeySelective(task);
        if (!success) {
            return RequestResult.failure("修改作业失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/updateOfflineTask/{id}")
    public RequestResult updateOfflineTask(@PathVariable("id") Integer id, @Validated({OfflineTaskSequence.class}) OfflineTask task, BindingResult result, HttpSession session) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (id == null || task == null) {
            return RequestResult.failure("修改作业失败，请稍后再试");
        }
        task.setTeacherId(teacher.getId());
        OfflineTask task1 = teacherService.getOfflineTaskById(id);
        if (task1.equals(task)) {
            return RequestResult.failure("未修改任何数据");
        }
        if (task1.getChapterId().equals(task.getChapterId())) {
            task.setChapterId(null);
        }
        if (task1.getTitle().equals(task.getTitle())) {
            task.setTitle(null);
        }
        task.setTeacherId(null);
        boolean success = teacherService.updateOfflineTaskByPrimaryKeySelective(task);
        if (!success) {
            return RequestResult.failure("修改作业失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchOnlineTaskQuestion/{id}")
    public String searchQuestion(@PathVariable("id") Integer id,
                                 @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                 Map<String, Object> map,
                                 @RequestParam(value = "page", required = false) Integer p,
                                 @RequestParam(value = "courseId", required = false) Integer courseId,
                                 @RequestParam(value = "chapterId", required = false) Integer chapterId) {

        List<OnlineTaskQuestion> questions = teacherService.getOnlineTaskQuestionsByTaskId(id);
        map.put("questions", questions);
        map.put("pageNumber", pageNumber);
        map.put("taskId", id);
        map.put("courseId", courseId);
        map.put("page", p);
        map.put("chapterId", chapterId);
        return "teacher/searchOnlineTaskQuestion";
    }

    @ResponseBody
    @PostMapping("/deleteTaskQuestionBatch")
    public RequestResult deleteTaskQuestionBatch(@RequestParam("ids") String id) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> questionIds = new ArrayList<>();
        for (String s : ids) {
            questionIds.add(Integer.parseInt(s));
        }
        boolean b = teacherService.deleteOnlineTaskQuestionByIdBatch(questionIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/createOnlineTaskQuestion/{id}")
    public String createOnlineTaskQuestion(@PathVariable("id") Integer id, Map<String, Object> map) {
        map.put("taskId", id);
        return "teacher/createOnlineTaskQuestion";
    }

    @ResponseBody
    @PostMapping("/saveOnlineTaskQuestion")
    public RequestResult saveOnlineTaskQuestion(@Validated({OnlineTaskQuestionSequence.class}) OnlineTaskQuestion taskQuestion, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        if (taskQuestion == null) {
            return RequestResult.failure("题目添加失败，请稍后再试");
        }
        boolean success = teacherService.insertOnlineTaskQuestion(taskQuestion);
        if (!success) {
            return RequestResult.failure("题目添加失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/editOnlineTaskQuestion/{id}")
    public String editOnlineTaskQuestion(@PathVariable("id") Integer id,
                                         Map<String, Object> map,
                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "page", required = false) Integer page,
                                         @RequestParam(value = "courseId", required = false) Integer courseId,
                                         @RequestParam(value = "chapterId", required = false) Integer chapterId) {

        map.put("pageNum", pageNum);
        map.put("pageNumber", pageNumber);
        map.put("page", page);
        map.put("courseId", courseId);
        map.put("chapterId", chapterId);
        map.put("taskQuestion", teacherService.getOnlineTaskQuestionsById(id));
        return "teacher/updateOnlineTaskQuestion";
    }

    @ResponseBody
    @PostMapping("/updateOnlineTaskQuestion")
    public RequestResult updateOnlineTaskQuestion(@Validated({OnlineTaskQuestionSequence.class}) OnlineTaskQuestion taskQuestion, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        if (taskQuestion == null) {
            return RequestResult.failure("题目修改失败，请稍后再试");
        }
        OnlineTaskQuestion question = teacherService.getOnlineTaskQuestionsById(taskQuestion.getId());
        if (question == null) {
            return RequestResult.failure("题目修改失败，请稍后再试");
        }
        if (question.equals(taskQuestion)) {
            return RequestResult.failure("未修改任何内容");
        }
        if (question.getAnswer().equals(taskQuestion.getAnswer())) {
            taskQuestion.setAnswer(null);
        }
        if (question.getItemA().equals(taskQuestion.getItemA())) {
            taskQuestion.setItemA(null);
        }
        if (question.getItemB().equals(taskQuestion.getItemB())) {
            taskQuestion.setItemB(null);
        }
        if (question.getItemC().equals(taskQuestion.getItemC())) {
            taskQuestion.setItemC(null);
        }
        if (question.getItemD().equals(taskQuestion.getItemD())) {
            taskQuestion.setItemD(null);
        }
        if (question.getTitle().equals(taskQuestion.getTitle())) {
            taskQuestion.setTitle(null);
        }
        if (question.getScore().equals(taskQuestion.getScore())) {
            taskQuestion.setScore(null);
        }
        taskQuestion.setOnlineTaskId(null);
        boolean success = teacherService.updateOnlineTaskQuestionByPrimaryKeySelective(taskQuestion);
        if (!success) {
            return RequestResult.failure("题目修改失败，请稍后再试");
        }
        return RequestResult.success();
    }

    //下载作业模板
    @RequestMapping("/downloadTaskQuestionTemplate")
    public ResponseEntity<byte[]> downloadTaskQuestionTemplate(HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/templates/");
        String fileName = "OnlineTaskQuestion_Template.xlsx";
        File file = new File(path + fileName);
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

    @ResponseBody
    @PostMapping("/taskQuestionBatchImport")
    public RequestResult taskQuestionBatchImport(@RequestParam(value = "file") MultipartFile file, @RequestParam("taskId") Integer taskId) {
        long size = file.getSize();
        if (size > FILE_MAX_SIZE * 1024 * 1024) {
            throw new MaxUploadSizeExceedException(FILE_MAX_SIZE);
        }
        String fileName = file.getOriginalFilename();
        List<OnlineTaskQuestion> taskQuestions = new ArrayList<>();
        //原表头顺序：题目、选项A、选项B、选项C、选项D、答案、分值；使用数组来比较
        String[] original_title = new String[]{"题目", "选项A", "选项B", "选项C", "选项D", "答案", "分值"};
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
                    for (int cellIndex = 0; cellIndex < original_title.length; cellIndex++) {
                        String cell = POIUtil.getStringHSSF(titleRow.getCell(cellIndex));
                        current_title[cellIndex] = cell;
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
                        OnlineTaskQuestion question = new OnlineTaskQuestion();
                        HSSFCell cell;
                        CellType cellType;
                        String data;
                        for (int i = 0; i < original_title.length; i++) {
                            cell = hssfRow.getCell(i);
                            cellType = cell.getCellType();
                            if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行题的" + original_title[i] + "不是文本类型");
                            }
                            data = "";
                            if (cellType == CellType.STRING) {
                                data = cell.getStringCellValue().trim();
                            }
                            if ("".equals(data)) {//空值
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行题的" + original_title[i] + "不能为空");
                            }
                            if (i == 0) {
                                question.setTitle(data);
                            }
                            if (i == 1) {
                                question.setItemA(data);
                            }
                            if (i == 2) {
                                question.setItemB(data);
                            }
                            if (i == 3) {
                                question.setItemC(data);
                            }
                            if (i == 4) {
                                question.setItemD(data);
                            }
                            if (i == 5) {
                                if (!(data.contains("A") || data.contains("B") || data.contains("C") || data.contains("D"))) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行题的" + original_title[i] + "不在范围之内");
                                }
                                question.setAnswer(data);
                            }
                            if (i == 6) {
                                if (data.contains(".")) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行题的" + original_title[i] + "不能为小数");
                                }
                                if (data.contains(" ")) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行题的" + original_title[i] + "不能包含空格");
                                }
                                question.setScore(Integer.parseInt(data));
                            }
                            question.setOnlineTaskId(taskId);
                        }
                        taskQuestions.add(question);
                    }
                    boolean i = teacherService.insertOnlineTaskQuestionBatch(taskQuestions);
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
                    //获取总行数
                    int totalRows = POIUtil.getExcelRealRowXSSF(xssfSheet);
                    //默认第一行是标题,index=0
                    XSSFRow titleRow = xssfSheet.getRow(0);
                    //判断表头是否被修改或换位置
                    for (int cellIndex = 0; cellIndex < original_title.length; cellIndex++) {
                        String cell = POIUtil.getStringXSSF(titleRow.getCell(cellIndex));
                        current_title[cellIndex] = cell;
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
                        OnlineTaskQuestion question = new OnlineTaskQuestion();
                        XSSFCell cell;
                        CellType cellType;
                        String data;
                        for (int i = 0; i < original_title.length; i++) {
                            cell = xssfRow.getCell(i);
                            cellType = cell.getCellType();
                            if (cellType != CellType.STRING && cellType != CellType.BLANK) {
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行题的" + original_title[i] + "不是文本类型");
                            }
                            data = "";
                            if (cellType == CellType.STRING) {
                                data = cell.getStringCellValue().trim();
                            }
                            if ("".equals(data)) {//空值
                                return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行题的" + original_title[i] + "不能为空");
                            }
                            if (i == 0) {
                                question.setTitle(data);
                            }
                            if (i == 1) {
                                question.setItemA(data);
                            }
                            if (i == 2) {
                                question.setItemB(data);
                            }
                            if (i == 3) {
                                question.setItemC(data);
                            }
                            if (i == 4) {
                                question.setItemD(data);
                            }
                            if (i == 5) {
                                if (!(data.contains("A") || data.contains("B") || data.contains("C") || data.contains("D"))) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行题的" + original_title[i] + "不在范围之内");
                                }
                                question.setAnswer(data);
                            }
                            if (i == 6) {
                                if (data.contains(".")) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行题的" + original_title[i] + "不能为小数");
                                }
                                if (data.contains(" ")) {
                                    return RequestResult.failure("导入失败，第" + (rowIndex + 1) + "行题的" + original_title[i] + "不能包含空格");
                                }
                                question.setScore(Integer.parseInt(data));
                            }
                            question.setOnlineTaskId(taskId);
                        }
                        System.out.println(question);
                        taskQuestions.add(question);
                    }
                    boolean i = teacherService.insertOnlineTaskQuestionBatch(taskQuestions);
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

    @RequestMapping("/editTeacherInfo")
    public String updateTeacherInfo(HttpSession session, Map<String, Object> map) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("teacher", teacherService.getTeacherByIdWithCollege(teacher.getId()));
        return "teacher/updateTeacherInfo";
    }

    @ResponseBody
    @PostMapping("/updateTeacherInfo")
    public RequestResult updateTeacherInfo(@Validated({UpdateTeacherSequence.class}) Teacher tea, BindingResult result, @RequestParam(value = "code") String code, HttpSession session) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        Teacher teacher = (Teacher) session.getAttribute("user");
        teacher = teacherService.getTeacherByIdWithoutCollegeAndCourse(teacher.getId());
        if (teacher == null || tea == null) {
            return RequestResult.failure("更新信息失败，请稍后再试");
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
        tea.setId(teacher.getId());
        tea.setCollegeId(teacher.getCollegeId());
        tea.setNumber(teacher.getNumber());
        if (teacher.equals(tea)) {
            return RequestResult.failure("未修改任何数据");
        }
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
        if (!teacher.getTelephone().equals(tea.getTelephone())) {
            //修改电话，判断电话是否已经存在
            criteria.andTelephoneEqualTo(tea.getTelephone());
            boolean exists = teacherService.isExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该手机号已经存在");
            }
        } else {
            tea.setTelephone(null);
        }
        if (!tea.getEmail().equals(teacher.getEmail())) {
            teacherExample.clear();
            criteria = teacherExample.createCriteria();
            criteria.andEmailEqualTo(tea.getEmail());
            boolean exists = teacherService.isExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该邮箱地址已经存在");
            }
        } else {
            tea.setEmail(null);
        }
        if (!tea.getAccount().equals(teacher.getAccount())) {
            teacherExample.clear();
            criteria = teacherExample.createCriteria();
            criteria.andAccountEqualTo(tea.getAccount());
            boolean exists = teacherService.isExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该账户已经存在");
            }
        } else {
            tea.setAccount(null);
        }
        if (!tea.getIdCardNo().equals(teacher.getIdCardNo())) {
            teacherExample.clear();
            criteria = teacherExample.createCriteria();
            criteria.andIdCardNoEqualTo(tea.getIdCardNo());
            boolean exists = teacherService.isExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该身份证号已经存在");
            }
        } else {
            tea.setIdCardNo(null);
        }
        if (teacher.getGender().equals(tea.getGender())) {
            tea.setGender(null);
        }
        if (teacher.getName().equals(tea.getName())) {
            tea.setName(null);
        }
        if (teacher.getPassword().equals(tea.getPassword())) {
            tea.setPassword(null);
        }
        tea.setNumber(null);
        tea.setCollegeId(null);
        boolean b = teacherService.updateTeacherByPrimaryKeySelective(tea);
        if (!b) {
            return RequestResult.failure("更新信息失败，请稍后再试");
        }
        session.removeAttribute("UpdateInfoCode");
        return RequestResult.success();
    }

    @RequestMapping("/viewTeacherInfo")
    public String viewTeacherInfo(Map<String, Object> map, HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("teacher", teacherService.getTeacherByIdWithCollege(teacher.getId()));
        return "teacher/viewTeacherInfo";
    }

    @RequestMapping("/searchCourse")
    public String searchCourse(Map<String, Object> map, HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        CourseExample courseExample = (CourseExample) session.getAttribute("courseExample");
        if (courseExample == null) {
            courseExample = new CourseExample();
            CourseExample.Criteria criteria = courseExample.createCriteria();
            criteria.andTeacherIdEqualTo(teacher.getId());
            courseExample.setOrderByClause("CONVERT(name using gbk) asc");
        }
        PageHelper.startPage(pageNum, 20);
        List<Course> courses = teacherService.getAllCoursesWithCollegeByExample(courseExample);
        PageInfo<Course> page = new PageInfo(courses, 10);
        map.put("pageInfo", page);
        map.put("colleges", teacherService.getAllCollegesByTeacherId(15));
        return "teacher/searchCourse";
    }

    @ResponseBody
    @PostMapping("/searchCourseByTerm")
    public RequestResult searchCourseByTerm(HttpSession session, @RequestParam("collegeId") Integer collegeId, @RequestParam("name") String name) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        map.put("collegeId", null);
        map.put("name", null);
        if (collegeId != 0) {
            criteria.andCollegeIdEqualTo(collegeId);
            map.put("collegeId", collegeId);
        }
        if (!"".equals(name.trim())) {
            criteria.andNameLike("%" + name.trim() + "%");
            map.put("name", name.trim());
        }
        criteria.andTeacherIdEqualTo(teacher.getId());
        courseExample.setOrderByClause("CONVERT(name using gbk) asc");
        session.setAttribute("courseExample", courseExample);
        session.setAttribute("courseQueryCriteria", map);
        return RequestResult.success();
    }

    @RequestMapping("/createChapter")
    public String createChapter(Map<String, Object> map, @RequestParam(value = "courseId") Integer courseId) {
        map.put("course", teacherService.getCourseWithChapterAndCollegeByCourseId(courseId));
        map.put("courseId", courseId);
        return "teacher/createChapter";
    }

    @ResponseBody
    @PostMapping("/saveChapter")
    public RequestResult saveChapter(@Validated({CourseChapterSequence.class}) CourseChapter chapter, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        if (chapter == null) {
            return RequestResult.failure("新增章节失败，请稍后再试");
        }
        chapter.setRecordTime(new Date());
        boolean success = teacherService.insertCourseChapterSelective(chapter);
        if (!success) {
            return RequestResult.failure("新增章节失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchChapter")
    public String searchChapter(Map<String, Object> map, @RequestParam("courseId") Integer courseId, @RequestParam("pageNum") Integer pageNum) {
        CourseChapterExample courseChapterExample = new CourseChapterExample();
        CourseChapterExample.Criteria criteria = courseChapterExample.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        courseChapterExample.setOrderByClause("record_time asc");
        List<CourseChapter> chapters = teacherService.getAllChapterWithBLOBsAndCourseByExample(courseChapterExample);
        map.put("chapters", chapters);
        map.put("courseId", courseId);
        map.put("pageNum", pageNum);
        return "teacher/searchChapter";
    }

    @ResponseBody
    @PostMapping("/deleteChapterBatch")
    public RequestResult deleteChapterBatch(@RequestParam("ids") String id, HttpServletRequest request, @RequestParam("courseId") Integer courseId) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> chapterIds = new ArrayList<>();
        for (String s : ids) {
            chapterIds.add(Integer.parseInt(s));
        }
        boolean success = teacherService.deleteChapterByIdBatch(chapterIds, request, courseId);
        if (!success) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/editChapter/{id}")
    public String editChapter(Map<String, Object> map, @PathVariable("id") Integer id, @RequestParam(value = "courseId", defaultValue = "0") Integer courseId, @RequestParam("pageNum") Integer pageNum, @RequestParam("preCourseId") Integer preCourseId) {
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(15));
        map.put("chapter", teacherService.getChapterById(id));
        if (courseId == 0) {
            map.put("courseId", preCourseId);
        } else {
            map.put("courseId", courseId);
        }
        map.put("pageNum", pageNum);
        map.put("preCourseId", preCourseId);
        return "teacher/updateChapter";
    }

    @ResponseBody
    @PostMapping("/updateChapter/{id}")
    public RequestResult updateChapter(@Validated({CourseChapterSequence.class}) CourseChapter chapter, BindingResult result) {
        if (result.getErrorCount() > 0) {
            return RequestResult.failure(result.getFieldErrors().get(0).getDefaultMessage());
        }
        if (chapter == null) {
            return RequestResult.failure("章节修改失败，请稍后再试");
        }
        if (chapter.getCourseId() == 0) {
            return RequestResult.failure("请选择章节所属课程");
        }
        CourseChapter chapterById = teacherService.getChapterById(chapter.getId());
        if (chapterById.equals(chapter)) {
            return RequestResult.failure("未修改任何数据");
        }
        if (chapterById.getTitle().equals(chapter.getTitle())) {
            chapter.setTitle(null);
        }
        if (chapterById.getContent().equals(chapter.getContent())) {
            chapter.setContent(null);
        }
        if (chapterById.getCourseId().equals(chapter.getCourseId())) {
            chapter.setCourseId(null);
        }
        boolean success = teacherService.updateCourseChapterByIdSelective(chapter);
        if (!success) {
            return RequestResult.failure("章节修改失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchChapterTask/{id}")
    public String searchChapterTask(@PathVariable("id") Integer id,
                                    Map<String, Object> map,
                                    @RequestParam("pageNum") Integer pageNum,
                                    @RequestParam("courseId") Integer courseId) {
        List<OnlineTask> onlineTasks = teacherService.getOnlineTaskByChapterId(id);
        List<OfflineTask> offlineTasks = teacherService.getOfflineTaskByChapterId(id);
        map.put("pageNum", pageNum);
        map.put("courseId", courseId);
        map.put("onlineTasks", onlineTasks);
        map.put("offlineTasks", offlineTasks);
        map.put("chapterId", id);
        return "teacher/searchChapterTask";
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
        List<CourseVideo> videos = teacherService.getCourseVideoByExample(courseVideoExample);
        List<CourseFile> files = teacherService.getCourseFileByExample(courseFileExample);
        map.put("videos", videos);
        map.put("files", files);
        map.put("id", id);
        map.put("minTime", minTime);
        map.put("maxTime", maxTime);
        map.put("pageNum", pageNum);
        map.put("courseId", courseId);
        return "teacher/viewChapterFiles";
    }

    @RequestMapping("/viewTaskFinish/{taskId}")
    public String viewTaskFinish(@PathVariable("taskId") Integer id,
                                 @RequestParam("type") String type,
                                 @RequestParam(value = "page", required = false) Integer page,
                                 @RequestParam(value = "courseId") Integer courseId,
                                 @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                 @RequestParam(value = "chapterId") Integer chapterId,
                                 Map<String, Object> map,
                                 @RequestParam(value = "name", defaultValue = "") String name,
                                 @RequestParam(value = "number", defaultValue = "") String number,
                                 HttpSession session) {
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        if (!"".equals(name.trim())) {
            criteria.andNameLike("%" + name.trim() + "%");
        }
        if (!"".equals(number.trim())) {
            criteria.andNumberLike("%" + number.trim() + "%");
        }
        CollectExample collectExample = new CollectExample();
        CollectExample.Criteria collectExampleCriteria = collectExample.createCriteria();
        collectExampleCriteria.andCourseIdEqualTo(courseId);
        List<Integer> studentIds = teacherService.selectCollectStudentIdByExample(collectExample);
        if (studentIds.size() == 0) {
            studentIds.add(0);
        }
        criteria.andIdIn(studentIds);
        //根据课程id，查出选择该课程的学生
        List<Student> students = teacherService.getCollectStudentByStudentExample(studentExample);
        studentIds.clear();
        for (Student student : students) {
            studentIds.add(student.getId());
        }
        if (studentIds.size() == 0) {
            studentIds.add(0);
        }
        map.put("students", students);
        map.put("taskId", id);
        map.put("courseId", courseId);
        map.put("pageNum", pageNum);
        map.put("page", page);
        map.put("chapterId", chapterId);
        map.put("type", type);
        map.put("name", name.trim());
        map.put("number", number.trim());
        if (type.equals("online")) {
            StudentOnlineTaskExample studentOnlineTaskExample = new StudentOnlineTaskExample();
            StudentOnlineTaskExample.Criteria studentOnlineTaskExampleCriteria = studentOnlineTaskExample.createCriteria();
            studentOnlineTaskExampleCriteria.andOnlineTaskIdEqualTo(id).andStudentIdIn(studentIds);
            List<StudentOnlineTask> studentOnlineTasks = teacherService.getOnlineTaskFinishByExample(studentOnlineTaskExample);
            Integer total = teacherService.getOnlineTaskTotalScore(id);
            Map<Integer, StudentOnlineTask> studentTaskMap = new HashMap<>();
            for (StudentOnlineTask studentOnlineTask : studentOnlineTasks) {
                studentTaskMap.put(studentOnlineTask.getStudentId(), studentOnlineTask);
            }
            map.put("studentOnlineTaskMap", studentTaskMap);
            map.put("total", total);
        } else if (type.equals("offline")) {
            StudentOfflineTaskExample studentOfflineTaskExample = new StudentOfflineTaskExample();
            StudentOfflineTaskExample.Criteria studentOfflineTaskExampleCriteria = studentOfflineTaskExample.createCriteria();
            studentOfflineTaskExampleCriteria.andOfflineTaskIdEqualTo(id).andStudentIdIn(studentIds);
            List<StudentOfflineTask> studentOfflineTasks = teacherService.getOfflineTaskFinishByExample(studentOfflineTaskExample);
            Map<Integer, StudentOfflineTask> studentTaskMap = new HashMap<>();
            for (StudentOfflineTask studentOfflineTask : studentOfflineTasks) {
                studentTaskMap.put(studentOfflineTask.getStudentId(), studentOfflineTask);
            }
            map.put("studentOfflineTaskMap", studentTaskMap);
            session.setAttribute("studentOfflineTasks", studentOfflineTasks);
        } else {
            throw new RuntimeException("查看任务完成情况出现异常，请稍后再试");
        }
        return "teacher/viewTaskFinish";
    }

    @ResponseBody
    @RequestMapping("/taskReFinish/{id}")
    public RequestResult taskReFinish(@PathVariable("id") Integer id, @RequestParam("type") String type) {
        if (type.equals("online")) {
            boolean success = teacherService.deleteStudentOnlineTaskById(id);
            if (!success) {
                return RequestResult.failure("处理失败，请稍后再试");
            }
        } else if (type.equals("offline")) {
            boolean success = teacherService.deleteStudentOfflineTaskById(id);
            if (!success) {
                return RequestResult.failure("处理失败，请稍后再试");
            }
        } else {
            return RequestResult.failure("处理失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @RequestMapping("/taskReFinishBatch")
    public RequestResult taskReFinishBatch(@RequestParam("ids") String ids, @RequestParam("type") String type, @RequestParam("taskId") Integer taskId) {
        String[] id = ids.split("-");
        List<Integer> studentIds = new ArrayList<>();
        for (String s : id) {
            studentIds.add(Integer.parseInt(s));
        }
        if (type.equals("online")) {
            StudentOnlineTaskExample studentOnlineTaskExample = new StudentOnlineTaskExample();
            StudentOnlineTaskExample.Criteria criteria = studentOnlineTaskExample.createCriteria();
            criteria.andStudentIdIn(studentIds).andOnlineTaskIdEqualTo(taskId);
            boolean success = teacherService.deleteStudentOnlineTaskByExample(studentOnlineTaskExample);
            if (!success) {
                return RequestResult.failure("处理失败，请稍后再试");
            }
        } else if (type.equals("offline")) {
            StudentOfflineTaskExample studentOfflineTaskExample = new StudentOfflineTaskExample();
            StudentOfflineTaskExample.Criteria criteria = studentOfflineTaskExample.createCriteria();
            criteria.andStudentIdIn(studentIds).andOfflineTaskIdEqualTo(taskId);
            boolean success = teacherService.deleteStudentOfflineTaskByExample(studentOfflineTaskExample);
            if (!success) {
                return RequestResult.failure("处理失败，请稍后再试");
            }
        } else {
            return RequestResult.failure("处理失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/downloadOfflineTaskFile/{id}")
    public ResponseEntity<byte[]> downloadOfflineTaskFile(@PathVariable("id") Integer id, HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/offlineTask/");
        StudentOfflineTask studentOfflineTask = teacherService.getOfflineTaskFileById(id);
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

    @RequestMapping("/downloadFiles")
    public ResponseEntity<byte[]> downloadFiles(@RequestParam("courseId") Integer courseId,
                                                @RequestParam("chapterId") Integer chapterId,
                                                @RequestParam("taskId") Integer taskId,
                                                HttpServletRequest request) {
        String basePath = request.getServletContext().getRealPath("/offlineTask/");
        String path = basePath + courseId + "\\" + chapterId + "\\" + taskId + "\\";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = "学生作业完成内容.zip";
        File zipFile = new File(path + fileName);
        if (zipFile.exists()) {
            zipFile.delete();
        }
        HttpSession session = request.getSession();
        List<StudentOfflineTask> studentOfflineTasks = (List<StudentOfflineTask>) session.getAttribute("studentOfflineTasks");
        if (studentOfflineTasks == null) {
            throw new DownloadException("下载异常，请稍后再试");
        }
        List<File> files = new ArrayList<>();
        for (StudentOfflineTask task : studentOfflineTasks) {
            file = new File(basePath + task.getPath());
            if (file.exists()) {
                files.add(file);
            }
        }
        FileUtil.zipFiles(files, zipFile);
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
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(zipFile), headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new DownloadException("下载异常，请稍后再试");
        }
    }

    @RequestMapping("/searchNoticeInfo")
    public String searchNoticeInfo(Map<String, Object> map) {
        List<Notice> notices = teacherService.getAllNotices();
        map.put("notices", notices);
        return "teacher/searchNoticeInfo";
    }

    @ResponseBody
    @PostMapping("/logout")
    public RequestResult logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        /*Teacher teacher = (Teacher) session.getAttribute("user");
        ServletContext application = request.getServletContext();
        List<Integer> adminLogin = (List<Integer>) application.getAttribute("teacherLogin");
        adminLogin.remove(teacher.getId());*/
        session.invalidate();
        return RequestResult.success();
    }
}
