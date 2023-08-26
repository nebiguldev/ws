package com.social.ws.social;

import com.social.ws.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service(value = "socialSecurity")
public class SocialSecurityService {

    @Autowired
    SocialRepository socialRepository;

    public boolean isAllowedToDelete(long id, User loggedInUser) {
        Optional<Social> optionalSocial = socialRepository.findById(id);
        if(!optionalSocial.isPresent()) {
            return false;
        }

        Social social = optionalSocial.get();
        if(social.getUser().getId() != loggedInUser.getId()) {
            return false;
        }

        return true;
    }

}