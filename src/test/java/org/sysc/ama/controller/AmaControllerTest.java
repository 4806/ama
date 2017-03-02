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

import org.sysc.ama.model.User;
import org.sysc.ama.model.UserRepository;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    private User testUser;

    @Before
    public void before () {
        this.testUser = new User("TestUser");
        userRepo.save(this.testUser);
    }

    @Test
    public void testCreateEndpointExists () throws Exception {
        mockMvc.perform(post("/ama/create?userId=" + this.testUser.getId() + "&title=Foo&public=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.public").value("true"))
                .andExpect(jsonPath("$.title").value("Foo"))
                .andExpect(jsonPath("$.id").isNumber());




    }

}
