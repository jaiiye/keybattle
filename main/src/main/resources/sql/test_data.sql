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

insert into history_action (id, name, description)
values (0, 'USER_ADDED', '');
insert into history_action (id, name, description)
values (1, 'USER_ROLE_ADDED', '');
insert into history_action (id, name, description)
values (2, 'USER_ROLE_REMOVED', '');
insert into history_action (id, name, description)
values (3, 'USER_LOCKED', '');
insert into history_action (id, name, description)
values (4, 'USER_UNLOCKED', '');
insert into history_action (id, name, description)
values (5, 'USER_DISABLED', '');
insert into history_action (id, name, description)
values (6, 'USER_ENABLED', '');
insert into history_action (id, name, description)
values (7, 'USER_INFO_ADDED', '');
insert into history_action (id, name, description)
values (8, 'USER_INFO_CHANGED', '');
insert into history_action (id, name, description)
values (9, 'USER_INFO_HIDDEN', '');
insert into history_action (id, name, description)
values (10, 'USER_INFO_SHOWN', '');
