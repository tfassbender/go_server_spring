import React from 'react';

export default function GoControllerComponent() {
	const [id, setId] = React.useState(0);
	
	const urlGames = "react_test/games";// add "/{id}" for load and delete
	
	function setGameState(gameState) {
		// TODO
		alert("setting game state: " + gameState);
	}
	
	function createGame() {
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function () {
			if (this.readyState === 4 && this.status === 201) {
				let gameState = JSON.parse(this.responseText);
				let gameId = gameState.id;
				setId(gameId);
			}
			else if (this.readyState === 4 && this.status != 201) {
				alert("something went wrong while creating the game: readyState = " + this.readyState + " status = " + this.status);
			}
		};
		xhttp.open("POST", urlGames);
		xhttp.send();
	}
	
	function loadGame() {
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function () {
			if (this.readyState === 4 && this.status === 200) {
				let gameState = JSON.parse(this.responseText);
				setGameState(gameState);
			}
			else if (this.readyState === 4 && this.status != 200) {
				alert("something went wrong while loading the game: readyState = " + this.readyState + " status = " + this.status);
			}
		};
		let url = urlGames + "/" + id;
		xhttp.open("GET", url);
		xhttp.send();
	}
	
	function deleteGame() {
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function () {
			if (this.readyState == 4) {
				if (this.status != 204) {
					alert("something went wrong while deleting the game: readyState = " + this.readyState + " status = " + this.status);					
				}
				else {
					setId(0);
					setGameState(0);					
				}
			}
		};
		let url = urlGames + "/" + id;
		xhttp.open("DELETE", url);
		xhttp.send();
	}
	
	return <div>
		<table>
			<tr>
				<td><label htmlFor="id">Game ID:</label></td>
				<td><input type="number" value={id} onInput={e => setId(e.target.value)} /></td>
			</tr>
			<tr>
				<td><button onClick={createGame}>Create Game</button></td>
			</tr>
			<tr>
				<td><button onClick={loadGame}>Load Game</button></td>
			</tr>
			<tr>
				<td><button onClick={deleteGame}>Delete Game</button></td>
			</tr>
		</table>
	</div>;
}