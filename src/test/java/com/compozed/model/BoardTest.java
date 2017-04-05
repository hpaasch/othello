package com.compozed.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardTest {

    @Test
    public void testCreateNewBoard(){
        Board board = new Board();

        int blackPieces = board.getBlackCount();
        int whitePieces = board.getWhiteCount();
        assertEquals("New board should have 2 black pieces", 2, blackPieces);
        assertEquals("New board should have 2 white pieces", 2, whitePieces);

        int[][] currentBoard = board.getState();

        assertEquals("New board should have black piece at 4,3", GamePiece.White, currentBoard[4][3]);
        assertEquals("New board should have black piece at 3,4", GamePiece.White, currentBoard[3][4]);
        assertEquals("New board should have white piece at 4,4", GamePiece.Black, currentBoard[4][4]);
        assertEquals("New board should have white piece at 3,3", GamePiece.Black, currentBoard[3][3]);

        assertEquals("Black can move to[3][5]", GamePiece.Possible, currentBoard[3][5]);
        assertEquals("Black can move to[4][2]", GamePiece.Possible, currentBoard[4][2]);
        assertEquals("Black can move to[2][4]", GamePiece.Possible, currentBoard[2][4]);
        assertEquals("Black can move to[5][3]", GamePiece.Possible, currentBoard[5][3]);
    }

    @Test
    public void testSetPiece(){

        Board board = new Board();

        board.setPiece(GamePiece.Black, 2, 4);

        int[][] currentBoard= board.getState();

        assertEquals("Set piece should change cell to specified piece", GamePiece.Black, currentBoard[2][4]);
    }

    @Test
    public void testCheckForWinner(){

        Board board = new Board();
        assertEquals("it returns no winner", GamePiece.Empty, board.checkForWinner());

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board.setPiece(GamePiece.Black, x, y);
            }
        }

        assertEquals("it returns Black player is winner", GamePiece.Black, board.checkForWinner());

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board.setPiece(GamePiece.White, x, y);
            }
        }

        assertEquals("it returns White player is winner", GamePiece.White, board.checkForWinner());

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board.setPiece( ( x + y ) % 2 == 0 ? GamePiece.Black : GamePiece.White, x, y);
            }
        }

        assertEquals("it returns tie for same number of pieces", GamePiece.Tie, board.checkForWinner());
    }

    @Test
    public void testCloneMaintainsPieceIntegrity() {
        Board board = new Board();

        Board board2 = board.clone();

        assertNotEquals("Clone board has different mem location", board, board2);

        assertEquals("Board clone should have identical pieces", board.getPiece(3,3), board2.getPiece(3,3));
    }


}
