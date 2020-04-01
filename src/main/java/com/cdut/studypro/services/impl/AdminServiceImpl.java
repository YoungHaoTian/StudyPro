package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.Admin;
import com.cdut.studypro.beans.AdminExample;
import com.cdut.studypro.daos.AdminMapper;
import com.cdut.studypro.services.AdminService;
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
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<Admin> selectAdminByExample(AdminExample example) {
        return adminMapper.selectByExample(example);
    }

    @Override
    public boolean isExistsByExample(AdminExample example) {
        long l = adminMapper.countByExample(example);
        if (l!=0){
            return true;
        }
        return false;
    }
}
