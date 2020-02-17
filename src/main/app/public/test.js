import React from "react";
import ReactDOM from "react-dom";

export default function TestComponent() {
	return <div>
		<p>Hello There!</p>
	</div>;
}

ReactDOM.render(<TestComponent />, document.getElementById('hello_there'));

alert("Hello There!");