import React from 'react';

import GoFieldComponent from './goField.js';
import GoControllerComponent from './goController.js';

export default function GoComponent() {
	return <div class='go_component'>
		<div class='headline'>
			<h1>Go Server</h1>
		</div>
		<div class='go_field'>
			<GoFieldComponent></GoFieldComponent>
		</div>
		<div class='go_components'>
			<GoControllerComponent></GoControllerComponent>
		</div>
	</div>;
}