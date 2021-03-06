import React, { Component } from 'react';
import './App.css';
import LoginForm from './LoginForm';
import RegisterForm from './RegisterForm';
import Game from './Game';
import GameHistory from './GameHistory';

class App extends Component {


  constructor(props){
    super(props);
    this.state = {
      mode: "Login",
      game: null,
      games: [],
      userID: 0,
      latestMove: null,
      undoRedoValue: 'Undo',
      nextPlayer: "Black"
    }
    this.register = this.register.bind(this);
    this.submitForm = this.submitForm.bind(this);
    this.postMove = this.postMove.bind(this);
    this.undoRedo = this.undoRedo.bind(this);
    this.getHistory = this.getHistory.bind(this);
  }

  register(){
    this.setState({mode:"Register"});
  }

  submitForm(event){
    event.preventDefault();
    let eventData = event.target.children;
    const payload = {'email': eventData[0].value, 'password': eventData[1].value }

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
        this.setState({mode: "Game", game: game.game, userID: game.userID})
      });

  }

  postMove(event){
    const game = this.state.game;
    if(event.target.className === "possiblePiece"){

      console.log("Piece Make move: ",event.target.parentNode.getAttribute('x')+ ", " + event.target.parentNode.getAttribute('y'))
      this.submitMove(game.nextPlayer, event.target.parentNode.getAttribute('x'), event.target.parentNode.getAttribute('y') )
    }
    else if(event.target.className === "cellDivs"){
      if(event.target.children.length > 0 && event.target.children[0].className === "possiblePiece"){
        console.log("Div Make move: ",event.target.getAttribute('x')+ ", " + event.target.getAttribute('y'))
        this.submitMove(game.nextPlayer, event.target.getAttribute('x'), event.target.getAttribute('y') )
      }
    }
  }

  getHistory( event ){
    let route = "/users/" + this.state.userID + "/games";

    let headers = new Headers();
    let init = { method: 'GET', headers: headers };

    return fetch(route, init)
      .then( (response) => response.json() )
      .then( (games) => {
        console.log( "Found " + games.length + " games" );
        this.setState({ games: games })

      });
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
        this.setState({mode: "Game", game: game, latestMove: payload, undoRedoValue: "Undo", nextPlayer: game.nextPlayer == 1 ? "White" : "Black"})
        this.displayWinner()
      });

  }

  undoRedo(){
    if(this.state.undoRedoValue === "Undo") {
      console.log("We clicked undo")
      let init = {method: 'DELETE'};
      let route = "/games/" + this.state.game.id;
      return fetch(route, init)
        .then((response) => response.json())
        .then((game) => {
          console.log("GAME: ", game)
          this.setState({mode: "Game", game: game, undoRedoValue: 'Redo', nextPlayer: game.nextPlayer == 1 ? "White" : "Black"})
        });
    }
    else if(this.state.undoRedoValue === "Redo"){
      console.log("LATEST MOVE: ", this.state.latestMove)

      this.submitMove(this.state.latestMove['color'], this.state.latestMove['xPosition'], this.state.latestMove['yPosition'])
      this.setState({undoRedoValue: 'Undo'})
    }
  }

  fetchComment(prompt){
    let headers = new Headers();
    let init = {method: 'POST', headers: headers, body: prompt };
    let route = "/games/" + this.state.game.id + "/comment";

    return fetch(route, init);

  }

  displayWinner(){
    if(this.state.game.winner === 1){
      var prompt = window.prompt("Congratulations, White player! You win!","(Optional) Leave a comment");
      this.fetchComment(prompt);
    }
    else if(this.state.game.winner === 2){
      var prompt = window.prompt("Congratulations, Black player! You win!","(Optional) Leave a comment");
      this.fetchComment(prompt);
    }
    else if(this.state.game.winner === 4){
      var prompt = window.prompt("You've tied!","(Optional) Leave a comment");
      this.fetchComment(prompt);
    }
    else {
      console.log("No winner yet");
    }

  }



  get display(){
    switch(this.state.mode.toLowerCase()){
      case "register":
        return (<RegisterForm onSubmit={this.submitForm}/>)

      case "game":
        console.log(this.state.game.currentBoard)
        return (
          <div className="gameDashboard">



            <GameHistory container="History" board={JSON.parse(this.state.game.currentBoard['serializedBoard'])} getHistory={this.getHistory} games={this.state.games}/>

            <Game container="Game" currentBoard={JSON.parse(this.state.game.currentBoard['serializedBoard'])} makeMove={this.postMove} undoRedo={this.undoRedo} undoRedoValue={this.state.undoRedoValue} nextPlayer={this.state.nextPlayer}/>

            <div className="playerScore">
              <div className="blackPieceScoreCounter"></div><span className="pieceCount">X {this.state.game.currentBoard.blackCount}</span><br/>
              <div className="whitePieceScoreCounter"></div><span className="pieceCount">X {this.state.game.currentBoard.whiteCount}</span><br/>
              <button id="undoRedoButton" onClick={this.undoRedo}>{this.state.undoRedoValue}</button>
            </div>
          </div>)

      default:
        return (<LoginForm registerClick={this.register} onSubmit={this.submitForm}/>)

    }
  }

  render() {
    return (
      <div className="App">
          {this.display}
      </div>
    );
  }
}

export default App;
