package com.wenwo.config;

import com.wenwo.core.base.BaseController;
import com.wenwo.module.security.model.AdminUser;
import com.wenwo.module.security.model.Permission;
import com.wenwo.module.security.model.AdminUser;
import com.wenwo.module.security.model.Permission;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig extends BaseController {

  /**
   * 判断是否有 role 的角色
   * @param role 传入的角色名
   * @return
   */
  public boolean hasRole(String role) {
    AdminUser adminUser = getAdminUser();
    return adminUser != null && adminUser.getRole().getName().equalsIgnoreCase(role);
  }

  /**
   * 判断是否有 permission 的权限
   * @param permission 传入的权限名
   * @return
   */
  public boolean hasPermission(String permission) {
    AdminUser adminUser = getAdminUser();
    if (adminUser == null || adminUser.getPermissions() == null) return false;
    for (Permission permission1 : adminUser.getPermissions()) {
      if (permission1.getValue().equalsIgnoreCase(permission)) {
        return true;
      }
    }
    return false;
  }
}
