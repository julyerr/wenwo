package com.wenwo.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CookieConfig {

  private String domain;
  private String userName;
  private Integer userMaxAge;

  private String adminUserName;
  private Integer adminUserMaxAge;

}
