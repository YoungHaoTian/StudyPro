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
public interface StudentService {
    List<Student> selectStudentByExample(StudentExample example);

    boolean insertStudentSelective(Student student);

    boolean isExistsByExample(StudentExample example);

    boolean updateStudentByPrimaryKeySelective(Student student);

    Integer getIdByTelephone(String phone);

    List<Course> getAllCourseWithBLOBsAndTeacherByExample(CourseExample courseExample);

    List<Integer> getTeacherIdByTeacherExample(TeacherExample teacherExample);

    List<Integer> getJoinCourseIdByStudentId(Integer id);

    boolean joinCourse(Collect collect);

    boolean joinCourseBatch(List<Collect> collects);

    boolean removeCourseBatch(List<Integer> courseIds, Integer id);

    boolean removeCourse(Integer courseId, Integer studentId);

    List<CourseChapter> getAllChapterWithBLOBsAndCourseByExample(CourseChapterExample courseChapterExample);

    List<CourseVideo> getCourseVideoByExample(CourseVideoExample courseVideoExample);

    List<CourseFile> getCourseFileByExample(CourseFileExample courseFileExample);

    CourseFile getCourseFileById(Integer id);

    CourseVideo getCourseVideoById(Integer id);

    List<Course> getCourseWithChapterAndVideoByExample(CourseExample courseExample);

    List<Course> getCourseByCourseIdsWithNameAndTeacher(List<Integer> courseIds);

    List<Integer> getChapterIdsByCourseIds(List<Integer> ids);

    List<CourseVideo> getCourseVideoByExampleWithCourseChapter(CourseVideoExample courseVideoExample);

    List<Course> getCourseWithChapterAndFileByExample(CourseExample courseExample);

    List<Discuss> getDiscussWithCourseByExample(DiscussExample discussExample);

    List<DiscussPost> getAllDiscussPostByMapWithStudentName(Map<String, Object> map);

    boolean insertDiscussPostSelective(DiscussPost discussPost);

    List<Notice> getAllNotices();

    Course getCourseWithChapterAndTeacherByCourseId(Integer courseId);

    List<OnlineTask> getAllOnlineTasksByChapterId(Integer id);

    List<OfflineTask> getAllOfflineTasksByChapterId(Integer id);

    List<OnlineTaskQuestion> getTaskQuestionsWithTitleAndItemByTaskId(Integer id);

    List<OnlineTaskQuestion> getTaskQuestionsWithAnswerAndScoreByIds(List<Integer> questionIds);

    List<OnlineTaskQuestion> getTaskQuestionsByTaskId(Integer id);

    List<Integer> getFinishTaskIdByStudentId(Integer id);

    List<Integer> getFinishTaskIdByStudentIdAndTaskIds(Integer id, List<Integer> taskIds);

    List<StudentOnlineTask> getFinishTaskByStudentId(Integer id);

    List<StudentOnlineTask> getFinishOnlineTaskByStudentIdAndTaskIds(Integer studentId, List<Integer> taskIds);

    boolean insertStudentOnlineTaskSelective(StudentOnlineTask studentTask);

    List<OnlineTask> getAllOnlineTasksWithChapterByTaskIds(List<Integer> taskIds);

    List<Integer> getChapterIdsByCourseId(Integer courseId);

    List<Integer> getOnlineTaskIdsByChapterIds(List<Integer> chapterIds);

    List<Integer> getTaskIdsByChapterId(Integer id);

    boolean isTaskFinish(Integer id);

    Student getStudentByStudentIdWithCollege(Integer id);

    Student getStudentByStudentId(Integer id);

    List<StudentOfflineTask> getFinishOfflineTaskByStudentIdAndTaskIds(Integer studentId, List<Integer> taskIds);

    boolean uploadOfflineTaskFile(StudentOfflineTask studentOfflineTask);

    StudentOfflineTask getStudentOfflineTask(Integer studentTaskId);

    boolean reUploadOfflineTaskFile(StudentOfflineTask studentOfflineTask);

    Integer getOnlineTaskTotalScore(Integer id);

    OnlineTask getOnlineTask(Integer id);

    List<Integer> getOfflineTaskIdsByChapterIds(List<Integer> chapterIds);

    List<OfflineTask> getAllOfflineTasksWithChapterByTaskIds(List<Integer> taskIds);
}
