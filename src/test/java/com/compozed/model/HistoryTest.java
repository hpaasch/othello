package com.compozed.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by localadmin on 4/6/17.
 */
public class HistoryTest {
    @Test
    public void testSaveGameHistory() throws Exception {
        Game game = new Game();
        game.placePiece( GamePiece.Black, 2, 4 );

        assertEquals( "Game should have history after moving a piece", 1, game.getBoardHistory().size() );

        game.placePiece( GamePiece.White, 2, 5 );

        assertEquals( "first element in history should not know about first move", 0, game.getBoardHistory().get(0).getBoard().getState()[2][5] );
        assertEquals( "second element in history should know about first move", 3, game.getBoardHistory().get(1).getBoard().getState()[2][5] );

        game.placePiece( GamePiece.Black, 2, 6 );
        System.out.println("GAME"+ game.getBoardHistory().get(2).toString());
        assertEquals( "third element in history should know about second move", GamePiece.White, game.getBoardHistory().get(2).getBoard().getState()[2][5] );

    }
}
