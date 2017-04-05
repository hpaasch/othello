import React from 'react';
import {shallow} from 'enzyme';
import Game from "./Game"

describe('Game', () => {
  it('should render initial state of board', () => {
    const div = document.createElement('div')
    const currentBoard = [[0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 3, 0, 0, 0, 0], [0, 0, 3, 1, 2, 0, 0, 0], [0, 0, 0, 2, 1, 3, 0, 0], [0, 0, 0, 0, 3, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0]]

    const game = shallow(<Game currentBoard={currentBoard}/>, div)
    const cells = game.find('div')
    const button = game.find('button')

    const cell24 = game.find('#cell24')

    expect(cells).toHaveLength(65)
    expect(button).toHaveLength(2)

    expect(cell24).toBe(3);
  })


});