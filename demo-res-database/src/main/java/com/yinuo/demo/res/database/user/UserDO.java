package com.yinuo.demo.res.database.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author liang
 */
@Data
@Table(name = "tbl_user")
@Entity
public class UserDO {
    @Id
    private Long id;
    private String loginId;
    private String password;
    private String mobile;
    private String firstName;
    private String lastName;
    private Integer age;
    private Date recCrtTm;

}
