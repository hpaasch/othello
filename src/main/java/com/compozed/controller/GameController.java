package com.compozed.controller;

import com.compozed.model.Game;
import com.compozed.model.GameMove;
import com.compozed.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameRepository repository;

    ObjectMapper mapper = new ObjectMapper();

    public GameController(GameRepository repository) {
        this.repository = repository;
    }

    @PostMapping("{id}")
    public Game playMove(@PathVariable Long id, @RequestBody GameMove move) throws Exception{
        Game game = repository.findOne( id );
        game.placePiece( move.getColor(), move.getxPosition(), move.getyPosition() );

        System.out.println(mapper.writeValueAsString(game));

        return repository.save( game );
    }
}