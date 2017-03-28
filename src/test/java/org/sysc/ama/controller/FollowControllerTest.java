package org.sysc.ama.controller;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.sysc.ama.model.User;
import org.sysc.ama.repo.UserRepository;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @Test
    @WithMockUser
    public void testFollowUser () throws Exception {
        User testUser = new User("TestUser");
        User targetUser = new User("TargetUser");

        userRepo.save(testUser);
        userRepo.save(targetUser);

        mockMvc.perform(post("/user/" + testUser.getId() + "/follow/" + targetUser.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(testUser.getName()));

    }

    @Test
    @WithMockUser
    public void testFollowNonExistantUser () throws Exception {
        User testUser = new User("TestUser");
        userRepo.save(testUser);

        mockMvc.perform(post("/user/" + testUser.getId() + "/follow/1000" ))
            .andExpect(status().isNotFound());

    }


    @Test
    @WithMockUser
    public void testGetFollowingUsers () throws Exception {
        User testUser = new User("TestUser");
        User targetUser = new User("TargetUser");

        testUser.follow(targetUser);
        userRepo.save(targetUser);
        userRepo.save(testUser);

        mockMvc.perform(get("/user/" + testUser.getId() + "/following"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['" + targetUser.getId() + "']").value(targetUser.getName()));
    }


}
