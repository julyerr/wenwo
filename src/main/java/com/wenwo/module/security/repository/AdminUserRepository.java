package com.wenwo.module.security.repository;

import com.wenwo.module.security.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Integer> {
  AdminUser findByToken(String token);

  AdminUser findByUsername(String username);

}
