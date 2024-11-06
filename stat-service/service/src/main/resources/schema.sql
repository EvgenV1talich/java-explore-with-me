DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE hits (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	app varchar(255),
	uri varchar(255),
	ip varchar(255),
	created timestamp NULL,
	CONSTRAINT pk_hits PRIMARY KEY (id)
);