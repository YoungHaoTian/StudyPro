package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.daos.*;
import com.cdut.studypro.services.AdminService;
import com.cdut.studypro.utils.FileUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:55
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private DiscussMapper discussMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private StudentOnlineTaskMapper studentOnlineTaskMapper;

    @Autowired
    private StudentOfflineTaskMapper studentOfflineTaskMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private CourseChapterMapper courseChapterMapper;

    @Autowired
    private CourseFileMapper courseFileMapper;

    @Autowired
    private CourseVideoMapper courseVideoMapper;

    @Autowired
    private OnlineTaskMapper onlineTaskMapper;

    @Autowired
    private OfflineTaskMapper offlineTaskMapper;

    @Autowired
    private OnlineTaskQuestionMapper onlineTaskQuestionMapper;

    @Override
    public List<Admin> selectAdminByExample(AdminExample example) {
        return adminMapper.selectByExample(example);
    }

    @Override
    public boolean isExistsByExample(AdminExample example) {
        return adminMapper.countByExample(example) != 0;
    }

    @Override
    public List<College> getAllColleges() {
        return collegeMapper.selectByExample(null);
    }


    @Override
    public List<Student> getAllStudentsWithCollegeByExampleWithoutPassword(StudentExample example) {
        return studentMapper.selectByExampleWithCollegeWithoutPassword(example);
    }

    @Override
    public boolean isStudentExistsByExample(StudentExample example) {
        return studentMapper.countByExample(example) > 0;
    }

    @Override
    public boolean insertStudentBatch(List<Student> students) {
        return studentMapper.insertBatch(students) > 0;
    }

    @Override
    public boolean deleteStudentById(Integer id) {
        //删除学生时，同时删除与学生相关的记录，选课记录和作业记录
        //删除选课记录
        CollectExample collectExample = new CollectExample();
        CollectExample.Criteria criteria = collectExample.createCriteria();
        criteria.andStudentIdEqualTo(id);

        int i = collectMapper.deleteByExample(collectExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //删除作业记录
        //线上作业
        StudentOnlineTaskExample studentOnlineTaskExample = new StudentOnlineTaskExample();
        StudentOnlineTaskExample.Criteria studentOnlineTaskExampleCriteria = studentOnlineTaskExample.createCriteria();
        studentOnlineTaskExampleCriteria.andStudentIdEqualTo(id);
        i = studentOnlineTaskMapper.deleteByExample(studentOnlineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //线下作业
        StudentOfflineTaskExample studentOfflineTaskExample = new StudentOfflineTaskExample();
        StudentOfflineTaskExample.Criteria studentOfflineTaskExampleCriteria = studentOfflineTaskExample.createCriteria();
        studentOfflineTaskExampleCriteria.andStudentIdEqualTo(id);
        i = studentOfflineTaskMapper.deleteByExample(studentOfflineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //删除学生记录
        i = studentMapper.deleteByPrimaryKey(id);
        if (i <= 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        return true;
    }

    @Override
    public boolean deleteStudentByIdBatch(List<Integer> ids) {
        //删除学生时，同时删除与学生相关的记录，选课记录和作业记录
        //删除选课记录
        CollectExample collectExample = new CollectExample();
        CollectExample.Criteria collectExampleCriteria = collectExample.createCriteria();
        collectExampleCriteria.andStudentIdIn(ids);
        int i = collectMapper.deleteByExample(collectExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //删除作业记录
        //删除线上作业
        StudentOnlineTaskExample studentTaskExample = new StudentOnlineTaskExample();
        StudentOnlineTaskExample.Criteria studentTaskExampleCriteria = studentTaskExample.createCriteria();
        studentTaskExampleCriteria.andStudentIdIn(ids);
        i = studentOnlineTaskMapper.deleteByExample(studentTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //线下作业
        StudentOfflineTaskExample studentOfflineTaskExample = new StudentOfflineTaskExample();
        StudentOfflineTaskExample.Criteria studentOfflineTaskExampleCriteria = studentOfflineTaskExample.createCriteria();
        studentOfflineTaskExampleCriteria.andStudentIdIn(ids);
        i = studentOfflineTaskMapper.deleteByExample(studentOfflineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //删除学生记录
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andIdIn(ids);
        i = studentMapper.deleteByExample(studentExample);
        if (i <= 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        return true;
    }

    @Override
    public Student getStudentByPrimaryKey(Integer id) {
        return studentMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateStudentByPrimaryKeySelective(Student student) {
        return studentMapper.updateByPrimaryKeySelective(student) > 0;
    }

    @Override
    public boolean insertStudentSelective(Student student) {
        return studentMapper.insertSelective(student) > 0;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseMapper.selectByExample(null);
    }

    @Override
    public boolean isTeacherExistsByExample(TeacherExample example) {
        return teacherMapper.countByExample(example) > 0;
    }

    @Override
    public boolean insertTeacherSelective(Teacher teacher) {
        return teacherMapper.insertSelective(teacher) > 0;
    }

    @Override
    public boolean insertTeacherBatch(List<Teacher> teachers) {
        return teacherMapper.insertBatch(teachers) > 0;
    }

    @Override
    public List<Teacher> getAllTeachersWithCollegeAndCourseByExample(TeacherExample example) {
        return teacherMapper.selectByExampleWithCollegeAndCourse(example);
    }

    @Override
    public List<Integer> getTeacherIdByCourseExample(CourseExample example) {
        return courseMapper.selectTeacherIdByExample(example);
    }

    @Override
    public boolean deleteTeacherById(Integer id) {
        //删除老师的同时，将所授课程的teacher_id置为0
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andTeacherIdEqualTo(id);
        List<Integer> courseIds = courseMapper.selectCourseIdByExample(courseExample);
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        courseExample.clear();
        criteria = courseExample.createCriteria();
        criteria.andIdIn(courseIds);
        Course course = new Course();
        course.setTeacherId(0);
        int i = courseMapper.updateByExampleSelective(course, courseExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        i = teacherMapper.deleteByPrimaryKey(id);
        if (i <= 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        return true;
    }

    @Override
    public boolean deleteTeacherByIdBatch(List<Integer> ids) {
        //删除老师的同时，将所授课程的teacher_id置为0
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andTeacherIdIn(ids);
        List<Integer> courseIds = courseMapper.selectCourseIdByExample(courseExample);
        if (courseIds.size() == 0) {
            courseIds.add(0);
        }
        courseExample.clear();
        criteria = courseExample.createCriteria();
        criteria.andIdIn(courseIds);
        Course course = new Course();
        course.setTeacherId(0);
        int i = courseMapper.updateByExampleSelective(course, courseExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria teacherCriteria = teacherExample.createCriteria();
        teacherCriteria.andIdIn(ids);
        i = teacherMapper.deleteByExample(teacherExample);
        if (i <= 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        return true;
    }

    @Override
    public Teacher getTeacherByPrimaryKeyWithCourse(Integer id) {
        return teacherMapper.selectByPrimaryKeyWithCourse(id);
    }

    @Override
    public boolean updateTeacherByPrimaryKeySelective(Teacher teacher) {
        return teacherMapper.updateByPrimaryKeySelective(teacher) > 0;
    }

    @Override
    public boolean unbindCourse(Course course) {
        return courseMapper.updateByPrimaryKeySelective(course) > 0;
    }


    @Override
    public List<College> getAllCollegesWithBLOBsByExample(CollegeExample example) {
        return collegeMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public boolean isCollegeExistsByExample(CollegeExample example) {
        return collegeMapper.countByExample(example) > 0;
    }

    @Override
    public boolean insertCollegeSelective(College college) {
        return collegeMapper.insertSelective(college) > 0;
    }

    @Override
    public boolean deleteCollegeById(Integer id) {
        //删除学院的同时，将学生、教师、课程的学院id置0
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria studentExampleCriteria = studentExample.createCriteria();
        studentExampleCriteria.andCollegeIdEqualTo(id);
        Student student = new Student();
        student.setCollegeId(0);
        int i = studentMapper.updateByExampleSelective(student, studentExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria teacherExampleCriteria = teacherExample.createCriteria();
        teacherExampleCriteria.andCollegeIdEqualTo(id);
        Teacher teacher = new Teacher();
        teacher.setCollegeId(0);
        i = teacherMapper.updateByExampleSelective(teacher, teacherExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria courseExampleCriteria = courseExample.createCriteria();
        courseExampleCriteria.andCollegeIdEqualTo(id);
        Course course = new Course();
        course.setCollegeId(0);
        i = courseMapper.updateByExampleSelective(course, courseExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        return collegeMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteCollegeByIdBatch(List<Integer> ids) {
        //删除学院的同时，将学生、教师、课程的学院id置0
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria studentExampleCriteria = studentExample.createCriteria();
        studentExampleCriteria.andCollegeIdIn(ids);
        Student student = new Student();
        student.setCollegeId(0);
        int i = studentMapper.updateByExampleSelective(student, studentExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria teacherExampleCriteria = teacherExample.createCriteria();
        teacherExampleCriteria.andCollegeIdIn(ids);
        Teacher teacher = new Teacher();
        teacher.setCollegeId(0);
        i = teacherMapper.updateByExampleSelective(teacher, teacherExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria courseExampleCriteria = courseExample.createCriteria();
        courseExampleCriteria.andCollegeIdIn(ids);
        Course course = new Course();
        course.setCollegeId(0);
        i = courseMapper.updateByExampleSelective(course, courseExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        CollegeExample collegeExample = new CollegeExample();
        CollegeExample.Criteria criteria = collegeExample.createCriteria();
        criteria.andIdIn(ids);
        return collegeMapper.deleteByExample(collegeExample) > 0;
    }

    @Override
    public College getCollegeByPrimaryKey(Integer id) {
        return collegeMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateCollegeByPrimaryKeySelective(College college) {
        return collegeMapper.updateByPrimaryKeySelective(college) > 0;
    }

    @Override
    public List<Teacher> getAllTeachersWithIdNameNumberAndCollege(boolean order) {
        TeacherExample teacherExample = new TeacherExample();
        if (order) {
            teacherExample.setOrderByClause("CONVERT(name using gbk) asc,number asc");
        }
        return teacherMapper.selectByExampleWithIdNameNumberAndCollege(teacherExample);
    }

    @Override
    public boolean isCourseExistsByExample(CourseExample example) {
        return courseMapper.countByExample(example) > 0;
    }

    @Override
    public boolean insertCourseSelective(Course course) {
        return courseMapper.insertSelective(course) > 0;
    }

    @Override
    public List<Course> getAllCoursesWithBLOBsCollegeAndTeacherByExample(CourseExample example) {
        return courseMapper.selectByExampleWithBLOBsAndCollegeAndTeacher(example);
    }

    @Override
    public boolean deleteCourseById(Integer id, HttpServletRequest request) {
        //删除课程信息的同时删除该课程的章节信息、作业等与课程相关的信息
        //1、删除选课信息
        CollectExample collectExample = new CollectExample();
        CollectExample.Criteria collectExampleCriteria = collectExample.createCriteria();
        collectExampleCriteria.andCourseIdEqualTo(id);
        int i = collectMapper.deleteByExample(collectExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //2.1、删除课程讨论信息
        DiscussExample discussExample = new DiscussExample();
        DiscussExample.Criteria discussExampleCriteria = discussExample.createCriteria();
        discussExampleCriteria.andCourseIdEqualTo(id);
        //获取与该课程有关讨论的id
        List<Integer> discussIds = discussMapper.selectDiscussIdByExample(discussExample);
        if (discussIds.size() == 0) {
            discussIds.add(0);
        }
        i = discussMapper.deleteByExample(discussExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //2.2、根据讨论的id，删除相关的讨论内容
        DiscussPostExample discussPostExample = new DiscussPostExample();
        DiscussPostExample.Criteria discussPostExampleCriteria = discussPostExample.createCriteria();
        discussPostExampleCriteria.andDiscussIdIn(discussIds);
        i = discussPostMapper.deleteByExample(discussPostExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //3、删除章节信息
        CourseChapterExample courseChapterExample = new CourseChapterExample();
        CourseChapterExample.Criteria courseChapterExampleCriteria = courseChapterExample.createCriteria();
        courseChapterExampleCriteria.andCourseIdEqualTo(id);
        //获取与该课程有关的章节id
        List<Integer> chapterIds = courseChapterMapper.selectChapterIdByExample(courseChapterExample);
        if (chapterIds.size() == 0) {
            chapterIds.add(0);
        }
        i = courseChapterMapper.deleteByExample(courseChapterExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //4、删除章节下的资料、作业等信息
        //4.1、删除章节课件
        CourseFileExample courseFileExample = new CourseFileExample();
        CourseFileExample.Criteria courseFileExampleCriteria = courseFileExample.createCriteria();
        courseFileExampleCriteria.andChapterIdIn(chapterIds);
        i = courseFileMapper.deleteByExample(courseFileExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //4.2、删除章节视频
        CourseVideoExample courseVideoExample = new CourseVideoExample();
        CourseVideoExample.Criteria courseVideoExampleCriteria = courseVideoExample.createCriteria();
        courseVideoExampleCriteria.andChapterIdIn(chapterIds);
        i = courseVideoMapper.deleteByExample(courseVideoExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //4.3、删除章节下的所有任务
        //4.3.1、删除在线任务
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
        //4.3.2、删除在线任务的题目
        OnlineTaskQuestionExample onlineTaskQuestionExample = new OnlineTaskQuestionExample();
        OnlineTaskQuestionExample.Criteria onlineTaskQuestionExampleCriteria = onlineTaskQuestionExample.createCriteria();
        onlineTaskQuestionExampleCriteria.andOnlineTaskIdIn(onlineTaskIds);
        i = onlineTaskQuestionMapper.deleteByExample(onlineTaskQuestionExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //4.3.3、删除学生完成在线任务的信息
        StudentOnlineTaskExample studentOnlineTaskExample = new StudentOnlineTaskExample();
        StudentOnlineTaskExample.Criteria studentOnlineTaskExampleCriteria = studentOnlineTaskExample.createCriteria();
        studentOnlineTaskExampleCriteria.andOnlineTaskIdIn(onlineTaskIds);
        i = studentOnlineTaskMapper.deleteByExample(studentOnlineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //4.3.4、删除离线任务
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
        //4.3.5、删除学生完成离线任务的信息
        StudentOfflineTaskExample studentOfflineTaskExample = new StudentOfflineTaskExample();
        StudentOfflineTaskExample.Criteria studentOfflineTaskExampleCriteria = studentOfflineTaskExample.createCriteria();
        studentOfflineTaskExampleCriteria.andOfflineTaskIdIn(offlineTaskIds);
        i = studentOfflineTaskMapper.deleteByExample(studentOfflineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        i = courseMapper.deleteByPrimaryKey(id);
        if (i <= 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //删除课程课件
        String path = request.getServletContext().getRealPath("/file/") + id;
        FileUtil.deleteDir(path);
        //删除课程视频
        path = request.getServletContext().getRealPath("/video/") + id;
        FileUtil.deleteDir(path);
        //删除离线任务文件
        path = request.getServletContext().getRealPath("/offlineTask/") + id;
        FileUtil.deleteDir(path);
        return true;
    }

    @Override
    public boolean deleteCourseByIdBatch(List<Integer> ids, HttpServletRequest request) {
//删除课程信息的同时删除该课程的章节信息、作业等与课程相关的信息
        //1、删除选课信息
        CollectExample collectExample = new CollectExample();
        CollectExample.Criteria collectExampleCriteria = collectExample.createCriteria();
        collectExampleCriteria.andCourseIdIn(ids);
        int i = collectMapper.deleteByExample(collectExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //2.1、删除课程讨论信息
        DiscussExample discussExample = new DiscussExample();
        DiscussExample.Criteria discussExampleCriteria = discussExample.createCriteria();
        discussExampleCriteria.andCourseIdIn(ids);
        //获取与该课程有关讨论的id
        List<Integer> discussIds = discussMapper.selectDiscussIdByExample(discussExample);
        if (discussIds.size() == 0) {
            discussIds.add(0);
        }
        i = discussMapper.deleteByExample(discussExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //2.2、根据讨论的id，删除相关的讨论内容
        DiscussPostExample discussPostExample = new DiscussPostExample();
        DiscussPostExample.Criteria discussPostExampleCriteria = discussPostExample.createCriteria();
        discussPostExampleCriteria.andDiscussIdIn(discussIds);
        i = discussPostMapper.deleteByExample(discussPostExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //3、删除章节信息
        CourseChapterExample courseChapterExample = new CourseChapterExample();
        CourseChapterExample.Criteria courseChapterExampleCriteria = courseChapterExample.createCriteria();
        courseChapterExampleCriteria.andCourseIdIn(ids);
        //获取与该课程有关的章节id
        List<Integer> chapterIds = courseChapterMapper.selectChapterIdByExample(courseChapterExample);
        if (chapterIds.size() == 0) {
            chapterIds.add(0);
        }
        i = courseChapterMapper.deleteByExample(courseChapterExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //4、删除章节下的资料、作业等信息
        //4.1、删除章节课件
        CourseFileExample courseFileExample = new CourseFileExample();
        CourseFileExample.Criteria courseFileExampleCriteria = courseFileExample.createCriteria();
        courseFileExampleCriteria.andChapterIdIn(chapterIds);
        i = courseFileMapper.deleteByExample(courseFileExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //4.2、删除章节视频
        CourseVideoExample courseVideoExample = new CourseVideoExample();
        CourseVideoExample.Criteria courseVideoExampleCriteria = courseVideoExample.createCriteria();
        courseVideoExampleCriteria.andChapterIdIn(chapterIds);
        i = courseVideoMapper.deleteByExample(courseVideoExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //4.3、删除章节下的所有任务
        //4.3.1、删除在线任务
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
        //4.3.2、删除在线任务的题目
        OnlineTaskQuestionExample onlineTaskQuestionExample = new OnlineTaskQuestionExample();
        OnlineTaskQuestionExample.Criteria onlineTaskQuestionExampleCriteria = onlineTaskQuestionExample.createCriteria();
        onlineTaskQuestionExampleCriteria.andOnlineTaskIdIn(onlineTaskIds);
        i = onlineTaskQuestionMapper.deleteByExample(onlineTaskQuestionExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //4.3.3、删除学生完成作业的信息
        //在线任务
        StudentOnlineTaskExample studentOnlineTaskExample = new StudentOnlineTaskExample();
        StudentOnlineTaskExample.Criteria studentOnlineTaskExampleCriteria = studentOnlineTaskExample.createCriteria();
        studentOnlineTaskExampleCriteria.andOnlineTaskIdIn(onlineTaskIds);
        i = studentOnlineTaskMapper.deleteByExample(studentOnlineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //4.3.4、删除离线任务
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
        //4.3.5、删除学生完成作业的信息
        //离线任务
        StudentOfflineTaskExample studentOfflineTaskExample = new StudentOfflineTaskExample();
        StudentOfflineTaskExample.Criteria studentOfflineTaskExampleCriteria = studentOfflineTaskExample.createCriteria();
        studentOfflineTaskExampleCriteria.andOfflineTaskIdIn(offlineTaskIds);
        i = studentOfflineTaskMapper.deleteByExample(studentOfflineTaskExample);
        if (i < 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andIdIn(ids);
        i = courseMapper.deleteByExample(courseExample);
        if (i <= 0) {
            throw new RuntimeException("删除失败，请稍后再试");
        }
        //删除课程课件
        String base = request.getServletContext().getRealPath("/file/");
        for (Integer id : ids) {
            String path = base + id;
            FileUtil.deleteDir(path);
        }
        //删除课程视频
        base = request.getServletContext().getRealPath("/video/");
        for (Integer id : ids) {
            String path = base + id;
            FileUtil.deleteDir(path);
        }
        //删除离线任务文件
        base = request.getServletContext().getRealPath("/offlineTask/");
        for (Integer id : ids) {
            String path = base + id;
            FileUtil.deleteDir(path);
        }
        return true;
    }

    @Override
    public Course getCourseByPrimaryKey(Integer id) {
        return courseMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateCourseByPrimaryKeySelective(Course course) {
        return courseMapper.updateByPrimaryKeySelective(course) > 0;
    }

    @Override
    public List<Integer> getTeacherIdByTeacherExample(TeacherExample example) {
        return teacherMapper.selectIdsByExample(example);
    }

    @Override
    public Course getCourseByPrimaryKeyWithoutTeacherAndCollege(Integer id) {
        return courseMapper.selectByPrimaryKeyWithoutTeacherAndCollege(id);
    }

    @Override
    public List<Discuss> getAllDiscussWithBLOBsAndTeacherAndCourseByExample(DiscussExample example) {
        return discussMapper.selectByExampleWithBLOBsAndTeacherAndCourse(example);
    }

    @Override
    public List<Integer> getCourseIdByCourseExample(CourseExample example) {
        return courseMapper.selectCourseIdByExample(example);
    }

    @Override
    public List<Course> getAllCoursesWithWithCollegeAndTeacher() {
        CourseExample courseExample = new CourseExample();
        courseExample.setOrderByClause("CONVERT(name using gbk) asc");
        return courseMapper.selectByExampleWithCollegeAndTeacher(courseExample);
    }


    @Override
    public Discuss getDiscussByPrimaryKey(Integer id) {
        return discussMapper.selectByPrimaryKey(id);
    }


    @Override
    public boolean deleteDiscussByIdBatch(List<Integer> ids) {
        DiscussExample discussExample = new DiscussExample();
        DiscussExample.Criteria criteria = discussExample.createCriteria();
        criteria.andIdIn(ids);
        return discussMapper.deleteByExample(discussExample) > 0;

    }


    @Override
    public boolean updateDiscussByPrimaryKeySelective(Discuss discuss) {
        return discussMapper.updateByPrimaryKeySelective(discuss) > 0;
    }

    @Override
    public boolean insertNoticeSelective(Notice notice) {
        return noticeMapper.insertSelective(notice) > 0;
    }

    @Override
    public List<Notice> getAllNoticesByExample(NoticeExample example) {
        return noticeMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public boolean deleteNoticeById(Integer id) {
        return noticeMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteNoticeByIdBatch(List<Integer> noticeIds) {
        NoticeExample noticeExample = new NoticeExample();
        NoticeExample.Criteria criteria = noticeExample.createCriteria();
        criteria.andIdIn(noticeIds);
        return noticeMapper.deleteByExample(noticeExample) > 0;
    }

    @Override
    public Notice getNoticeByPrimaryKey(Integer id) {
        return noticeMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateNoticeByPrimaryKeySelective(Notice notice) {
        return noticeMapper.updateByPrimaryKeySelective(notice) > 0;
    }

    @Override
    public Admin getAdminById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateAdminByPrimaryKeySelective(Admin admin) {
        return adminMapper.updateByPrimaryKeySelective(admin) > 0;
    }

    @Override
    public boolean deleteDiscussPostByDiscussIds(List<Integer> discussIds) {
        DiscussPostExample discussPostExample = new DiscussPostExample();
        DiscussPostExample.Criteria criteria = discussPostExample.createCriteria();
        criteria.andDiscussIdIn(discussIds);
        return discussPostMapper.deleteByExample(discussPostExample) > 0;
    }

    @Override
    public List<String> getAllStudentTelephone() {
        return studentMapper.selectTelephoneByExample(null);
    }

    @Override
    public List<String> getAllTeacherTelephone() {
        return teacherMapper.selectTelephoneByExample(null);
    }

    @Override
    public Teacher getTeacherByPrimaryKeyWithoutCourses(Integer id) {
        return teacherMapper.selectByPrimaryKeyWithoutCollegeAndCourse(id);
    }

    @Override
    public List<String> getAllCollegeName() {
        return collegeMapper.selectNameByExample(null);
    }

    @Override
    public boolean insertCollegeBatch(List<College> colleges) {
        return collegeMapper.insertBatch(colleges);
    }

    @Override
    public List<String> getAllCourseNumber() {
        return courseMapper.selectNumberByExample(null);
    }

    @Override
    public boolean insertCourseBatch(List<Course> courses) {
        return courseMapper.insertBatch(courses);
    }
}
