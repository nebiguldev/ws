package com.social.ws.social;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.social.ws.shared.CurrentUser;
import com.social.ws.shared.GenericResponse;
import com.social.ws.social.vm.SocialSubmitVM;
import com.social.ws.social.vm.SocialVM;
import com.social.ws.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/1.0")
public class SocialController {

    @Autowired
    SocialService socialService;

    @PostMapping("/socials")
    GenericResponse saveSocial(@Valid @RequestBody SocialSubmitVM social, @CurrentUser User user) {
        socialService.save(social, user);
        return new GenericResponse("Social is saved");
    }

    @GetMapping("/socials")
    Page<SocialVM> getSocials(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable page){
        return socialService.getSocials(page).map(SocialVM::new);
    }

    @GetMapping({"/socials/{id:[0-9]+}", "/users/{username}/socials/{id:[0-9]+}"})
    ResponseEntity<?> getSocialsRelative(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable page,
                                        @PathVariable long id,
                                        @PathVariable(required=false) String username,
                                        @RequestParam(name="count", required = false, defaultValue = "false") boolean count,
                                        @RequestParam(name="direction", defaultValue = "before") String direction){
        if(count) {
            long newSocialCount = socialService.getNewSocialsCount(id, username);
            Map<String, Long> response = new HashMap<>();
            response.put("count", newSocialCount);
            return ResponseEntity.ok(response);
        }
        if(direction.equals("after")) {
            List<SocialVM> newSocials = socialService.getNewSocials(id, username, page.getSort())
                    .stream().map(SocialVM::new).collect(Collectors.toList());
            return ResponseEntity.ok(newSocials);
        }

        return ResponseEntity.ok(socialService.getOldSocials(id, username, page).map(SocialVM::new));
    }

    @GetMapping("/users/{username}/socials")
    Page<SocialVM> getUserSocials(@PathVariable String username, @PageableDefault(sort = "id", direction = Direction.DESC) Pageable page){
        return socialService.getSocialsOfUser(username, page).map(SocialVM::new);
    }

    @DeleteMapping("/socials/{id:[0-9]+}")
    @PreAuthorize("@socialSecurity.isAllowedToDelete(#id, principal)")
    GenericResponse deleteSocial(@PathVariable long id) {
        socialService.delete(id);
        return new GenericResponse("Social removed");
    }

}