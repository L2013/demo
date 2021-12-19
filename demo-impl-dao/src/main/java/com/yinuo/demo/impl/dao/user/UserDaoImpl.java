package com.yinuo.demo.impl.dao.user;

import cn.hutool.core.bean.BeanUtil;
import com.yinuo.base.jpa.BaseDaoImpl;
import com.yinuo.base.jpa.criterion.JpaCriteria;
import com.yinuo.base.jpa.criterion.JpaRestrictions;
import com.yinuo.demo.domain.user.User;
import com.yinuo.demo.domain.user.UserDao;
import com.yinuo.utils.ObjectFuncUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liang
 */
@Service
public class UserDaoImpl extends BaseDaoImpl<UserDO, UserRepo> implements UserDao {
    @Resource
    private UserRepo repo;

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public List<User> find(User user) {
        return repo.findAll(getCriteria(BeanUtil.toBean(user, UserDO.class))).stream()
                .map(t -> BeanUtil.toBean(t, User.class)).collect(Collectors.toList());
    }

    @Override
    public void save(User user) {
        UserDO userDO = ObjectFuncUtil.returnByDo(BeanUtil.toBean(user, UserDO.class), (t) -> {
            t.setRecCrtTm(new Date());
        });
        save(userDO);
    }

    @Override
    public Specification<UserDO> getCriteria(UserDO userDO) {
        return ObjectFuncUtil.returnByDo(new JpaCriteria<>(), (t) -> {
            t.add(JpaRestrictions.eq("loginId", userDO.getLoginId(), true));
        });
    }
}
