import React, { Component } from 'react';
import Game from './Game'

export default class GameHistory extends Component {

  get historyGames() {
    return this.props.games.map( (game, i) => {
      return (<Game container={this.props.container} currentBoard={JSON.parse(game.currentBoard['serializedBoard'])}/>)
    })
  }


  render() {
    return(
      <div className="history">
        <button id="gameHistoryButton" type="button" class="btn btn-info" onClick={this.props.getHistory}>Game History</button>
        {this.historyGames}
      </div>
    )

  }

}