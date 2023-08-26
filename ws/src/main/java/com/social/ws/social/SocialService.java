package com.social.ws.social;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.social.ws.file.FileAttachment;
import com.social.ws.file.FileAttachmentRepository;
import com.social.ws.file.FileService;
import com.social.ws.social.vm.SocialSubmitVM;
import com.social.ws.user.User;
import com.social.ws.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;



@Service
public class SocialService {

    SocialRepository socialRepository;

    UserService userService;

    FileAttachmentRepository fileAttachmentRepository;

    FileService fileService;

    public SocialService(SocialRepository socialRepository, UserService userService, FileAttachmentRepository fileAttachmentRepository
            ,FileService fileService) {
        super();
        this.socialRepository = socialRepository;
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.fileService = fileService;
        this.userService = userService;
    }

    public void save(SocialSubmitVM socialSubmitVM, User user) {
        Social social = new Social();
        social.setContent(socialSubmitVM.getContent());
        social.setTimestamp(new Date());
        social.setUser(user);
        socialRepository.save(social);
        Optional<FileAttachment> optionalFileAttachment = fileAttachmentRepository.findById(socialSubmitVM.getAttachmentId());
        if(optionalFileAttachment.isPresent()) {
            FileAttachment fileAttachment = optionalFileAttachment.get();
            fileAttachment.setSocial(social);
            fileAttachmentRepository.save(fileAttachment);
        }
    }

    public Page<Social> getSocials(Pageable page) {
        return socialRepository.findAll(page);
    }

    public Page<Social> getSocialsOfUser(String username, Pageable page) {
        User inDB = userService.getByUsername(username);
        return socialRepository.findByUser(inDB, page);
    }

    public Page<Social> getOldSocials(long id, String username, Pageable page) {
        Specification<Social> specification = idLessThan(id);
        if(username != null) {
            User inDB = userService.getByUsername(username);
            specification = specification.and(userIs(inDB));
        }
        return socialRepository.findAll(specification, page);
    }

    public long getNewSocialsCount(long id, String username) {
        Specification<Social> specification = idGreaterThan(id);
        if(username != null) {
            User inDB = userService.getByUsername(username);
            specification = specification.and(userIs(inDB));
        }
        return socialRepository.count(specification);
    }

    public List<Social> getNewSocials(long id, String username, Sort sort) {
        Specification<Social> specification = idGreaterThan(id);
        if(username != null) {
            User inDB = userService.getByUsername(username);
            specification = specification.and(userIs(inDB));
        }
        return socialRepository.findAll(specification, sort);
    }


    Specification<Social> idLessThan(long id){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThan(root.get("id"), id);
        };
    }

    Specification<Social> userIs(User user){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("user"), user);
        };
    }

    Specification<Social> idGreaterThan(long id){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThan(root.get("id"), id);
        };
    }

    public void delete(long id) {
        Social inDB = socialRepository.getOne(id);
        if(inDB.getFileAttachment() != null) {
            String fileName = inDB.getFileAttachment().getName();
            fileService.deleteAttachmentFile(fileName);
        }
        socialRepository.deleteById(id);
    }

}