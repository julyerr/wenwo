package com.wenwo.core.base;

import com.wenwo.config.SiteConfig;
import com.wenwo.core.exception.ApiAssert;
import com.wenwo.module.security.model.AdminUser;
import com.wenwo.module.user.model.User;
import com.wenwo.config.SiteConfig;
import com.wenwo.core.exception.ApiAssert;
import com.wenwo.module.security.model.AdminUser;
import com.wenwo.module.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

  @Autowired private BaseEntity baseEntity;
  @Autowired private SiteConfig siteConfig;

  /**
   * 带参重定向
   *
   * @param path
   * @return
   */
  protected String redirect(String path) {
    String baseUrl = siteConfig.getBaseUrl();
    return "redirect:" + baseUrl + path;
  }

  /**
   * 获取用户信息
   *
   * @return 没登录返回错误信息
   */
  protected User getUser() {
    return baseEntity.getUser();
  }

  protected AdminUser getAdminUser() {
    return baseEntity.getAdminUser();
  }

  /**
   * 获取用户信息
   *
   * @return 没登录返回json
   */
  protected User getApiUser() {
    User user = baseEntity.getUser();
    ApiAssert.notNull(user, "请先登录");
    return user;
  }
}
