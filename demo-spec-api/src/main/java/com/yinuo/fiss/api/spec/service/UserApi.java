package com.yinuo.demo.api.spec.service;

import com.yinuo.base.dto.SingleResponse;
import com.yinuo.demo.api.spec.dto.UserDTO;
import com.yinuo.demo.api.spec.dto.UserQry;

import java.util.List;

/**
 * @author liang
 */
public interface UserApi {
    /**
     * 查询用户信息
     *
     * @param userQry
     * @return
     */
    SingleResponse<List<UserDTO>> find(UserQry userQry);
}
