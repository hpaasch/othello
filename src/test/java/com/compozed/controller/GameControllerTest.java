package com.compozed.controller;

import com.compozed.model.Board;
import com.compozed.model.Game;
import com.compozed.model.GamePiece;
import com.compozed.repository.GameRepository;
import org.hibernate.annotations.SourceType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        MockHttpServletRequestBuilder request = post("/games/" + game.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\": 2, \"xPosition\": 2, \"yPosition\": 4}");

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPlayer", equalTo(1)));

        Game savedGame = gameRepository.findOne( game.getId() );

        savedGame.getCurrentBoard().setState();

        board = savedGame.getCurrentBoard();
        state = board.getState();
        assertEquals( "Chip at 3, 4 should now be black", GamePiece.Black, state[3][4] );
        assertEquals( "Should have a saved game history", 1, game.getHistory().size());
    }

    @Test
    @Transactional
    @Rollback
    public void testGameUndo() throws Exception {
        Game game = new Game();

        game.placePiece( GamePiece.Black, 2, 4 );

        game.placePiece( GamePiece.White, 2, 5 );
        gameRepository.save(game);

        MockHttpServletRequestBuilder request = delete("/games/" + game.getId());

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPlayer", equalTo(GamePiece.White)));

        Game savedGame = gameRepository.findOne(game.getId());

        assertEquals(GamePiece.Possible, savedGame.getCurrentBoard().getState()[2][5] );
    }
}
