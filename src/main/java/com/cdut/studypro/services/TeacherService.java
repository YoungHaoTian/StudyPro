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

    boolean saveTask(OnlineTask task);

    List<OnlineTask> getAllTasksWithCourseAndChapterExample(OnlineTaskExample taskExample);

    boolean deleteTaskById(Integer id);

    boolean deleteTaskByIdBatch(List<Integer> taskIds);

    OnlineTask getTaskWithCourseAndChapterById(Integer id);

    OnlineTask getTaskById(Integer id);

    boolean updateTaskByPrimaryKeySelective(OnlineTask task);

    Discuss getDiscussById(Integer id);

    boolean updateDiscussByPrimaryKeySelective(Discuss discuss);

    List<OnlineTaskQuestion> getTaskQuestionsByTaskId(Integer id);

    boolean deleteTaskQuestionByIdBatch(List<Integer> questionIds);

    boolean insertTaskQuestion(OnlineTaskQuestion taskQuestion);

    OnlineTaskQuestion getTaskQuestionsById(Integer id);

    boolean updateTaskQuestionByPrimaryKeySelective(OnlineTaskQuestion taskQuestion);

    boolean insertTaskQuestionBatch(List<OnlineTaskQuestion> taskQuestions);

    Teacher getTeacherByIdWithCollege(Integer id);

    Teacher getTeacherByIdWithoutCollegeAndCourse(Integer id);

    boolean insertCourseChapterSelective(CourseChapter chapter);

    List<CourseChapter> getAllChapterWithBLOBsAndCourseByExample(CourseChapterExample chapterExample);

    boolean deleteChapterByIdBatch(List<Integer> chapterIds);

    CourseChapter getChapterById(Integer id);

    boolean updateCourseChapterByIdSelective(CourseChapter chapter);

    List<CourseVideo> getCourseVideoByChapterId(Integer id);

    List<CourseFile> getCourseFileByChapterId(Integer id);

    List<OnlineTask> getTaskByChapterId(Integer id);

    List<CourseVideo> getCourseVideoByExample(CourseVideoExample example);

    List<CourseFile> getCourseFileByExample(CourseFileExample courseFileExample);

    List<Course> getAllCoursesWithCollegeByExample(CourseExample courseExample);

    List<College> getAllCollegesByTeacherId(Integer id);

    List<OnlineTask> getTaskByChapterIdWithChapterAndCourse(Integer id);
}
