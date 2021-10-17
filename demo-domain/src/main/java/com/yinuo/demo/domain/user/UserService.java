package com.yinuo.demo.domain.user;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liang
 */
@Service
public class UserService {
    @Resource
    private UserDao userDao;

    public List<User> find(User user) {
        return userDao.find(user);
    }


}
