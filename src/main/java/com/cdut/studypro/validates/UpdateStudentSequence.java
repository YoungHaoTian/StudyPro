package com.cdut.studypro.validates;

import com.cdut.studypro.validates.common.*;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-04-28 17:00
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@GroupSequence(value = {
        Default.class,
        Validate1.class,
        Validate2.class,
        Validate7.class,
        Validate8.class,
        Validate9.class,
        Validate10.class,
        Validate11.class,
        Validate12.class,
        Validate13.class,
        Validate14.class,
        Validate15.class,
        Validate16.class,
        Validate17.class,
        Validate18.class
})
public interface UpdateStudentSequence {
}
