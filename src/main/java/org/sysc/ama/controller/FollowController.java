package org.sysc.ama.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.validation.*;

import org.sysc.ama.model.User;
import org.sysc.ama.repo.UserRepository;
import org.sysc.ama.services.CustomUserDetails;
import org.sysc.ama.controller.exception.EntityNotFoundException;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/user/")
public class FollowController {

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/follow/{target}")
    public User follow (@PathVariable("target") Long target,
                        @AuthenticationPrincipal CustomUserDetails principal) {
        User targetUser = userRepo.findById(target).orElseThrow(() -> new EntityNotFoundException("user"));
        principal.getUser().follow(targetUser);
	userRepo.save(principal.getUser());
        return principal.getUser();
    }

    @GetMapping("/following")
    public Map<Long, String> following (@AuthenticationPrincipal CustomUserDetails principal) {
        HashMap<Long, String> following = new HashMap<Long, String>();

        for (User u : principal.getUser().getFollowing()) {
            following.put(u.getId(), u.getName());
        }

        return following;
    }
}
