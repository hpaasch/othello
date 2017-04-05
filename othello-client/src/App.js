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
      currentBoard: null,
      latestMove: null

    }
    this.register = this.register.bind(this);
    this.submitForm = this.submitForm.bind(this);
    this.postUser = this.postUser.bind(this);
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
      .then( (game) => {this.setState({mode: "Game"})});

  }

  postUser(user){
    let route = "/login";

    if (this.state.mode === "Register") {
      route = "/users"
    }
    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    let init = {method: 'POST', headers: headers, body: user };
    console.log("Stringify: ", JSON.stringify( user ))
    return fetch(route, init)
      .then( (response) => response.json() )
      .then( (game) => {this.setState({mode: "Game"})});
  }

  postMove(){
    //TODO
  }

  get display(){
    switch(this.state.mode.toLowerCase()){
      case "register":
        return (<RegisterForm onSubmit={this.submitForm}/>)

      case "game":
        return (<Game currentBoard={this.state.currentBoard} makeMove={this.postMove}/>)

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
