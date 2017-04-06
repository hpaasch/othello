package com.compozed.acceptance;

import com.compozed.model.Game;
import com.compozed.model.GamePiece;
import com.compozed.model.User;
import com.compozed.repository.GameRepository;
import com.compozed.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.fluentlenium.adapter.junit.FluentTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.fluentlenium.core.hook.wait.Wait;
import sun.jvm.hotspot.utilities.IntArray;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@Wait
public class LoginAcceptanceTest extends FluentTest {
    @Value("${local.server.port}")
    private String port;

    @Autowired
    UserRepository repository;

    @Autowired
    GameRepository gameRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void before() {

        repository.deleteAll();
        gameRepository.deleteAll();

    }


//    1.Allow user to register by visiting the home page and clicking a “Register” button, using email address and password, which stores the new user in the database and sends a welcome email to the user.
//          (Hint: SendGrid has a free API for sending email, if you register with them).
    @Test
    public void testLoginPage() {
        goTo("http://localhost:" + this.port + "/");

        assertEquals("Email input exists and is empty", "", $("#emailInput").text());
        assertEquals("Password input exists and is empty", "", $("#passwordInput").text());
        assertEquals("Login button exists", "Login", $("#loginButton").text());
        assertEquals("Register button exists", "Register", $("#registerButton").text());
    }

    @Test
    public void testRegisterButton() throws Exception{
        goTo("http://localhost:" + this.port + "/");

        $("#registerButton").click();
        String header = $("#registerHeader").text();
        assertEquals(
                "Given that I am on the home page, " +
                        "When I click on \"Register\" " +
                        "Then I see a new form allowing me to sign up using email and password",
                "Sign Up", header
        );

        assertEquals("Email input exists and is empty", "", $("#emailRegisterInput").text());
        assertEquals("Password input exists and is empty", "", $("#passwordRegisterInput").text());
        assertEquals("SignUp button exists", "Sign Up", $("#signUpButton").value());



        User user = new User();
        user.setEmail("us@youandme.com");
        user.setPassword("password");
        user.setGameList(new ArrayList<Game>());

        $("#emailRegisterInput").write("us@youandme.com");
        $("#passwordRegisterInput").write("password");

        $("#signUpButton").click();

        await().until(() -> $("#gameHeader").present());

        User newUser = repository.findOne(1L);




        assertEquals("Given that the register form is completed, " +
                        "when I submit the form " +
                        "then the new user is saved to the database",
                user.getEmail(), newUser.getEmail());
        assertEquals(user.getPassword(), newUser.getPassword());

    }

    @Test
    public void testLoginButton() throws Exception{
        goTo("http://localhost:" + this.port + "/");

        User user = new User();
        user.setEmail("us@youandme.com");
        user.setPassword("password");
        user.setGameList(new ArrayList<Game>());

        repository.save(user);

        $("#emailInput").write("us@youandme.com");
        $("#passwordInput").write("password");
        $("#loginButton").click();
        await().until(() -> $("#gameHeader").present());


        String header = $("#gameHeader").text();
        assertEquals(
                "Given that I am on the home page, " +
                        "When I click on \"Login\" " +
                        "Then I see a new othello game",
                "Welcome to Othello!", header
        );

    }




//    2.Allow existing user to log in or recover his/her password via email.
//    3.Once logged in, show an initial game board and allow black to make a move. Only legal moves should be allowed

    @Test
    public void testShouldShowInitialBoardAfterLoginAndMakeLegalFirstMove() throws Exception{
        //LOGIN FIRST
        goTo("http://localhost:" + this.port + "/");

        User user = new User();
        user.setEmail("us@youandme.com");
        user.setPassword("password");
        user.setGameList(new ArrayList<Game>());

        repository.save(user);

        $("#emailInput").write("us@youandme.com");
        $("#passwordInput").write("password");
        $("#loginButton").click();
        await().until(() -> $("#gameHeader").present());


        $("#cell24").click();

        User newUser = repository.findOne(1L);

        System.out.println(mapper.writeValueAsString(newUser));
        System.out.println(newUser.getGameList().get(0).getCurrentBoard().toString());
        String board = newUser.getGameList().get(0).getCurrentBoard().getSerializedBoard();

        Gson gson = new Gson();

        List<List<Double>> arrayBoard = new ArrayList<>();
        arrayBoard = gson.fromJson(board, arrayBoard.getClass());
        System.out.println("FromJson: " + arrayBoard);


        int cell24 = arrayBoard.get(3).get(2).intValue();
        int cell34 = arrayBoard.get(3).get(3).intValue();
        int cell25 = arrayBoard.get(2).get(2).intValue();

        assertEquals("Black played at cell 2,4", GamePiece.Black, cell24);
        assertEquals("Black captures cell 3,4", GamePiece.Black, cell34);
        assertEquals("Possible at cell 2,5", GamePiece.Possible, cell25);
//
//        System.out.println("3,2: " + cell);

//        Game game = gameRepository.findOne(1L);





    }



}
