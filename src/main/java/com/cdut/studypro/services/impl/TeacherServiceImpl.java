package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.daos.*;
import com.cdut.studypro.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:55
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private CourseVideoMapper courseVideoMapper;

    @Autowired
    private CourseFileMapper courseFileMapper;

    @Autowired
    private CourseChapterMapper courseChapterMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Override
    public List<Teacher> selectTeacherByExample(TeacherExample example) {
        return teacherMapper.selectByExample(example);
    }

    @Override
    public boolean isExistsByExample(TeacherExample example) {
        long l = teacherMapper.countByExample(example);
        if (l != 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTeacherByPrimaryKeySelective(Teacher teacher) {
        int i = teacherMapper.updateByPrimaryKeySelective(teacher);
        if (i > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Integer getIdByTelephone(String phone) {
        return teacherMapper.getIdByTelephone(phone);
    }

    @Override
    public List<Course> getAllCoursesWithChapterAndCollegeByTeacherId(Integer id) {
        return courseMapper.selectByTeacherIdWithChapterAndCollege(id);
    }

/*
    @Override
    public List<DiscussPost> getDiscussPostByDiscussId(Integer id) {
        DiscussPostExample discussPostExample = new DiscussPostExample();
        DiscussPostExample.Criteria criteria = discussPostExample.createCriteria();
        criteria.andDiscussIdEqualTo(id);
        discussPostExample.setOrderByClause("record_time asc");
        return discussPostMapper.selectByExampleWithBLOBsAndStudentName(discussPostExample);
    }

    @Override
    public boolean deleteDiscussPostByIdBatch(List<Integer> ids) {
        DiscussPostExample discussPostExample = new DiscussPostExample();
        DiscussPostExample.Criteria criteria = discussPostExample.createCriteria();
        criteria.andIdIn(ids);
        return discussPostMapper.deleteByExample(discussPostExample) > 0;
    }

    @Override
    public boolean insertDiscussPostSelective(DiscussPost discussPost) {
        return discussPostMapper.insertSelective(discussPost) > 0;
    }*/

    @Override
    public boolean saveCourseVideo(CourseVideo courseVideo) {
        return courseVideoMapper.insertSelective(courseVideo) > 0;
    }

    @Override
    public List<CourseVideo> searchCourseVideoByExampleWithCourseChapter(CourseVideoExample courseVideoExample) {
        return courseVideoMapper.selectByExampleWithCourseChapter(courseVideoExample);
    }

    @Override
    public List<Integer> getCourseIdsByTeacherId(Integer id) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andTeacherIdEqualTo(id);
        return courseMapper.selectCourseIdByExample(courseExample);
    }

    @Override
    public List<Integer> getChapterIdsByCourseIds(List<Integer> ids) {
        CourseChapterExample courseChapterExample = new CourseChapterExample();
        CourseChapterExample.Criteria criteria = courseChapterExample.createCriteria();
        criteria.andCourseIdIn(ids);
        return courseChapterMapper.selectChapterIdByExample(courseChapterExample);
    }

    @Override
    public List<Integer> getVideoIdsByChapterIds(List<Integer> ids) {
        CourseVideoExample courseVideoExample = new CourseVideoExample();
        CourseVideoExample.Criteria criteria = courseVideoExample.createCriteria();
        criteria.andChapterIdIn(ids);
        return courseVideoMapper.selectVideoIdByExample(courseVideoExample);
    }

    @Override
    public List<College> getAllColleges() {
        return collegeMapper.selectByExample(null);
    }

    @Override
    public boolean deleteCourseVideoById(Integer id) {
        return courseVideoMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public CourseVideo getCourseVideoById(Integer id) {
        return courseVideoMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean deleteCourseVideoByIdBatch(List<Integer> videoIds) {
        CourseVideoExample courseVideoExample = new CourseVideoExample();
        CourseVideoExample.Criteria criteria = courseVideoExample.createCriteria();
        criteria.andIdIn(videoIds);
        return courseVideoMapper.deleteByExample(courseVideoExample) > 0;
    }

    @Override
    public List<CourseVideo> getCourseVideoByIds(List<Integer> videoIds) {
        CourseVideoExample courseVideoExample = new CourseVideoExample();
        CourseVideoExample.Criteria criteria = courseVideoExample.createCriteria();
        criteria.andIdIn(videoIds);
        return courseVideoMapper.selectByExample(courseVideoExample);
    }

    @Override
    public List<Integer> getCourseIdsByExample(CourseExample courseExample) {
        return courseMapper.selectCourseIdByExample(courseExample);
    }

    @Override
    public List<Integer> getChapterIdsByExample(CourseChapterExample courseChapterExample) {
        return courseChapterMapper.selectChapterIdByExample(courseChapterExample);
    }

    @Override
    public boolean updateCourseVideoByPrimaryKeySelective(CourseVideo video) {
        return courseVideoMapper.updateByPrimaryKeySelective(video) > 0;
    }

    @Override
    public boolean saveCourseFile(CourseFile courseFile) {
        return courseFileMapper.insertSelective(courseFile) > 0;
    }

    @Override
    public List<Integer> getFileIdsByChapterIds(List<Integer> ids) {
        CourseFileExample courseFileExample = new CourseFileExample();
        CourseFileExample.Criteria criteria = courseFileExample.createCriteria();
        criteria.andChapterIdIn(ids);
        return courseFileMapper.selectFileIdByExample(courseFileExample);
    }

    @Override
    public List<CourseFile> searchCourseFileByExampleWithCourseChapter(CourseFileExample courseFileExample) {
        return courseFileMapper.selectByExampleWithCourseChapter(courseFileExample);
    }

    @Override
    public CourseFile getCourseFileById(Integer id) {
        return courseFileMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean deleteCourseFileById(Integer id) {
        return courseFileMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public List<CourseFile> getCourseFileByIds(List<Integer> videoIds) {
        CourseFileExample courseFileExample = new CourseFileExample();
        CourseFileExample.Criteria criteria = courseFileExample.createCriteria();
        criteria.andIdIn(videoIds);
        return courseFileMapper.selectByExample(courseFileExample);
    }

    @Override
    public boolean deleteCourseFileByIdBatch(List<Integer> fileIds) {
        CourseFileExample courseFileExample = new CourseFileExample();
        CourseFileExample.Criteria criteria = courseFileExample.createCriteria();
        criteria.andIdIn(fileIds);
        return courseFileMapper.deleteByExample(courseFileExample) > 0;
    }

    @Override
    public boolean updateCourseFileByPrimaryKeySelective(CourseFile courseFile) {
        return courseFileMapper.updateByPrimaryKeySelective(courseFile) > 0;
    }


}
