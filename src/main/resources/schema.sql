CREATE TABLE IF NOT EXISTS users (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(15) UNIQUE,
	password VARCHAR(100),
	enabled BOOLEAN
);

CREATE TABLE IF NOT EXISTS user_authorities (
	user_id INT NOT NULL REFERENCES users(id),
	authority VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS groups (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	group_name VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS group_members (
	group_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(15) REFERENCES users(username)
);

CREATE TABLE IF NOT EXISTS group_authorities (
	group_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	authority VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS boards (
	board_size INT NOT NULL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS games (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	player_black INT NOT NULL REFERENCES users(id),
	player_white INT NOT NULL REFERENCES users(id),
	moves TEXT,
	started_on TIMESTAMP,
	last_played_on TIMESTAMP,
	board_size INT NOT NULL REFERENCES boards(board_size),
	resign BOOLEAN,
	points FLOAT,
	over BOOLEAN,
	handycap INT,
	comi FLOAT
);