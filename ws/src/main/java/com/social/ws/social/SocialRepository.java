package com.social.ws.social;

import com.social.ws.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SocialRepository extends JpaRepository<Social, Long>, JpaSpecificationExecutor<Social> {

    Page<Social> findByUser(User user, Pageable page);

}