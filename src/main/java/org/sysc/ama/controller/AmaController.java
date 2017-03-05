package org.sysc.ama.controller;

import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.sysc.ama.model.Ama;
import org.sysc.ama.model.AmaRepository;
import org.sysc.ama.model.User;
import org.sysc.ama.model.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/ama")
public class AmaController {

    @Autowired
    private AmaRepository amaRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/create")
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


    @GetMapping("/list")
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


    @DeleteMapping("/{id}")
    public Ama delete (@PathVariable("id") Long id) {
        Ama ama = amaRepo.findById(id);

        if (ama == null) {
            throw new EntityNotFoundException();
        }

        // TODO Ensure that the current user has the required authorization for this delete
        // If the user does not have the correct authorization, then `401 Unauthorized` error
        // should be returned
        //
        // Example:
        //
        // ```json
        // {
        //      "error"     : true,
        //      "message"   : "The given user is not authorized to delete this AMA"
        // }
        //
        // ```
        amaRepo.delete(id);

        return ama;
    }

}

