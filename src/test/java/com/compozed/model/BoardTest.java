package com.compozed.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

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

        int[][] currentBoard = board.getCurrentState();

        assertEquals("New board should have black piece at 4,3", GamePiece.White, currentBoard[4][3]);
        assertEquals("New board should have black piece at 3,4", GamePiece.White, currentBoard[3][4]);
        assertEquals("New board should have white piece at 4,4", GamePiece.Black, currentBoard[4][4]);
        assertEquals("New board should have white piece at 3,3", GamePiece.Black, currentBoard[3][3]);

        assertEquals("Black can move to[3][5]", GamePiece.BlackPossible, currentBoard[3][5]);
        assertEquals("Black can move to[4][2]", GamePiece.BlackPossible, currentBoard[4][2]);
        assertEquals("Black can move to[2][4]", GamePiece.BlackPossible, currentBoard[2][4]);
        assertEquals("Black can move to[5][3]", GamePiece.BlackPossible, currentBoard[5][3]);
    }

    @Test
    public void testSetPiece(){

        Board board = new Board();

        board.setPiece(GamePiece.Black, 2, 4);

        int[][] currentBoard= board.getCurrentState();

        assertEquals("Set piece should change cell to specified piece", GamePiece.Black, currentBoard[2][4]);
    }
}
