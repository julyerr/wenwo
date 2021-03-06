package com.wenwo.web.front;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wenwo.config.SiteConfig;
import com.wenwo.core.base.BaseController;
import com.wenwo.core.util.CookieHelper;
import com.wenwo.core.util.StrUtil;
import com.wenwo.core.util.security.Base64Helper;
import com.wenwo.module.user.model.OAuth2User;
import com.wenwo.module.user.model.User;
import com.wenwo.module.user.service.OAuth2UserService;
import com.wenwo.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

//TODO 正式上线的时候调试
@Controller
@RequestMapping("/oauth2")
public class OAuth2Controller extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private OAuth2UserService oAuth2UserService;
    @Autowired
    private SiteConfig siteConfig;

    @GetMapping("/github/login")
    public String githubLogin(HttpSession session) {
        String state = StrUtil.randomString(6);
        session.setAttribute("state", state); // TODO 如果做分布式，这里还有些问题
        String url = "https://github.com/login/oauth/authorize?client_id=" +
                siteConfig.getOauth2().getGithub().getClientId() +
                "&redirect_uri=" + siteConfig.getOauth2().getGithub().getCallbackUrl() +
                "&scope=user" +
                "&state=" + state;
        return "redirect:" + url;
    }

    @GetMapping("/github/callback")
    public String githubCallback(String code, String state, HttpServletResponse response, HttpSession session) {
        String sessionState = (String) session.getAttribute("state");
        Assert.isTrue(state.equalsIgnoreCase(sessionState), "非法请求");

        String url = "https://github.com/login/oauth/access_token";
        RestTemplate restTemplate = new RestTemplate();
        String params = "client_id=" + siteConfig.getOauth2().getGithub().getClientId() +
                "&client_secret=" + siteConfig.getOauth2().getGithub().getClientSecret() +
                "&redirect_uri=" + siteConfig.getOauth2().getGithub().getCallbackUrl() +
                "&state=" + state +
                "&code=" + code;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        Map<String, Object> map = StrUtil.formatParams(exchange.getBody());
        Assert.notNull(map, "没有拿到AccessToken");
        String accessToken = map.get("access_token").toString();
        String url1 = "https://api.github.com/user?access_token=" + accessToken;
        RestTemplate restTemplate1 = new RestTemplate();
        String resp1 = restTemplate1.getForObject(url1, String.class);
	JsonObject jsonObject = new JsonParser().parse(resp1).getAsJsonObject();
        String nickName = jsonObject.get("login").getAsString();
        String avatar = jsonObject.get("avatar_url").getAsString();
        String githubId = jsonObject.get("id").getAsString();
        JsonElement tmp = jsonObject.get("email");
        String email = null;
        if (!tmp.isJsonNull()) {
            email = tmp.getAsString();
        }
        tmp = jsonObject.get("bio");
        String bio = null;
        if (!tmp.isJsonNull()) {
            bio = tmp.getAsString();
        }
        String html_url = null;
        tmp = jsonObject.get("html_url");
        if (!tmp.isJsonNull()) {
            html_url = tmp.getAsString();
        }

        OAuth2User oAuth2User = oAuth2UserService.findByOauthUserIdAndType(githubId, OAuth2User.Type.GITHUB.name());
        User user;
        if (oAuth2User != null) {
            oAuth2User.setNickName(nickName);
            oAuth2User.setAvatar(avatar);
            oAuth2UserService.save(oAuth2User);
            user = userService.findById(oAuth2User.getUserId());
        } else {
            user = (User) session.getAttribute("user");
            if (user == null) {
                User _user = userService.findByUsername(nickName);
                if (_user != null) nickName = nickName + "_" + githubId;
                user = userService.createUser(nickName, StrUtil.randomString(16), email, avatar, html_url, bio);
            }
            oAuth2UserService.createOAuth2User(nickName, avatar, user.getId(), githubId,
                    accessToken, OAuth2User.Type.GITHUB.name());
        }
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
        return redirect("/");
    }
}
