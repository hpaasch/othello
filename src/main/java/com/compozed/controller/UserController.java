package com.compozed.controller;

import com.compozed.exception.UserNotFoundException;
import com.compozed.model.Game;
import com.compozed.model.GameResponse;
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
    public GameResponse register(@RequestBody User user) throws Exception{
        Game game = new Game();
        game.setUser(user);
        user.addGame(game);
        repository.save( user );

        Email from = new Email("othello@allstate.com");
        String subject = "Welcome to Othello!";
        Email to = new Email(user.getEmail());
        Content content = new Content("text/plain", "Hello, Welcome");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }


        GameResponse response = new GameResponse( game );
        return response;
    }

    @PostMapping("/login")
    public GameResponse login(@RequestBody User user) throws Exception {
        Iterable<User> users = repository.findAll();
        for (User knownUser: users) {
            if( knownUser.getEmail().contentEquals(user.getEmail()) && knownUser.getPassword().contentEquals(user.getPassword()) ) {
                Game game = new Game();
                game.setUser(knownUser);
                gameRepository.save( game );

                knownUser.addGame(game);
                repository.save( knownUser );

                GameResponse response = new GameResponse( game );
                return response;
            }
        }

        throw new UserNotFoundException();
    }

    @GetMapping("/users/{id}/games")
    public List<Game> getGameList(@PathVariable int id) {
        List<Game> result = new ArrayList<>();
        Iterable<Game> games = gameRepository.findAll();
        for ( Game game: games ) {
            if( game.getUser().getId() == id ) {
                result.add( game );
            }
        }

        return result;
    }


    @PostMapping("/recover")
    public void recoverPassword(@RequestBody User user) throws Exception{
        String email = user.getEmail();
        Iterable<User> users = repository.findAll();
        for ( User u: users ) {
            if( u.getEmail().toLowerCase().contentEquals( user.getEmail().toLowerCase() )) {
                Email from = new Email("othello@allstate.com");
                String subject = "Password Recovery";
                Email to = new Email(user.getEmail());
                Content content = new Content("text/plain", "Your password is: " + u.getPassword() );
                Mail mail = new Mail(from, subject, to, content);

//                SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
//                Request request = new Request();
//                try {
//                    request.method = Method.POST;
//                    request.endpoint = "mail/send";
//                    request.body = mail.build();
//                    Response response = sg.api(request);
//                } catch (IOException ex) {
//                    throw ex;
//                }
            }
        }
    }

}