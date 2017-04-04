package com.compozed.controller;

import com.compozed.model.Board;
import com.compozed.model.Game;
import com.compozed.model.GamePiece;
import com.compozed.repository.GameRepository;
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
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    @Transactional
    @Rollback
    public void testGameMove() throws Exception {
        Game game = new Game();
        gameRepository.save(game);

        Board board = game.getCurrentBoard();
        int[][] state = board.getState();
        assertEquals( "Chip at 3, 4 should be white", GamePiece.White, state[3][4] );

        MockHttpServletRequestBuilder request = post("/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\": 2, \"xPosition\": 2, \"yPosition\": 4}");

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPlayer", equalTo(1)));

        Game savedGame = gameRepository.findOne(1L);
        savedGame.getCurrentBoard().setState();

        board = savedGame.getCurrentBoard();
        state = board.getState();
        assertEquals( "Chip at 3, 4 should now be black", GamePiece.Black, state[3][4] );
    }
}
