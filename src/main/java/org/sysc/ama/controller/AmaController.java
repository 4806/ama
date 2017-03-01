package org.sysc.ama.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Autowired;

import org.sysc.ama.model.Ama;
import org.sysc.ama.model.AmaRepository;
import org.sysc.ama.model.User;
import org.sysc.ama.model.UserRepository;

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


}

