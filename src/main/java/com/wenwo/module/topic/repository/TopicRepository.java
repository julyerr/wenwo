package com.wenwo.module.topic.repository;

import com.wenwo.module.topic.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer>, JpaSpecificationExecutor<Topic> {

    //  TODO countQuery用法，直接使用关联查询而不是外键约束等,使用Page<MAP>缓存结果？
    @Query(value = "select t as topic, u as user from Topic t, User u where t.userId = u.id and t.userId = ?1",
            countQuery = "select count(1) from Topic t, User u where t.userId = u.id and t.userId = ?1")
    Page<Map> findByUserId(Integer userId, Pageable pageable);

    void deleteByUserId(Integer userId);

    @Query(value = "select t as topic, u as user from Topic t, User u where t.userId = u.id",
            countQuery = "select count(1) from Topic t, User u where t.userId = u.id")
    Page<Map> findTopics(Pageable pageable);

    @Query(value = "select t as topic, u as user from Topic t, User u where t.userId = u.id and t.good = ?1",
            countQuery = "select count(1) from Topic t, User u where t.userId = u.id and t.good = ?1")
    Page<Map> findByGood(Boolean b, Pageable pageable);

    @Query(value = "select t as topic, u as user from Topic t, User u where t.userId = u.id and t.commentCount = ?1",
            countQuery = "select count(1) from Topic t, User u where t.userId = u.id and t.commentCount = ?1")
    Page<Map> findByCommentCount(Integer commentCount, Pageable pageable);

    Topic findByTitle(String title);

    void delete(Topic topic);

    @Query(value = "select t as topic, u as user from Topic t, User u where t.userId = u.id",
            countQuery = "select count(1) from Topic t, User u where t.userId = u.id")
    Page<Map> findAllForAdmin(Pageable pageable);

    @Query(value = "select t as topic, u as user from Topic t, User u, TopicTag tt where t.userId = u.id and t.id = tt.topicId and tt.tagId = ?1",
            countQuery = "select count(1) from Topic t, User u, TopicTag tt where t.userId = u.id and t.id = tt.topicId and tt.tagId = ?1")
    Page<Map> findTopicsByTagId(Integer tagId, Pageable pageable);

    Page<Topic> findByTitleContaining(String title, Pageable pageable);
}
