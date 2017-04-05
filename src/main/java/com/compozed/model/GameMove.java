package com.compozed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class GameMove {
    private int color;
    private int xPosition;
    private int yPosition;

    @Id
    @GeneratedValue( strategy =  GenerationType.AUTO )
    Long id;

    @JoinColumn(name = "board_id")
    @OneToOne
    @JsonIgnore
    private Board board;

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
