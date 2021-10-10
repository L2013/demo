package com.yinuo.fiss.api.spec.dto;

import com.yinuo.base.dto.Query;
import lombok.Data;

/**
 * @author liang
 */
@Data
public class UserQry extends Query {
    private String mobile;
    private String loginId;
}
