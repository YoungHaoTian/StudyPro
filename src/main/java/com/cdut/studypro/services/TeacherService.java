package com.cdut.studypro.services;


import com.cdut.studypro.beans.*;

import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:54
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public interface TeacherService {
    List<Teacher> selectTeacherByExample(TeacherExample example);

    boolean isExistsByExample(TeacherExample example);

    boolean updateTeacherByPrimaryKeySelective(Teacher teacher);

    Integer getIdByTelephone(String phone);

    List<Course> getAllCoursesWithChapterAndCollegeByTeacherId(Integer id);


    /*List<DiscussPost> getDiscussPostByDiscussId(Integer id);

    boolean deleteDiscussPostByIdBatch(List<Integer> ids);

    boolean insertDiscussPostSelective(DiscussPost discussPost);*/

    boolean saveCourseVideo(CourseVideo collegeVideo);


    List<CourseVideo> searchCourseVideoByExampleWithCourseChapter(CourseVideoExample courseVideoExample);

    List<Integer> getCourseIdsByTeacherId(Integer id);

    List<Integer> getChapterIdsByCourseIds(List<Integer> ids);

    List<Integer> getVideoIdsByChapterIds(List<Integer> ids);

    List<College> getAllColleges();

    boolean deleteCourseVideoById(Integer id);

    CourseVideo getCourseVideoById(Integer id);

    boolean deleteCourseVideoByIdBatch(List<Integer> videoIds);

    List<CourseVideo> getCourseVideoByIds(List<Integer> videoIds);

    List<Integer> getCourseIdsByExample(CourseExample courseExample);

    List<Integer> getChapterIdsByExample(CourseChapterExample courseChapterExample);

    boolean updateCourseVideoByPrimaryKeySelective(CourseVideo video);

    boolean saveCourseFile(CourseFile courseFile);

    List<Integer> getFileIdsByChapterIds(List<Integer> ids);

    List<CourseFile> searchCourseFileByExampleWithCourseChapter(CourseFileExample courseFileExample);

    CourseFile getCourseFileById(Integer id);

    boolean deleteCourseFileById(Integer id);

    List<CourseFile> getCourseFileByIds(List<Integer> fileIds);

    boolean deleteCourseFileByIdBatch(List<Integer> fileIds);

    boolean updateCourseFileByPrimaryKeySelective(CourseFile courseFile);
}
