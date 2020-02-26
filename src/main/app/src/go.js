import React from 'react';

import GoFieldComponent from './goField.js';
import GoControllerComponent from './goController.js';

export default function GoComponent() {
	const [gameState, setGameState] = React.useState(null);
	
	return <div class='go_component'>
		<div class='headline'>
			<h1>Go Server</h1>
		</div>
		<div class='go_field'>
			<GoFieldComponent gameState={gameState} setGameState={setGameState}></GoFieldComponent>
		</div>
		<div class='go_components'>
			<GoControllerComponent setGameState={setGameState}></GoControllerComponent>
		</div>
	</div>;
}