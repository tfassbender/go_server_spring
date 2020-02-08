CREATE TABLE IF NOT EXISTS users (
	id INT NOT NULL PRIMARY KEY,
	name VARCHAR(15),
	password VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS roles (
	name VARCHAR(100) NOT NULL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS user_roles (
	user_id INT NOT NULL REFERENCES users(id),
	role VARCHAR(100) NOT NULL REFERENCES roles(name),
	PRIMARY KEY (id, role)
);

CREATE TABLE IF NOT EXISTS boards (
	size INT NOT NULL PRIMARY KEY,
);

CREATE TABLE IF NOT EXISTS games (
	id INT NOT NULL PRIMARY KEY,
	player_black INT NOT NULL REFERENCES users(id),
	player_white INT NOT NULL REFERENCES users(id),
	moves TEXT,
	started_on TIMESTAMP,
	last_played_on TIMESTAMP,
	board_size INT NOT NULL REFERENCES boards(size),
	resign BOOLEAN,
	points FLOAT,
	over BOOLEAN,
	handycap INT,
	comi FLOAT
);