package com.wenwo.module.security.service;

import com.wenwo.module.security.model.RolePermission;
import com.wenwo.module.security.repository.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RolePermissionService {

  @Autowired
  private RolePermissionRepository rolePermissionRepository;

  public RolePermission save(RolePermission rolePermission) {
    return rolePermissionRepository.save(rolePermission);
  }

  public List<RolePermission> save(List<RolePermission> rolePermissions) {
    return rolePermissionRepository.saveAll(rolePermissions);
  }

  public void deleteRoleId(Integer roleId) {
    rolePermissionRepository.deleteByRoleId(roleId);
  }

  public List<RolePermission> findByRoleId(Integer roleId) {
    return rolePermissionRepository.findByRoleId(roleId);
  }

  public void deleteByPermissionId(Integer permissionId) {
    rolePermissionRepository.deleteByPermissionId(permissionId);
  }
}
