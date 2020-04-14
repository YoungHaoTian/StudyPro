package com.cdut.studypro.services;


import com.cdut.studypro.beans.*;

import java.util.List;
import java.util.Map;

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

    boolean deleteDiscussPostByIdBatch(List<Integer> ids);

    boolean insertDiscussPostSelective(DiscussPost discussPost);

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

    boolean saveDiscuss(Discuss discuss);

    List<Discuss> getAllDiscussWithBLOBsAndCourseByExample(DiscussExample discussExample);

    List<Course> getAllCoursesWithCollegeByTeacherId(Integer id);

    boolean deleteDiscussByIdBatch(List<Integer> discussIds);

    boolean deleteDiscussPostByDiscussIds(List<Integer> discussIds);

    List<DiscussPost> getAllDiscussPostByMapWithStudentName(Map<String, Object> map1);

    boolean saveTask(Task task);

    List<Task> getAllTasksWithCourseAndChapterExample(TaskExample taskExample);

    boolean deleteTaskById(Integer id);

    boolean deleteTaskByIdBatch(List<Integer> taskIds);

    Task getTaskWithCourseAndChapterById(Integer id);

    Task getTaskById(Integer id);

    boolean updateTaskByPrimaryKeySelective(Task task);

    Discuss getDiscussById(Integer id);

    boolean updateDiscussByPrimaryKeySelective(Discuss discuss);

    List<TaskQuestion> getTaskQuestionsByTaskId(Integer id);

    boolean deleteTaskQuestionByIdBatch(List<Integer> questionIds);

    boolean insertTaskQuestion(TaskQuestion taskQuestion);

    TaskQuestion getTaskQuestionsById(Integer id);

    boolean updateTaskQuestionByPrimaryKeySelective(TaskQuestion taskQuestion);

    boolean insertTaskQuestionBatch(List<TaskQuestion> taskQuestions);

    Teacher getTeacherByIdWithCollege(Integer id);

    Teacher getTeacherByIdWithoutCollegeAndCourse(Integer id);

    boolean insertCourseChapterSelective(CourseChapter chapter);

    List<CourseChapter> getAllChapterWithBLOBsAndCourseByExample(CourseChapterExample chapterExample);

    boolean deleteChapterByIdBatch(List<Integer> chapterIds);
}
