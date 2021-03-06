package com.compozed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Board {
    @Transient
    @JsonIgnore
    private ObjectMapper mapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Transient
    @JsonIgnore
    private int[][] state;

    private int lastMoveColor;
    private int lastMoveXPosition;
    private int lastMoveYPosition;

    private String serializedBoard;

    @JoinColumn(name = "game_id")
    @OneToOne
    @JsonIgnore
    private Game parent;

    private int blackCount;
    private int whiteCount;

    public Board(){
        this.state = new int[8][8];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                this.state[y][x] = GamePiece.Empty;
            }
        }

        this.state[3][3] = GamePiece.Black;
        this.state[4][4] = GamePiece.Black;
        this.state[4][3] = GamePiece.White;
        this.state[3][4] = GamePiece.White;

        this.state[2][4] = GamePiece.Possible;
        this.state[5][3] = GamePiece.Possible;
        this.state[4][2] = GamePiece.Possible;
        this.state[3][5] = GamePiece.Possible;

        lastMoveColor = GamePiece.Empty;
        lastMoveXPosition = -1;
        lastMoveYPosition = -1;

        setSerializedBoard();
    }

    public Board( String serializedBoard ) {
        this.state = new int[8][8];
        this.serializedBoard = serializedBoard;
        setState();
    }

    public GameMove getLastPiecePlaced() {
        return new GameMove( this.lastMoveColor, this.lastMoveXPosition, this.lastMoveYPosition );
    }

    public void setLastPiecePlaced(GameMove lastPiecePlaced) {
        this.lastMoveColor = lastPiecePlaced.getColor();
        this.lastMoveXPosition = lastPiecePlaced.getxPosition();
        this.lastMoveYPosition = lastPiecePlaced.getyPosition();
    }

    public String getSerializedBoard() throws Exception{
        return this.serializedBoard;
    }

    public void setSerializedBoard() {
        this.serializedBoard = this.toJson();
    }

    public void setState() {
        List<List<Integer>> stateArray = new ArrayList<>();
        try {
            stateArray = mapper.readValue(this.serializedBoard, stateArray.getClass());
            int y = 7;
            for( List<Integer> rows : stateArray ) {
                int x = 0;
                for( Integer val : rows ) {
                    state[x][y] = val;
                    x++;
                }

                y--;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Game getParent() {
        return parent;
    }

    public void setParent(Game parent) {
        this.parent = parent;
    }

    public int getWhiteCount(){
        System.out.println( "BOARD AT WHITE COUNT:\n" + this.toString() );
        int counter = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                counter += (this.state[x][y] == GamePiece.White) ? 1 : 0;
            }
        }

        this.whiteCount = counter;

        return counter;
    }

    public int getBlackCount(){
        int counter = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                counter += (this.state[x][y] == GamePiece.Black) ? 1 : 0;
            }
        }

        this.blackCount = counter;

        return counter;
    }

    public void setBlackCount(int blackCount) {
        this.blackCount = blackCount;
    }

    public void setWhiteCount(int whiteCount) {
        this.whiteCount = whiteCount;
    }

    public int[][] getState(){
        return this.state;
    }

    public void setPiece(int color, int xPosition, int yPosition){
        this.state[xPosition][yPosition] = color;
    }

    public int getPiece( int xPosition, int yPosition ) {
        return this.state[xPosition][yPosition];
    }

    public void flipPiece(int xPosition, int yPosition){
        this.state[xPosition][yPosition] = this.state[xPosition][yPosition] == GamePiece.Black ? GamePiece.White : GamePiece.Black;
    }

    public int checkForWinner() {
        blackCount = this.getBlackCount();
        whiteCount = this.getWhiteCount();
        if (blackCount + whiteCount == 64) {
            if (blackCount == whiteCount) {
                return GamePiece.Tie;
            } else {
                return blackCount > whiteCount ? GamePiece.Black : GamePiece.White;
            }
        }
        return GamePiece.Empty;
    }

    public Board clone() {
        Board board = new Board();

        this.setSerializedBoard();
        board.serializedBoard = this.serializedBoard;
        board.setState();
        board.parent = this.parent;

        return board;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                builder.append(state[x][y]);
                if(x< 7){
                    builder.append(", ");
                }
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public String toJson(){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int y = 7; y >= 0; y--) {
            if(y != 7){
                builder.append(", ");
            }
            builder.append("[");
            for (int x = 0; x < 8; x++) {
                builder.append(state[x][y]);
                if(x< 7){
                    builder.append(", ");
                }
            }
            builder.append("]");

        }
        builder.append("]");

        return builder.toString();
    }

    public int getLastMoveColor() {
        return lastMoveColor;
    }

    public void setLastMoveColor(int lastMoveColor) {
        this.lastMoveColor = lastMoveColor;
    }

    public int getLastMoveXPosition() {
        return lastMoveXPosition;
    }

    public void setLastMoveXPosition(int lastMoveXPosition) {
        this.lastMoveXPosition = lastMoveXPosition;
    }

    public int getLastMoveYPosition() {
        return lastMoveYPosition;
    }

    public void setLastMoveYPosition(int lastMoveYPosition) {
        this.lastMoveYPosition = lastMoveYPosition;
    }
}
