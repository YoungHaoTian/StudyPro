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
        Validate3.class,
        Validate4.class
})
public interface NoticeSequence {
}
