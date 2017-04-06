package com.compozed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class BoardHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "game_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Game boardHistory;

    private String serializedBoard;

    public BoardHistory() throws Exception {
        this.serializedBoard = new Board().getSerializedBoard();
    }

    public BoardHistory( String serializedBoard ) {
        this.serializedBoard = serializedBoard;
    }

    public String getSerializedBoard() {
        return serializedBoard;
    }

    public void setSerializedBoard(String serializedBoard) {
        this.serializedBoard = serializedBoard;
    }

    public Board getBoard(){
        Board board = new Board( serializedBoard );

        return board;
    }
}
