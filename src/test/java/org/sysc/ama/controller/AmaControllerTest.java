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

import org.springframework.http.MediaType;
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
import java.util.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AmaControllerTest {

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
    private Ama amaBar;
    private Ama amaBaz;
    private Ama privateAma;

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

        Set<User> allowedUsers = new HashSet<User>();
        allowedUsers.add(this.testUser);
        allowedUsers.add(this.secondaryUser);
        this.privateAma = new Ama("Private", this.testUser, false, allowedUsers);

        amaRepo.save(this.privateAma);
        amaRepo.save(this.amaFoo);
        amaRepo.save(this.amaBar);
        amaRepo.save(this.amaBaz);

        this.fooQuestion = new Question(this.secondaryUser, this.amaFoo, "Don't avoid the question");
        questionRepo.save(this.fooQuestion);
    }

    @After
    public void after() {
        this.questionRepo.deleteAll();
        this.amaRepo.deleteAll();
        this.userRepo.deleteAll();
    }

    @Test
    @WithUserDetails("TestUser")
    public void testCreateEndpointExists () throws Exception {
        mockMvc.perform(post("/ama?title=Foo2&public=true")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.public").value("true"))
                .andExpect(jsonPath("$.title").value("Foo2"))
                .andExpect(jsonPath(  "$.id").isNumber());
    }

    @Test
    @WithUserDetails("TestUser")
    public void testUserCannotCreateTwoAmasWithSameTitle () throws Exception {
        mockMvc.perform(post("/ama?title=Foo2&public=true"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/ama?title=Foo2&public=true"))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithUserDetails("TestUser")
    public void testCreateEndpointAcceptsAllowedUsers () throws Exception {
        mockMvc.perform(post("/ama?title=Foo2&public=false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"SecondaryUser\"]"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("BadUser")
    public void testUninvitedUserCannotSeePrivateAmaInList () throws Exception {
        mockMvc.perform(get("/ama/list?start=0&count=2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[?(@.id == " + this.privateAma.getId() + ")]").doesNotExist());
    }

    @Test
    @WithUserDetails("TestUser")
    public void testOwnerCanSeePrivateAmaInList () throws Exception {
        mockMvc.perform(get("/ama/list?start=0&count=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[?(@.id == " + this.privateAma.getId() + ")]").exists());
    }

    @Test
    @WithUserDetails("SecondaryUser")
    public void testInvitedUserCanSeePrivateAmaInList () throws Exception {
        mockMvc.perform(get("/ama/list?start=0&count=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[?(@.id == " + this.privateAma.getId() + ")]").exists());
    }

    @Test
    @WithUserDetails("BadUser")
    public void testUninvitedUserCannotViewPrivateAmaDetails() throws Exception {
        mockMvc.perform(get("/ama/" + this.privateAma.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("SecondaryUser")
    public void testInvitedUserCanViewAmaDetails() throws Exception {
        mockMvc.perform(get("/ama/" + this.privateAma.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("TestUser")
    public void testAmaSubjectCanViewAmaDetails() throws Exception {
        mockMvc.perform(get("/ama/" + this.privateAma.getId()))
                .andExpect(status().isOk());
    }


    @Test
    @WithUserDetails("TestUser")
    public void testListAma () throws Exception {
        mockMvc.perform(get("/ama/list?start=0&count=2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[?(@.title==\"Baz\")]").exists())
            .andExpect(jsonPath("$.data[?(@.title==\"Private\")]").exists())
            .andExpect(jsonPath("$.total_count").value(4))
            .andExpect(jsonPath("$.pos").value(0));
    }

    @Test
    @WithUserDetails("TestUser")
    public void testListAmaPaging () throws Exception {
        mockMvc.perform(get("/ama/list?start=1&count=2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].title").value("Bar"));
    }

    @Test
    @WithUserDetails("TestUser")
    public void testListAmaOrdering () throws Exception {
        mockMvc.perform(get("/ama/list?start=0&count=2&asc=true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].title").value("Foo"))
            .andExpect(jsonPath("$.data[1].title").value("Bar"));
    }

    @Test
    @WithUserDetails("TestUser")
    public void testListAmaSorting () throws Exception {
        mockMvc.perform(get("/ama/list?start=0&count=2&sort=title&asc=true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].title").value("Bar"))
            .andExpect(jsonPath("$.data[1].title").value("Baz"));
    }


    @Test
    @WithUserDetails("TestUser")
    public void testDeleteExistingAma () throws Exception {
        mockMvc.perform(delete("/ama/" + this.amaFoo.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Foo"));
    }

    @Test
    @WithUserDetails("BadUser")
    public void testDeleteAmaUnauthorized () throws Exception {
        mockMvc.perform(delete("/ama/" + this.amaFoo.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("TestUser")
    public void testDeleteAmaDoesNotExist () throws Exception {
        mockMvc.perform(delete("/ama/100"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("TestUser")
    public void testViewAma () throws Exception {

        mockMvc.perform(get("/ama/" + this.amaFoo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(this.amaFoo.getId()));
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
