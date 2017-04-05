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

    @Transient
    @JsonIgnore
    private int[][] state;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private GameMove lastPiecePlaced = null;

    private String serializedBoard;

    public GameMove getLastPiecePlaced() {
        return lastPiecePlaced;
    }

    public void setLastPiecePlaced(GameMove lastPiecePlaced) {
        this.lastPiecePlaced = lastPiecePlaced;
    }

    public String getSerializedBoard() throws Exception{
        return this.serializedBoard;
    }

    public void setSerializedBoard() {
        this.serializedBoard = this.toJson();
    }

    @JoinColumn(name = "game_id")
    @OneToOne
    @JsonIgnore
    private Game parent;

    public Game getParent() {
        return parent;
    }

    public void setParent(Game parent) {
        this.parent = parent;
    }

    @ManyToOne
    @JoinColumn(name = "history_id")
    @JsonIgnore
    private Game history = null;

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

        setSerializedBoard();
    }

    public int getWhiteCount(){
        int counter = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                counter += (this.state[x][y] == GamePiece.White) ? 1 : 0;
            }
        }
        return counter;
    }

    public int getBlackCount(){
        int counter = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                counter += (this.state[x][y] == GamePiece.Black) ? 1 : 0;
            }
        }
        return counter;
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
        int blackCount = this.getBlackCount();
        int whiteCount = this.getWhiteCount();
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

}
