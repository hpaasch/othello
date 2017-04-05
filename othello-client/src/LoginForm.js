import React, { Component } from 'react';

export default class LoginForm extends Component {

  get loginForm() {

    return (
      <div className="loginFormContainer">
        <h1 className="formHeader" id="loginHeader">Login</h1>
        <form id="loginForm">
          <input placeholder="Email" type="email" id="emailInput"/>
          <input placeholder="Password" type="password" id="passwordInput"/>
          <button id="loginButton" type="submit">Login</button>
          <button id="registerButton" onClick={this.props.registerClick}>Register</button>
        </form>
      </div>
    );
  }

  render() {
    return this.loginForm;
  }

}