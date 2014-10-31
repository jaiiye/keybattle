DROP DATABASE keybattle;

CREATE DATABASE `keybattle`
	CHARACTER SET utf8
	COLLATE utf8_general_ci;

USE keybattle;

DROP TABLE battle_text;
DROP TABLE user_stat;
DROP TABLE stat_type;
DROP TABLE user_role;
DROP TABLE user_info;
DROP TABLE user;

CREATE TABLE user (
	id BIGINT NOT NULL AUTO_INCREMENT,
	username VARCHAR(45) NOT NULL,
	password VARCHAR(45) NOT NULL,
	email VARCHAR(45) NOT NULL,
	creation_time datetime not null default CURRENT_TIME,
	modification_time datetime not null default CURRENT_TIME,
	enabled TINYINT(1) NOT NULL DEFAULT 1,

	PRIMARY KEY (id),

	KEY sk_username_idx (username),
	CONSTRAINT sk_username UNIQUE (username),

	KEY sk_email_idx (email),
	CONSTRAINT sk_email UNIQUE (email)
);

CREATE TABLE user_info (
	user_id BIGINT NOT NULL,
	creation_time datetime not null default CURRENT_TIME,
	modification_time datetime not null default CURRENT_TIME,
	name VARCHAR(200),
	age INT(3),
	gender INT(1),
	dob DATE,
	country VARCHAR(100),
	status VARCHAR(140),
	avatar MEDIUMBLOB,

	PRIMARY KEY (user_id),

	KEY fk_user_id_idx (user_id),
	CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE user_role (
	id BIGINT NOT NULL AUTO_INCREMENT,
	user_id BIGINT NOT NULL,
	role VARCHAR(45) NOT NULL,

	PRIMARY KEY (id),

	UNIQUE KEY uniq_user_id_role (user_id, role),

	KEY fk_user_id_idx (user_id),
	CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE stat_type (
	id INT(4) NOT NULL AUTO_INCREMENT,
	name VARCHAR(200) NOT NULL,
	description VARCHAR(400)
);

CREATE TABLE user_stat (
	id BIGINT NOT NULL AUTO_INCREMENT,
	user_id BIGINT NOT NULL,
	stat_type_id INT(4) NOT NULL,
	value BLOB NOT NULL,

	PRIMARY KEY (id),

	UNIQUE KEY uniq_user_id_stat_type_id (user_id, stat_type_id),

	KEY fk_user_id_idx (user_id),
	CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user(id),

	KEY fk_stat_type_id_idx (stat_type_id),
	CONSTRAINT fk_stat_type_id FOREIGN KEY (stat_type_id) REFERENCES stat_type(id)
);

CREATE TABLE battle_text (
	id BIGINT NOT NULL auto_increment,
	text LONGTEXT NOT NULL,

);
