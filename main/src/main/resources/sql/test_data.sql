INSERT INTO user (username, password, email, enabled)
VALUES ('true', '1q2w3e4r', 'angelen@mail.ru', TRUE);
INSERT INTO user (username, password, email, enabled)
VALUES ('valter', 'xxx12345', 'valter@mail.ru', TRUE);

INSERT INTO user_role (user_id, role)
VALUES ('3', 'ROLE_USER');
INSERT INTO user_role (user_id, role)
VALUES ('3', 'ROLE_ADMIN');
INSERT INTO user_role (user_id, role)
VALUES ('1', 'ROLE_USER');
