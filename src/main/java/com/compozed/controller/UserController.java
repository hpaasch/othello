package com.compozed.controller;

import com.compozed.exception.UserNotFoundException;
import com.compozed.model.Game;
import com.compozed.model.User;
import com.compozed.repository.GameRepository;
import com.compozed.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import com.sendgrid.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("")
public class UserController {
    private final UserRepository repository;
    private final GameRepository gameRepository;

    ObjectMapper mapper = new ObjectMapper();

    public UserController(UserRepository repository, GameRepository gameRepository) {
        this.repository = repository;
        this.gameRepository = gameRepository;
    }

    @PostMapping("/users")
    public Game register(@RequestBody User user) throws Exception{
        Game game = new Game();
        game.setUser(user);
        user.addGame(game);
        repository.save( user );

        Email from = new Email("othello@allstate.com");
        String subject = "Welcome to Othello!";
        Email to = new Email(user.getEmail());
        Content content = new Content("text/plain", "Hello, Welcome");
        Mail mail = new Mail(from, subject, to, content);

//        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
//        Request request = new Request();
//        try {
//            request.method = Method.POST;
//            request.endpoint = "mail/send";
//            request.body = mail.build();
//            Response response = sg.api(request);
//        } catch (IOException ex) {
//            throw ex;
//        }
//

        return game;
    }

    @PostMapping("/login")
    public Game login(@RequestBody User user) throws Exception {
        Iterable<User> users = repository.findAll();
        for (User knownUser: users) {
            if( knownUser.getEmail().contentEquals(user.getEmail()) && knownUser.getPassword().contentEquals(user.getPassword()) ) {
                Game game = new Game();
                game.setUser(knownUser);
                gameRepository.save( game );

                knownUser.addGame(game);
                repository.save( knownUser );

                return game;
            }
        }

        throw new UserNotFoundException();
    }

    @GetMapping("/users/{id}/games")
    public List<Game> getGameList(@PathVariable int id) {
        List<Game> result = new ArrayList<>();
        System.out.println( "getGameList: entry" );
        Iterable<Game> games = gameRepository.findAll();
        for ( Game game: games ) {
            System.out.println( "Found game: " + game.getId() );
            if( game.getUser().getId() == id ) {
                result.add( game );
            }
        }
        System.out.println( "Game count: " + result.size());

        return result;
    }



}