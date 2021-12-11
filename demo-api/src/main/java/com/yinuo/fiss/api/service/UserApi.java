package com.yinuo.fiss.api.service;

import com.yinuo.base.dto.SingleResponse;
import com.yinuo.fiss.api.dto.UserDTO;
import com.yinuo.fiss.api.dto.UserQry;

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
