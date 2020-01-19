CREATE TABLE hores_temperatures (
	data_inici DATE			NOT NULL,
	nom_ciutat VARCHAR(20)	NOT NULL,
	temperatura NUMBER		NOT NULL,
	icon CHAR(3)			NOT NULL,
	weather VARCHAR(20)		NOT NULL,

	CONSTRAINT PK_hores_temperatures PRIMARY KEY (data_inici, nom_ciutat)
);

-- SELECT l'última hora de modificació d'una ciutat.
SELECT *
	FROM hores_temperatures
	WHERE
		nom_ciutat = ?
	ORDER BY data_inici DESC
;

SELECT *
	FROM hores_temperatures
	WHERE
		nom_ciutat = ?
	ORDER BY data_inici DESC
;


device file xeplorer -> buscar el paquet -> databases -> pos allà

right click -> save as -> així es pot explorar amb l'sqlite viewer online
