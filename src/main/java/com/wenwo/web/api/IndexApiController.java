package com.wenwo.web.api;

import com.wenwo.config.SiteConfig;
import com.wenwo.core.base.BaseController;
import com.wenwo.core.bean.Result;
import com.wenwo.core.exception.ApiAssert;
import com.wenwo.core.util.CookieHelper;
import com.wenwo.core.util.EnumUtil;
import com.wenwo.core.util.StrUtil;
import com.wenwo.core.util.identicon.Identicon;
import com.wenwo.core.util.security.Base64Helper;
import com.wenwo.core.util.security.crypto.BCryptPasswordEncoder;
import com.wenwo.module.code.model.CodeEnum;
import com.wenwo.module.code.service.CodeService;
import com.wenwo.module.tag.service.TagService;
import com.wenwo.module.topic.model.TopicTab;
import com.wenwo.module.topic.service.TopicService;
import com.wenwo.module.user.model.User;
import com.wenwo.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class IndexApiController extends BaseController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private Identicon identicon;
    @Autowired
    private SiteConfig siteConfig;

    /**
     * 首页接口
     *
     * @param pageNo 页数
     * @param tab    类别，只能为空或者填: NEWEST, NOANSWER, GOOD
     * @return Page对象，里面有分页信息
     */
    @GetMapping("/")
    public Result index(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "") String tab) {
        if (!StringUtils.isEmpty(tab)) {
            ApiAssert.isTrue(EnumUtil.isDefined(TopicTab.values(), tab), "参数错误");
        }
        Page<Map> page = topicService.page(pageNo, siteConfig.getPageSize(), tab);
        return Result.success(page);
    }

    /**
     * 搜索
     *
     * @param keyword 关键字
     * @param pageNo
     * @return
     */
    @GetMapping("/search")
    public Result search(String keyword, @RequestParam(defaultValue = "1") Integer pageNo) {
        return Result.success(topicService.pageByTitleKeyWord(keyword, pageNo, siteConfig.getPageSize()));
    }

    /**
     * 标签页
     *
     * @param pageNo 页数
     * @return Page对象，里面有分页信息
     */
    @GetMapping("/tags")
    public Result tags(@RequestParam(defaultValue = "1") Integer pageNo) {
        return Result.success(tagService.page(pageNo, siteConfig.getPageSize()));
    }

    /**
     * 声望前100名用户
     *
     * @return Page对象，里面有分页信息
     */
    @GetMapping("/top100")
    public Result top100() {
        return Result.success(userService.findByReputation(1, 100));
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param response
     * @return
     */
    @PostMapping("/login")
    public Result login(String username, String password, HttpServletResponse response) {
        ApiAssert.notEmpty(username, "用户名不能为空");
        ApiAssert.notEmpty(password, "密码不能为空");

        User user = userService.findByUsername(username);
        ApiAssert.notNull(user, "用户不存在");
        ApiAssert.notTrue(user.getBlock(), "用户已被禁");

//        BCrypt 算法
        ApiAssert.isTrue(new BCryptPasswordEncoder().matches(password, user.getPassword()), "密码不正确");

        // 把用户信息写入cookie
        CookieHelper.addCookie(
                response,
                siteConfig.getCookie().getDomain(),
                "/",
                siteConfig.getCookie().getUserName(),
                Base64Helper.encode(user.getToken().getBytes()),
                siteConfig.getCookie().getUserMaxAge() * 24 * 60 * 60,
                true,
                false
        );

        return Result.success();
    }

    /**
     * 注册验证
     *
     * @param username  用户名
     * @param password  密码
     * @param email     邮箱
     * @param emailCode 邮箱验证码
     * @param code      图形验证码
     * @param session
     * @return
     */
    @PostMapping("/register")
    public Result register(String username, String password, String email, String emailCode, String code, HttpSession session) {
//    即时前端进行了验证，但是服务器端还是需要重复验证的
        String genCaptcha = (String) session.getAttribute("index_code");
        ApiAssert.notEmpty(code, "验证码不能为空");
        ApiAssert.notEmpty(username, "用户名不能为空");
        ApiAssert.notEmpty(password, "密码不能为空");
        ApiAssert.notEmpty(email, "邮箱不能为空");

        ApiAssert.isTrue(genCaptcha.toLowerCase().equals(code.toLowerCase()), "验证码错误");
        ApiAssert.isTrue(StrUtil.check(username, StrUtil.userNameCheck), "用户名不合法");

        User user = userService.findByUsername(username);
        ApiAssert.isNull(user, "用户名已经被注册");

        User user_email = userService.findByEmail(email);
        ApiAssert.isNull(user_email, "邮箱已经被使用");

        int validateResult = codeService.validateCode(email, emailCode, CodeEnum.EMAIL);
        ApiAssert.notTrue(validateResult == 1, "邮箱验证码不正确");
        ApiAssert.notTrue(validateResult == 2, "邮箱验证码已过期");
        ApiAssert.notTrue(validateResult == 3, "邮箱验证码已经被使用");

        // generator avatar
        String avatar = identicon.generator(username);

        // 创建用户
        userService.createUser(username, password, email, avatar, null, null);

        return Result.success();
    }
}
