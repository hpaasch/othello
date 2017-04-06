import React, { Component } from 'react';

export default class Piece extends Component {

  get piece() {
      if(this.props.color === 1){
        return(<div className="whitePiece"></div>)
      }
      else if(this.props.color === 2){
        return(<div className="blackPiece"></div>)
      }
      else if(this.props.color === 3){
        return(<div className="possiblePiece"></div>)
      }
  }

  render() {
    return (
      this.piece
    )

  }


}