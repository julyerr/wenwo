package com.wenwo.module.security.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wenwo_admin_user")
@Setter
@Getter
public class AdminUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    private Date inTime;

    private String token;

    private Integer roleId;

    //  不使用外键约束，写法还是非常清爽的
    @Transient
    private Role role;
    @Transient
    private List<Permission> permissions;
}
