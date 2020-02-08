DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM roles;
DELETE FROM games;
DELETE FROM boards;

INSERT INTO boards(size) VALUES (9);
INSERT INTO boards(size) VALUES (13);
INSERT INTO boards(size) VALUES (19);

INSERT INTO roles(name) VALUES ("USER");
INSERT INTO roles(name) VALUES ("ADMIN");

INSERT INTO users(id, name, password) VALUES(0, "Player1", "password1");
INSERT INTO users(id, name, password) VALUES(0, "Player2", "password2");