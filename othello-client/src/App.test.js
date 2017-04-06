import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import {shallow, mount} from 'enzyme';
import RegisterForm from './RegisterForm'
import fetchMock from 'fetch-mock';

describe('App', () => {

  it('renders without crashing', () => {
    const div = document.createElement('div');
    const app = shallow(<App/>, div);

    expect(app.state().mode).toBe("Login");

  });

  it('renders register component if register mode', () => {
    const div = document.createElement('div');
    const app = mount(<App/>, div);

    app.instance().register();
    const form = app.find(RegisterForm.name)
    expect(app.state().mode).toBe("Register");
    const header = form.find('#registerHeader')
    expect(header).toHaveLength(1)

  });

  it("should POST a new user to the database", () => {

    const div = document.createElement('div');
    const app = mount(<App />, div);


    const user = {email: 'us@youandme.com', password: "password", id: 1};

    app.instance().register();
    app.instance().postUser(user);

    fetchMock.post('/users', user);

    app.instance().postUser(user).then(function(response) {
      // console.log('Response', response);
      expect(response.status).toBe(200);
    })

    // Unmock.
    fetchMock.restore();
  });

  // it('renders Game component if Game mode', () => {
  //   const div = document.createElement('div');
  //   const app = shallow(<App/>, div);
  //
  //   app.node._self.register();
  //
  //   expect(app.state().mode).toBe("Register");
  //
  // })

});


