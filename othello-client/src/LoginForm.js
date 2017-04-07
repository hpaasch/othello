import React, { Component } from 'react';

export default class LoginForm extends Component {

  get loginForm() {

    return (
      <div className="loginFormContainer">
        <h1 className="formHeader" id="loginHeader">Login</h1>
        <form id="loginForm" onSubmit={this.props.onSubmit}>
          <input placeholder="Email" type="email" id="emailInput"/>
          <input placeholder="Password" type="password" id="passwordInput"/>
          <button id="loginButton" type="submit">Login</button>
        </form>
        <br/>
        <br/>
        <button id="registerButton" onClick={this.props.registerClick}>Register</button>
      </div>
    );
  }

  render() {
    return this.loginForm;
  }

}