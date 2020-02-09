DELETE FROM user_authorities;
DELETE FROM users;
DELETE FROM games;
DELETE FROM boards;

INSERT INTO boards(board_size) VALUES (9);
INSERT INTO boards(board_size) VALUES (13);
INSERT INTO boards(board_size) VALUES (19);

INSERT INTO users(id, username, password, enabled) VALUES(0, 'root', 'asdf', true);
INSERT INTO users(id, username, password, enabled) VALUES(1, 'Player1', 'password1', true);
INSERT INTO users(id, username, password, enabled) VALUES(2, 'Player2', 'password2', true);

INSERT INTO user_authorities (user_id, authority) VALUES (0, 'USER');
INSERT INTO user_authorities (user_id, authority) VALUES (0, 'ADMIN');
INSERT INTO user_authorities (user_id, authority) VALUES (1, 'USER');
INSERT INTO user_authorities (user_id, authority) VALUES (2, 'USER');