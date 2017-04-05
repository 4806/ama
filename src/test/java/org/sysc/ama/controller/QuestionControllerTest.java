package org.sysc.ama.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.annotation.DirtiesContext;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.sysc.ama.model.Question;
import org.sysc.ama.model.User;
import org.sysc.ama.model.Ama;

import org.sysc.ama.repo.AnswerRepository;
import org.sysc.ama.repo.QuestionRepository;
import org.sysc.ama.repo.UserRepository;
import org.sysc.ama.repo.AmaRepository;

import javax.annotation.PostConstruct;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AmaRepository amaRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private AnswerRepository answerRepo;

    private User testUser;

    private User secondaryUser;

    private Ama amaFoo;
    private Ama amaBar;
    private Ama amaBaz;

    private Question fooQuestion;

    @PostConstruct
    public void init() {
        this.testUser = new User("TestUser");
        this.userRepo.save(this.testUser);


        this.userRepo.save(new User("BadUser"));

        this.secondaryUser = new User("SecondaryUser");
        this.userRepo.save(this.secondaryUser);
    }

    @Before
    public void before () {
        // Note About Delay
        //
        // When an AMA is created it receives a created timestamp that is accurate to the
        // nearest millisecond. In order to properly test sorting by time, an artificial
        // delay of 2 milliseconds has been added between the creation of these test AMAs.
        // This will guarantee the order of the AMAs when sorting by created dates.

        this.amaFoo = new Ama("Foo", this.testUser, true);
        delay(2);
        this.amaBar = new Ama("Bar", this.testUser, true);
        delay(2);
        this.amaBaz = new Ama("Baz", this.testUser, true);

        amaRepo.save(this.amaFoo);
        amaRepo.save(this.amaBar);
        amaRepo.save(this.amaBaz);
        this.fooQuestion = new Question(this.secondaryUser, this.amaFoo, "Don't avoid the question");
        questionRepo.save(this.fooQuestion);
    }

    @After
    public void after() {
        this.answerRepo.deleteAll();
        this.questionRepo.deleteAll();
        this.amaRepo.deleteAll();
        this.userRepo.deleteAll();
    }

    @Test
    @WithUserDetails("TestUser")
    public void testAddQuestionToAma () throws Exception {

        mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question")
                            .param("body", "What is the meaning of life?")
                            .param("userId", this.testUser.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("What is the meaning of life?"))
                .andExpect(jsonPath("$.ama.id").value(this.amaFoo.getId()));
    }


    @Test
    @WithUserDetails("TestUser")
    public void testViewQuestions () throws Exception {

        mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question")
                .param("body", "What is the meaning of life?")
                .param("userId", this.testUser.getId().toString()))
                .andReturn();

        mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question")
                .param("body", "Is that a hippo?")
                .param("userId", this.testUser.getId().toString()))
                .andReturn();

        mockMvc.perform(get("/ama/" + this.amaFoo.getId() + "/questions")
                            .param("start", "0")
                            .param("count", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[?(@.body == \"Is that a hippo?\")]").exists())
                .andExpect(jsonPath("$.data[?(@.body == \"What is the meaning of life?\")]").exists())
                .andReturn();
    }

    @Test
    @WithUserDetails("TestUser")
    public void testViewQuestion () throws Exception {

        mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question")
                .param("body", "What is the meaning of life?")
                .param("userId", this.testUser.getId().toString()));

        mockMvc.perform(get("/ama/" + this.amaFoo.getId() + "/question/" + this.fooQuestion.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("Don't avoid the question"));
    }

    @Test
    @WithUserDetails("TestUser")
    public void testAnaswerIsIncludedWithQuestion () throws Exception {

        MvcResult result = mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question")
                .param("body", "What is the meaning of life?")
                .param("userId", this.testUser.getId().toString()))
                .andReturn();
        Integer questionId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");

        mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question/" + questionId + "/answer")
                .param("body", "No clue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("No clue"));

        mockMvc.perform(get("/ama/" + this.amaFoo.getId() + "/question/" + questionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer.body").value("No clue"));
    }

    public void testDeleteQuestionAsAmaSubject () throws Exception {
        MvcResult result =  mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question")
                .param("body", "What is the meaning of life?")
                .param("userId", this.testUser.getId().toString()))
                .andReturn();

        Integer id = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");

        mockMvc.perform(delete("/ama/" + this.amaFoo.getId() + "/question/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("What is the meaning of life?"));

        mockMvc.perform(get("/ama/" + this.amaFoo.getId() + "/questions")
                .param("start", "0")
                .param("count", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[?(@.body == \"What is the meaning of life?\")]").doesNotExist());
    }

    @Test
    @WithUserDetails("SecondaryUser")
    public void testDeleteQuestionAsAuthor () throws Exception {

        mockMvc.perform(delete("/ama/" + this.amaFoo.getId() + "/question/" + this.fooQuestion.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("Don't avoid the question"));
    }

    @Test
    @WithUserDetails("BadUser")
    public void testDeleteQuestionUnauthorized () throws Exception {
        mockMvc.perform(delete("/ama/" + this.amaFoo.getId() + "/question/" + this.fooQuestion.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("TestUser")
    public void testDeleteQuestionWithAnswer () throws Exception {
        MvcResult result = mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question")
                .param("body", "What is the meaning of life?")
                .param("userId", this.testUser.getId().toString()))
                .andReturn();
        Integer questionId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");

        mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question/" + questionId + "/answer")
                .param("body", "No clue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("No clue"));

        mockMvc.perform(delete("/ama/" + this.amaFoo.getId() + "/question/" + questionId))
                .andExpect(status().isOk());
    }


    /**
     * Sleeps the current process for the given number of milliseconds
     *
     * @param time - The duration to sleep the process
     */
    public void delay (int time) {
        try {
            Thread.sleep(time);
        } catch(InterruptedException ex) {}
    }

}
