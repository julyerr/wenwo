package com.wenwo.module.attachment.repository;

import com.wenwo.module.attachment.model.Attachment;
import com.wenwo.module.attachment.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {

  Attachment findByMd5(String md5);
}
