package org.sysc.ama.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateEndpointExists () throws Exception {
        mockMvc.perform(post("/ama/create?userId=1&name=Foo&visibility=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visibility").value("0"))
                .andExpect(jsonPath("$.name").value("Foo"))
                .andExpect(jsonPath("$.userId").value("1"));

    }

}
