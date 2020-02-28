import React from 'react';

export default function GoControllerComponent({setGameState}) {
	const [id, setId] = React.useState(0);
	const [comi, setComi] = React.useState(5.5);
	
	const urlGames = "react_test/games";// add "/{id}" for load and delete
	
	function createGame() {
		let gameCreation = {
				comi: comi
		};
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function () {
			if (this.readyState === 4 && this.status === 201) {
				let gameState = JSON.parse(this.responseText);
				let gameId = gameState.id;
				setId(gameId);
				
				// load the new game to set the correct game state
				loadGame(gameId);
			}
			else if (this.readyState === 4 && this.status !== 201) {
				alert("something went wrong while creating the game: readyState = " + this.readyState + " status = " + this.status);
			}
		};
		xhttp.open("POST", urlGames);
		xhttp.setRequestHeader("Content-Type", "application/json");
		xhttp.send(JSON.stringify(gameCreation));
	}
	
	function loadCurrentGame() {
		loadGame(id);
	}
	function loadGame(gameId) {
		console.log("loading game: " + gameId);
		
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function () {
			if (this.readyState === 4 && this.status === 200) {
				let gameState = JSON.parse(this.responseText);
				
				// console.log(this.responseText);
				// console.log(gameState);
				
				setGameState(gameState);
			}
			else if (this.readyState === 4 && this.status === 404) {
				alert("a game with this id was not found (id was: " + gameId + ")");
			}
			else if (this.readyState === 4) {
				alert("something went wrong while loading the game: readyState = " + this.readyState + " status = " + this.status);
			}
		};
		let url = urlGames + "/" + gameId;
		xhttp.open("GET", url);
		xhttp.send();
	}
	
	function deleteGame() {
		let xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function () {
			if (this.readyState === 4) {
				if (this.status !== 204) {
					alert("something went wrong while deleting the game: readyState = " + this.readyState + " status = " + this.status);					
				}
				else {
					setId(0);
					setGameState(null);					
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
				<th colspan="2">Create a new Game</th>
			</tr>
			<tr>
				<td><label htmlFor="comi">Comi:</label></td>
				<td><input type="number" value={comi} onInput={e => setComi(e.target.value)} /></td>
			</tr>
			<tr>
				<td><button onClick={createGame}>Create Game</button></td>
			</tr>
			<tr style={{height: "10px"}}></tr>
			<tr>
				<th colspan="2">Create a new Game</th>
			</tr>
			<tr>
				<td><label htmlFor="id">Game ID:</label></td>
				<td><input type="number" value={id} onInput={e => setId(e.target.value)} /></td>
			</tr>
			<tr>
				<td><button onClick={loadCurrentGame}>Load Game</button></td>
			</tr>
			<tr>
				<td><button onClick={deleteGame}>Delete Game</button></td>
			</tr>
		</table>
	</div>;
}