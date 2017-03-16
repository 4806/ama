package org.sysc.ama.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.annotation.PostConstruct;
import javax.swing.text.AbstractDocument.Content;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.sysc.ama.model.Ama;
import org.sysc.ama.model.Question;
import org.sysc.ama.model.User;
import org.sysc.ama.repo.AmaRepository;
import org.sysc.ama.repo.QuestionRepository;
import org.sysc.ama.repo.UserRepository;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class StaticControllerTest {

	@Autowired
	private UserRepository userRepo;

	private User testUser;

	@PostConstruct
	public void init() {
		this.testUser = new User("TestUser");
		this.userRepo.save(this.testUser);
	}


	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithUserDetails("TestUser")
	public void test() throws Exception {
		
		mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attribute("userId", this.testUser.getId()));
	}

}
