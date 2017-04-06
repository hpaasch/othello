import React, { Component } from 'react';

export default class RegisterForm extends Component {

  get registerForm() {

    return (
      <div className="registerFormContainer">
        <h1 className="formHeader" id="registerHeader">Sign Up</h1>
        <form id="registerForm" onSubmit={this.props.onSubmit}>
          <input placeholder="Email" type="email" id="emailRegisterInput"/>
          <input placeholder="Password" type="password" id="passwordRegisterInput"/>
          <input id="signUpButton" type="submit" value="Sign Up"/>
        </form>
      </div>
    );
  }

  render() {
    return this.registerForm;
  }

}