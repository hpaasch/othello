import React, { Component } from 'react';
import Game from './Game'

export default class GameHistory extends Component {

  // get gameHistory() {
  //
  // }

  render() {
    return(
      <div className="history">
        <h3>Game History</h3>
        <Game container={this.props.container} currentBoard={this.props.board}/>
      </div>
    )

  }

}