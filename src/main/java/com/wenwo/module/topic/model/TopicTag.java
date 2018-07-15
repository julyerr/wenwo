package com.wenwo.module.topic.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wenwo_topic_tag")
@Setter
@Getter
public class TopicTag implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer topicId;

  private Integer tagId;
}
