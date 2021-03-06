package com.compozed.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Game> gameList = new ArrayList<>();

    private String email;
    private String password;
    private int winCount;
    private int lossCount;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getLossCount() {
        return lossCount;
    }

    public void setLossCount(int lossCount) {
        this.lossCount = lossCount;
    }

    public void addGame(Game game){
        this.gameList.add(game);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder( "User: " );
        builder.append( "id: " );
        builder.append( id );
        builder.append( " email: " );
        builder.append( email );
        builder.append( " password: " );
        builder.append( password );
        builder.append( " game count: " );
        builder.append( gameList.size() );

        return builder.toString();
    }

}
