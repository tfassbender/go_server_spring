import React from 'react';

export default function GoFieldInfoComponent({gameState, setGameState}) {
	const [pointsBlack, setPointsBlack] = React.useState(0);
	const [pointsWhite, setPointsWhite] = React.useState(0);
	
	var blackStonesCaptured = 0;
	var whiteStonesCaptured = 0;
	var gameOver = false;
	var points = 0;
	var comi = 0;
	var currentGameId = "None (game not started yet)";
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
					<button onClick={submitPoints}>Submit</button><button onClick={updateGameState}>Get Result</button>
				</div>;
		}
		
		return null;
	}
	
	function submitPoints() {
		let urlSubmitResult = "react_test/games/" + gameState.id + "/result";
		
		// send the result
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
		
		// only works at the second time...
		// getResult();
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
	
	function createFieldSettings() {
		return <div>
				<label htmlFor="currentGameId" style={{fontSize: "14pt", fontWeight: "bold"}}>Current Game: {currentGameId}</label>
				<br/>
				<label htmlFor="nextMove" style={{fontSize: "14pt", fontWeight: "bold"}}>Next Move Color: {nextMoveColor}</label>
				<br/>
				<label htmlFor="comi">Comi: {comi}</label>
				<br/>
				<label htmlFor="blackStonesCaptured">Black Stones Captured: {blackStonesCaptured}</label>
				<br/>
				<label htmlFor="whiteStonesCaptured">White Stones Captured: {whiteStonesCaptured}</label>
			</div>;
	}
	
	return <div>
		{createFieldSettings()}
		<br/>
		<br/>
		{createGameResultControllers()}
		{createGameResult()}
		<br/>
		<br/>
		<br/>
	</div>;
}