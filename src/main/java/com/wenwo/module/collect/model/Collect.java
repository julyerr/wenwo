package com.wenwo.module.collect.model;

import com.wenwo.core.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wenwo_collect")
@Getter
@Setter
public class Collect implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  //与话题的关联关系
  private Integer topicId;

  //与用户的关联关系
  private Integer userId;

  @Column(name = "in_time")
  @JsonFormat(pattern = Constants.DATETIME_FORMAT)
  private Date inTime;
}

