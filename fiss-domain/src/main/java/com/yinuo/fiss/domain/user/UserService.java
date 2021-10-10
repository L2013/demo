package com.yinuo.fiss.domain.user;

import cn.hutool.core.util.StrUtil;
import com.yinuo.fiss.api.spec.dto.UserDTO;
import com.yinuo.fiss.api.spec.dto.UserQry;
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
