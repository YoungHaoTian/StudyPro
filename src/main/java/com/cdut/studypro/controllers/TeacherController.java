package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.services.TeacherService;
import com.cdut.studypro.utils.MD5Util;
import com.cdut.studypro.utils.POIUtils;
import com.cdut.studypro.utils.RequestResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
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
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
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

    @Autowired
    private TeacherService teacherService;

    @ResponseBody
    @PostMapping(value = "/login")
    public RequestResult login(@RequestBody Map<String, String> map, HttpServletRequest request) {
        System.out.println(map);
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
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
        List<Teacher> teachers = teacherService.selectTeacherByExample(teacherExample);
        if (teachers != null && teachers.size() != 0) {
            Teacher teacher = teachers.get(0);
            String password = map.get("password");
            if (password.equals(MD5Util.stringToMD5(teacher.getPassword()))) {
                //登录成功后将教师信息保存在session中
                request.getSession().setAttribute("user", teacher);
                return RequestResult.success();
            } else {
                return RequestResult.failure("密码错误，请稍后重试");
            }
        }
        return RequestResult.failure(message);
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
        Integer id = teacherService.getIdByTelephone(phone);
        //不存在
        if (id == null) {
            //删除session中的验证码
            request.getSession().removeAttribute("ForgetCode");
            return RequestResult.failure("该手机号不存在，请稍后再试");
        }
        //存在，修老师密码
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setPassword(password);
        boolean b = teacherService.updateTeacherByPrimaryKeySelective(teacher);
        if (!b) {
            //删除session中的验证码
            request.getSession().removeAttribute("ForgetCode");
            return RequestResult.failure("密码找回失败，请稍后再试");
        }
        //删除session中的验证码
        request.getSession().removeAttribute("ForgetCode");
        return RequestResult.success();
    }

    @RequestMapping("/teacherIndex")
    public String teacherIndex() {
        return "teacher/teacherIndex";
    }

    @RequestMapping("/createCourseVideo")
    public String createCourseVideo(Map<String, Object> map, HttpSession session) {
        /*Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("courses", teacherService.getAllCoursesWithChapter(teacher.getId()));*/
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(15));
        return "teacher/createCourseVideo";
    }

    @ResponseBody
    @PostMapping("/saveCourseVideo")
    public RequestResult saveCourseVideo(@RequestParam(value = "file") MultipartFile file, @RequestParam("chapterId") Integer chapterId, @RequestParam("courseId") Integer courseId, HttpServletRequest request) {
        System.out.println(file.getOriginalFilename());
        System.out.println(chapterId);
//        Teacher teacher = (Teacher)request.getSession().getAttribute("user");

        String path = request.getServletContext().getRealPath("/video/");
        //System.out.println(request.getServletPath());//请求路径
        //System.out.println(request.getContextPath());//项目路径
        //视频存放路径：/video/课程id/章节id/上传时间_fileName
        String fileDir = courseId + "\\" + chapterId + "\\";
        path += fileDir;
        System.out.println(path);
        File file1 = new File(path);
        System.out.println(file1.exists());
        Date date = new Date();
        String fileName = date.getTime() + "_" + file.getOriginalFilename();
        if (!file1.exists()) {
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
        /*Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("videos",teacherService.searchCourseVideo(teacher.getId()));*/

        CourseVideoExample courseVideoExample = (CourseVideoExample) session.getAttribute("courseVideoExample");
        if (courseVideoExample == null) {
            courseVideoExample = new CourseVideoExample();
            List<Integer> ids = teacherService.getCourseIdsByTeacherId(15);
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
        PageHelper.startPage(pageNum, 10);
        List<CourseVideo> videos = teacherService.searchCourseVideoByExampleWithCourseChapter(courseVideoExample);
        for (CourseVideo video : videos) {
            String path = video.getPath();
            path = path.substring(path.indexOf('_') + 1);
            video.setPath(path);
        }
        PageInfo<CourseVideo> page = new PageInfo(videos, 10);
        map.put("pageInfo", page);
        map.put("colleges", teacherService.getAllColleges());
//        map.put("videos",videos);
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
        //删除课程视频在数据库中的记录
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
                courseExampleCriteria.andNameLike("%" + name.trim() + "%").andTeacherIdEqualTo(15);
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
                courseExampleCriteria.andCollegeIdEqualTo(collegeId).andTeacherIdEqualTo(15);
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

    @RequestMapping("/editCourseVideo/{id}")
    public String editCourseVideo(@PathVariable("id") Integer id,
                                  Map<String, Object> map, @RequestParam("pageNum") Integer pageNum,
                                  @RequestParam(value = "chapterId", required = false) Integer chapterId,
                                  @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                  @RequestParam(value = "minTime", defaultValue = "") String minTime,
                                  @RequestParam(value = "maxTime", defaultValue = "") String maxTime) {
        map.put("courseVideoId", id);
        map.put("pageNum", pageNum);
        map.put("pageNumber", pageNumber);
        map.put("chapterId", chapterId);
        map.put("minTime", minTime);
        map.put("maxTime", maxTime);
        return "teacher/editCourseVideo";
    }

    @ResponseBody
    @PostMapping("/updateCourseVideo")
    public RequestResult updateCourseVideo(@RequestParam(value = "file") MultipartFile file, @RequestParam("courseVideoId") Integer courseVideoId, HttpServletRequest request) {
        if (file == null) {
            return RequestResult.failure("视频更新失败，请稍后再试");
        }
        CourseVideo video = teacherService.getCourseVideoById(courseVideoId);
        if (video == null) {
            return RequestResult.failure("视频更新失败，请稍后再试");
        }
        String basePath = request.getServletContext().getRealPath("/video/");
        String dirPath = video.getPath().substring(0, video.getPath().lastIndexOf("\\") + 1);
        String fileName = video.getPath().substring(video.getPath().lastIndexOf("\\") + 1);
        System.out.println(basePath + dirPath + fileName);
        //删除原来的文件
        File oldFile = new File(basePath + video.getPath());
        if (oldFile.exists()) {
            boolean delete = oldFile.delete();
            if (!delete) {
                return RequestResult.failure("视频更新失败，请稍后再试");
            }
        }
        Date date = new Date();
        fileName = date.getTime() + "_" + file.getOriginalFilename();
        System.out.println(basePath + dirPath + fileName);
        //写入新的文件
        try {
            file.transferTo(new File(basePath + dirPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return RequestResult.failure("视频更新失败，请稍后再试");
        }
        //修改文件信息
        video.setPath(dirPath + fileName);
        video.setRecordTime(date);
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
        /*Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("courses", teacherService.getAllCoursesWithChapter(teacher.getId()));*/
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(15));
        return "teacher/createCourseFile";
    }

    @ResponseBody
    @PostMapping("/saveCourseFile")
    public RequestResult saveCourseFile(@RequestParam(value = "file") MultipartFile file, @RequestParam("chapterId") Integer chapterId, @RequestParam("courseId") Integer courseId, HttpServletRequest request) {
        System.out.println(file.getOriginalFilename());
        System.out.println(chapterId);
//        Teacher teacher = (Teacher)request.getSession().getAttribute("user");

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
        /*Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("videos",teacherService.searchCourseVideo(teacher.getId()));*/
        CourseFileExample courseFileExample = (CourseFileExample) session.getAttribute("courseFileExample");
        if (courseFileExample == null) {
            courseFileExample = new CourseFileExample();
            List<Integer> ids = teacherService.getCourseIdsByTeacherId(15);
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
        PageHelper.startPage(pageNum, 10);
        List<CourseFile> files = teacherService.searchCourseFileByExampleWithCourseChapter(courseFileExample);
        for (CourseFile file : files) {
            String path = file.getPath();
            path = path.substring(path.indexOf('_') + 1);
            file.setPath(path);
        }
        PageInfo<CourseFile> page = new PageInfo(files, 10);
        map.put("pageInfo", page);
        map.put("colleges", teacherService.getAllColleges());
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
                courseExampleCriteria.andNameLike("%" + name.trim() + "%").andTeacherIdEqualTo(15);
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
                courseExampleCriteria.andCollegeIdEqualTo(collegeId).andTeacherIdEqualTo(15);
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

    @RequestMapping("/editCourseFile/{id}")
    public String editCourseFile(@PathVariable("id") Integer id,
                                 Map<String, Object> map, @RequestParam("pageNum") Integer pageNum,
                                 @RequestParam(value = "chapterId", required = false) Integer chapterId,
                                 @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                 @RequestParam(value = "minTime", defaultValue = "") String minTime,
                                 @RequestParam(value = "maxTime", defaultValue = "") String maxTime) {
        map.put("courseFileId", id);
        map.put("pageNum", pageNum);
        map.put("pageNumber", pageNumber);
        map.put("chapterId", chapterId);
        map.put("minTime", minTime);
        map.put("maxTime", maxTime);
        return "teacher/editCourseFile";
    }

    @ResponseBody
    @PostMapping("/updateCourseFile")
    public RequestResult updateCourseFile(@RequestParam(value = "file") MultipartFile file, @RequestParam("courseFileId") Integer courseFileId, HttpServletRequest request) {
        if (file == null) {
            return RequestResult.failure("文档更新失败，请稍后再试");
        }
        CourseFile courseFile = teacherService.getCourseFileById(courseFileId);
        if (courseFile == null) {
            return RequestResult.failure("文档更新失败，请稍后再试");
        }
        String basePath = request.getServletContext().getRealPath("/file/");
        String dirPath = courseFile.getPath().substring(0, courseFile.getPath().lastIndexOf("\\") + 1);
        String fileName = courseFile.getPath().substring(courseFile.getPath().lastIndexOf("\\") + 1);
        System.out.println(basePath + dirPath + fileName);
        //删除原来的文件
        File oldFile = new File(basePath + courseFile.getPath());
        if (!oldFile.exists()) {
            return RequestResult.failure("文档更新失败，请稍后再试");
        }
        boolean delete = oldFile.delete();
        if (!delete) {
            return RequestResult.failure("文档更新失败，请稍后再试");
        }
        Date date = new Date();
        fileName = date.getTime() + "_" + file.getOriginalFilename();
        System.out.println(basePath + dirPath + fileName);
        try {
            file.transferTo(new File(basePath + dirPath + fileName));
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

    @ExceptionHandler(IOException.class)
    public String IOExceptionHandler(IOException e, Map<String, Object> map) {
        map.put("exception", e);
        return "error";
    }

    @RequestMapping("/downloadCourseFile/{id}")
    public ResponseEntity<byte[]> downloadTeacherTemplate(@PathVariable("id") Integer id, HttpServletRequest request) throws IOException {
        String path = request.getServletContext().getRealPath("/file/");
        CourseFile courseFile = teacherService.getCourseFileById(id);
        String fileName = courseFile.getPath().substring(courseFile.getPath().indexOf("_") + 1);
        File file = new File(path + courseFile.getPath());
        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        // 转码，避免文件名显示不出中文
        fileName = URLEncoder.encode(fileName, "UTF-8");
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    @RequestMapping("/createDiscuss")
    public String createDiscuss(Map<String, Object> map, HttpSession session) {
		/*Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("courses", teacherService.getAllCoursesWithCollegeByTeacherId(teacher.getId()));*/
        map.put("courses", teacherService.getAllCoursesWithCollegeByTeacherId(15));
        return "teacher/createDiscuss";
    }

    @ResponseBody
    @PostMapping("/saveDiscuss")
    public RequestResult saveDiscuss(Discuss discuss) {
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
            List<Integer> ids = teacherService.getCourseIdsByTeacherId(15);
            if (ids.size() == 0) {
                ids.add(0);
            }
            criteria.andCourseIdIn(ids);
            discussExample.setOrderByClause("record_time desc");
        }
        PageHelper.startPage(pageNum, 10);
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
            List<Integer> ids = teacherService.getCourseIdsByTeacherId(15);
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
        //删除讨论的同时删除对应的讨论内容
        boolean b = teacherService.deleteDiscussByIdBatch(discussIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        boolean s = teacherService.deleteDiscussPostByDiscussIds(discussIds);
        return RequestResult.success();
    }

    @RequestMapping("/editDiscuss/{id}")
    public String editDiscuss(@PathVariable("id") Integer id, @RequestParam("pageNumber") Integer pageNumber, Map<String, Object> map, HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("discuss", teacherService.getDiscussById(id));
        map.put("pageNumber", pageNumber);
        map.put("courses", teacherService.getAllCoursesWithCollegeByTeacherId(15));
        return "teacher/updateDiscuss";
    }

    @ResponseBody
    @PostMapping("/updateDiscuss/{id}")
    public RequestResult updateDiscuss(@PathVariable("id") Integer id, Discuss discuss) {
        if (id == null || discuss == null) {
            return RequestResult.failure("讨论更新失败，请稍后再试");
        }
        Discuss discuss1 = teacherService.getDiscussById(id);
        if (discuss1 == null) {
            return RequestResult.failure("讨论更新失败，请稍后再试");
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
            map1.put("id", id);

        }/* else {
            map.put("name", map1.get("name"));
            map.put("minTime", map1.get("minTime"));
            map.put("maxTime", map1.get("maxTime"));
            map.put("content", map1.get("content"));
            session.removeAttribute("discussPostQueryCriteria");
        }*/
        PageHelper.startPage(pageNum, 10);
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
            map.put("id", id);
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
        if (id == null) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        if (content == null) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        if ("".equals(content)) {
            return RequestResult.failure("回复内容为空，请重新输入");
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

    @RequestMapping("/createDiscussReply/{id}")
    public String createReply(@PathVariable("id") Integer id, @RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageNum") Integer pageNum, Map<String, Object> map) {
        map.put("id", id);
        map.put("pageNumber", pageNumber);
        map.put("pageNum", pageNum);
        return "teacher/createDiscussReply";
    }

    @RequestMapping("/createTask")
    public String createTask(Map<String, Object> map) {
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(15));
        return "teacher/createTask";
    }

    @ResponseBody
    @PostMapping("/saveTask")
    public RequestResult saveTask(Task task, HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (task == null) {
            return RequestResult.failure("添加作业失败，请稍后再试");
        }
        task.setRecordTime(new Date());
//        task.setTeacherId(teacher.getId());
        task.setTeacherId(15);
        boolean success = teacherService.saveTask(task);
        if (!success) {
            return RequestResult.failure("添加作业失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchTask")
    public String searchTask(Map<String, Object> map, HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        TaskExample taskExample = (TaskExample) session.getAttribute("taskExample");
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (taskExample == null) {
            taskExample = new TaskExample();
            TaskExample.Criteria criteria = taskExample.createCriteria();
//            criteria.andTeacherIdEqualTo(teacher.getId());
            criteria.andTeacherIdEqualTo(15);
            taskExample.setOrderByClause("record_time desc");
        }
        PageHelper.startPage(pageNum, 10);
        List<Task> tasks = teacherService.getAllTasksWithCourseAndChapterExample(taskExample);
        PageInfo<Task> page = new PageInfo(tasks, 10);
        map.put("pageInfo", page);
        map.put("courses", teacherService.getAllCoursesWithCollegeByTeacherId(15));
        return "teacher/searchTask";
    }


    //异常处理
    @ExceptionHandler(RuntimeException.class)
    public String handRuntimeException(RuntimeException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return "error";
    }

    //异常处理
    @ExceptionHandler(ParseException.class)
    public String handParseException(ParseException e, HttpServletRequest request) {
        request.setAttribute("exception", e);
        return "error";
    }

    @ResponseBody
    @PostMapping("/deleteTask")
    public RequestResult deleteTask(@RequestParam("id") Integer id) {
        if (id == null) {
            return RequestResult.failure("作业删除失败，请稍后再试");
        }
        boolean success = teacherService.deleteTaskById(id);
        if (!success) {
            return RequestResult.failure("作业删除失败，请稍后再试");
        }
        return RequestResult.success();
    }


    @ResponseBody
    @PostMapping("/deleteTaskBatch")
    public RequestResult deleteTaskBatch(@RequestParam("ids") String id) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> taskIds = new ArrayList<>();
        for (String s : ids) {
            taskIds.add(Integer.parseInt(s));
        }
        boolean success = teacherService.deleteTaskByIdBatch(taskIds);
        if (!success) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/searchTaskByTerm")
    public RequestResult searchTaskByTerm(HttpSession session, @RequestParam("courseId") Integer courseId, @RequestParam("title") String title, @RequestParam("minTime") String minTime, @RequestParam("maxTime") String maxTime) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //将查询条件存放进session中，以回显查询条件
            Map<String, Object> map = new HashMap<>();
            map.put("courseId", null);
            map.put("title", null);
            map.put("minTime", null);
            map.put("maxTime", null);
            TaskExample taskExample = new TaskExample();
            TaskExample.Criteria criteria = taskExample.createCriteria();
            if (!"".equals(title.trim())) {
                map.put("title", title.trim());
                criteria.andTitleLike("%" + title.trim() + "%");
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
                criteria.andChapterIdIn(ids);
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
            criteria.andTeacherIdEqualTo(15);
            taskExample.setOrderByClause("record_time desc");
            session.setAttribute("taskQueryCriteria", map);
            session.setAttribute("taskExample", taskExample);
        } catch (Exception e) {
            return RequestResult.failure("查询失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/editTask/{id}")
    public String updateTask(@PathVariable("id") Integer id, Map<String, Object> map, HttpSession session, @RequestParam("pageNum") Integer pageNum) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("task", teacherService.getTaskWithCourseAndChapterById(id));
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(15));
        map.put("pageNum", pageNum);
        return "teacher/updateTask";
    }

    @ResponseBody
    @PostMapping("/updateTask/{id}")
    public RequestResult updateTask(@PathVariable("id") Integer id, Task task, HttpSession session) {
        System.out.println(id);
        System.out.println(task);
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (id == null || task == null) {
            return RequestResult.failure("修改作业失败，请稍后再试");
        }
        task.setId(id);
        task.setTeacherId(15);
        Task task1 = teacherService.getTaskById(id);
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
        boolean success = teacherService.updateTaskByPrimaryKeySelective(task);
        if (!success) {
            return RequestResult.failure("修改作业失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/searchTaskQuestion/{id}")
    public String searchQuestion(@PathVariable("id") Integer id, @RequestParam("pageNumber") Integer pageNumber, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, Map<String, Object> map) {
        PageHelper.startPage(pageNum, 10);
        List<TaskQuestion> questions = teacherService.getTaskQuestionsByTaskId(id);
        PageInfo<TaskQuestion> page = new PageInfo(questions, 10);
        map.put("pageInfo", page);
        map.put("pageNumber", pageNumber);
        map.put("taskId", id);
        return "teacher/searchTaskQuestion";
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
        boolean b = teacherService.deleteTaskQuestionByIdBatch(questionIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/createTaskQuestion/{id}")
    public String createTaskQuestion(@PathVariable("id") Integer id, Map<String, Object> map) {

        map.put("taskId", id);
        return "teacher/createTaskQuestion";
    }

    @ResponseBody
    @PostMapping("/saveTaskQuestion")
    public RequestResult saveTaskQuestion(TaskQuestion taskQuestion) {
        if (taskQuestion == null) {
            return RequestResult.failure("题目添加失败，请稍后再试");
        }
        boolean success = teacherService.insertTaskQuestion(taskQuestion);
        if (!success) {
            return RequestResult.failure("题目添加失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/editTaskQuestion/{id}")
    public String editTaskQuestion(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNumber") Integer pageNumber, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {

        map.put("pageNum", pageNum);
        map.put("pageNumber", pageNumber);
        map.put("taskQuestion", teacherService.getTaskQuestionsById(id));
        return "teacher/updateTaskQuestion";
    }

    @ResponseBody
    @PostMapping("/updateTaskQuestion")
    public RequestResult updateTaskQuestion(TaskQuestion taskQuestion) {
        if (taskQuestion == null) {
            return RequestResult.failure("题目修改失败，请稍后再试");
        }
        TaskQuestion question = teacherService.getTaskQuestionsById(taskQuestion.getId());
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
        taskQuestion.setTaskId(null);
        boolean success = teacherService.updateTaskQuestionByPrimaryKeySelective(taskQuestion);
        if (!success) {
            return RequestResult.failure("题目修改失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("createTaskQuestionBatch/{id}")
    public String createTaskQuestionBatch(@PathVariable("id") Integer id, Map<String, Object> map) {
        map.put("taskId", id);
        return "teacher/createTaskQuestionBatch";
    }

    //下载作业模板
    @RequestMapping("/downloadTaskQuestionTemplate")
    public ResponseEntity<byte[]> downloadTaskQuestionTemplate(HttpServletRequest request) throws IOException {
        String path = request.getServletContext().getRealPath("/excels/");
        String fileName = "Task_Template.xlsx";
        File file = new File(path + fileName);

        // 设置响应头通知浏览器下载
        HttpHeaders headers = new HttpHeaders();
        // 将对文件做的特殊处理还原
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/taskQuestionBatchImport")
    public RequestResult taskQuestionBatchImport(@RequestParam(value = "file") MultipartFile file, @RequestParam("taskId") Integer taskId) {
        String fileName = file.getOriginalFilename();
        List<TaskQuestion> taskQuestions = new ArrayList<>();
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
                    //原表头顺序：题目、选项A、选项B、选项C、选项D、答案、分值；使用数组来比较
                    String[] original_title = new String[]{"题目", "选项A", "选项B", "选项C", "选项D", "答案", "分值"};
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
                        TaskQuestion question = new TaskQuestion();
                        String data = POIUtils.getStringHSSF(hssfRow.getCell(0));
                        HSSFCell cell = hssfRow.getCell(0);
                        cell.setCellType(CellType.STRING);
                        question.setTitle(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(1));
                        question.setItemA(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(2));
                        question.setItemB(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(3));
                        question.setItemC(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(4));
                        question.setItemD(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(5));
                        if (!(data.contains("A") || data.contains("B") || data.contains("C") || data.contains("D"))) {
                            return RequestResult.failure("批量导入失败，请检查答案是否符合");
                        }
                        question.setAnswer(data);
                        data = POIUtils.getStringHSSF(hssfRow.getCell(6));
                        if (data.contains(" ")) {
                            return RequestResult.failure("批量导入失败，请检查分值中是否有空格");
                        }
                        //当用户传入的分值是小数时，该语句会抛出异常，被异常处理的方法catch住
                        question.setScore(Integer.parseInt(data));
                        question.setTaskId(taskId);
                        System.out.println(question);
                        taskQuestions.add(question);
                    }
                    boolean i = teacherService.insertTaskQuestionBatch(taskQuestions);
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
                    //原表头顺序：题目、选项A、选项B、选项C、选项D、答案、分值；使用数组来比较
                    String[] original_title = new String[]{"题目", "选项A", "选项B", "选项C", "选项D", "答案", "分值"};
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
                        TaskQuestion question = new TaskQuestion();
                        String data = POIUtils.getStringXSSF(xssfRow.getCell(0));
                        XSSFCell cell = xssfRow.getCell(0);
                        cell.setCellType(CellType.STRING);
                        question.setTitle(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(1));
                        question.setItemA(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(2));
                        question.setItemB(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(3));
                        question.setItemC(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(4));
                        question.setItemD(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(5));
                        if (!(data.contains("A") || data.contains("B") || data.contains("C") || data.contains("D"))) {
                            return RequestResult.failure("批量导入失败，请检查答案是否符合");
                        }
                        question.setAnswer(data);
                        data = POIUtils.getStringXSSF(xssfRow.getCell(6));
                        if (data.contains(" ")) {
                            return RequestResult.failure("批量导入失败，请检查分值中是否有空格");
                        }
                        //当用户传入的分值是小数时，该语句会抛出异常，被异常处理的方法catch住
                        question.setScore(Integer.parseInt(data));
                        question.setTaskId(taskId);
                        System.out.println(question);
                        taskQuestions.add(question);
                    }
                    boolean i = teacherService.insertTaskQuestionBatch(taskQuestions);
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
        /*Teacher teacher = (Teacher) session.getAttribute("user");
        map.put("teacher", teacherService.getTeacherByIdWithCollege(teacher.getId()));*/
        map.put("teacher", teacherService.getTeacherByIdWithCollege(15));
        return "teacher/updateTeacherInfo";
    }

    @ResponseBody
    @PostMapping("/updateTeacherInfo/{id}")
    public RequestResult updateTeacherInfo(@PathVariable("id") Integer id, Teacher teacher) {

        Teacher teacher1 = teacherService.getTeacherByIdWithoutCollegeAndCourse(id);
        if (teacher1 == null) {
            return RequestResult.failure("更新信息失败，请稍后再试");
        }
        teacher.setCollegeId(teacher1.getCollegeId());
        teacher.setNumber(teacher1.getNumber());
        System.out.println(teacher);
        System.out.println(teacher1);
        if (teacher1.equals(teacher)) {
            return RequestResult.failure("未修改任何数据");
        }
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
        if (!teacher1.getTelephone().equals(teacher.getTelephone())) {
            //修改电话，判断电话是否已经存在
            criteria.andTelephoneEqualTo(teacher.getTelephone());
            boolean exists = teacherService.isExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该手机号已经存在");
            }
        } else {
            teacher.setTelephone(null);
        }
        if (!teacher.getEmail().equals(teacher1.getEmail())) {
            teacherExample.clear();
            criteria = teacherExample.createCriteria();
            criteria.andEmailEqualTo(teacher.getEmail());
            boolean exists = teacherService.isExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该邮箱地址已经存在");
            }
        } else {
            teacher.setEmail(null);
        }
        if (!teacher.getAccount().equals(teacher1.getAccount())) {
            teacherExample.clear();
            criteria = teacherExample.createCriteria();
            criteria.andAccountEqualTo(teacher.getAccount());
            boolean exists = teacherService.isExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该账户已经存在");
            }
        } else {
            teacher.setAccount(null);
        }
        if (!teacher.getIdCardNo().equals(teacher1.getIdCardNo())) {
            teacherExample.clear();
            criteria = teacherExample.createCriteria();
            criteria.andIdCardNoEqualTo(teacher.getIdCardNo());
            boolean exists = teacherService.isExistsByExample(teacherExample);
            if (exists) {
                return RequestResult.failure("该身份证号已经存在");
            }
        } else {
            teacher.setIdCardNo(null);
        }
        if (teacher1.getGender().equals(teacher.getGender())) {
            teacher.setGender(null);
        }
        if (teacher1.getName().equals(teacher.getName())) {
            teacher.setName(null);
        }
        if (teacher1.getPassword().equals(teacher.getPassword())) {
            teacher.setPassword(null);
        }
        teacher.setNumber(null);
        teacher.setCollegeId(null);

        boolean b = teacherService.updateTeacherByPrimaryKeySelective(teacher);
        if (!b) {
            return RequestResult.failure("更新信息失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/viewTeacherInfo")
    public String viewTeacherInfo(Map<String, Object> map) {
        map.put("teacher", teacherService.getTeacherByIdWithCollege(15));
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(15));
        return "teacher/viewTeacherInfo";
    }


    @RequestMapping("/createChapter")
    public String createChapter(Map<String, Object> map, @RequestParam(value = "courseId", defaultValue = "0") Integer courseId) {
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(15));
        map.put("courseId", courseId);
        return "teacher/createChapter";
    }

    @ResponseBody
    @PostMapping("/saveChapter")
    public RequestResult saveChapter(CourseChapter chapter) {
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
    public String searchChapter(Map<String, Object> map, HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Teacher teacher = (Teacher) session.getAttribute("user");
        CourseChapterExample chapterExample = (CourseChapterExample) session.getAttribute("chapterExample");
        if (chapterExample == null) {
            chapterExample = new CourseChapterExample();
            CourseChapterExample.Criteria criteria = chapterExample.createCriteria();
            List<Integer> ids = teacherService.getCourseIdsByTeacherId(15);
            if (ids.size() == 0) {
                ids.add(0);
            }
            criteria.andCourseIdIn(ids);
            chapterExample.setOrderByClause("course_id asc,record_time asc");
        }
        PageHelper.startPage(pageNum, 10);
        List<CourseChapter> chapters = teacherService.getAllChapterWithBLOBsAndCourseByExample(chapterExample);
        PageInfo<CourseChapter> page = new PageInfo(chapters, 10);
        map.put("pageInfo", page);
        map.put("courses", teacherService.getAllCoursesWithCollegeByTeacherId(15));
        return "teacher/searchChapter";
    }

    @ResponseBody
    @PostMapping("/searchChapterByTerm")
    public RequestResult searchChapterByTerm(@RequestParam("courseId") Integer courseId, @RequestParam("minTime") String minTime, @RequestParam("maxTime") String maxTime, @RequestParam("title") String title, HttpSession session) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //将查询条件存放进session中，以回显查询条件
            Map<String, Object> map = new HashMap<>();
            map.put("courseId", null);
            map.put("title", null);
            map.put("minTime", null);
            map.put("maxTime", null);
            CourseChapterExample chapterExample = new CourseChapterExample();
            CourseChapterExample.Criteria criteria = chapterExample.createCriteria();
            if (!"".equals(title.trim())) {
                map.put("title", title.trim());
                criteria.andTitleLike("%" + title.trim() + "%");
            }
            if (courseId != 0) {
                map.put("courseId", courseId);
                criteria.andCourseIdEqualTo(courseId);
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
            List<Integer> ids = teacherService.getCourseIdsByTeacherId(15);
            if (ids.size() == 0) {
                ids.add(0);
            }
            criteria.andCourseIdIn(ids);
            chapterExample.setOrderByClause("record_time asc");
            session.setAttribute("chapterQueryCriteria", map);
            session.setAttribute("chapterExample", chapterExample);
        } catch (Exception e) {
            return RequestResult.failure("查询失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteChapterBatch")
    public RequestResult deleteChapterBatch(@RequestParam("ids") String id) {
        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> chapterIds = new ArrayList<>();
        for (String s : ids) {
            chapterIds.add(Integer.parseInt(s));
        }
        boolean success = teacherService.deleteChapterByIdBatch(chapterIds);
        if (!success) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();
    }


    @RequestMapping("/editChapter/{id}")
    public String editChapter(Map<String, Object> map, @PathVariable("id") Integer id, @RequestParam(value = "courseId", defaultValue = "0") Integer courseId, @RequestParam("pageNum") Integer pageNum) {
        map.put("courses", teacherService.getAllCoursesWithChapterAndCollegeByTeacherId(15));
        map.put("chapter", teacherService.getChapterById(id));
        map.put("courseId", courseId);
        map.put("pageNum", pageNum);
        return "teacher/updateChapter";
    }

    @ResponseBody
    @PostMapping("/updateChapter/{id}")
    public RequestResult updateChapter(CourseChapter chapter) {
        if (chapter == null) {
            return RequestResult.failure("章节修改失败，请稍后再试");
        }
        CourseChapter chapterById = teacherService.getChapterById(chapter.getId());
        System.out.println(chapter);
        System.out.println(chapterById);
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

    @RequestMapping("/viewChapterFiles/{id}")
    public String viewChapterFiles(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam(value = "minTime", defaultValue = "") String minTime, @RequestParam(value = "maxTime", defaultValue = "") String maxTime, @RequestParam("pageNum") Integer pageNum) throws ParseException {
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
        return "teacher/viewChapterFiles";
    }

    @ResponseBody
    @PostMapping("/logout")
    public RequestResult logout(HttpSession session) {
        session.invalidate();
        return RequestResult.success();
    }
}
