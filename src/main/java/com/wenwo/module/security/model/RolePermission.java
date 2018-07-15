package com.wenwo.module.security.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "wenwo_role_permission")
@Setter
@Getter
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer roleId;
    private Integer permissionId;
}
