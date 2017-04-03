package com.compozed.model;


public class Game {

    private Board currentBoard;
    private int nextPlayer;

    public Game(){
        this.currentBoard = new Board();
        this.nextPlayer = GamePiece.Black;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(Board currentBoard) {
        this.currentBoard = currentBoard;
    }

    public void placePiece(int color, int xPosition, int yPosition) {
        currentBoard.setPiece(color, xPosition, yPosition);
        flipIfNeeded(xPosition, yPosition, -1, 0);
        flipIfNeeded(xPosition, yPosition, 0, -1);
        flipIfNeeded(xPosition, yPosition, 1, 0);
        flipIfNeeded(xPosition, yPosition, 0, 1);
        flipIfNeeded(xPosition, yPosition, 1, 1);
        flipIfNeeded(xPosition, yPosition, -1, -1);
        flipIfNeeded(xPosition, yPosition, -1, 1);
        flipIfNeeded(xPosition, yPosition, 1, -1);

        nextPlayer = color == GamePiece.Black ? GamePiece.White : GamePiece.Black;
        findPossibleMoves( nextPlayer );
    }

    private boolean checkForFlip(int x, int y, boolean doFlip, int deltaX, int deltaY ){
        int currentPiece = nextPlayer;
        int oppositePiece = currentPiece == GamePiece.Black ? GamePiece.White : GamePiece.Black;
        int nextX = x + deltaX;
        int nextY = y + deltaY;

        while( nextX < 8 && nextY < 8  && nextX >= 0 && nextY >= 0
                && currentBoard.getPiece(nextX, nextY) == oppositePiece ) {
            if( doFlip ) {
                currentBoard.flipPiece(nextX, nextY);
            }

            nextX += deltaX;
            nextY += deltaY;
        }

        if( nextX == 8 || nextY == 8 || nextX < 0 || nextY < 0 ) {
            return false;
        }

        if( currentBoard.getPiece( nextX, nextY ) == currentPiece ) {
            // FIX THIS
//            if( deltaX < 0 && nextX + x  ) {
//
//            }
            return true;
        }

        return false;
    }

    private boolean flipIfNeeded(int xPosition, int yPosition, int deltaX, int deltaY ) {
        boolean needFlip = checkForFlip( xPosition, yPosition, false, deltaX, deltaY );
        if( needFlip ) {
            checkForFlip( xPosition, yPosition, true, deltaX, deltaY );
        }

        return needFlip;
    }

    private void findPossibleMoves( int nextPlayer ) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                System.out.print( "x = " + x + " y = " + y );
                if (x == 3 && y == 2){
                    System.out.println("coool");
                }
                if( currentBoard.getPiece( x, y ) != GamePiece.Black && currentBoard.getPiece( x, y ) != GamePiece.White ) {
                    boolean canMove = checkForFlip(x, y, false, -1, 0);
                    canMove = checkForFlip(x, y, false, 0, -1) || canMove;
                    canMove = checkForFlip(x, y, false, 1, 0) || canMove;
                    canMove = checkForFlip(x, y, false, 0, 1) || canMove;
                    canMove = checkForFlip(x, y, false, 1, 1) || canMove;
                    canMove = checkForFlip(x, y, false, -1, -1) || canMove;
                    canMove = checkForFlip(x, y, false, -1, 1) || canMove;
                    canMove = checkForFlip(x, y, false, 1, -1) || canMove;
                    if( canMove ) {
                        System.out.println( " can move" );
                        currentBoard.setPiece( GamePiece.Possible, x, y );
                    }
                    else {
                        System.out.println( " cannot move" );
                        currentBoard.setPiece( GamePiece.Empty, x, y );
                    }
                }
            }
        }
    }
}

