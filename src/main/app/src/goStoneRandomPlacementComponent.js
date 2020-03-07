import React from 'react';

export default function GoStoneRandomPlacementFieldComponent({row, col, gameStateStone}) {
	const [stone, setStone] = React.useState(0);
	
	const stoneBlackImage = "images/stones/black.png";
	const stoneWhiteImage = "images/stones/white.png";
	
	var imageStyle;
	
	if (stone === 1) {
		// black stone
		imageStyle = {
				backgroundImage: 'url(' + stoneBlackImage + ')',
				backgroundSize: '100% 100%',
			};
	}
	else if (stone === 2) {
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
		if (stone === 0) {
			let randomColor = Math.floor(Math.random() * 2) + 1;
			setStone(randomColor);			
		}
	}

	return <td key={col} style={imageStyle} onClick={handleClick}></td>;
}