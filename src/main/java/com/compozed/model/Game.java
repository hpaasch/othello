package com.compozed.model;

/**
 * Created by localadmin on 4/3/17.
 */
public class Game {

    private Board currentBoard;

    public Game(){
        currentBoard = new Board();
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
    }

    private boolean checkForFlip(int x, int y, boolean doFlip, int deltaX, int deltaY ){
        int currentPiece = currentBoard.getPiece( x, y );
        int oppositePiece = currentPiece == GamePiece.Black ? GamePiece.White : GamePiece.Black;
        int nextX = x + deltaX;
        int nextY = y + deltaY;

        while( currentBoard.getPiece(nextX, nextY) == oppositePiece
                && nextX < 8 && nextY < 8  && nextX >= 0 && nextY >= 0 ) {
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
            return true;
        }

        return false;
    }

    private void flipIfNeeded(int xPosition, int yPosition, int deltaX, int deltaY ) {
        boolean needFlip = checkForFlip( xPosition, yPosition, false, deltaX, deltaY );
        if( needFlip ) {
            checkForFlip( xPosition, yPosition, true, deltaX, deltaY );
        }
    }
}

