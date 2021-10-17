package com.yinuo.demo.server.mgt.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.yinuo.base.dto.SingleResponse;
import com.yinuo.demo.api.spec.dto.UserDTO;
import com.yinuo.demo.api.spec.dto.UserQry;
import com.yinuo.demo.api.spec.service.UserApi;
import com.yinuo.demo.domain.user.User;
import com.yinuo.demo.domain.user.UserService;
import com.yinuo.demo.server.mgt.consts.PathConstant;
import com.yinuo.utils.ObjectFuncUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liang
 */
@RestController
public class UserController implements UserApi {
    @Resource
    private UserService userService;

    @Override
    @GetMapping(PathConstant.USER_FIND)
    public SingleResponse<List<UserDTO>> find(UserQry userQry) {
        List<User> users = userService.find(BeanUtil.toBean(userQry, User.class));
        return SingleResponse.of(users.stream().map(t -> convert(t)).collect(Collectors.toList()));
    }

    private UserDTO convert(User bo) {
        return ObjectFuncUtil.returnByDo(BeanUtil.toBean(bo, UserDTO.class), (t) -> {
            String replacedMobileNo = StrUtil.replace(bo.getMobile(), 3, 7, '*');
            t.setShieldMobileNo(replacedMobileNo);
        });
    }
}
