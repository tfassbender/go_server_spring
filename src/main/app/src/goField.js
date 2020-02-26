import React from 'react';

import GoStoneFieldComponent from './goStoneField.js';

export default function GoFieldComponent({gameState, setGameState}) {
	const fieldSize = 9;
	const fieldSizePixels = 700;
	const bgImage = "images/boards/go_board_9.png";
	const border = 0;
	const tableStyle = {
			backgroundImage: 'url(' + bgImage + ')',
			backgroundSize: '100% 100%',
			border: border,
			width: fieldSizePixels,
			height: fieldSizePixels
		};
	
	function createTable() {
	    let table = [];

	    // Outer loop to create parent
	    for (let i = 0; i < fieldSize; i++) {
	        let children = [];
	        // Inner loop to create children
	        for (let j = 0; j < fieldSize; j++) {
	        	let gameStateStone = null;
	        	let nextMoveColor = "BLACK";
	        	let gameId = -1;
	        	if (gameState !== null) {
	        		//console.log(i + " " + j);
	        		//console.log(gameState);
	        		//console.log(gameState.state);
	        		gameStateStone = gameState.state[i][j];
	        		nextMoveColor = gameState.playersTurn;
	        		gameId = gameState.id;
	        	}
	        	
	            children.push(<GoStoneFieldComponent row={i} col={j} gameStateStone={gameStateStone} nextMoveColor={nextMoveColor} setGameState={setGameState} gameId={gameId}>
	            	</GoStoneFieldComponent>);
	        }
	        // Create the parent and add the children
	        table.push(<tr key={i}>{children}</tr>);
	    }
	    
	    return table;
	}
	
	return <div>
		<table style = {tableStyle} border={border}>
			{createTable()}
		</table>
	</div>;
}