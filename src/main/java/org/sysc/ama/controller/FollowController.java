package org.sysc.ama.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.validation.*;

import org.sysc.ama.model.User;
import org.sysc.ama.model.UserFollowException;
import org.sysc.ama.model.UserUnfollowException;
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
                        @AuthenticationPrincipal CustomUserDetails principal) throws UserFollowException {
        User targetUser = userRepo.findById(target).orElseThrow(() -> new EntityNotFoundException("user"));
        User user = userRepo.findById(principal.getUser().getId()).orElseThrow(() -> new EntityNotFoundException("user"));

        user.follow(targetUser);
        userRepo.save(user);
        return user;
    }

    @GetMapping("/following")
    public Map<Long, String> following (@AuthenticationPrincipal CustomUserDetails principal) {
        HashMap<Long, String> following = new HashMap<Long, String>();
        User user = userRepo.findById(principal.getUser().getId()).orElseThrow(() -> new EntityNotFoundException("user"));

        for (User u : user.getFollowing()) {
            following.put(u.getId(), u.getName());
        }

        return following;
    }

    @DeleteMapping("follow/{target}")
    public User unfollow (@PathVariable("target") Long target,
                          @AuthenticationPrincipal CustomUserDetails principal) throws UserUnfollowException {
        User targetUser = userRepo.findById(target).orElseThrow(() -> new EntityNotFoundException("user"));
        User user = userRepo.findById(principal.getUser().getId()).orElseThrow(() -> new EntityNotFoundException("user"));

        user.unfollow(targetUser);
        userRepo.save(user);
        return user;

    }
}
