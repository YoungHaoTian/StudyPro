package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.services.TeacherService;
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
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
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
            courseVideoExample.setOrderByClause("record_time asc");
        }
        PageHelper.startPage(pageNum, 10);
        List<CourseVideo> videos = teacherService.searchCourseVideoByExampleWithCourseChapter(courseVideoExample);
        for (CourseVideo video : videos) {
            String path = video.getPath();
            path = path.substring(path.indexOf('_') + 1);
            video.setPath(path);
        }
        PageInfo<Student> page = new PageInfo(videos, 10);
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
        if (videos == null || videos.size() == 0) {
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
    public RequestResult searchCourseVideoByTerm(@RequestParam("name") String name, @RequestParam("collegeId") Integer collegeId, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        CourseVideoExample courseVideoExample = new CourseVideoExample();
        CourseVideoExample.Criteria criteria = courseVideoExample.createCriteria();
        Teacher teacher = (Teacher) session.getAttribute("user");
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
        courseVideoExample.setOrderByClause("record_time asc");
        session.setAttribute("courseVideoExample", courseVideoExample);
        session.setAttribute("courseVideoQueryCriteria", map);
        return RequestResult.success();
    }

    @RequestMapping("/editCourseVideo/{id}")
    public String editCourseVideo(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        map.put("courseVideoId", id);
        map.put("pageNum", pageNum);
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
        if (!oldFile.exists()) {
            return RequestResult.failure("视频更新失败，请稍后再试");
        }
        boolean delete = oldFile.delete();
        if (!delete) {
            return RequestResult.failure("视频更新失败，请稍后再试");
        }
        Date date = new Date();
        fileName = date.getTime() + "_" + file.getOriginalFilename();
        System.out.println(basePath + dirPath + fileName);
        try {
            file.transferTo(new File(basePath + dirPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return RequestResult.failure("视频更新失败，请稍后再试");
        }
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
            courseFileExample.setOrderByClause("record_time asc");
        }
        PageHelper.startPage(pageNum, 10);
        List<CourseFile> files = teacherService.searchCourseFileByExampleWithCourseChapter(courseFileExample);
        for (CourseFile file : files) {
            String path = file.getPath();
            path = path.substring(path.indexOf('_') + 1);
            file.setPath(path);
        }
        PageInfo<Student> page = new PageInfo(files, 10);
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
        if (files == null || files.size() == 0) {
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
    public RequestResult searchCourseFileByTerm(@RequestParam("name") String name, @RequestParam("collegeId") Integer collegeId, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        CourseFileExample courseFileExample = new CourseFileExample();
        CourseFileExample.Criteria criteria = courseFileExample.createCriteria();
        Teacher teacher = (Teacher) session.getAttribute("user");
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
        courseFileExample.setOrderByClause("record_time asc");
        session.setAttribute("courseFileExample", courseFileExample);
        session.setAttribute("courseFileQueryCriteria", map);
        return RequestResult.success();
    }

    @RequestMapping("/editCourseFile/{id}")
    public String editCourseFile(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam("pageNum") Integer pageNum) {
        map.put("courseFileId", id);
        map.put("pageNum", pageNum);
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

    /*@ResponseBody
    @PostMapping("/searchDiscussPostByTerm")
    public RequestResult searchDiscussPostByTerm(@RequestParam("name") String name, HttpSession session) {
        //将查询条件存放进session中，以回显查询条件
        Map<String, Object> map = new HashMap<>();
        map.put("name", null);
        if (!"".equals(name.trim())) {
            map.put("name", name.trim());
        }
        session.setAttribute("discussPostQueryCriteria", map);
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
        boolean b = adminService.deleteDiscussPostByIdBatch(postIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();

    }


    @RequestMapping("/searchDiscussReply/{id}")
    public String searchDiscussReply(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam("pageNumber") Integer pageNumber, HttpSession session) {

        Map<String, Object> map1 = (Map<String, Object>) session.getAttribute("discussPostQueryCriteria");
        if (map1 == null) {
            map1 = new HashMap<>();
            map1.put("id", id);
            map1.put("name", null);
        } else {
            map1.put("id", id);
        }
        PageHelper.startPage(pageNum, 10);
        List<DiscussPost> discussPosts = adminService.getDiscussPostByDiscussIdWithStudentName(map1);
        PageInfo<Student> page = new PageInfo(discussPosts, 10);
        map.put("pageInfo", page);
        map.put("pageNumber", pageNumber);
        map.put("id", id);
        return "teacher/searchDiscussReply";
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
        boolean b = adminService.insertDiscussPostSelective(discussPost);
        if (!b) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/createReply/{id}")
    public String createReply(@PathVariable("id") Integer id, Map<String, Object> map) {
        map.put("id", id);
        return "admin/createReply";
    }*/
}
