package com.compozed.controller;

import com.compozed.model.Game;
import com.compozed.model.User;
import com.compozed.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import com.sendgrid.*;

import java.io.IOException;

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
            System.out.println(response.statusCode);
            System.out.println(response.body);
            System.out.println(response.headers);
        } catch (IOException ex) {
            throw ex;
        }


        return game;
    }
}