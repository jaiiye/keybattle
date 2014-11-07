DROP DATABASE keybattle;

CREATE DATABASE `keybattle`
	CHARACTER SET utf8
	COLLATE utf8_general_ci;

USE keybattle;

DROP TABLE user_history;
DROP TABLE history_action;
DROP TABLE user_info;
DROP TABLE user_role;
DROP TABLE user;

CREATE TABLE user (
	id BIGINT NOT NULL AUTO_INCREMENT,
	username VARCHAR(45) NOT NULL,
	password VARCHAR(45) NOT NULL,
	email VARCHAR(45) NOT NULL,
	locked TINYINT(1) NOT NULL DEFAULT 0,
	enabled TINYINT(1) NOT NULL DEFAULT 1,
	creation_time TIMESTAMP NOT NULL DEFAULT now(),
	modification_time TIMESTAMP NOT NULL DEFAULT now(),

	PRIMARY KEY (id),

	KEY idx_u_username (username),
	CONSTRAINT uk_u_username UNIQUE (username),

	KEY idx_u_email (email),
	CONSTRAINT uk_u_email UNIQUE (email)
);

CREATE TABLE user_role (
	id BIGINT NOT NULL AUTO_INCREMENT,
	user_id BIGINT NOT NULL,
	role VARCHAR(45) NOT NULL,
	enabled INT(1) NOT NULL DEFAULT 1,
	creation_time TIMESTAMP NOT NULL DEFAULT now(),
	modification_time TIMESTAMP NOT NULL DEFAULT now(),

	PRIMARY KEY (id),

	KEY idx_ur_user_id_role (user_id, role),
	CONSTRAINT uk_ur_user_id_role UNIQUE (user_id, role),

	KEY idx_ur_user_id (user_id),
	CONSTRAINT fk_ur_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE user_info (
	user_id BIGINT NOT NULL,
	name VARCHAR(200) DEFAULT NULL,
	gender INT(1) NOT NULL DEFAULT 2,
	dob DATE DEFAULT NULL,
	country VARCHAR(100) DEFAULT NULL,
	status VARCHAR(140) DEFAULT NULL,
	avatar MEDIUMBLOB DEFAULT NULL,
	hidden INT(1) NOT NULL DEFAULT 1,
	creation_time TIMESTAMP NOT NULL DEFAULT now(),
	modification_time TIMESTAMP NOT NULL DEFAULT now(),

	PRIMARY KEY (user_id),

	CONSTRAINT fk_ui_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE VIEW v_user_info
AS
	SELECT
		u.*,
		DATE_FORMAT(NOW(), '%Y') - DATE_FORMAT(dob, '%Y') - (DATE_FORMAT(NOW(), '00-%m-%d')
																												 < DATE_FORMAT(dob, '00-%m-%d')) AS age
	FROM user_info u;

CREATE TABLE history_action (
	id INT(4) NOT NULL,
	name VARCHAR(200) NOT NULL,
	description VARCHAR(400),

	PRIMARY KEY (id)
);

CREATE TABLE user_history (
	id BIGINT NOT NULL AUTO_INCREMENT,
	user_id BIGINT NOT NULL,
	history_action_id INT(4) NOT NULL,
	actor_id BIGINT NOT NULL,
	creation_time TIMESTAMP NOT NULL DEFAULT now(),

	PRIMARY KEY (id),

	KEY idx_uh_user_id (user_id),
	CONSTRAINT fk_uh_user_id FOREIGN KEY (user_id) REFERENCES user (id),

	KEY idx_uh_history_action_id (history_action_id),
	CONSTRAINT fk_uh_history_action_id FOREIGN KEY (history_action_id) REFERENCES history_action (id)
);

CREATE TABLE dict_type (
	id INT NOT NULL,
	name VARCHAR(200) NOT NULL,
	description VARCHAR(400),

	PRIMARY KEY (id),

	KEY idx_dt_name (name),
	CONSTRAINT uk_dt_name UNIQUE (name)
);

CREATE TABLE dict (
	type_id INT NOT NULL,
	term_id BIGINT NOT NULL,
	locale VARCHAR(20) NOT NULL,
	value TEXT NOT NULL,

	PRIMARY KEY (term_id, locale),

	KEY idx_d_type_id (type_id)
);

CREATE TABLE tag (
	id BIGINT NOT NULL AUTO_INCREMENT,
	term_id BIGINT NOT NULL,
	deleted INT(1) NOT NULL DEFAULT 0,
	creation_time TIMESTAMP NOT NULL DEFAULT now(),
	modification_time TIMESTAMP NOT NULL DEFAULT now(),

	PRIMARY KEY (id),

	KEY idx_t_name (name),
	CONSTRAINT uk_t_name UNIQUE (name)
);

CREATE TABLE text_tag (
	text_id BIGINT NOT NULL,
	tag_id BIGINT NOT NULL,

	KEY idx_tt_text_id (text_id),

	KEY idx_tt_tag_id (tag_id),
	CONSTRAINT uk_tt_text_id_tag_id UNIQUE (text_id, tag_id)
);

CREATE TABLE text (
	id BIGINT NOT NULL AUTO_INCREMENT,
	text LONGTEXT NOT NULL,
	deleted INT(1) NOT NULL DEFAULT 0,
	creation_time TIMESTAMP NOT NULL DEFAULT now(),
	modification_time TIMESTAMP NOT NULL DEFAULT now(),

	creation_time TIMESTAMP NOT NULL DEFAULT now(),
	modification_time TIMESTAMP NOT NULL DEFAULT now()
);
