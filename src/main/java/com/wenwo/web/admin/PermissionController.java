package com.wenwo.web.admin;

import com.wenwo.core.base.BaseController;
import com.wenwo.core.bean.Result;
import com.wenwo.module.security.model.Permission;
import com.wenwo.module.security.service.PermissionService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/permission")
public class PermissionController extends BaseController {

  @Autowired
  private PermissionService permissionService;

  @GetMapping("/list")
  public String list(Model model) {
    List<Map<String, Object>> node = permissionService.findAll();
    model.addAttribute("data", new Gson().toJson(node));
    return "admin/permission/list";
  }

  @PostMapping("/add")
  @ResponseBody
  public Result add(Integer id, String name, String value, String url, @RequestParam(defaultValue = "0") Integer pid) {
    Permission permission = new Permission();
    permission.setId(id);
    permission.setName(name);
    permission.setValue(value);
    permission.setPid(pid);
    permission.setUrl(url);
    permissionService.save(permission);
    return Result.success();
  }

  @GetMapping("/delete")
  @ResponseBody
  public Result delete(Integer id) {
    permissionService.delete(id);
    return Result.success();
  }
}
