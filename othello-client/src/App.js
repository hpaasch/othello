import React, { Component } from 'react';
import './App.css';
import LoginForm from './LoginForm';
import RegisterForm from './RegisterForm';

class App extends Component {


  constructor(props){
    super(props);
    this.state = {
      mode: "Login"
    }
    this.register = this.register.bind(this);
  }

  register(){
    this.setState({mode:"Register"});
  }

  submitForm(event){
    //event.preventDefault();
    let eventData = event.target.children;
    const payload = {'email': eventData[0], 'password': eventData[1] }
    postUser(payload)

  }

  postUser(user){
    let route = "/login";

    if (this.state.mode == "Register") {
      route = "/users"
    }
    let headers = new Headers();
    headers.append("Content-Type", "application/json");
    let init = {method: 'POST', headers: headers, body: JSON.stringify( user )};
    return fetch(route, init)
      .then( (response) => response.json() )
      .then( (game) => {this.setState({mode: "Game"})});
  }

  get display(){
    switch(this.state.mode.toLowerCase()){
      case "register":
        return (<RegisterForm/>)
        break;

      case "game":
        break;

      default:
        return (<LoginForm registerClick={this.register}/>)

    }
  }

  render() {
    return (
      this.display
    );
  }
}

export default App;
