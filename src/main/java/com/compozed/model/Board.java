package com.compozed.model;

/**
 * Created by localadmin on 4/3/17.
 */
public class Board {

    private int[][] state;

    public Board() {
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

        this.state[2][4] = GamePiece.BlackPossible;
        this.state[5][3] = GamePiece.BlackPossible;
        this.state[4][2] = GamePiece.BlackPossible;
        this.state[3][5] = GamePiece.BlackPossible;
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

    public int[][] getCurrentState(){
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

}
