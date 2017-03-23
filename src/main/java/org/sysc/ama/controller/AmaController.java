package org.sysc.ama.controller;

import org.springframework.security.access.prepost.PreAuthorize;
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
        for (Question q : questionRepo.findByAma(ama)){
            questionRepo.delete(q);
        }
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
                                 @RequestParam("body") String body,
                                 @AuthenticationPrincipal User user
        ){
        Ama ama = amaRepo.findById(amaId).orElseThrow(() -> new EntityNotFoundException("ama"));

        Question q = new Question(user, ama, body);

        questionRepo.save(q);
        return q;
    }

    @DeleteMapping("/{amaId}/question/{id}")
    public Question deleteQuestion(@PathVariable("amaId") Long amId,
                                   @PathVariable("id") Long id
        ){

        Question question = questionRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("question"));
        questionRepo.delete(question);
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

    @PostMapping("/{amaId}/question/{questionId}/answer")
    @PreAuthorize("#ama.subject.id == principal.user.id")
    public Answer answerQuestion(@PathVariable("amaId") Ama ama,
                                   @PathVariable("questionId") Question question,
                                   @RequestParam("body") String body,
                                   @AuthenticationPrincipal CustomUserDetails principal)
    {
        Answer answer = new Answer(principal.getUser(), question.getAma(), question,  body);
        answerRepo.save(answer);
        return answer;
    }

    @PostMapping("/{amaId}/question/{questionId}/upvote")
    @PreAuthorize("#question.author.id != principal.user.id")
    public Question upVoteQuestion (@PathVariable("amaId") Ama ama,
                                    @PathVariable("questionId") Question question,
                                    @AuthenticationPrincipal CustomUserDetails principal) {

        User voter = principal.getUser();

        if (question.hasVoted(voter)) {
            throw new UserHasVotedException(voter);
        }

        question.upVote(voter);
        questionRepo.save(question);
        return question;
    }


    @PostMapping("/{amaId}/question/{questionId}/downvote")
    @PreAuthorize("#question.author.id != principal.user.id")
    public Question downVoteQuestion(@PathVariable("amaId") Ama ama,
                                    @PathVariable("questionId") Question question,
                                    @AuthenticationPrincipal CustomUserDetails principal) {

        User voter = principal.getUser();

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

