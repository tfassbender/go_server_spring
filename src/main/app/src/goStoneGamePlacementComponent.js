import React from 'react';

export default function GoStoneRandomPlacementFieldComponent({row, col, setGameState, gameId, gameStateStone, nextMoveColor, gameOver}) {
	const stoneBlackImage = "images/stones/black.png";
	const stoneWhiteImage = "images/stones/white.png";
	
	const black = "BLACK";
	const white = "WHITE";
	
	var imageStyle;
	
// console.log("gameStateStone: " + gameStateStone);
// console.log("nextMoveColor: " + nextMoveColor);
// console.log("gameId: " + gameId);
// console.log("compare: " + (black.localCompare(gameStateStone)));
// console.log("compare: " + (black == gameStateStone));
// console.log("compare: " + (black === gameStateStone));
// if (-1) {
// console.log("-1 is true!!!!");
// }
	
	if (black === gameStateStone) {
		// black stone
		imageStyle = {
				backgroundImage: 'url(' + stoneBlackImage + ')',
				backgroundSize: '100% 100%',
			};
	}
	else if (white === gameStateStone) {
		// white stone
		imageStyle = {
				backgroundImage: 'url(' + stoneWhiteImage + ')',
				backgroundSize: '100% 100%',
			};
	}
	else {
		// no stone
		imageStyle = {
				backgroundSize: '100% 100%',
			};
	}
	
	function handleClick() {
		if (gameStateStone === null) {
			if (gameId !== -1) {
				if (!gameOver) {
					let url = "react_test/games/" + gameId + "/move";
					let move = {
							row: row,
							col: col,
							type: "STONE",
							color: nextMoveColor
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
					//game over
					alert("The game is over! No more moves allowed");
				}
			}
			else {
				//gameId == -1
				alert("No game has been started yet");
			}
		}
	}

	return <td key={col} style={imageStyle} onClick={handleClick}></td>;
}