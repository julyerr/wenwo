package com.wenwo.module.topic.repository;

import com.wenwo.module.topic.model.TopicTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TopicTagRepository extends JpaRepository<TopicTag, Long> {

//  TODO 直接使用关联查询而不是外键约束的好处，在这里就体现出来了
  List<TopicTag> findByTopicId(Integer topicId);

  void deleteByTopicId(Integer topicId);
}
