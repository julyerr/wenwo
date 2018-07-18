package com.wenwo.web.front;

import com.wenwo.config.SiteConfig;
import com.wenwo.core.base.BaseController;
import com.wenwo.core.util.identicon.Identicon;
import com.wenwo.module.log.service.LogService;
import com.wenwo.module.user.model.User;
import com.wenwo.module.user.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

  @Autowired
  private UserService userService;
  @Autowired
  private Identicon identicon;
  @Autowired
  private SiteConfig siteConfig;
  @Autowired
  private LogService logService;

  /**
   * 个人资料
   *
   * @param username
   * @param model
   * @return
   */
  @GetMapping("/{username}")
  public String profile(@PathVariable String username, Model model) {
    model.addAttribute("username", username);
    return "front/user/info";
  }

  /**
   * 用户发布的所有话题
   *
   * @param username
   * @return
   */
  @GetMapping("/{username}/topics")
  public String topics(@PathVariable String username, Integer p, Model model) {
    model.addAttribute("username", username);
    model.addAttribute("p", p);
    return "front/user/topics";
  }

  /**
   * 用户发布的所有评论
   *
   * @param username
   * @return
   */
  @GetMapping("/{username}/comments")
  public String comments(@PathVariable String username, Integer p, Model model) {
    model.addAttribute("username", username);
    model.addAttribute("p", p);
    return "front/user/comments";
  }

  /**
   * 用户收藏的所有话题
   *
   * @param username
   * @return
   */
  @GetMapping("/{username}/collects")
  public String collects(@PathVariable String username, Integer p, Model model) {
    model.addAttribute("username", username);
    model.addAttribute("p", p);
    return "front/user/collects";
  }

  /**
   * 进入用户个人设置页面
   *
   * @param model
   * @return
   */
  @GetMapping("/setting/profile")
  public String setting(Model model) {
    model.addAttribute("user", getUser());
    return "front/user/setting/profile";
  }

  /**
   * 更新用户的个人设置
   *
   * @param email
   * @param url
   * @param bio
   * @return
   */
  @PostMapping("/setting/profile")
  public String updateUserInfo(String email, String url, String bio,
                               @RequestParam(defaultValue = "false") Boolean commentEmail,
                               @RequestParam(defaultValue = "false") Boolean replyEmail) throws Exception {
    User user = getUser();
    if (user.getBlock())
      throw new Exception("你的帐户已经被禁用，不能进行此项操作");
//    user.setEmail(email); TODO 还要对邮箱进行验证 另外这个方法将被删除，提到接口Controller里处理
    if (bio != null && bio.trim().length() > 0) user.setBio(Jsoup.clean(bio, Whitelist.none()));
    user.setCommentEmail(commentEmail);
    user.setReplyEmail(replyEmail);
    user.setUrl(url);
    userService.save(user);
    return redirect("/user/" + user.getUsername());
  }

  /**
   * 修改头像
   *
   * @param model
   * @return
   */
  @GetMapping("/setting/changeAvatar")
  public String changeAvatar(Model model) {
    model.addAttribute("user", getUser());
    return "front/user/setting/changeAvatar";
  }

  /**
   * 修改密码
   * @return
   */
  @GetMapping("/setting/changePassword")
  public String changePassword() {
    return "front/user/setting/changePassword";
  }

}
