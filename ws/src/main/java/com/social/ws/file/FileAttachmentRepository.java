package com.social.ws.file;

import com.social.ws.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {

    List<FileAttachment> findByDateBeforeAndSocialIsNull(Date date);

    List<FileAttachment> findBySocialUser(User user);

}