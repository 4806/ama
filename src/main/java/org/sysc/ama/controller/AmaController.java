package org.sysc.ama.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.sysc.ama.model.Ama;
import org.sysc.ama.model.Answer;
import org.sysc.ama.model.Question;
import org.sysc.ama.model.User;
import org.sysc.ama.repo.AnswerRepository;
import org.sysc.ama.repo.QuestionRepository;
import org.sysc.ama.repo.UserRepository;
import org.sysc.ama.repo.AmaRepository;
import org.sysc.ama.services.CustomUserDetails;
import org.sysc.ama.controller.exception.UnauthorizedAccessException;
import org.sysc.ama.controller.exception.EntityNotFoundException;
import org.sysc.ama.controller.exception.UserHasVotedException;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

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
    private AnswerRepository answerRepo;

    @Autowired
    private UserController userController;

    @PostMapping("")
    public Ama create (
            @RequestParam("title") String title,
            @RequestParam(value="userId", defaultValue = "") Long userId,
            @RequestParam("public") Boolean isPublic,
            @AuthenticationPrincipal CustomUserDetails user
        ) {
        Ama ama = new Ama(title, user.getUser(), isPublic);

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
    public Ama delete (@PathVariable("id") Long amaId,
                       @AuthenticationPrincipal CustomUserDetails principal) {
        Ama ama = amaRepo.findById(amaId).orElseThrow(() -> new EntityNotFoundException("ama"));

        if (ama.getSubject().getId() != principal.getId()){
            throw new UnauthorizedAccessException("Only the user who created an AMA may delete it");
        }

        for (Question q : questionRepo.findByAma(ama)){
            questionRepo.delete(q);
        }
        amaRepo.delete(ama);

        return ama;
    }

    @GetMapping("/{id}")
    public Ama view ( @PathVariable("id") Long id ) {
        Ama ama = amaRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("ama"));
        return ama;
    }

    @PostMapping("/{amaId}/question/{questionId}/upvote")
    public Question upVoteQuestion (@PathVariable("amaId") Long amaId,
                                    @PathVariable("questionId") Long questionId,
                                    @AuthenticationPrincipal CustomUserDetails principal) {
        Question question = questionRepo.findById(questionId).orElseThrow(() -> new EntityNotFoundException("question"));
        User voter = principal.getUser();

        if (question.getAuthor().getId() == principal.getId()){
            throw new UnauthorizedAccessException("The author of a question may not vote on it");
        }

        if (question.hasVoted(voter)) {
            throw new UserHasVotedException(voter);
        }

        question.upVote(voter);
        questionRepo.save(question);
        return question;
    }


    @PostMapping("/{amaId}/question/{questionId}/downvote")
    public Question downVoteQuestion(@PathVariable("amaId") Long amaId,
                                    @PathVariable("questionId") Long questionId,
                                    @AuthenticationPrincipal CustomUserDetails principal) {

        Question question = questionRepo.findById(questionId).orElseThrow(() -> new EntityNotFoundException("question"));
        User voter = principal.getUser();

        if (question.getAuthor().getId() == principal.getId()){
            throw new UnauthorizedAccessException("The author of a question may not vote on it");
        }

        if (question.hasVoted(voter)) {
            throw new UserHasVotedException(voter);
        }

        question.downVote(voter);
        questionRepo.save(question);
        return question;
    }


    @DeleteMapping("/{amaId}/question/{questionId}/vote")
    public Question deleteVote (@PathVariable("amaId") Ama ama,
                                @PathVariable("questionId") Question question,
                                @AuthenticationPrincipal CustomUserDetails principal) {

        question.removeVote(principal.getUser());
        questionRepo.save(question);
        return question;
    }
}

