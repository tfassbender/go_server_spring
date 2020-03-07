import React from 'react';

import GoStoneFieldComponent from './goStoneField.js';
import GoFieldInfoComponent from './goFieldInfo.js';

export default function GoFieldComponent({gameState, setGameState}) {
	const [playOnlyBlack, setPlayOnlyBlack] = React.useState(false);
	const [playOnlyWhite, setPlayOnlyWhite] = React.useState(false);
	
	var boardSize = 9;
	var boardSizePixels = 500;
	var bgImage = "images/boards/go_board_9.png";
	var border = 0;
	
	var gameOver = false;
	var nextMoveColor = "None (game not started yet)";
	
	if (gameState !== null) {
		boardSize = gameState.boardSize;
		bgImage = "images/boards/go_board_" + boardSize + ".png";
		switch (boardSize) {
			case 13:
				boardSizePixels = 650;
				break;
			case 19:
				boardSizePixels = 800;
				break;
			case 9:
			default:
				boardSizePixels = 500;
				break;
		}
		gameOver = gameState.over;
		nextMoveColor = gameState.playersTurn;
		if (nextMoveColor === null) {
			nextMoveColor = "None (game not started yet)";
		}
		if (gameOver) {
			nextMoveColor = "None (game over)";
		}
	}
	
	var tableStyle = {
			backgroundImage: 'url(' + bgImage + ')',
			backgroundSize: '100% 100%',
			border: border,
			width: boardSizePixels,
			height: boardSizePixels,
		};
	
	if (playOnlyBlack && playOnlyWhite) {
		setPlayOnlyBlack(false);
		setPlayOnlyWhite(false);
	}
	
	function createTable() {
	    let table = [];

	    // Outer loop to create parent
	    for (let i = 0; i < boardSize; i++) {
	        let children = [];
	        // Inner loop to create children
	        for (let j = 0; j < boardSize; j++) {
	        	let gameStateStone = null;
	        	let nextMoveColor = "BLACK";
	        	let gameId = -1;
	        	if (gameState !== null) {
	        		// console.log(i + " " + j);
	        		// console.log(gameState);
	        		// console.log(gameState.state);
	        		gameStateStone = gameState.state[i][j];
	        		nextMoveColor = gameState.playersTurn;
	        		gameId = gameState.id;
	        	}
	        	
	            children.push(<GoStoneFieldComponent row={i} col={j} gameStateStone={gameStateStone} nextMoveColor={nextMoveColor} 
	            		setGameState={setGameState} gameId={gameId} gameOver={gameOver} playOnlyBlack={playOnlyBlack} 
	            		playOnlyWhite={playOnlyWhite}>
	            	</GoStoneFieldComponent>);
	        }
	        // Create the parent and add the children
	        table.push(<tr key={i}>{children}</tr>);
	    }
	    
	    return table;
	}
	
	function pass() {
		if (gameState !== null) {
			if (!gameOver) {
				if (playOnlyBlack && nextMoveColor === "WHITE") {
					alert("White's turn (you selected to only play black)");
					return;
				}
				if (playOnlyWhite && nextMoveColor === "BLACK") {
					alert("Black's turn (you selected to only play white)");
					return;
				}
				let url = "react_test/games/" + gameState.id + "/move";
				let move = {
						row: -1,
						col: -1,
						type: "PASS",
						color: gameState.playersTurn
				};
				// send the move to the server
				let xhttp = new XMLHttpRequest();
				xhttp.onreadystatechange = function () {
					if (this.readyState === 4 && this.status === 201) {
						let gameState = JSON.parse(this.responseText);
						
						setGameState(gameState);
					}
					else if (this.readyState === 4 && this.status === 400) {
						alert("invalid move");
					}
					else if (this.readyState === 4) {
						alert("something went wrong while making the move: readyState = " + this.readyState + " status = " + this.status);
					}
				};
				xhttp.open("PUT", url);
				xhttp.setRequestHeader("Content-Type", "application/json");
				xhttp.send(JSON.stringify(move));
			}
			else {
				alert("The game is already over");
			}
		}
	}
	
	function updateGameState() {
		// request the updated game state
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function () {
			if (this.readyState === 4 && this.status === 200) {
				let gameState = JSON.parse(this.responseText);
				
				setGameState(gameState);
			}
			else if (this.readyState === 4 && this.status === 404) {
				alert("a game with this id was not found (id was: " + gameState.id + ")");
			}
			else if (this.readyState === 4) {
				alert("something went wrong while loading the game: readyState = " + this.readyState + " status = " + this.status);
			}
		};
		let urlUpdateGameState = "react_test/games/" + gameState.id;
		xhttp.open("GET", urlUpdateGameState);
		xhttp.send();
	}
	
	function createFieldFunctions() {
		return <div>
				<button onClick={pass} style={{marginLeft: "200px", fontSize: "16pt"}}>Pass</button>
				<button onClick={resign} style={{fontSize: "16pt"}}>Resign</button>
				<button onClick={updateGameState} style={{marginLeft: "30px", fontSize: "16pt"}}>Refresh</button>
				<br/>
				<label htmlFor="playColorOnly" style={{marginTop: "5px"}}>Play only: </label>
				<input type="checkbox" checked={playOnlyBlack} onChange={e => setPlayOnlyBlack(e.target.checked)} name="Black" />Black
				<input type="checkbox" checked={playOnlyWhite} onChange={e => setPlayOnlyWhite(e.target.checked)} name="White" />White
			</div>;
	}
	
	function resign() {
		if (gameState !== null) {
			if (!gameOver) {
				if (playOnlyBlack && nextMoveColor === "WHITE") {
					alert("White's turn (you selected to only play black)");
					return;
				}
				if (playOnlyWhite && nextMoveColor === "BLACK") {
					alert("Black's turn (you selected to only play white)");
					return;
				}
				alert("not yet implemented");
			}
			else {
				alert("The game is already over");
			}
		}
	}
	
	return <div>
		<table style = {tableStyle} border={border}>
			{createTable()}
		</table>
		{createFieldFunctions()}
		<br/>
		<br/>
		<GoFieldInfoComponent gameState={gameState} setGameState={setGameState}></GoFieldInfoComponent>
	</div>;
}