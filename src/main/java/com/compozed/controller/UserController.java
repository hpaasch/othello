package com.compozed.controller;

import com.compozed.model.Game;
import com.compozed.model.User;
import com.compozed.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repository;

    ObjectMapper mapper = new ObjectMapper();

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("")
    public Game register(@RequestBody User user) throws Exception{
        Game game = new Game();
        game.setUser(user);
        user.addGame(game);
        repository.save( user );
        return game;
    }
}