package com.wenwo.module.attachment.service;

import com.wenwo.module.attachment.model.Attachment;
import com.wenwo.module.attachment.repository.AttachmentRepository;
import com.wenwo.module.attachment.model.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class AttachmentService {

  @Autowired
  private AttachmentRepository attachmentRepository;

  public Attachment save(Attachment attachment) {
    return attachmentRepository.save(attachment);
  }

  public Attachment createAttachment(String localPath, String fileName, String requestUrl, String fileType,
                                     Integer width, Integer height, String size, String suffix, String md5) {
    Attachment attachment = new Attachment();
    attachment.setLocalPath(localPath);
    attachment.setFileName(fileName);
    attachment.setInTime(new Date());
    attachment.setRequestUrl(requestUrl);
    attachment.setType(fileType);
    attachment.setWidth(width);
    attachment.setHeight(height);
    attachment.setSize(size);
    attachment.setSuffix(suffix);
    attachment.setMd5(md5);
    return this.save(attachment);
  }

  public Attachment findByMd5(String md5) {
    return attachmentRepository.findByMd5(md5);
  }
}
