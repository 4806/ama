package org.sysc.ama.controller;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.annotation.DirtiesContext;

import org.sysc.ama.model.User;
import org.sysc.ama.model.Ama;
import org.sysc.ama.model.UserRepository;
import org.sysc.ama.model.AmaRepository;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AmaRepository amaRepo;

    private User testUser;

    @Before
    public void before () {
        this.testUser = new User("TestUser");

        userRepo.save(this.testUser);

        amaRepo.save(new Ama("Foo", this.testUser, true));
        delay(2);
        amaRepo.save(new Ama("Bar", this.testUser, true));
        delay(2);
        amaRepo.save(new Ama("Baz", this.testUser, true));
    }

    @Test
    public void testCreateEndpointExists () throws Exception {
        mockMvc.perform(post("/ama/create?userId=" + this.testUser.getId() + "&title=Foo&public=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.public").value("true"))
                .andExpect(jsonPath("$.title").value("Foo"))
                .andExpect(jsonPath("$.id").isNumber());

    }

    @Test
    public void testListAma () throws Exception {
        mockMvc.perform(get("/ama/list?page=0&limit=2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value("Baz"))
            .andExpect(jsonPath("$[1].title").value("Bar"));
    }

    @Test
    public void testListAmaPaging () throws Exception {
        mockMvc.perform(get("/ama/list?page=1&limit=2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].title").value("Foo"));
    }

    @Test
    public void testListAmaOrdering () throws Exception {
        mockMvc.perform(get("/ama/list?page=0&limit=2&asc=true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value("Foo"))
            .andExpect(jsonPath("$[1].title").value("Bar"));
    }

    @Test
    public void testListAmaSorting () throws Exception {
        mockMvc.perform(get("/ama/list?page=0&limit=2&sort=title&asc=true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value("Bar"))
            .andExpect(jsonPath("$[1].title").value("Baz"));
    }

    public void delay (int time) {
        try {
            Thread.sleep(time);
        } catch(InterruptedException ex) {}
    }
}
