package org.sysc.ama.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.sysc.ama.model.Ama;
import org.sysc.ama.model.Question;
import org.sysc.ama.model.User;
import org.sysc.ama.repo.QuestionRepository;
import org.sysc.ama.repo.UserRepository;
import org.sysc.ama.repo.AmaRepository;

import java.util.List;

@RestController
@RequestMapping("/ama")
public class AmaController {

    @Autowired
    private AmaRepository amaRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private UserController userController;

    @PostMapping("")
    public Ama create (
            @RequestParam("title") String title,
            @RequestParam("userId") Long userId,
            @RequestParam("public") Boolean isPublic,
            @AuthenticationPrincipal User user
        ) {

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
        Ama ama = amaRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("ama"));

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

    @GetMapping("/{id}")
    public Ama view ( @PathVariable("id") Long id ) {
        Ama ama = amaRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("ama"));
        return ama;
    }

    @PostMapping("/{amaId}/question")
    public Question addQuestion (@PathVariable("amaId") Long amaId,
                                 @RequestParam("userId") Long userId,
                                 @RequestParam("body") String body
        ){
        Ama ama = amaRepo.findById(amaId).orElseThrow(() -> new EntityNotFoundException("ama"));
        User user = userController.getCurrentUserLogin();

        Question q = new Question(user, ama, body);

        questionRepo.save(q);
        return q;
    }

    @DeleteMapping("/{amaId}/question/{id}")
    public Question deleteQuestion(@PathVariable("amaId") Long amId,
                                   @PathVariable("id") Long id
        ){

        Question question = questionRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("question"));
        questionRepo.delete(id);

        return question;
    }

    @GetMapping("/{amaId}/question/{id}")
    public Question viewQuestion(@PathVariable("amaId") Long amId,
                                   @PathVariable("id") Long id
    ){

        Question question = questionRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("question"));
        return question;
    }


    @GetMapping("/{id}/questions")
    public List<Question> viewQuestions(@PathVariable("id") Long id,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("limit") Integer limit,
                                        @RequestParam(value = "sort", defaultValue = "updated", required = false) String column,
                                        @RequestParam(value = "asc", defaultValue = "false", required = false) Boolean asc
    ) {

        Ama ama = amaRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("ama"));
        Sort sort = new Sort(asc ? Sort.Direction.ASC : Sort.Direction.DESC, column);
        PageRequest request = new PageRequest(page, limit, sort);

        return questionRepo.findByAma(ama, request);
    }

}

