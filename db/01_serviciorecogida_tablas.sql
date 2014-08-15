-- BD para el servidor del servicio de recogida de muebles y enseres
CREATE DATABASE servrecog;

DROP VIEW servrecog_muebles_solicitud_view;
DROP TABLE servrecog_muebles_solicitud;
DROP TABLE servrecog_solicitudes;
DROP SEQUENCE servrecog_id_solicitud_seq;
DROP TABLE servrecog_puntos_recogida;
DROP SEQUENCE servrecog_id_puntos_recogida_seq;
DROP TABLE servrecog_zones;
DROP SEQUENCE servrecog_id_zone_seq;
DROP TABLE servrecog_furnitures;
DROP SEQUENCE servrecog_id_furniture_seq;
DROP TABLE servrecog_categories;
DROP SEQUENCE servrecog_id_category_seq;
DROP TABLE servrecog_usuarios;

-- TABLA USUARIOS DEL SERVICIO DE RECOGIDA DE MUEBLES Y ENSERES
CREATE TABLE servrecog_usuarios	(
	telephone_pk	varchar(12) PRIMARY KEY,
	name_nn			varchar(50) NOT NULL
);

-- TABLA DE CATEGORIAS DE MUEBLES Y ENSERES
CREATE TABLE servrecog_categories	(
	id_category_pk 		int PRIMARY KEY,
	name_nn				varchar(30) NOT NULL
);

CREATE SEQUENCE servrecog_id_furniture_seq;

-- TABLA DE MUEBLES Y ENSERES
CREATE TABLE servrecog_furnitures (
	id_furniture_pk		int PRIMARY KEY,
	name_nn				varchar(60) NOT NULL, 
	id_category_uk 		int REFERENCES  servrecog_categories(id_category_pk), 
						UNIQUE(name_nn)	
);

CREATE SEQUENCE servrecog_id_zone_seq;

-- TABLA DE ZONAS DEL MUNICIPIO
CREATE TABLE servrecog_zones (
	id_zone_pk		int DEFAULT nextval('servrecog_id_zone_seq') PRIMARY KEY,
	nombre_uk 		VARCHAR(30) NOT NULL,UNIQUE(nombre_uk)
);

CREATE SEQUENCE servrecog_id_puntos_recogida_seq;

-- TABLA DE PUNTOS DE RECOGIDA DE LA LOCALIDAD
CREATE TABLE servrecog_puntos_recogida(
	id_punto_recogida_pk 	int DEFAULT nextval('servrecog_id_puntos_recogida_seq') PRIMARY KEY,
	lon_nn					double precision NOT NULL,
	lat_nn 					double precision NOT NULL,
	direccion				VARCHAR(60),
	zone					int REFERENCES servrecog_zones(id_zone_pk),
	UNIQUE (lon_nn,lat_nn)
);

CREATE SEQUENCE servrecog_id_solicitud_seq;

-- TABLA DE SOLICITUDES DE RECOGIDA
CREATE TABLE servrecog_solicitudes(
	id_solicitud_pk			int DEFAULT nextval('servrecog_id_solicitud_seq') PRIMARY KEY,
	fch_solicitud_nn		date NOT NULL,
	fch_recogida_nn			date NOT NULL,
	telephone_nn			varchar(12)	REFERENCES servrecog_usuarios(telephone_pk) NOT NULL,
	punto_recogida_nn		int REFERENCES servrecog_puntos_recogida(id_punto_recogida_pk) NOT NULL,
	num_total_enseres_ck	int DEFAULT 0 CHECK(cantidad_ck <= 4 and cantidad_ck > 0), 
);	

-- TABLA DE ENSERES ASOCIADOS A LAS SOLICITUDES 
CREATE TABLE servrecog_muebles_solicitud (
	id_furniture_nn	int REFERENCES servrecog_furnitures(id_furniture_pk) NOT NULL,
	id_solicitud_nn	int REFERENCES servrecog_solicitudes(id_solicitud_pk) NOT NULL,
	cantidad_ck		int DEFAULT 1,
	CHECK(cantidad_ck <= 4 and cantidad_ck > 0), 
	PRIMARY KEY(id_furniture_nn,id_solicitud_nn)
);

-- VISTA DE SOLICITUD CON EL TOTAL DE ENSERES SOLICITADOS
CREATE VIEW servrecog_muebles_solicitud_view AS
SELECT  id_solicitud_pk,
	fch_solicitud_nn,
	fch_recogida_nn,
	telephone_nn,
	punto_recogida_nn,
	(SELECT SUM(servrecog_muebles_solicitud.cantidad_ck) 
		FROM servrecog_muebles_solicitud
		WHERE servrecog_muebles_solicitud.id_solicitud_nn = id_solicitud_pk) 
		AS num_enseres
	FROM servrecog_solicitudes;

COMMIT;
