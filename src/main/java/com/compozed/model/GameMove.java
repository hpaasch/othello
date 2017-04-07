package com.compozed.model;

public class GameMove {
    private int color;
    private int xPosition;
    private int yPosition;

    public GameMove() {}

    public GameMove( int color, int xPosition, int yPosition ) {
        this.color = color;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }
}
