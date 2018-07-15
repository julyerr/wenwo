package com.wenwo.module.security.repository;

import com.wenwo.module.security.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

  void deleteByRoleId(Integer roleId);

  void deleteByPermissionId(Integer permissionId);

  List<RolePermission> findByRoleId(Integer roleId);

}
