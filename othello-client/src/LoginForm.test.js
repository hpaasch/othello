import React from 'react';
import {shallow} from 'enzyme';
import LoginForm from "./LoginForm"

describe('Login Form', () => {
  it('should render all components without crashing', () => {
    const div = document.createElement('div')
    const loginForm = shallow(<LoginForm/>, div)
    const inputs = loginForm.find('input')
    const form = loginForm.find('form')
    const button = loginForm.find('button')

    expect(inputs).toHaveLength(2)
    expect(form).toHaveLength(1)
    expect(button).toHaveLength(2)
  })


});