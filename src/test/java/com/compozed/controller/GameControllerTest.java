package com.compozed.controller;

import com.compozed.model.Board;
import com.compozed.model.Game;
import com.compozed.model.GamePiece;
import com.compozed.model.User;
import com.compozed.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    @Rollback
    public void testGameCanPlayThreeMoves() throws Exception {
        User user = new User();
        user.setEmail("abc1234@junk.com");
        user.setPassword("password");

        MockHttpServletRequestBuilder request1 = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user));

        MvcResult result = this.mockMvc.perform(request1)
                .andExpect(status().isOk())
                .andReturn();

        JSONObject response = new JSONObject( result.getResponse().getContentAsString() );
        JSONObject game = response.getJSONObject( "game" );
        int gameID = game.getInt("id");

        MockHttpServletRequestBuilder login = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user));

        this.mockMvc.perform(login)
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletRequestBuilder request2 = post("/games/" + gameID )
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\": 2, \"xPosition\": 2, \"yPosition\": 4}");
        this.mockMvc.perform(request2)
                .andExpect(status().isOk());

        request2 = post("/games/" + gameID )
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\": 1, \"xPosition\": 2, \"yPosition\": 5}");
        this.mockMvc.perform(request2)
                .andExpect(status().isOk());

        request2 = post("/games/" + gameID )
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\": 2, \"xPosition\": 2, \"yPosition\": 6}");
        this.mockMvc.perform(request2)
                .andExpect(status().isOk());
//
//        MockHttpServletRequestBuilder request3 = post("/games/2")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"color\": 2, \"xPosition\": 2, \"yPosition\": 4}");
//        this.mockMvc.perform(request3)
//                .andExpect(status().isOk());
//
//        user.setId(1L);
//
//        MvcResult result = this.mockMvc.perform(get("/users/" + user.getId() + "/games" ))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[1].id", isA(Integer.class)))
//                .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        System.out.println("Result content: " + content);
//        JSONArray gamesArr = new JSONArray();
//        JSONObject game = (JSONObject)gamesArr.get( 0 );
//        System.out.println( game.toJSONString() );
//
//        this.mockMvc.perform(get("/games/" + gameID ))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[1].id", isA(Integer.class)));
//
    }


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
        assertEquals( "Should have a saved game history", 1, game.getBoardHistory().size());
    }

    @Test
    @Transactional
    @Rollback
    public void testGameUndo() throws Exception {
        User user = new User();
        user.setEmail("abc1234@junk.com");
        user.setPassword("password");

        MockHttpServletRequestBuilder request1 = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user));

        MvcResult result = this.mockMvc.perform(request1)
                .andExpect(status().isOk())
                .andReturn();

        JSONObject response = new JSONObject( result.getResponse().getContentAsString() );
        JSONObject game = response.getJSONObject( "game" );
        int gameID = game.getInt("id");

        MockHttpServletRequestBuilder request2 = post("/games/" + gameID )
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\": 2, \"xPosition\": 2, \"yPosition\": 4}");
        this.mockMvc.perform(request2)
                .andExpect(status().isOk());

        request2 = post("/games/" + gameID )
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\": 1, \"xPosition\": 2, \"yPosition\": 5}");
        this.mockMvc.perform(request2)
                .andExpect(status().isOk());

        MockHttpServletRequestBuilder request = delete("/games/" + gameID);

        MvcResult result2 = this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPlayer", equalTo(GamePiece.White)))
                .andReturn();
        JSONObject response2 = new JSONObject( result2.getResponse().getContentAsString() );
        System.out.println(response2.toString());
        JSONObject board = response2.getJSONObject( "currentBoard" );
        String serializedBoard = board.getString( "serializedBoard" );
        Board board2 = new Board( serializedBoard );


        assertEquals(GamePiece.Possible, board2.getPiece(2, 5) );

        assertEquals(4, board2.getBlackCount() );

        assertEquals(4, board2.getBlackCount() );

    }

    @Test
    @Transactional
    @Rollback
    public void testCanLeaveACommentOnGame() throws Exception {
        Game game = new Game();
        Board currentBoard = game.getCurrentBoard();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                currentBoard.setPiece(GamePiece.White, x, y);
            }
        }

        currentBoard.setPiece(GamePiece.Black, 0, 0);
        currentBoard.setPiece(GamePiece.Empty, 7, 7);

        game.getCurrentBoard().setSerializedBoard();

        game.placePiece(GamePiece.Black, 7, 7);

        gameRepository.save(game);

        System.out.println( "GAME ID: " + game.getId() );

        MockHttpServletRequestBuilder request = post("/games/" + game.getId() + "/comment")
                .content("This is a comment.");

        this.mockMvc.perform(request)
                .andExpect(status().isOk());
    }



//    @Test
//    @javax.transaction.Transactional
//    @Rollback
//    public void testUserCanLoadAGame() throws Exception {
//        User user = new User();
//        user.setEmail("abc1234@junk.com");
//        user.setPassword("password");
//
//        MockHttpServletRequestBuilder request1 = post("/users")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(user));
//
//        this.mockMvc.perform(request1)
//                .andExpect(status().isOk());
//
//        MockHttpServletRequestBuilder login = post("/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(user));
//
//        this.mockMvc.perform(login)
//                .andExpect(status().isOk());
//
//        MockHttpServletRequestBuilder request2 = get("/games/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"color\": 2, \"xPosition\": 2, \"yPosition\": 4}");
//        this.mockMvc.perform(request2)
//                .andExpect(status().isOk());
//
//        MockHttpServletRequestBuilder request3 = post("/games/2")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"color\": 2, \"xPosition\": 2, \"yPosition\": 4}");
//        this.mockMvc.perform(request3)
//                .andExpect(status().isOk());
//
//        user.setId(1L);
//
//        MvcResult result = this.mockMvc.perform(get("/users/" + user.getId() + "/games" ))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[1].id", isA(Integer.class)))
//                .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        System.out.println("Result content: " + content);
//        JSONArray gamesArr = new JSONArray();
//        JSONObject game = (JSONObject)gamesArr.get( 0 );
//        System.out.println( game.toJSONString() );

//        this.mockMvc.perform(get("/games/" + gameID ))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[1].id", isA(Integer.class)));
//
//    }

}
