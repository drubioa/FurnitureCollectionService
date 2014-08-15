-- Se insertan todas las zonas
INSERT INTO servrecog_zones(nombre_uk) 
	VALUES ('Centro');
INSERT INTO servrecog_zones(nombre_uk) 
	VALUES ('Río San Pedro');
INSERT INTO servrecog_zones(nombre_uk) 
	VALUES ('las Canteras');
INSERT INTO servrecog_zones(nombre_uk) 
	VALUES ('Casines');
INSERT INTO servrecog_zones(nombre_uk) 
	VALUES ('Barrio Jarana');
INSERT INTO servrecog_zones(nombre_uk) 
	VALUES ('Meadero de la Reina ');
INSERT INTO servrecog_zones(nombre_uk) 
	VALUES ('Zonas Rurales');		
				
-- Centro

--Río San Pedro

--las Canteras
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.536233,-6.193095,'Calle Sol',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.536090,-6.191984,'Calle Géminis',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.536159,-6.190359,'Calle Leo',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.534123,-6.191078,'Calle Tierra',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.537189,-6.191169,'Calle Luna',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.534814,-6.191040,'Calle Sol con Calle Marte',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.534814,-6.191040,'Calle Sol con Calle Marte',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.535183,-6.188556,'Calle Aries con Calle Sol',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.535961,-6.188878,'Calle Aries con Calle Luna',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.535577,-6.189964,'Calle Leo',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.537019,-6.187105,'Calle Casiopea',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.533631,-6.187671,'Calle Aries',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.533631,-6.187671,'Calle Aries',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.531651,-6.185560,'Calle Tierra, Virgen del Carmen',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.532638,-6.187280,'Final de la calle Aries',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.533857,-6.187203,'Orion',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
		
		
-- Casines

--Barrio Jarana

--Meadero de la Reina 

--Zonas Rurales.

