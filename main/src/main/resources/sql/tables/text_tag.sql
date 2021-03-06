CREATE TABLE TEXT_TAG (
	TEXT_ID BIGINT NOT NULL,
	TAG_ID BIGINT NOT NULL,

	KEY IDX_TT_TEXT_ID (TEXT_ID),
	CONSTRAINT FK_TT_TEXT_ID FOREIGN KEY (TEXT_ID) REFERENCES TEXT (ID),
	
	KEY IDX_TT_TAG_ID (TAG_ID),
	CONSTRAINT FK_TT_TAG_ID FOREIGN KEY (TYPE_ID) REFERENCES TAG (ID),
	
	CONSTRAINT UK_TT_TEXT_ID_TAG_ID UNIQUE (TEXT_ID, TAG_ID)
)
