package com.compozed.controller;

import com.compozed.model.Game;
import com.compozed.model.GamePiece;
import com.compozed.model.User;
import com.compozed.repository.GameRepository;
import com.compozed.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

//    @Test
//    public void testShouldRegisterNewUser() throws Exception {
//        User user = new User();
//        user.setEmail("tjkomor@clemson.edu");
//        user.setPassword("password");
//
//
//        MockHttpServletRequestBuilder request = post("/users")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(user));
//
//
//        this.mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.nextPlayer", equalTo(2)))
//                .andExpect(jsonPath("$.winner", equalTo(0)))
//                .andExpect(jsonPath("$.id", isA(Integer.class)));
//    }

//    @Test
//    public void testUserCanLogin() throws Exception {
//        User user = new User();
//        user.setEmail("tjkomor@clemson.edu");
//        user.setPassword("password");
//
//
//        MockHttpServletRequestBuilder request = post("/users")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(user));
//
//        this.mockMvc.perform(request)
//                .andExpect(status().isOk());
//
//        MockHttpServletRequestBuilder login = post("/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(user));
//
//        this.mockMvc.perform(login)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.nextPlayer", equalTo(2)))
//                .andExpect(jsonPath("$.winner", equalTo(0)))
//                .andExpect(jsonPath("$.id", isA(Integer.class)));
//
//    }

    @Test
    public void testBadUserCannotLogin() throws Exception {
        User user = new User();
        user.setEmail("abc1234@junk.com");
        user.setPassword("password");


        MockHttpServletRequestBuilder login = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user));

        this.mockMvc.perform(login)
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @Rollback
    public void testUserCanAccessListOfAllGamesPlayed() throws Exception {
        User user = new User();
        user.setEmail("abc1234@junk.com");
        user.setPassword("password");

        MockHttpServletRequestBuilder request1 = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user));

        this.mockMvc.perform(request1)
                .andExpect(status().isOk());

        MockHttpServletRequestBuilder login = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user));

        this.mockMvc.perform(login)
                .andExpect(status().isOk());

        Game game = new Game();

        MockHttpServletRequestBuilder request2 = post("/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\": 2, \"xPosition\": 2, \"yPosition\": 4}");
        this.mockMvc.perform(request2)
                .andExpect(status().isOk());

        MockHttpServletRequestBuilder request3 = post("/games/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\": 2, \"xPosition\": 2, \"yPosition\": 4}");
        this.mockMvc.perform(request3)
                .andExpect(status().isOk());

        user.setId(1L);

        this.mockMvc.perform(get("/users/" + user.getId() + "/games" ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id", isA(Integer.class)));

    }

}