package com.yinuo.fiss.domain.user;

import com.yinuo.fiss.api.spec.dto.UserQry;

import java.util.List;

/**
 * @author liang
 */
public interface UserDao {
    List<User> find(User user);

    void save(User user);
}
