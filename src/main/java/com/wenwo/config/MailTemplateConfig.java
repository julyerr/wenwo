package com.wenwo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "mail")
@Getter
@Setter
public class MailTemplateConfig {

  Map<String, Object> register;
  Map<String, Object> commentTopic;
  Map<String, Object> replyComment;

}
