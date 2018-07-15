package com.wenwo.module.topic.service;

import com.wenwo.module.tag.model.Tag;
import com.wenwo.module.tag.service.TagService;
import com.wenwo.module.topic.model.TopicTag;
import com.wenwo.module.topic.repository.TopicTagRepository;
import com.wenwo.module.topic.repository.TopicTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TopicTagService {
  @Autowired
  private TopicTagRepository topicTagRepository;
  @Autowired
  private TagService tagService;

  public void deleteByTopicId(Integer topicId) {
    // 先把标签的话题数都 - 1
    List<TopicTag> topicTags = topicTagRepository.findByTopicId(topicId);
    List<Tag> tags = new ArrayList<>();
    topicTags.forEach(topicTag -> {
      Tag tag = tagService.findById(topicTag.getTagId());
      tag.setTopicCount(tag.getTopicCount() - 1);
      tags.add(tag);
    });
    tagService.save(tags);
    // 操作完了，再删除中间表里的数据
    topicTagRepository.deleteByTopicId(topicId);
  }

  public List<TopicTag> save(List<Tag> tags, Integer topicId) {
    List<TopicTag> topicTags = new ArrayList<>();
    tags.forEach(_tag -> {
      TopicTag topicTag = new TopicTag();
      topicTag.setTagId(_tag.getId());
      topicTag.setTopicId(topicId);
      topicTags.add(topicTag);
    });
    return topicTagRepository.saveAll(topicTags);
  }

}
