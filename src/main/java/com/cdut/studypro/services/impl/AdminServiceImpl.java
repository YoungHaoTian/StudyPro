package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.daos.*;
import com.cdut.studypro.services.AdminService;
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
    public List<Student> getAllStudentsWithCollegeByExample(StudentExample example) {
        return studentMapper.selectByExampleWithCollege(example);
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
        return studentMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteStudentByIdBatch(List<Integer> ids) {
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andIdIn(ids);
        return studentMapper.deleteByExample(studentExample) > 0;
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
        return teacherMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteTeacherByIdBatch(List<Integer> ids) {
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
        criteria.andIdIn(ids);
        return teacherMapper.deleteByExample(teacherExample) > 0;
    }

    @Override
    public Teacher getTeacherByPrimaryKey(Integer id) {
        return teacherMapper.selectByPrimaryKey(id);
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
        return collegeMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteCollegeByIdBatch(List<Integer> ids) {
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
    public List<Teacher> getAllTeachersWithIdNameAndCollege() {
        TeacherExample teacherExample = new TeacherExample();
        teacherExample.setOrderByClause("CONVERT(name using gbk) asc");
        return teacherMapper.selectByExampleWithIdNameAndCollege(teacherExample);
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
    public boolean deleteCourseById(Integer id) {
        return courseMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteCourseByIdBatch(List<Integer> ids) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andIdIn(ids);
        return courseMapper.deleteByExample(courseExample) > 0;
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
}
