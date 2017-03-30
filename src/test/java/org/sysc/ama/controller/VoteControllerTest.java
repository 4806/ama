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

import org.sysc.ama.repo.QuestionRepository;
import org.sysc.ama.repo.UserRepository;
import org.sysc.ama.repo.AmaRepository;

import javax.annotation.PostConstruct;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AmaRepository amaRepo;

    @Autowired
    private QuestionRepository questionRepo;

    private User testUser;

    private User secondaryUser;

    private Ama amaFoo;

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

        amaRepo.save(this.amaFoo);
    }

    @After
    public void after() {
        this.questionRepo.deleteAll();
        this.amaRepo.deleteAll();
        this.userRepo.deleteAll();
    }

    @Test
    @WithUserDetails("SecondaryUser")
    public void testUpVoteQuestion () throws Exception {
        Question q = new Question(this.testUser, this.amaFoo, "Why?");
        this.questionRepo.save(q);

        mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question/" + q.getId() + "/upvote"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.upVotes").value(1))
            .andExpect(jsonPath("$.downVotes").value(0));
    }

    @Test
    @WithUserDetails("TestUser")
    public void testUpVoteSelfAuthoredQuestion () throws Exception {
        Question q = new Question(this.testUser, this.amaFoo, "Why?");
        this.questionRepo.save(q);

        mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question/" + q.getId() + "/upvote"))
            .andExpect(status().isForbidden());
    }


    @Test
    @WithUserDetails("SecondaryUser")
    public void testDownVoteQuestion () throws Exception {
        Question q = new Question(this.testUser, this.amaFoo, "Why?");
        this.questionRepo.save(q);

        mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question/" + q.getId() + "/downvote"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.upVotes").value(0))
            .andExpect(jsonPath("$.downVotes").value(1));
    }

    @Test
    @WithUserDetails("TestUser")
    public void testDownVoteSelfAuthoredQuestion () throws Exception {
        Question q = new Question(this.testUser, this.amaFoo, "Why?");
        this.questionRepo.save(q);

        mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question/" + q.getId() + "/downvote"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("SecondaryUser")
    public void testDeleteVote () throws Exception {
        Question q = new Question(this.testUser, this.amaFoo, "Why?");
        this.questionRepo.save(q);

        mockMvc.perform(post("/ama/" + this.amaFoo.getId() + "/question/" + q.getId() + "/upvote"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.upVotes").value(1))
            .andExpect(jsonPath("$.downVotes").value(0));

        mockMvc.perform(delete("/ama/" + this.amaFoo.getId() + "/question/" + q.getId() + "/vote"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.upVotes").value(0))
            .andExpect(jsonPath("$.downVotes").value(0));

    }
}
