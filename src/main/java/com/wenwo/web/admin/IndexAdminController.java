package com.wenwo.web.admin;

import com.wenwo.config.SiteConfig;
import com.wenwo.core.base.BaseController;
import com.wenwo.core.bean.Result;
import com.wenwo.core.exception.ApiAssert;
import com.wenwo.core.util.CookieHelper;
import com.wenwo.core.util.security.Base64Helper;
import com.wenwo.core.util.security.crypto.BCryptPasswordEncoder;
import com.wenwo.module.security.model.AdminUser;
import com.wenwo.module.security.model.Permission;
import com.wenwo.module.security.model.Role;
import com.wenwo.module.security.service.AdminUserService;
import com.wenwo.module.security.service.PermissionService;
import com.wenwo.module.security.service.RoleService;
import com.wenwo.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class IndexAdminController extends BaseController {

  @Autowired
  private AdminUserService adminUserService;
  @Autowired
  private SiteConfig siteConfig;
  @Autowired
  private RoleService roleService;
  @Autowired
  private PermissionService permissionService;
  @Autowired
  private UserService userService;

  @GetMapping("/index")
  public String index() {
    return "admin/index";
  }

  @GetMapping("/login")
  public String login() {
    if(getAdminUser() != null) return redirect("/admin/index");
    return "admin/login";
  }

  @PostMapping("/login")
  @ResponseBody
  public Result login(String username, String password, String code, HttpServletResponse response, HttpSession session) {
    ApiAssert.notEmpty(username, "用户名不能为空");
    ApiAssert.notEmpty(password, "密码不能为空");
    ApiAssert.notEmpty(code, "验证码不能为空");

    String index_code = (String) session.getAttribute("index_code");
    ApiAssert.isTrue(code.equalsIgnoreCase(index_code), "验证码不正确");

    AdminUser adminUser = adminUserService.findByUsername(username);
    ApiAssert.notNull(adminUser, "用户不存在");
    ApiAssert.isTrue(new BCryptPasswordEncoder().matches(password, adminUser.getPassword()), "密码不正确");

    // 查询用户的角色权限封装进adminuser里
    Role role = roleService.findById(adminUser.getRoleId());
    List<Permission> permissions = permissionService.findByUserId(adminUser.getId());
    adminUser.setRole(role);
    adminUser.setPermissions(permissions);

    session.setAttribute("admin_user", adminUser);
    System.out.println("siteConfig.getCookie().getAdminUserName:"+siteConfig.getCookie().getAdminUserName());
    CookieHelper.addCookie(
        response,
        siteConfig.getCookie().getDomain(),
        "/admin/",
        siteConfig.getCookie().getAdminUserName(),
        Base64Helper.encode(adminUser.getToken().getBytes()),
        siteConfig.getCookie().getAdminUserMaxAge() * 24 * 60 * 60,
        true,
        false
    );
    return Result.success();
  }

  @GetMapping("/clear")
  @ResponseBody
  public Result clear(Integer type) {
    if(type == 1) {
      userService.deleteAllRedisUser();
    } else if(type == 2) {
      adminUserService.deleteAllRedisAdminUser();
    }
    return Result.success();
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    CookieHelper.clearCookieByName(request, response, siteConfig.getCookie().getAdminUserName(),
        siteConfig.getCookie().getDomain(), "/admin/");
    return redirect("/admin/login");
  }
}
