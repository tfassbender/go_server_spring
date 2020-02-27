import React from 'react';
import GoStoneRandomPlacementComponent from './goStoneRandomPlacementComponent.js';
import GoStoneGamePlacementComponent from './goStoneGamePlacementComponent.js';

export default function GoStoneFieldComponent({row, col, setGameState, gameId, gameStateStone, nextMoveColor, gameOver}) {
	 // console.log("GoStoneFieldComponent created: row=" + row + " col=" +
		// col + " gameStateStone=" + gameStateStone);
	
	return <GoStoneGamePlacementComponent row={row} col={col} setGameState={setGameState} gameId={gameId} 
			gameStateStone={gameStateStone} nextMoveColor={nextMoveColor} gameOver={gameOver}>
		</GoStoneGamePlacementComponent>;
}