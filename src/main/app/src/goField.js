import React from 'react';

import GoStoneFieldComponent from './goStoneField.js';

export default function GoFieldComponent({gameState, setGameState}) {
	const [pointsBlack, setPointsBlack] = React.useState(0);
	const [pointsWhite, setPointsWhite] = React.useState(0);
	
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
	
	var blackStonesCaptured = 0;
	var whiteStonesCaptured = 0;
	var gameOver = false;
	var points = 0;
	var comi = 0;
	var currentGameId = "Game not started yet";
	var nextMoveColor = "None (game not started yet)";
	
	if (gameState !== null) {
		blackStonesCaptured = gameState.blackStonesCaptured;
		whiteStonesCaptured = gameState.whiteStonesCaptured;
		gameOver = gameState.over;
		points = gameState.points;
		comi = gameState.comi;
		currentGameId = gameState.id;
		nextMoveColor = gameState.playersTurn;
		if (nextMoveColor === null) {
			nextMoveColor = "None (game not started yet)";
		}
		if (gameOver) {
			nextMoveColor = "None (game over)";
		}
	}
	
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
	        		// console.log(i + " " + j);
	        		// console.log(gameState);
	        		// console.log(gameState.state);
	        		gameStateStone = gameState.state[i][j];
	        		nextMoveColor = gameState.playersTurn;
	        		gameId = gameState.id;
	        	}
	        	
	            children.push(<GoStoneFieldComponent row={i} col={j} gameStateStone={gameStateStone} nextMoveColor={nextMoveColor} 
	            		setGameState={setGameState} gameId={gameId} gameOver={gameOver}>
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
	
	function createGameResultControllers() {
		if (gameOver) {
			return <div id="gameResultController">
					<table>
						<tr>
							<td><label htmlFor="pointsBlack">Points for Black: </label></td>
							<td><input type="number" value={pointsBlack} onInput={e => setPointsBlack(e.target.value)}></input></td>
						</tr>
						<tr>
							<td><label htmlFor="pointsWhite">Points for White: </label></td>
							<td><input type="number" value={pointsWhite} onInput={e => setPointsWhite(e.target.value)}></input></td>
						</tr>
					</table>
					<button onClick={submitPoints}>Submit</button><button onClick={getResult}>Get Result</button>
				</div>;
		}
		
		return null;
	}
	
	function submitPoints() {
		let urlSubmitResult = "react_test/games/" + gameState.id + "/result";
		
		//send the result
		let result = {
				pointsBlack: pointsBlack,
				pointsWhite: pointsWhite,
		};
		// send the move to the server
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function () {
			if (this.readyState === 4 && this.status === 202) {
				let gameResult = JSON.parse(this.responseText);
				console.log(gameResult);
			}
			else if (this.readyState === 4) {
				alert("something went wrong while submitting the points: readyState = " + this.readyState + " status = " + this.status);
			}
		};
		xhttp.open("PUT", urlSubmitResult);
		xhttp.setRequestHeader("Content-Type", "application/json");
		xhttp.send(JSON.stringify(result));
		
		//only works at the second time...
		//getResult();
	}
	
	function getResult() {
		//request the updated game state
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
	
	function createGameResult() {
		if (gameOver) {
			// points are calculated or game ended draw because of no comi
			if (points !== 0 || (Math.abs(Math.floor(comi) - comi) < 1e-5)) {
				let result = "Draw";
				if (points > 0) {
					result = "Black Wins!";
				}
				else if (points < 0) {
					result = "White Wins!";
				}
				
				return <div id="">
						<label htmlFor="points">{points} </label><label htmlFor="winner"> {result}</label>
					</div>;
			}
		}
		
		return null;
	}
	
	function resign() {
		if (gameState !== null) {
			if (!gameOver) {
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
		<button onClick={pass}>Pass</button><button onClick={resign}>Resign</button><label htmlFor="message"></label>
		<br/>
		<br/>
		<label htmlFor="currentGameId">Current Game: {currentGameId}</label>
		<br/>
		<label htmlFor="nextMove">Next Move Color: {nextMoveColor}</label>
		<br/>
		<label htmlFor="comi">Comi: {comi}</label>
		<br/>
		<label htmlFor="blackStonesCaptured">Black Stones Captured: {blackStonesCaptured}</label>
		<br/>
		<label htmlFor="whiteStonesCaptured">White Stones Captured: {whiteStonesCaptured}</label>
		<br/>
		<br/>
		{createGameResultControllers()}
		{createGameResult()}
		<br/>
		<br/>
		<br/>
	</div>;
}