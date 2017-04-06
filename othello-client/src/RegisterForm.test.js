import React from 'react';
import {shallow} from 'enzyme';
import RegisterForm from "./RegisterForm"

describe('Register Form', () => {
  it('should render all components without crashing', () => {
    const div = document.createElement('div')
    const registerForm = shallow(<RegisterForm/>, div)
    const inputs = registerForm.find('input')
    const form = registerForm.find('form')
    const button = registerForm.find('button')

    expect(inputs).toHaveLength(3)
    expect(form).toHaveLength(1)
  })
});