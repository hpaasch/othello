import React, { Component } from 'react';
import Piece from './Piece';

export default class Game extends Component {

  renderPiece (cell) {
    {if(cell){
      return <Piece color={cell}/>
    }}
  }

  get game() {
    let boardDivArray = [];

    for (var y = 7; y >= 0; y--) {
      let boardDivRow = [];

      for (var x = 0; x <= 7; x++) {
        boardDivRow.push(<div className="cellDivs" id={`cell${x}${y}`} x={`${x}`} y={`${y}`} onClick={this.props.makeMove} >
          {this.renderPiece(this.props.currentBoard[7-y][x])}
        </div>)
      }
      boardDivArray.push(boardDivRow)
    }
    return (boardDivArray);
  }

  render() {

    if(this.props.container === "Game") {
      return (
        <div>
          <h1 id="gameHeader">Welcome to Othello!</h1>
          <h2 id="playerTurn">{this.props.nextPlayer}'s Turn</h2>
          <div className="gameContainer">
            {this.game}

          </div>

        </div>

      )
    }
    else {
      return(
        <div className="gameHistory">
          {this.game}
        </div>
      )
    }

  }

}