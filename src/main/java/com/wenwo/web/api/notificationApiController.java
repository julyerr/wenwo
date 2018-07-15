package com.wenwo.web.api;

import com.wenwo.core.base.BaseController;
import com.wenwo.core.bean.Result;
import com.wenwo.core.exception.ApiException;
import com.wenwo.module.notification.service.NotificationService;
import com.wenwo.module.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class notificationApiController extends BaseController {

  @Autowired
  private NotificationService notificationService;

  /**
   * 查询当前用户未读的消息数量
   *
   * @return
   */
  @GetMapping("/notRead")
  public Result notRead() throws ApiException {
    User user = getApiUser();
    return Result.success(notificationService.countByTargetUserAndIsRead(user, false));
  }
}
