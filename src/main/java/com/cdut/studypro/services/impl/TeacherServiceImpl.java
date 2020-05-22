package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.daos.*;
import com.cdut.studypro.services.TeacherService;
import com.cdut.studypro.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
    private OnlineTaskMapper onlineTaskMapper;

    @Autowired
    private OfflineTaskMapper offlineTaskMapper;

    @Autowired
    private OnlineTaskQuestionMapper taskQuestionMapper;

    @Autowired
    private StudentOfflineTaskMapper studentOfflineTaskMapper;

    @Autowired
    private StudentOnlineTaskMapper studentOnlineTaskMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private OnlineTaskQuestionMapper onlineTaskQuestionMapper;

    @Autowired
    private NoticeMapper noticeMapper;

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
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andTeacherIdEqualTo(id);
        return courseMapper.selectByExampleWithChapterAndCollege(courseExample);
    }

    @Override
    public List<Course> getAllCoursesWithCollegeByTeacherId(Integer id) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andTeacherIdEqualTo(id);
        courseExample.setOrderByClause("CONVERT(name using gbk) asc");
        return courseMapper.selectByExampleWithCollege(courseExample);
    }

    @Override
    public boolean deleteDiscussByIdBatch(List<Integer> discussIds) {
        //删除讨论之前，先删除对应的讨论记录
        DiscussPostExample discussPostExample = new DiscussPostExample();
        DiscussPostExample.Criteria criteria1 = discussPostExample.createCriteria();
        criteria1.andDiscussIdIn(discussIds);
        int i = discussPostMapper.deleteByExample(discussPostExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //删除讨论
        DiscussExample discussExample = new DiscussExample();
        DiscussExample.Criteria criteria = discussExample.createCriteria();
        criteria.andIdIn(discussIds);
        i = discussMapper.deleteByExample(discussExample);
        if (i <= 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        return true;
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
    public boolean saveOnlineTask(OnlineTask task) {
        return onlineTaskMapper.insertSelective(task) > 0;
    }

    @Override
    public List<OnlineTask> getAllOnlineTasksWithCourseAndChapterExample(OnlineTaskExample taskExample) {
        return onlineTaskMapper.selectByExampleWithCourseAndCollegeAndChapter(taskExample);
    }

    @Override
    public boolean deleteOnlineTaskById(Integer id) {
        //删除作业时，删除作业对应的题目和作业记录
        OnlineTaskQuestionExample taskQuestionExample = new OnlineTaskQuestionExample();
        OnlineTaskQuestionExample.Criteria criteria = taskQuestionExample.createCriteria();
        criteria.andOnlineTaskIdEqualTo(id);
        int i = taskQuestionMapper.deleteByExample(taskQuestionExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        StudentOnlineTaskExample studentOnlineTaskExample = new StudentOnlineTaskExample();
        StudentOnlineTaskExample.Criteria studentOnlineTaskExampleCriteria = studentOnlineTaskExample.createCriteria();
        studentOnlineTaskExampleCriteria.andOnlineTaskIdEqualTo(id);
        i = studentOnlineTaskMapper.deleteByExample(studentOnlineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        i = onlineTaskMapper.deleteByPrimaryKey(id);
        if (i <= 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        return true;
    }

    @Override
    public boolean deleteOnlineTaskByIdBatch(List<Integer> taskIds) {
        //删除作业之前先将对应的所有题目删除
        OnlineTaskQuestionExample taskQuestionExample = new OnlineTaskQuestionExample();
        OnlineTaskQuestionExample.Criteria questionExampleCriteria = taskQuestionExample.createCriteria();
        questionExampleCriteria.andOnlineTaskIdIn(taskIds);
        int i = taskQuestionMapper.deleteByExample(taskQuestionExample);
        if (i < 0) {
            throw new RuntimeException("批量删除题目时出现错误，请稍后再试");
        }
        OnlineTaskExample taskExample = new OnlineTaskExample();
        OnlineTaskExample.Criteria criteria = taskExample.createCriteria();
        criteria.andIdIn(taskIds);
        int j = onlineTaskMapper.deleteByExample(taskExample);
        if (j < 0) {
            throw new RuntimeException("批量删除作业时出现错误，请稍后再试");
        }
        return j > 0;
    }

    @Override
    public OnlineTask getOnlineTaskWithCourseAndChapterById(Integer id) {
        return onlineTaskMapper.selectByPrimaryKeyWithCourseAndChapter(id);
    }

    @Override
    public OnlineTask getOnlineTaskById(Integer id) {
        return onlineTaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateOnlineTaskByPrimaryKeySelective(OnlineTask task) {
        return onlineTaskMapper.updateByPrimaryKeySelective(task) > 0;
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
    public List<OnlineTaskQuestion> getOnlineTaskQuestionsByTaskId(Integer id) {
        return taskQuestionMapper.selectByOnlineTaskId(id);
    }

    @Override
    public boolean deleteOnlineTaskQuestionByIdBatch(List<Integer> questionIds) {
        OnlineTaskQuestionExample taskQuestionExample = new OnlineTaskQuestionExample();
        OnlineTaskQuestionExample.Criteria criteria = taskQuestionExample.createCriteria();
        criteria.andIdIn(questionIds);
        return taskQuestionMapper.deleteByExample(taskQuestionExample) > 0;
    }

    @Override
    public boolean insertOnlineTaskQuestion(OnlineTaskQuestion taskQuestion) {
        return taskQuestionMapper.insert(taskQuestion) > 0;
    }

    @Override
    public OnlineTaskQuestion getOnlineTaskQuestionsById(Integer id) {
        return taskQuestionMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateOnlineTaskQuestionByPrimaryKeySelective(OnlineTaskQuestion taskQuestion) {
        return taskQuestionMapper.updateByPrimaryKeySelective(taskQuestion) > 0;
    }

    @Override
    public boolean insertOnlineTaskQuestionBatch(List<OnlineTaskQuestion> taskQuestions) {
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
    public boolean deleteChapterByIdBatch(List<Integer> chapterIds, HttpServletRequest request, Integer courseId) {
        //删除章节之前，先删除章节对应的文件信息
        //1.1、删除课件信息
        CourseFileExample courseFileExample = new CourseFileExample();
        CourseFileExample.Criteria courseFileExampleCriteria = courseFileExample.createCriteria();
        courseFileExampleCriteria.andChapterIdIn(chapterIds);
        int i = courseFileMapper.deleteByExample(courseFileExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //1.2、删除视频信息
        CourseVideoExample courseVideoExample = new CourseVideoExample();
        CourseVideoExample.Criteria courseVideoExampleCriteria = courseVideoExample.createCriteria();
        courseVideoExampleCriteria.andChapterIdIn(chapterIds);
        i = courseVideoMapper.deleteByExample(courseVideoExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //删除章节下的所有任务
        //2.1、删除在线任务
        OnlineTaskExample onlineTaskExample = new OnlineTaskExample();
        OnlineTaskExample.Criteria onlineTaskExampleCriteria = onlineTaskExample.createCriteria();
        onlineTaskExampleCriteria.andChapterIdIn(chapterIds);
        //获取章节下的所有在线任务id
        List<Integer> onlineTaskIds = onlineTaskMapper.selectOnlineTaskIdsByExample(onlineTaskExample);
        if (onlineTaskIds.size() == 0) {
            onlineTaskIds.add(0);
        }
        i = onlineTaskMapper.deleteByExample(onlineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //2.2、删除在线任务的题目信息
        OnlineTaskQuestionExample onlineTaskQuestionExample = new OnlineTaskQuestionExample();
        OnlineTaskQuestionExample.Criteria onlineTaskQuestionExampleCriteria = onlineTaskQuestionExample.createCriteria();
        onlineTaskQuestionExampleCriteria.andOnlineTaskIdIn(onlineTaskIds);
        i = onlineTaskQuestionMapper.deleteByExample(onlineTaskQuestionExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //2.3、删除学生完成在线任务的信息
        StudentOnlineTaskExample studentOnlineTaskExample = new StudentOnlineTaskExample();
        StudentOnlineTaskExample.Criteria studentOnlineTaskExampleCriteria = studentOnlineTaskExample.createCriteria();
        studentOnlineTaskExampleCriteria.andOnlineTaskIdIn(onlineTaskIds);
        i = studentOnlineTaskMapper.deleteByExample(studentOnlineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //3.1、删除离线任务
        OfflineTaskExample offlineTaskExample = new OfflineTaskExample();
        OfflineTaskExample.Criteria offlineTaskExampleCriteria = offlineTaskExample.createCriteria();
        offlineTaskExampleCriteria.andChapterIdIn(chapterIds);
        //获取章节下的所有离线任务id
        List<Integer> offlineTaskIds = offlineTaskMapper.selectOfflineTaskIdsByExample(offlineTaskExample);
        if (offlineTaskIds.size() == 0) {
            offlineTaskIds.add(0);
        }
        i = offlineTaskMapper.deleteByExample(offlineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //3.2、删除学生完成离线任务的信息
        StudentOfflineTaskExample studentOfflineTaskExample = new StudentOfflineTaskExample();
        StudentOfflineTaskExample.Criteria studentOfflineTaskExampleCriteria = studentOfflineTaskExample.createCriteria();
        studentOfflineTaskExampleCriteria.andOfflineTaskIdIn(offlineTaskIds);
        i = studentOfflineTaskMapper.deleteByExample(studentOfflineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }

        //删除章节信息
        CourseChapterExample example = new CourseChapterExample();
        CourseChapterExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(chapterIds);
        i = courseChapterMapper.deleteByExample(example);
        if (i <= 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        String fileBasePath = request.getServletContext().getRealPath("/file/");
        String videoBasePath = request.getServletContext().getRealPath("/video/");
        String offlineBasePath = request.getServletContext().getRealPath("/offlineTask/");
        for (Integer chapterId : chapterIds) {
            FileUtil.deleteDir(fileBasePath + courseId + "\\" + chapterId);
            FileUtil.deleteDir(videoBasePath + courseId + "\\" + chapterId);
            FileUtil.deleteDir(offlineBasePath + courseId + "\\" + chapterId);
        }
        return true;
    }

    @Override
    public CourseChapter getChapterById(Integer id) {
        return courseChapterMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateCourseChapterByIdSelective(CourseChapter chapter) {
        return courseChapterMapper.updateByPrimaryKeySelective(chapter) > 0;
    }

    @Override
    public List<CourseVideo> getCourseVideoByChapterId(Integer id) {
        CourseVideoExample courseVideoExample = new CourseVideoExample();
        CourseVideoExample.Criteria criteria = courseVideoExample.createCriteria();
        criteria.andChapterIdEqualTo(id);
        return courseVideoMapper.selectByExample(courseVideoExample);
    }

    @Override
    public List<CourseFile> getCourseFileByChapterId(Integer id) {
        CourseFileExample courseFileExample = new CourseFileExample();
        CourseFileExample.Criteria criteria = courseFileExample.createCriteria();
        criteria.andChapterIdEqualTo(id);
        return courseFileMapper.selectByExample(courseFileExample);
    }

    @Override
    public List<OnlineTask> getOnlineTaskByChapterId(Integer id) {
        OnlineTaskExample taskExample = new OnlineTaskExample();
        OnlineTaskExample.Criteria criteria = taskExample.createCriteria();
        criteria.andChapterIdEqualTo(id);
        return onlineTaskMapper.selectByExample(taskExample);
    }

    @Override
    public List<CourseVideo> getCourseVideoByExample(CourseVideoExample example) {
        return courseVideoMapper.selectByExample(example);
    }

    @Override
    public List<CourseFile> getCourseFileByExample(CourseFileExample courseFileExample) {
        return courseFileMapper.selectByExample(courseFileExample);
    }

    @Override
    public List<Course> getAllCoursesWithCollegeByExample(CourseExample courseExample) {
        return courseMapper.selectByExampleWithBLOBsAndCollege(courseExample);
    }

    @Override
    public List<College> getAllCollegesByTeacherId(Integer id) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andTeacherIdEqualTo(id);
        courseExample.setDistinct(true);
        List<Integer> ids = courseMapper.selectCollegeIdByExample(courseExample);
        if (ids.size() == 0) {
            ids.add(0);
        }
        CollegeExample collegeExample = new CollegeExample();
        CollegeExample.Criteria criteria1 = collegeExample.createCriteria();
        criteria1.andIdIn(ids);
        return collegeMapper.selectByExample(collegeExample);
    }

    @Override
    public List<OnlineTask> getOnlineTaskByChapterIdWithChapterAndCourse(Integer id) {
        OnlineTaskExample taskExample = new OnlineTaskExample();
        OnlineTaskExample.Criteria criteria = taskExample.createCriteria();
        criteria.andChapterIdEqualTo(id);
        return onlineTaskMapper.selectByExampleWithCourseAndCollegeAndChapter(taskExample);
    }

    @Override
    public Course getCourseWithChapterAndCollegeByCourseId(Integer courseId) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andIdEqualTo(courseId);
        return courseMapper.selectByExampleWithChapterAndCollege(courseExample).get(0);
    }

    @Override
    public List<OfflineTask> getOfflineTaskByChapterId(Integer id) {
        OfflineTaskExample offlineTaskExample = new OfflineTaskExample();
        OfflineTaskExample.Criteria criteria = offlineTaskExample.createCriteria();
        criteria.andChapterIdEqualTo(id);
        return offlineTaskMapper.selectByExampleWithBLOBs(offlineTaskExample);
    }

    @Override
    public boolean saveOfflineTask(OfflineTask task) {
        return offlineTaskMapper.insertSelective(task) > 0;
    }

    @Override
    public boolean deleteOfflineTaskById(Integer id, String path) {
        //删除线下作业时，同时删除学生的作业记录以及与改作业有关的文件
        //删除作业记录
        StudentOfflineTaskExample studentOfflineTaskExample = new StudentOfflineTaskExample();
        StudentOfflineTaskExample.Criteria criteria = studentOfflineTaskExample.createCriteria();
        criteria.andOfflineTaskIdEqualTo(id);
        int i = studentOfflineTaskMapper.deleteByExample(studentOfflineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //删除线下作业
        i = offlineTaskMapper.deleteByPrimaryKey(id);
        if (i <= 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //删除学生提交的文件
        FileUtil.deleteDir(path);
        return true;
    }

    @Override
    public OfflineTask getOfflineTaskWithCourseAndChapterById(Integer id) {
        return offlineTaskMapper.selectByPrimaryKeyWithCourseAndChapter(id);
    }

    @Override
    public boolean updateOfflineTaskByPrimaryKeySelective(OfflineTask task) {
        return offlineTaskMapper.updateByPrimaryKeySelective(task) > 0;
    }

    @Override
    public OfflineTask getOfflineTaskById(Integer id) {
        return offlineTaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<StudentOnlineTask> getOnlineTaskFinishByExample(StudentOnlineTaskExample example) {
        return studentOnlineTaskMapper.selectByExample(example);
    }

    @Override
    public List<StudentOfflineTask> getOfflineTaskFinishByExample(StudentOfflineTaskExample example) {
        return studentOfflineTaskMapper.selectByExample(example);
    }

    @Override
    public List<Student> getCollectStudentByStudentExample(StudentExample studentExample) {
        return studentMapper.selectByExampleWithIdNameAndNumber(studentExample);
    }

    @Override
    public boolean deleteStudentOnlineTaskById(Integer id) {
        return studentOnlineTaskMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteStudentOfflineTaskById(Integer id) {
        return studentOfflineTaskMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteStudentOnlineTaskByExample(StudentOnlineTaskExample studentOnlineTaskExample) {
        return studentOnlineTaskMapper.deleteByExample(studentOnlineTaskExample) > 0;
    }

    @Override
    public boolean deleteStudentOfflineTaskByExample(StudentOfflineTaskExample studentOfflineTaskExample) {
        return studentOfflineTaskMapper.deleteByExample(studentOfflineTaskExample) > 0;
    }

    @Override
    public List<Integer> selectCollectStudentIdByExample(CollectExample collectExample) {
        return collectMapper.selectStudentIdByExample(collectExample);
    }

    @Override
    public StudentOfflineTask getOfflineTaskFileById(Integer id) {
        return studentOfflineTaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OfflineTask> getAllOfflineTasksWithCourseAndChapterExample(OfflineTaskExample offlineTaskExample) {
        return offlineTaskMapper.selectByExampleWithCourseAndCollegeAndChapter(offlineTaskExample);
    }

    @Override
    public Integer getOnlineTaskTotalScore(Integer id) {
        return onlineTaskQuestionMapper.getTotalScore(id);
    }

    @Override
    public List<Notice> getAllNotices() {
        NoticeExample noticeExample = new NoticeExample();
        noticeExample.setOrderByClause("record_time desc");
        return noticeMapper.selectByExampleWithBLOBs(noticeExample);
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
        if (courseVideoMapper.deleteByExample(courseVideoExample) != videoIds.size()) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        return true;
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
    public List<CourseFile> getCourseFileByIds(List<Integer> fileIds) {
        CourseFileExample courseFileExample = new CourseFileExample();
        CourseFileExample.Criteria criteria = courseFileExample.createCriteria();
        criteria.andIdIn(fileIds);
        return courseFileMapper.selectByExample(courseFileExample);
    }

    @Override
    public boolean deleteCourseFileByIdBatch(List<Integer> fileIds) {
        CourseFileExample courseFileExample = new CourseFileExample();
        CourseFileExample.Criteria criteria = courseFileExample.createCriteria();
        criteria.andIdIn(fileIds);
        if (courseFileMapper.deleteByExample(courseFileExample) != fileIds.size()) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        return true;
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
