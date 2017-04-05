import React, { Component } from 'react';

export default class Game extends Component {

  get game() {
    let boardDivArray = [];

    for (var y = 8; y > 0; y--) {
      let boardDivRow = [];

      for (var x = 0; x < 8; x++) {
        boardDivRow.push(<div className="cellDivs" id={`cell${y}${x}`} x={`${x}`} y={`${y}`} onClick={this.props.makeMove} >{this.props.currentBoard[y][x]}</div>)
        }
      boardDivArray.push(boardDivRow)
      }
    return (boardDivArray);
    }

  render() {
  return(
      <div className="gameContainer">
        <h1 className="formHeader" id="gameHeader">Welcome to Othello!</h1>
        {this.game}
        <button>Undo</button>
        <button>Redo</button>
      </div>
    )

  }

}