package org.sysc.ama.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.sysc.ama.model.Ama;
import org.sysc.ama.model.Question;
import org.sysc.ama.model.Answer;
import org.sysc.ama.repo.QuestionRepository;
import org.sysc.ama.repo.AmaRepository;
import org.sysc.ama.repo.AnswerRepository;
import org.sysc.ama.services.CustomUserDetails;
import org.sysc.ama.controller.exception.EntityNotFoundException;
import org.sysc.ama.controller.exception.UnauthorizedAccessException;

import java.util.List;

@RestController
@RequestMapping("/ama/{amaId}")
public class QuestionController {

    @Autowired
    private AmaRepository amaRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private AnswerRepository answerRepo;


    @PostMapping("/question")
    public Question addQuestion (@PathVariable("amaId") Long amaId,
                                 @RequestParam(value="userId", defaultValue = "") Long userId,
                                 @RequestParam("body") String body,
                                 @AuthenticationPrincipal CustomUserDetails principal
        ){
        Ama ama = amaRepo.findById(amaId).orElseThrow(() -> new EntityNotFoundException("ama"));
        Question q = new Question(principal.getUser(), ama, body);

        questionRepo.save(q);
        return q;
    }

    @DeleteMapping("/question/{id}")
    public Question deleteQuestion(@PathVariable("amaId") Long amaId,
                                   @PathVariable("id") Long questionId,
                                   @AuthenticationPrincipal CustomUserDetails principal
        ){
        Ama ama = amaRepo.findById(amaId).orElseThrow(() -> new EntityNotFoundException("ama"));
        Question question = questionRepo.findById(questionId).orElseThrow(() -> new EntityNotFoundException("question"));

        if ((question.getAuthor().getId() != principal.getId()) && (ama.getSubject().getId() != principal.getId())){
            throw new UnauthorizedAccessException("Only the author of a question or the AMA subject may delete the question");
        }

        Answer answer = answerRepo.findByQuestion(question).orElse(null);
        if (answer != null){
            answerRepo.delete(answer);
        }

        questionRepo.delete(question);
        return question;
    }

    @GetMapping("/question/{id}")
    public Question viewQuestion(@PathVariable("amaId") Long amId,
                                   @PathVariable("id") Long id
    ){

        Question question = questionRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("question"));
        return question;
    }

    @GetMapping("/questions")
    public List<Question> viewQuestions(@PathVariable("amaId") Long amaId,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("limit") Integer limit,
                                        @RequestParam(value = "sort", defaultValue = "updated", required = false) String column,
                                        @RequestParam(value = "asc", defaultValue = "false", required = false) Boolean asc
    ) {

        Ama ama = amaRepo.findById(amaId).orElseThrow(() -> new EntityNotFoundException("ama"));
        Sort sort = new Sort(asc ? Sort.Direction.ASC : Sort.Direction.DESC, column);
        PageRequest request = new PageRequest(page, limit, sort);

        return questionRepo.findByAma(ama, request);
    }

}


