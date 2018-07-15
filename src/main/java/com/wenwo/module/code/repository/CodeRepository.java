package com.wenwo.module.code.repository;

import com.wenwo.module.code.model.Code;
import com.wenwo.module.code.model.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<Code, Integer> {

  List<Code> findByEmailAndCodeAndType(String email, String code, String type);

}
