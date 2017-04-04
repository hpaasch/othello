package com.compozed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user = new User();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Board currentBoard;

    private int nextPlayer;
    private int gameWinner;

    public Game(){
        this.currentBoard = new Board();
        this.currentBoard.setParent( this );

        this.nextPlayer = GamePiece.Black;
        this.gameWinner = GamePiece.Empty;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(Board currentBoard) {
        this.currentBoard = currentBoard;
        currentBoard.setParent( this );
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
        gameWinner = currentBoard.checkForWinner();
        if (gameWinner == GamePiece.Empty){
            if (!findPossibleMoves()) {
                nextPlayer = nextPlayer == GamePiece.Black ? GamePiece.White : GamePiece.Black;
            }
        }
        currentBoard.setSerializedBoard();
    }

    private boolean checkForFlip(int x, int y, boolean doFlip, int deltaX, int deltaY ){
        int currentPiece = nextPlayer;
        int oppositePiece = currentPiece == GamePiece.Black ? GamePiece.White : GamePiece.Black;
        int nextX = x + deltaX;
        int nextY = y + deltaY;
        int flippedChips = 0;

        while( nextX < 8 && nextY < 8  && nextX >= 0 && nextY >= 0
                && currentBoard.getPiece(nextX, nextY) == oppositePiece ) {
            flippedChips++;
            if( doFlip ) {
                currentBoard.flipPiece(nextX, nextY);
            }

            nextX += deltaX;
            nextY += deltaY;
        }

        if( nextX == 8 || nextY == 8 || nextX < 0 || nextY < 0 ) {
            return false;
        }

        if( currentBoard.getPiece( nextX, nextY ) == currentPiece && flippedChips > 0 ) {
            return true;
        }

        return false;
    }

    public int getWinner(){
        return gameWinner;
    }

    private boolean flipIfNeeded(int xPosition, int yPosition, int deltaX, int deltaY ) {
        boolean needFlip = checkForFlip( xPosition, yPosition, false, deltaX, deltaY );
        if( needFlip ) {
            checkForFlip( xPosition, yPosition, true, deltaX, deltaY );
        }

        return needFlip;
    }

    private boolean findPossibleMoves() {
        boolean foundPossibleMove = false;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {

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
                        currentBoard.setPiece( GamePiece.Possible, x, y );
                        foundPossibleMove = true;
                    }
                    else {
                        currentBoard.setPiece( GamePiece.Empty, x, y );
                    }
                }
            }
        }
        return foundPossibleMove;
    }

    public int getNextPlayer() {
        return nextPlayer;
    }
}

