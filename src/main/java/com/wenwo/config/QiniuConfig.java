package com.wenwo.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QiniuConfig {

  private String accessKey;
  private String secretKey;
  private String bucket;
  private String domain;

}
