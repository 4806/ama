package org.sysc.ama.controller;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.sysc.ama.model.UserRepository;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by cameronblanchard on 3/2/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @Test
    public void testCreateEndpointExists () throws Exception {
        mockMvc.perform(post("/user/create").param("name", "TestUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestUser"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    public void testUserNameMustNotBeEmpty () throws Exception {
        mockMvc.perform(post("/user/create").param("name", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUserNameMustBeAlphanumeric () throws Exception {
        mockMvc.perform(post("/user/create").param("name", "asd?asdasd"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUserNameMayContainUnderscores () throws Exception {
        mockMvc.perform(post("/user/create").param("name", "test_user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test_user"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    public void testUserNameMayNotStartWithNumber () throws Exception {
        mockMvc.perform(post("/user/create").param("name", "1user"))
                .andExpect(status().isBadRequest());
    }
}
