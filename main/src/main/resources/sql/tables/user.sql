CREATE TABLE USER (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	USERNAME VARCHAR(45) NOT NULL,
	PASSWORD VARCHAR(45) NOT NULL,
	EMAIL VARCHAR(45) NOT NULL,
	LOCKED TINYINT(1) NOT NULL DEFAULT 0,
	ENABLED TINYINT(1) NOT NULL DEFAULT 1,
	CREATION_TIME TIMESTAMP NOT NULL DEFAULT NOW(),
	MODIFICATION_TIME TIMESTAMP NOT NULL DEFAULT NOW(),

	PRIMARY KEY (ID),

	KEY IDX_U_USERNAME (USERNAME),
	CONSTRAINT UK_U_USERNAME UNIQUE (USERNAME),

	KEY IDX_U_EMAIL (EMAIL),
	CONSTRAINT UK_U_EMAIL UNIQUE (EMAIL)
)
