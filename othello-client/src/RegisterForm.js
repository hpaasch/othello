import React, { Component } from 'react';

export default class RegisterForm extends Component {

  get registerForm() {

    return (
      <div className="registerFormContainer">
        <h1 className="formHeader" id="registerHeader">Sign Up</h1>
        <form id="registerForm">
          <input placeholder="Email" type="email" id="emailRegisterInput"/>
          <input placeholder="Password" type="password" id="passwordRegisterInput"/>
          <button id="signUpButton" type="submit">Sign Up</button>
        </form>
      </div>
    );
  }

  render() {
    return this.registerForm;
  }

}