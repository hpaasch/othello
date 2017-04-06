package com.compozed.model;

public class GameResponse {
    Game game;
    long userID;

    public GameResponse() {}

    public GameResponse( Game game ) {
        this.game = game;
        this.userID = game.getUser().getId();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
