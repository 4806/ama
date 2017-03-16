package org.sysc.ama.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.sysc.ama.model.Ama;
import org.sysc.ama.model.Answer;
import org.sysc.ama.model.Question;
import org.sysc.ama.model.User;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AnswerRepositoryTest {

    @Autowired AnswerRepository answerRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private AmaRepository amaRepo;

    @Autowired
    private UserRepository userRepo;


    @Test
    public void testPersistQuestion () {
        User author     = new User("Author");
        User subject    = new User("Subject");
        Ama ama         = new Ama("My AMA", subject, true);
        Question q = new Question(author, ama, "Why?");

        userRepo.save(author);
        userRepo.save(subject);
        amaRepo.save(ama);
        questionRepo.save(q);

        Answer answer = new Answer(subject, ama, q, "Because");
        answerRepo.save(answer);
        assertEquals(answerRepo.findById(answer.getId()).get(), answer);
    }
}