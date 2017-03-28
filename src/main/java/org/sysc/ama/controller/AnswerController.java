package org.sysc.ama.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.sysc.ama.model.Ama;
import org.sysc.ama.model.Answer;
import org.sysc.ama.model.Question;
import org.sysc.ama.repo.AnswerRepository;
import org.sysc.ama.repo.QuestionRepository;
import org.sysc.ama.repo.AmaRepository;
import org.sysc.ama.services.CustomUserDetails;
import org.sysc.ama.controller.exception.UnauthorizedAccessException;
import org.sysc.ama.controller.exception.EntityNotFoundException;

@RestController
@RequestMapping("/ama/{amaId}/question/{qId}")
public class AnswerController {

    @Autowired
    private AmaRepository amaRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private AnswerRepository answerRepo;


    @PostMapping("/answer")
    public Answer answerQuestion(@PathVariable("amaId") Long amaId,
                                   @PathVariable("qId") Long questionId,
                                   @RequestParam("body") String body,
                                   @AuthenticationPrincipal CustomUserDetails principal)
    {
        Ama ama = amaRepo.findById(amaId).orElseThrow(() -> new EntityNotFoundException("ama"));
        Question question = questionRepo.findById(questionId).orElseThrow(()->new EntityNotFoundException("question"));

        if (principal.getId() != ama.getSubject().getId()){
            throw new UnauthorizedAccessException("Only the subject of an AMA may answer questions");
        }
        if (question.getAnswer() != null){
            answerRepo.delete(question.getAnswer());
        }

        Answer answer = new Answer(principal.getUser(), question.getAma(), question,  body);
        answerRepo.save(answer);
        return answer;
    }

    @GetMapping("/answers")
    public Answer viewAnswer(@PathVariable("amaId") Ama ama,
                              @PathVariable("qId") Question question
    ){
        Answer answer = answerRepo.findByQuestion(question).orElseThrow(() -> new EntityNotFoundException("answer"));
        return answer;
    }


}
