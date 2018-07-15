package com.wenwo.web.tag;

import com.wenwo.core.base.BaseController;
import com.wenwo.module.user.model.User;
import com.wenwo.module.user.service.UserService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

@Component
public class UserDirective extends BaseController implements TemplateDirectiveModel {

  @Autowired
  private UserService userService;

  @Override
  public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                      TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
    DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

    User user;
    if (map.containsKey("username") && !StringUtils.isEmpty(map.get("username").toString())) {
      user = userService.findByUsername(map.get("username").toString());
    } else {
      user = getUser();
    }

    environment.setVariable("user", builder.build().wrap(user));
    templateDirectiveBody.render(environment.getOut());
  }
}