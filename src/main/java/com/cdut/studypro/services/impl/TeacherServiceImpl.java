package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.daos.*;
import com.cdut.studypro.services.TeacherService;
import org.apache.poi.ss.formula.functions.T;
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
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private StudentMapper studentMapper;

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
    private DiscussMapper discussMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskQuestionMapper taskQuestionMapper;

    @Override
    public List<Teacher> selectTeacherByExample(TeacherExample example) {
        return teacherMapper.selectByExample(example);
    }

    @Override
    public boolean isExistsByExample(TeacherExample example) {
        long l = teacherMapper.countByExample(example);
        return l != 0;
    }

    @Override
    public boolean updateTeacherByPrimaryKeySelective(Teacher teacher) {
        int i = teacherMapper.updateByPrimaryKeySelective(teacher);
        return i > 0;
    }

    @Override
    public Integer getIdByTelephone(String phone) {
        return teacherMapper.getIdByTelephone(phone);
    }

    @Override
    public List<Course> getAllCoursesWithChapterAndCollegeByTeacherId(Integer id) {
        return courseMapper.selectByTeacherIdWithChapterAndCollege(id);
    }

    @Override
    public List<Course> getAllCoursesWithCollegeByTeacherId(Integer id) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andTeacherIdEqualTo(id);
        courseExample.setOrderByClause("CONVERT(name using gbk) asc");
        return courseMapper.selectByExampleWithCollegeAndTeacher(courseExample);
    }

    @Override
    public boolean deleteDiscussByIdBatch(List<Integer> discussIds) {
        DiscussExample discussExample = new DiscussExample();
        DiscussExample.Criteria criteria = discussExample.createCriteria();
        criteria.andIdIn(discussIds);
        return discussMapper.deleteByExample(discussExample) > 0;
    }

    @Override
    public boolean deleteDiscussPostByDiscussIds(List<Integer> discussIds) {
        DiscussPostExample discussPostExample = new DiscussPostExample();
        DiscussPostExample.Criteria criteria = discussPostExample.createCriteria();
        criteria.andDiscussIdIn(discussIds);
        return discussPostMapper.deleteByExample(discussPostExample) > 0;
    }

    @Override
    public List<DiscussPost> getAllDiscussPostByMapWithStudentName(Map<String, Object> map1) {
        return discussPostMapper.selectByDiscussIdWithStudentName(map1);
    }

    @Override
    public boolean saveTask(Task task) {
        return taskMapper.insertSelective(task) > 0;
    }

    @Override
    public List<Task> getAllTasksWithCourseAndChapterExample(TaskExample taskExample) {
        return taskMapper.selectByExampleWithCourseAndChapter(taskExample);
    }

    @Override
    public boolean deleteTaskById(Integer id) {
        //删除作业时，删除作业对应的题目
        TaskQuestionExample taskQuestionExample = new TaskQuestionExample();
        TaskQuestionExample.Criteria criteria = taskQuestionExample.createCriteria();
        criteria.andTaskIdEqualTo(id);
        int i = taskQuestionMapper.deleteByExample(taskQuestionExample);
        if (i < 0) {
            throw new RuntimeException("删除题目时出现错误，请稍后再试");
        }
        int j = taskMapper.deleteByPrimaryKey(id);
        if (j < 0) {
            throw new RuntimeException("删除作业时出现错误，请稍后再试");
        }
        return j > 0;
    }

    @Override
    public boolean deleteTaskByIdBatch(List<Integer> taskIds) {
        //删除作业之前先将对应的所有题目删除
        TaskQuestionExample taskQuestionExample = new TaskQuestionExample();
        TaskQuestionExample.Criteria questionExampleCriteria = taskQuestionExample.createCriteria();
        questionExampleCriteria.andTaskIdIn(taskIds);
        int i = taskQuestionMapper.deleteByExample(taskQuestionExample);
        if (i < 0) {
            throw new RuntimeException("批量删除题目时出现错误，请稍后再试");
        }
        TaskExample taskExample = new TaskExample();
        TaskExample.Criteria criteria = taskExample.createCriteria();
        criteria.andIdIn(taskIds);
        int j = taskMapper.deleteByExample(taskExample);
        if (j < 0) {
            throw new RuntimeException("批量删除作业时出现错误，请稍后再试");
        }
        return j > 0;
    }

    @Override
    public Task getTaskWithCourseAndChapterById(Integer id) {
        return taskMapper.selectByPrimaryKeyWithCourseAndChapter(id);
    }

    @Override
    public Task getTaskById(Integer id) {
        return taskMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateTaskByPrimaryKeySelective(Task task) {
        return taskMapper.updateByPrimaryKeySelective(task) > 0;
    }

    @Override
    public Discuss getDiscussById(Integer id) {
        return discussMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateDiscussByPrimaryKeySelective(Discuss discuss) {
        return discussMapper.updateByPrimaryKeySelective(discuss) > 0;
    }

    @Override
    public List<TaskQuestion> getTaskQuestionsByTaskId(Integer id) {
        return taskQuestionMapper.selectByTaskId(id);
    }

    @Override
    public boolean deleteTaskQuestionByIdBatch(List<Integer> questionIds) {
        TaskQuestionExample taskQuestionExample = new TaskQuestionExample();
        TaskQuestionExample.Criteria criteria = taskQuestionExample.createCriteria();
        criteria.andIdIn(questionIds);
        return taskQuestionMapper.deleteByExample(taskQuestionExample) > 0;
    }

    @Override
    public boolean insertTaskQuestion(TaskQuestion taskQuestion) {
        return taskQuestionMapper.insert(taskQuestion) > 0;
    }

    @Override
    public TaskQuestion getTaskQuestionsById(Integer id) {
        return taskQuestionMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateTaskQuestionByPrimaryKeySelective(TaskQuestion taskQuestion) {
        return taskQuestionMapper.updateByPrimaryKeySelective(taskQuestion) > 0;
    }

    @Override
    public boolean insertTaskQuestionBatch(List<TaskQuestion> taskQuestions) {
        return taskQuestionMapper.insertBatch(taskQuestions) > 0;
    }

    @Override
    public Teacher getTeacherByIdWithCollege(Integer id) {
        return teacherMapper.selectByPrimaryKeyWithCollege(id);
    }

    @Override
    public Teacher getTeacherByIdWithoutCollegeAndCourse(Integer id) {
        return teacherMapper.selectByPrimaryKeyWithoutCollegeAndCourse(id);
    }

    @Override
    public boolean insertCourseChapterSelective(CourseChapter chapter) {
        return courseChapterMapper.insertSelective(chapter) > 0;
    }

    @Override
    public List<CourseChapter> getAllChapterWithBLOBsAndCourseByExample(CourseChapterExample chapterExample) {
        return courseChapterMapper.selectByExampleWithBLOBsAndCourse(chapterExample);
    }

    @Override
    public boolean deleteChapterByIdBatch(List<Integer> chapterIds) {
        //删除章节之前，先删除章节对应的课件信息


        //删除章节信息
        CourseChapterExample example = new CourseChapterExample();
        CourseChapterExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(chapterIds);
        return courseChapterMapper.deleteByExample(example) > 0;
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
    }

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

    @Override
    public boolean saveDiscuss(Discuss discuss) {
        return discussMapper.insertSelective(discuss) > 0;
    }

    @Override
    public List<Discuss> getAllDiscussWithBLOBsAndCourseByExample(DiscussExample example) {
        return discussMapper.selectByExampleWithBLOBsAndCourse(example);
    }


}
