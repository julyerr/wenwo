package com.wenwo.web.tag;

import com.wenwo.config.SiteConfig;
import com.wenwo.module.topic.model.Topic;
import com.wenwo.module.topic.service.TopicService;
import com.wenwo.config.SiteConfig;
import com.wenwo.module.topic.model.Topic;
import com.wenwo.module.topic.service.TopicService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class HotTopicDirective implements TemplateDirectiveModel {

  @Autowired
  private TopicService topicService;
  @Autowired
  private SiteConfig siteConfig;

  @Override
  public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                      TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
    DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

    int p = map.get("p") == null ? 1 : Integer.parseInt(map.get("p").toString());
    int limit = map.get("limit") == null ? siteConfig.getPageSize() : Integer.parseInt(map.get("limit").toString());

    Page<Topic> page = topicService.findHotTopicAWeek(p, limit);

    environment.setVariable("page", builder.build().wrap(page));

    templateDirectiveBody.render(environment.getOut());
  }
}