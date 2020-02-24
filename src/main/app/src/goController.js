import React from 'react';

export default function GoControllerComponent() {
	
	const [id, setId] = React.useState(0);
	
	function createGame() {
		alert("Creating game: " + id);
	}
	
	function loadGame() {
		alert("Loading game: " + id);
	}
	
	function deleteGame() {
		alert("Deleting game: " + id);
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