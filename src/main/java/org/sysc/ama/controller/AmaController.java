package org.sysc.ama.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.sysc.ama.model.Ama;
import org.sysc.ama.model.User;
import org.sysc.ama.repo.UserRepository;
import org.sysc.ama.repo.AmaRepository;

import java.util.List;

@RestController
public class AmaController {

    @Autowired
    private AmaRepository amaRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/ama/create")
    public Ama create (
            @RequestParam("title") String title,
            @RequestParam("userId") Long userId,
            @RequestParam("public") Boolean isPublic
        ) {
        User user = userRepo.findById(userId);

        // TODO Add check for case where user is not found
        // In this case there should be a `400 Bad Request` error with a body reporting that
        // the user was not found.
        //
        // Example:
        //
        // ```json
        // {
        //      "error"     : true,
        //      "message"   : "The user identity provided does not exist"
        // }
        // ```

        Ama ama = new Ama(title, user, isPublic);

        // TODO Check that AMA title and user pair is unique
        // If the Ama is not unique then an error must be returned. This error should be a
        // `400 Bad Request` error with a body reporting that the title field is already in
        // use for this user.
        //
        // Example:
        //
        // ```json
        // {
        //      "error"     : true,
        //      "message"   : "The given AMA title is already in use for the given user"
        // }
        // ```
        //

        // At this point, the AMA should have passed all validation checks. It can be added
        // to the repository and a successful response could be returned.
        amaRepo.save(ama);

        return ama;
    }


    @GetMapping("/ama/list")
    public List<Ama> list (
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "sort", defaultValue = "updated", required = false) String column,
            @RequestParam(value = "asc", defaultValue = "false", required = false) Boolean asc
        ) {
        // TODO Ensure user has permission to view AMAs
        List<Ama> results;
        Sort sort = new Sort(asc ? Sort.Direction.ASC : Sort.Direction.DESC, column);
        PageRequest request = new PageRequest(page, limit, sort);

        results = amaRepo.findAllByIsPublic(true, request);
        return results;


    }


}

