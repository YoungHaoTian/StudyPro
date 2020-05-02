package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.daos.*;
import com.cdut.studypro.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:55
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private CourseChapterMapper courseChapterMapper;

    @Autowired
    private CourseVideoMapper courseVideoMapper;

    @Autowired
    private CourseFileMapper courseFileMapper;

    @Autowired
    private DiscussMapper discussMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private OnlineTaskMapper taskMapper;

    @Autowired
    private OnlineTaskQuestionMapper taskQuestionMapper;

    @Autowired
    private StudentOnlineTaskMapper studentOnlineTaskMapper;

    @Override
    public List<Student> selectStudentByExample(StudentExample example) {
        return studentMapper.selectByExample(example);
    }

    @Override
    public boolean insertStudentSelective(Student student) {
        int i = studentMapper.insertSelective(student);
        return i > 0;
    }

    @Override
    public boolean isExistsByExample(StudentExample example) {
        long l = studentMapper.countByExample(example);
        return l > 0;
    }

    @Override
    public boolean updateStudentByPrimaryKeySelective(Student student) {
        int i = studentMapper.updateByPrimaryKeySelective(student);
        return i > 0;
    }

    @Override
    public Integer getIdByTelephone(String phone) {
        return studentMapper.getIdByTelephone(phone);
    }

    @Override
    public List<Course> getAllCourseWithBLOBsAndTeacherByExample(CourseExample courseExample) {

        return courseMapper.selectByExampleWithBLOBsAndTeacher(courseExample);
    }

    @Override
    public List<Integer> getTeacherIdByTeacherExample(TeacherExample teacherExample) {
        return teacherMapper.selectIdsByExample(teacherExample);
    }

    @Override
    public List<Integer> getJoinCourseIdByStudentId(Integer id) {
        return collectMapper.selectCourseIdByStudentId(id);
    }

    @Override
    public boolean joinCourse(Collect collect) {
        return collectMapper.insert(collect) > 0;
    }

    @Override
    public boolean joinCourseBatch(List<Collect> collects) {
        return collectMapper.insertBatch(collects) > 0;
    }

    @Override
    public boolean removeCourseBatch(List<Integer> courseIds, Integer id) {
        CollectExample collectExample = new CollectExample();
        CollectExample.Criteria criteria = collectExample.createCriteria();
        criteria.andCourseIdIn(courseIds).andStudentIdEqualTo(id);
        return collectMapper.deleteByExample(collectExample) > 0;
    }

    @Override
    public boolean removeCourse(Integer courseId, Integer studentId) {
        CollectExample collectExample = new CollectExample();
        CollectExample.Criteria criteria = collectExample.createCriteria();
        criteria.andStudentIdEqualTo(studentId).andCourseIdEqualTo(courseId);
        return collectMapper.deleteByExample(collectExample) > 0;
    }

    @Override
    public List<CourseChapter> getAllChapterWithBLOBsAndCourseByExample(CourseChapterExample courseChapterExample) {
        return courseChapterMapper.selectByExampleWithBLOBsAndCourseWithoutCollege(courseChapterExample);
    }

    @Override
    public List<CourseVideo> getCourseVideoByExample(CourseVideoExample courseVideoExample) {
        return courseVideoMapper.selectByExample(courseVideoExample);
    }

    @Override
    public List<CourseFile> getCourseFileByExample(CourseFileExample courseFileExample) {
        return courseFileMapper.selectByExample(courseFileExample);
    }

    @Override
    public CourseFile getCourseFileById(Integer id) {
        return courseFileMapper.selectByPrimaryKey(id);
    }

    @Override
    public CourseVideo getCourseVideoById(Integer id) {
        return courseVideoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Course> getCourseWithChapterAndVideoByExample(CourseExample courseExample) {
        return courseMapper.selectByExampleWithChapterAndVideo(courseExample);
    }

    @Override
    public List<Course> getCourseByCourseIdsWithNameAndTeacher(List<Integer> courseIds) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andIdIn(courseIds);
        courseExample.setOrderByClause("CONVERT(name using gbk) asc");
        return courseMapper.selectByExampleWithIdAndNameAndTeacher(courseExample);
    }

    @Override
    public List<Integer> getChapterIdsByCourseIds(List<Integer> ids) {
        CourseChapterExample courseChapterExample = new CourseChapterExample();
        CourseChapterExample.Criteria criteria = courseChapterExample.createCriteria();
        criteria.andCourseIdIn(ids);
        return courseChapterMapper.selectChapterIdByExample(courseChapterExample);
    }

    @Override
    public List<CourseVideo> getCourseVideoByExampleWithCourseChapter(CourseVideoExample courseVideoExample) {
        return courseVideoMapper.selectByExampleWithChapterAndCourseAndTeacher(courseVideoExample);
    }

    @Override
    public List<Course> getCourseWithChapterAndFileByExample(CourseExample courseExample) {
        return courseMapper.selectByExampleWithChapterAndFile(courseExample);
    }

    @Override
    public List<Discuss> getDiscussWithCourseByExample(DiscussExample discussExample) {
        return discussMapper.selectByExampleWithBLOBsAndTeacherAndCourse(discussExample);
    }

    @Override
    public List<DiscussPost> getAllDiscussPostByMapWithStudentName(Map<String, Object> map) {
        return discussPostMapper.selectByDiscussIdWithStudentName(map);
    }

    @Override
    public boolean insertDiscussPostSelective(DiscussPost discussPost) {
        return discussPostMapper.insertSelective(discussPost) > 0;
    }

    @Override
    public List<Notice> getAllNotices() {
        NoticeExample noticeExample = new NoticeExample();
        noticeExample.setOrderByClause("record_time desc");
        return noticeMapper.selectByExampleWithBLOBs(noticeExample);
    }

    @Override
    public Course getCourseWithChapterAndTeacherByCourseId(Integer courseId) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andIdEqualTo(courseId);
        return courseMapper.selectByExampleWithChapterAndTeacher(courseExample).get(0);
    }

    @Override
    public List<OnlineTask> getAllTasksByChapterId(Integer id) {
        OnlineTaskExample taskExample = new OnlineTaskExample();
        OnlineTaskExample.Criteria criteria = taskExample.createCriteria();
        criteria.andChapterIdEqualTo(id);
        taskExample.setOrderByClause("record_time desc");
        return taskMapper.selectByExample(taskExample);
    }

    @Override
    public List<OnlineTaskQuestion> getTaskQuestionsWithTitleAndItemByTaskId(Integer id) {
        OnlineTaskQuestionExample taskQuestionExample = new OnlineTaskQuestionExample();
        OnlineTaskQuestionExample.Criteria criteria = taskQuestionExample.createCriteria();
        criteria.andOnlineTaskIdEqualTo(id);
        taskQuestionExample.setOrderByClause("id asc");
        return taskQuestionMapper.selectByExampleWithTitleAndItem(taskQuestionExample);
    }

    @Override
    public List<OnlineTaskQuestion> getTaskQuestionsWithAnswerAndScoreByIds(List<Integer> questionIds) {
        OnlineTaskQuestionExample taskQuestionExample = new OnlineTaskQuestionExample();
        OnlineTaskQuestionExample.Criteria criteria = taskQuestionExample.createCriteria();
        criteria.andIdIn(questionIds);
        taskQuestionExample.setOrderByClause("id asc");
        return taskQuestionMapper.selectByExampleWithAnswerAndScore(taskQuestionExample);
    }

    @Override
    public List<OnlineTaskQuestion> getTaskQuestionsByTaskId(Integer id) {
        OnlineTaskQuestionExample taskQuestionExample = new OnlineTaskQuestionExample();
        OnlineTaskQuestionExample.Criteria criteria = taskQuestionExample.createCriteria();
        criteria.andOnlineTaskIdEqualTo(id);
        taskQuestionExample.setOrderByClause("id asc");
        return taskQuestionMapper.selectByExample(taskQuestionExample);
    }

    @Override
    public List<Integer> getFinishTaskIdByStudentId(Integer id) {
        StudentOnlineTaskExample studentTaskExample = new StudentOnlineTaskExample();
        StudentOnlineTaskExample.Criteria criteria = studentTaskExample.createCriteria();
        criteria.andStudentIdEqualTo(id);
        return studentOnlineTaskMapper.selectTaskIdsByExample(studentTaskExample);
    }

    @Override
    public List<Integer> getFinishTaskIdByStudentIdAndTaskIds(Integer id, List<Integer> taskIds) {
        StudentOnlineTaskExample studentTaskExample = new StudentOnlineTaskExample();
        StudentOnlineTaskExample.Criteria criteria = studentTaskExample.createCriteria();
        criteria.andStudentIdEqualTo(id).andOnlineTaskIdIn(taskIds);
        return studentOnlineTaskMapper.selectTaskIdsByExample(studentTaskExample);
    }

    @Override
    public List<StudentOnlineTask> getFinishTaskByStudentIdAndTaskIds(Integer studentId, List<Integer> taskIds) {
        StudentOnlineTaskExample studentTaskExample = new StudentOnlineTaskExample();
        StudentOnlineTaskExample.Criteria criteria = studentTaskExample.createCriteria();
        criteria.andStudentIdEqualTo(studentId).andOnlineTaskIdIn(taskIds);
        return studentOnlineTaskMapper.selectByExample(studentTaskExample);
    }

    @Override
    public boolean insertStudentTaskSelective(StudentOnlineTask studentTask) {
        return studentOnlineTaskMapper.insertSelective(studentTask) > 0;
    }

    @Override
    public List<OnlineTask> getAllTasksWithChapterByTaskIds(List<Integer> taskIds) {
        OnlineTaskExample taskExample = new OnlineTaskExample();
        OnlineTaskExample.Criteria criteria = taskExample.createCriteria();
        criteria.andIdIn(taskIds);
        taskExample.setOrderByClause("record_time asc");
        return taskMapper.selectByExampleWithChapter(taskExample);
    }

    @Override
    public List<Integer> getChapterIdsByCourseId(Integer courseId) {
        CourseChapterExample courseChapterExample = new CourseChapterExample();
        CourseChapterExample.Criteria criteria = courseChapterExample.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        return courseChapterMapper.selectChapterIdByExample(courseChapterExample);
    }

    @Override
    public List<Integer> getTaskIdsByChapterIds(List<Integer> chapterIds) {
        OnlineTaskExample taskExample = new OnlineTaskExample();
        OnlineTaskExample.Criteria criteria = taskExample.createCriteria();
        criteria.andChapterIdIn(chapterIds);
        return taskMapper.selectOnlineTaskIdsByExample(taskExample);
    }

    @Override
    public List<StudentOnlineTask> getFinishTaskByStudentId(Integer id) {
        StudentOnlineTaskExample studentTaskExample = new StudentOnlineTaskExample();
        StudentOnlineTaskExample.Criteria criteria = studentTaskExample.createCriteria();
        criteria.andStudentIdEqualTo(id);
        return studentOnlineTaskMapper.selectByExample(studentTaskExample);
    }

    @Override
    public List<Integer> getTaskIdsByChapterId(Integer id) {
        OnlineTaskExample taskExample = new OnlineTaskExample();
        OnlineTaskExample.Criteria criteria = taskExample.createCriteria();
        criteria.andChapterIdEqualTo(id);
        return taskMapper.selectOnlineTaskIdsByExample(taskExample);
    }

    @Override
    public boolean isTaskFinish(Integer id) {
        StudentOnlineTaskExample studentTaskExample = new StudentOnlineTaskExample();
        StudentOnlineTaskExample.Criteria criteria = studentTaskExample.createCriteria();
        criteria.andOnlineTaskIdEqualTo(id);
        return studentOnlineTaskMapper.countByExample(studentTaskExample) > 0;
    }

    @Override
    public Student getStudentByStudentIdWithCollege(Integer id) {
        return studentMapper.selectByPrimaryKeyWithCollege(id);
    }

    @Override
    public Student getStudentByStudentId(Integer id) {
        return studentMapper.selectByPrimaryKey(id);
    }


}
