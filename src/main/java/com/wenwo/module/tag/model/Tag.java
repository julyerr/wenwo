package com.wenwo.module.tag.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wenwo_tag")
@Setter
@Getter
public class Tag implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true)
  private String name;

  private String intro;

  private Date inTime;

  private String logo;

  private Integer topicCount;

}
