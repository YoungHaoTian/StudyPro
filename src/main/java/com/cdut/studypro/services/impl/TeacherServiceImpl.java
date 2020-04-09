package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.DiscussPost;
import com.cdut.studypro.beans.DiscussPostExample;
import com.cdut.studypro.beans.Teacher;
import com.cdut.studypro.beans.TeacherExample;
import com.cdut.studypro.daos.DiscussPostMapper;
import com.cdut.studypro.daos.TeacherMapper;
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
    }
}
