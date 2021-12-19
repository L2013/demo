package com.yinuo.demo.domain.user;

import java.util.List;

/**
 * @author liang
 */
public interface UserDao {
    long count();

    List<User> find(User user);

    void save(User user);
}
