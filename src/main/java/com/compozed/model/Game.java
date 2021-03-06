package com.compozed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user = new User();

    @OneToOne(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Board currentBoard;

    @OneToMany(mappedBy = "boardHistory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BoardHistory> boardHistory;

    private int nextPlayer;
    private int gameWinner;
    private String comment;
    private String completionDateTime;

    public Game(){
        this.currentBoard = new Board();
        this.currentBoard.setParent( this );

        this.nextPlayer = GamePiece.Black;
        this.gameWinner = GamePiece.Empty;

        this.boardHistory = new ArrayList<>();
    }

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

    public List<BoardHistory> getBoardHistory() {
        return boardHistory;
    }

    public void setBoardHistory(List<BoardHistory> boardHistory) {
        this.boardHistory = boardHistory;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(Board currentBoard) {
        this.currentBoard = currentBoard;
        currentBoard.setParent( this );
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCompletionDateTime() {
        return completionDateTime;
    }

    public void setCompletionDateTime(String completionDateTime) {
        this.completionDateTime = completionDateTime;
    }

    public void placePiece(int color, int xPosition, int yPosition) throws Exception {
        getCurrentBoard().setState();

        Board copyOfBoard = currentBoard.clone();
        addBoard(copyOfBoard);

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
                findPossibleMoves();
            }
        }
        else {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");

            completionDateTime = dtf.print( new DateTime() );
            if( gameWinner == GamePiece.Black ) {
                getUser().setWinCount( getUser().getWinCount() + 1 );
            }
            if( gameWinner == GamePiece.White ) {
                getUser().setLossCount( getUser().getLossCount() + 1 );
            }
        }

        GameMove move = new GameMove( color, xPosition, yPosition );
        currentBoard.setLastPiecePlaced( move );
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

    public void undo() {
        this.nextPlayer = this.currentBoard.getLastPiecePlaced().getColor();
        this.currentBoard = new Board( this.getBoardHistory().get(this.getBoardHistory().size() - 1).getSerializedBoard() );
        System.out.println( this.currentBoard);
        findPossibleMoves();
        this.getBoardHistory().remove(this.getBoardHistory().size() - 1);
        this.currentBoard.setWhiteCount( this.currentBoard.getWhiteCount() );
        this.currentBoard.setBlackCount( this.currentBoard.getBlackCount() );;
    }

    public void redo( int xPosition, int yPosition ) throws Exception {
        this.placePiece( this.nextPlayer, xPosition, yPosition );
    }

    public void addBoard( Board board ) throws Exception {
        board.setSerializedBoard();
        BoardHistory boardHistory = new BoardHistory( board.getSerializedBoard( ) );
        boardHistory.setBoardHistory( this );
        this.boardHistory.add( boardHistory );
    }

}

