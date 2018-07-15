package com.wenwo.web.tag;

import com.wenwo.core.base.BaseController;
import com.wenwo.module.comment.service.CommentService;
import com.wenwo.module.user.model.User;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class CommentsDirective extends BaseController implements TemplateDirectiveModel {

  @Autowired
  private CommentService commentService;

  @Override
  public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                      TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
    DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

    int topicId = Integer.parseInt(map.get("id").toString());

    List<Map> comments = commentService.findCommentWithTopic(topicId);
    environment.setVariable("comments", builder.build().wrap(comments));

    User user = getUser();
    if (user != null) {
      environment.setVariable("user", builder.build().wrap(user));
    }

    templateDirectiveBody.render(environment.getOut());
  }
}