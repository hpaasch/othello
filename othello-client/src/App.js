import React, { Component } from 'react';
import './App.css';
import LoginForm from './LoginForm';
import RegisterForm from './RegisterForm';
import Game from './Game'

class App extends Component {


  constructor(props){
    super(props);
    this.state = {
      mode: "Login",
      game: null,
      latestMove: null

    }
    this.register = this.register.bind(this);
    this.submitForm = this.submitForm.bind(this);
    // this.postUser = this.postUser.bind(this);
    this.postMove = this.postMove.bind(this);
  }

  register(){
    this.setState({mode:"Register"});
  }

  submitForm(event){
    console.log("We submitted")
    event.preventDefault();
    let eventData = event.target.children;
    console.log(eventData[0].value)
    const payload = {'email': eventData[0].value, 'password': eventData[1].value }
    // this.postUser(payload)

    let route = "/login";

    if (this.state.mode === "Register") {
      route = "/users"
    }
    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    let init = {method: 'POST', headers: headers, body: JSON.stringify(payload) };

    return fetch(route, init)
      .then( (response) => response.json() )
      .then( (game) => {
        console.log("GAME: ", game)
        this.setState({mode: "Game", game: game})
      });

  }

  // postUser(user){
  //   console.log("YOOOO IN POST ")
  //   let route = "/login";
  //
  //   if (this.state.mode === "Register") {
  //     route = "/users"
  //   }
  //   let headers = new Headers();
  //   headers.append("Content-Type", "application/json");
  //   let init = {method: 'POST', headers: headers, body: user };
  //   console.log("Stringify: ", JSON.stringify( user ))
  //   return fetch(route, init)
  //     .then( (response) => response.json() )
  //     .then( (game) => {
  //       console.log("GAME: ", game)
  //       this.setState({mode: "Game"})
  //     });
  // }

  postMove(event){
    //TODO
    const game = this.state.game;
    if(event.target.className === "possiblePiece"){

      // console.log("My Parent is: ",event.target.parentNode)
      console.log("Make move: ",event.target.parentNode.getAttribute('x')+ ", " + event.target.parentNode.getAttribute('y'))
      this.submitMove(game.nextPlayer, event.target.parentNode.getAttribute('x'), event.target.parentNode.getAttribute('y') )
    }
    else if(event.target.className === "cellDivs"){
      // console.log("My child is: ",event.target.children[0])
      if(event.target.children.length > 0 && event.target.children[0].className === "possiblePiece"){
        console.log("Make move: ",event.target.getAttribute('x')+ ", " + event.target.getAttribute('y'))
        this.submitMove(game.nextPlayer, event.target.parentNode.getAttribute('x'), event.target.parentNode.getAttribute('y') )
      }
    }

  }

  submitMove(color, xPos, yPos){

    const payload = {'color': color, 'xPosition': xPos, 'yPosition': yPos }
    let route = "/games/" + this.state.game.id;

    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    let init = {method: 'POST', headers: headers, body: JSON.stringify(payload) };

    return fetch(route, init)
      .then( (response) => response.json() )
      .then( (game) => {
        console.log("GAME: ", game)
        this.setState({mode: "Game", game: game})
      });
  }

  get display(){
    switch(this.state.mode.toLowerCase()){
      case "register":
        return (<RegisterForm onSubmit={this.submitForm}/>)

      case "game":
        console.log(this.state.game.currentBoard)
        return (<Game currentBoard={JSON.parse(this.state.game.currentBoard['serializedBoard'])} makeMove={this.postMove}/>)

      default:
        return (<LoginForm registerClick={this.register} onSubmit={this.submitForm}/>)

    }
  }

  render() {
    return (
      <div class="rootApp">
        {this.display}
      </div>
    );
  }
}

export default App;
