package com.wenwo.module.user.repository;

import com.wenwo.module.user.model.OAuth2User;
import com.wenwo.module.user.model.OAuth2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2UserRepository extends JpaRepository<OAuth2User, Integer> {

  OAuth2User findByOauthUserIdAndType(String oauthUserId, String type);
}
