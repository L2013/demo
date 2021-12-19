package com.yinuo.demo.server.mgt.utils;

import cn.hutool.core.date.DateUtil;
import com.yinuo.demo.domain.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liang
 * 测试静态工具类中如何通过@Value注入值
 * 测试静态工具类中如何注入其它springBean
 */
@Component
public class DateKit {
    private static String today;

    private static UserDao userDao;

    @Value("${today}")
    public void setToday(String today) {
        DateKit.today = today;
    }

    @Autowired
    public DateKit(UserDao userDao) {
        DateKit.userDao = userDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        DateKit.userDao = userDao;
    }

    public static String today() {
        return today == null ? DateUtil.formatDate(DateUtil.date()) : today;
    }

    public static void reportUserCount() {
        System.out.println(userDao.count());
    }
}
