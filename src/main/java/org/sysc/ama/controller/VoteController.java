package org.sysc.ama.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.sysc.ama.model.Ama;
import org.sysc.ama.model.Question;
import org.sysc.ama.model.User;
import org.sysc.ama.repo.QuestionRepository;
import org.sysc.ama.services.CustomUserDetails;
import org.sysc.ama.controller.exception.UnauthorizedAccessException;
import org.sysc.ama.controller.exception.EntityNotFoundException;
import org.sysc.ama.controller.exception.UserHasVotedException;

@RestController
@RequestMapping("/ama/{amaId}/question/{qId}")
public class VoteController {

    @Autowired
    private QuestionRepository questionRepo;

    @PostMapping("/upvote")
    public Question upVoteQuestion (@PathVariable("amaId") Long amaId,
                                    @PathVariable("qId") Long qId,
                                    @AuthenticationPrincipal CustomUserDetails principal) {
        Question question = questionRepo.findById(qId).orElseThrow(() -> new EntityNotFoundException("question"));
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


    @PostMapping("/downvote")
    public Question downVoteQuestion(@PathVariable("amaId") Long amaId,
                                    @PathVariable("qId") Long qId,
                                    @AuthenticationPrincipal CustomUserDetails principal) {

        Question question = questionRepo.findById(qId).orElseThrow(() -> new EntityNotFoundException("question"));
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


    @DeleteMapping("/vote")
    public Question deleteVote (@PathVariable("amaId") Ama ama,
                                @PathVariable("qId") Question question,
                                @AuthenticationPrincipal CustomUserDetails principal) {

        question.removeVote(principal.getUser());
        questionRepo.save(question);
        return question;
    }
}

