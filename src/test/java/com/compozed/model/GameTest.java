package com.compozed.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameTest {




    @Test
    public void testInitialGameHasBoard() {
        Game game = new Game();
        Board currentBoard = game.getCurrentBoard();
        System.out.println(currentBoard.toString());

        assertNotNull(game.getCurrentBoard());
    }

    @Test
    public void testPlacePieceAndCaptures() {
        Game game = new Game();
        game.placePiece(GamePiece.Black, 2, 4);

        Board currentBoard = game.getCurrentBoard();
        int[][] board = currentBoard.getCurrentState();

        assertEquals("New board should have 4 black pieces", 4, currentBoard.getBlackCount());
        assertEquals("New board should have 1 white piece", 1, currentBoard.getWhiteCount());
        assertEquals("Cell 2,4 should contain a black piece", GamePiece.Black, board[2][4]);
        assertEquals("Cell 3,4 should flip to a black piece", GamePiece.Black, board[3][4]);

        game.placePiece(GamePiece.White, 2, 5);
        currentBoard = game.getCurrentBoard();

        assertEquals("New board should have 3 black pieces", 3, currentBoard.getBlackCount());
        assertEquals("New board should have 3 white pieces", 3, currentBoard.getWhiteCount());
        assertEquals("Cell 2,5 should contain a black piece", GamePiece.White, board[2][5]);
        assertEquals("Cell 3,4 should flip to a black piece", GamePiece.White, board[3][4]);
    }

    @Test
    public void testFindPossibleMoves() {
        Game game = new Game();
        Board currentBoard = game.getCurrentBoard();
        game.placePiece(GamePiece.Black, 2, 4);

        System.out.println(currentBoard.toString());
//        assertEquals("Cell 5,2 is a possible move for white", GamePiece.Possible, currentBoard.getPiece(5,2));
//        assertEquals("Cell 5,3 is a possible move for white", GamePiece.Possible, currentBoard.getPiece(5,3));
//        assertEquals("Cell 5,4 is a possible move for white", GamePiece.Possible, currentBoard.getPiece(5,4));
//
//        assertEquals("Cell 0,0 is not a possible move", GamePiece.Empty, currentBoard.getPiece(0,0));
//        assertEquals("Cell 7,7 is not a possible move", GamePiece.Empty, currentBoard.getPiece(7,7));
//        assertEquals("Cell 1,1 is not a possible move", GamePiece.Empty, currentBoard.getPiece(1,1));
//        assertEquals("Cell 6,6 is not a possible move", GamePiece.Empty, currentBoard.getPiece(6,6));
        assertEquals("Cell 3,2 is not a possible move", GamePiece.Empty, currentBoard.getPiece(3,2));



    }
}

