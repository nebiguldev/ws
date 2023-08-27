package com.social.ws.file;

import com.social.ws.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {

    List<FileAttachment> findByDateBeforeAndSocialIsNull(Date date);

    List<FileAttachment> findBySocialUser(User user);

}