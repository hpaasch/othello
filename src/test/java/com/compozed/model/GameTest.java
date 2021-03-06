package com.compozed.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameTest {

    @Test
    public void testInitialGameHasBoard() {
        Game game = new Game();

        assertNotNull(game.getCurrentBoard());
    }

    @Test
    public void testPlacePieceAndCaptures() throws Exception {
        Game game = new Game();
        game.placePiece(GamePiece.Black, 2, 4);

        Board currentBoard = game.getCurrentBoard();
        int[][] board = currentBoard.getState();

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
    public void testFindPossibleMoves() throws Exception {
        Game game = new Game();
        Board currentBoard = game.getCurrentBoard();
        game.placePiece(GamePiece.Black, 2, 4);

        assertEquals("Cell 5,2 is a possible move for white", GamePiece.Possible, currentBoard.getPiece(2,5));
        assertEquals("Cell 5,3 is a possible move for white", GamePiece.Possible, currentBoard.getPiece(2,3));
        assertEquals("Cell 5,4 is a possible move for white", GamePiece.Possible, currentBoard.getPiece(4,5));

        assertEquals("Cell 0,0 is not a possible move", GamePiece.Empty, currentBoard.getPiece(0,0));
        assertEquals("Cell 7,7 is not a possible move", GamePiece.Empty, currentBoard.getPiece(7,7));
        assertEquals("Cell 1,1 is not a possible move", GamePiece.Empty, currentBoard.getPiece(1,1));
        assertEquals("Cell 6,6 is not a possible move", GamePiece.Empty, currentBoard.getPiece(6,6));
        assertEquals("Cell 3,2 is not a possible move", GamePiece.Empty, currentBoard.getPiece(3,2));

        game.placePiece(GamePiece.White, 2, 5);
        currentBoard = game.getCurrentBoard();

        assertEquals("New board should have 3 black pieces", 3, currentBoard.getBlackCount());
        assertEquals("New board should have 3 white pieces", 3, currentBoard.getWhiteCount());
        assertEquals("Cell 4,2 is a possible move for black", GamePiece.Possible, currentBoard.getPiece(4,2));
        assertEquals("Cell 5,3 is a possible move for black", GamePiece.Possible, currentBoard.getPiece(5,3));
        assertEquals("Cell 3,5 is a possible move for black", GamePiece.Possible, currentBoard.getPiece(3,5));
        assertEquals("Cell 2,6 is a possible move for black", GamePiece.Possible, currentBoard.getPiece(2,6));

        game.placePiece( GamePiece.Black, 2, 6 );
        assertEquals("Cell 2,6 is a possible move for black", GamePiece.Black, currentBoard.getPiece(2,6));

    }

    @Test
    public void testCheckForWinnerAfterEveryTurn() throws Exception {
        Game game = new Game();
        Board currentBoard = game.getCurrentBoard();


        assertEquals("No winner right after board is initialized", GamePiece.Empty, game.getWinner());

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                currentBoard.setPiece(GamePiece.White, x, y);
            }
        }

        currentBoard.setPiece(GamePiece.Black, 0, 0);
        currentBoard.setPiece(GamePiece.Empty, 7, 7);

        game.getCurrentBoard().setSerializedBoard();

        game.placePiece(GamePiece.Black, 7, 7);

        assertEquals("White should win", GamePiece.White, game.getWinner());

    }

    @Test
    public void testPlayerPassesIfNoPossibleMoves() throws Exception {
        Game game = new Game();
        Board currentBoard = game.getCurrentBoard();

        game.placePiece(GamePiece.Black, 2, 4);

        assertEquals("No winner right after board is initialized", GamePiece.Empty, game.getWinner());
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                currentBoard.setPiece(GamePiece.White, x, y);
            }
        }

        currentBoard.setPiece(GamePiece.Black, 7, 2);
        currentBoard.setPiece(GamePiece.Black, 6, 0);
        currentBoard.setPiece(GamePiece.Empty, 7, 0);
        currentBoard.setPiece(GamePiece.Empty, 7, 1);

        game.getCurrentBoard().setSerializedBoard();

        assertEquals("White should be next player", GamePiece.White, game.getNextPlayer());

        game.placePiece(GamePiece.White, 7, 1);

        assertEquals("White should be next player after black passes", GamePiece.White, game.getNextPlayer());

    }

    @Test
    public void testUndoLastMoveShouldReturnPreviousMove() throws Exception {
        Game game = new Game();
        game.placePiece( GamePiece.Black, 2, 4 );
        game.placePiece( GamePiece.White, 2, 5 );

        game.undo();

//        assertEquals( "current board should return possible move at 2,5", GamePiece.Possible, game.getCurrentBoard().getState()[2][5] );
//        assertEquals( "next player should be white", GamePiece.White, game.getNextPlayer() );

    }

    @Test
    public void testRedoShouldReturnPreviouslyUndidMove() throws Exception {
        Game game = new Game();
        game.placePiece( GamePiece.Black, 2, 4 );
        game.placePiece( GamePiece.White, 2, 5 );

        game.undo();
        game.redo(2,5);

//        assertEquals(game.getCurrentBoard().getLastPiecePlaced().getxPosition(), 2);
//        assertEquals(game.getCurrentBoard().getLastPiecePlaced().getyPosition(), 5);
//        assertEquals(game.getCurrentBoard().getLastPiecePlaced().getColor(), GamePiece.White);
//        assertEquals( "next player should be black", GamePiece.Black, game.getNextPlayer() );

    }

    @Test
    public void testBoardKnowsLastGameMove() throws Exception {
        Game game = new Game();
        assertEquals("initial board has no piece placed", GamePiece.Empty, game.getCurrentBoard().getLastPiecePlaced().getColor());

        game.placePiece(GamePiece.Black, 2, 4);
        game.placePiece( GamePiece.White, 2, 5 );

        assertEquals(game.getCurrentBoard().getLastPiecePlaced().getxPosition(), 2);
        assertEquals(game.getCurrentBoard().getLastPiecePlaced().getyPosition(), 5);
        assertEquals(game.getCurrentBoard().getLastPiecePlaced().getColor(), GamePiece.White);

    }

    @Test
    public void testGameCloneMaintainsParentIntegrity() throws Exception {
        Game game = new Game();
        game.placePiece(GamePiece.Black, 2, 4);

//        assertEquals(game.getHistory().get(0).getParent().getId(), game.getCurrentBoard().getParent().getId() );
    }

    @Test
    public void testGameWillNotPutPieceInCorner() throws Exception {
        Game game = new Game();
        Board board = new Board("[[0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 1, 0, 0, 0, 0, 0], [0, 0, 1, 1, 2, 3, 0, 0], [0, 2, 2, 2, 2, 2, 0, 0], [3, 3, 3, 3, 3, 3, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0]]");
        game.setCurrentBoard(board);
        System.out.println(board.toString());

        game.placePiece(GamePiece.White, 5, 2);
        System.out.println(game.getCurrentBoard().toString());
//        assertEquals(game.getHistory().get(0).getParent().getId(), game.getCurrentBoard().getParent().getId() );
    }

}

