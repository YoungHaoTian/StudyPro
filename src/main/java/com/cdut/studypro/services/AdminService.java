package com.cdut.studypro.services;

import com.cdut.studypro.beans.Admin;
import com.cdut.studypro.beans.AdminExample;

import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:54
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public interface AdminService {
    List<Admin> selectAdminByExample(AdminExample example);
    boolean isExistsByExample(AdminExample example);
}
