package org.sysc.ama.controller;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.sysc.ama.model.User;
import org.sysc.ama.repo.UserRepository;

import javax.transaction.Transactional;
import javax.annotation.PostConstruct;

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

	private User testUser;

    @PostConstruct
    public void init() {
        this.testUser = new User("TestUser");
        this.userRepo.save(this.testUser);
    }

    @Test
    @WithUserDetails("TestUser")
  	public void testFollowUser () throws Exception {
        User targetUser = new User("TargetUser");
        userRepo.save(targetUser);

        mockMvc.perform(post("/user/follow/" + targetUser.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(testUser.getName()));

    }

    @Test
   	@WithUserDetails("TestUser")
    public void testFollowNonExistantUser () throws Exception {
        mockMvc.perform(post("/user/follow/1000" ))
            .andExpect(status().isNotFound());

    }

    @Test
    @WithUserDetails("TestUser")
    public void testGetFollowingUsers () throws Exception {
        User targetUser = new User("TargetUser");

        userRepo.save(targetUser);
        this.testUser.follow(targetUser);
        userRepo.save(testUser);

        mockMvc.perform(get("/user/following"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['" + targetUser.getId() + "']").value(targetUser.getName()));
    }


}
