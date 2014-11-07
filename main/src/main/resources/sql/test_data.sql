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

INSERT INTO history_action (id, name, description)
VALUES (0, 'USER_ADDED', '');
INSERT INTO history_action (id, name, description)
VALUES (1, 'USER_ROLE_ADDED', '');
INSERT INTO history_action (id, name, description)
VALUES (2, 'USER_ROLE_REMOVED', '');
INSERT INTO history_action (id, name, description)
VALUES (3, 'USER_LOCKED', '');
INSERT INTO history_action (id, name, description)
VALUES (4, 'USER_UNLOCKED', '');
INSERT INTO history_action (id, name, description)
VALUES (5, 'USER_DISABLED', '');
INSERT INTO history_action (id, name, description)
VALUES (6, 'USER_ENABLED', '');
INSERT INTO history_action (id, name, description)
VALUES (7, 'USER_INFO_ADDED', '');
INSERT INTO history_action (id, name, description)
VALUES (8, 'USER_INFO_CHANGED', '');
INSERT INTO history_action (id, name, description)
VALUES (9, 'USER_INFO_HIDDEN', '');
INSERT INTO history_action (id, name, description)
VALUES (10, 'USER_INFO_SHOWN', '');
