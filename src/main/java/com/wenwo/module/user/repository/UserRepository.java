package com.wenwo.module.user.repository;

import com.wenwo.module.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  User findById(int id);

  User findByUsername(String username);

  void deleteById(int id);

  User findByToken(String token);

  User findByEmail(String email);
}
