package com.wenwo.web.api;

import com.wenwo.core.base.BaseController;
import com.wenwo.core.bean.Result;
import com.wenwo.core.exception.ApiAssert;
import com.wenwo.module.collect.model.Collect;
import com.wenwo.module.collect.service.CollectService;
import com.wenwo.module.topic.model.Topic;
import com.wenwo.module.topic.service.TopicService;
import com.wenwo.module.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/collect")
public class CollectApiController extends BaseController {

  @Autowired
  private CollectService collectService;
  @Autowired
  private TopicService topicService;

  @GetMapping("/add")
  public Result add(Integer topicId) {
    User user = getApiUser();
    Topic topic = topicService.findById(topicId);

    ApiAssert.notNull(topic, "话题不存在");

    Collect collect = collectService.findByUserIdAndTopicId(getUser().getId(), topicId);
    ApiAssert.isNull(collect, "你已经收藏了这个话题");

    collectService.createCollect(topic, user.getId());

    return Result.success(collectService.countByTopicId(topicId));
  }

  @GetMapping("/delete")
  public Result delete(Integer topicId) {
    User user = getApiUser();
    Collect collect = collectService.findByUserIdAndTopicId(user.getId(), topicId);

    ApiAssert.notNull(collect, "你还没收藏这个话题");

    collectService.deleteById(collect.getId());
    return Result.success(collectService.countByTopicId(topicId));
  }
}
