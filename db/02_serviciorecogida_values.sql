-- Se insertan todas las zonas
INSERT INTO servrecog_zones(nombre_uk) 
	VALUES ('Centro');
INSERT INTO servrecog_zones(nombre_uk) 
	VALUES ('Rio San Pedro');
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

--Rio San Pedro

--las Canteras
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.536233,-6.193095,'Calle Sol',
		(SELECT id_zone_pk FROM servrecog_zones WHERE servrecog_zones.nombre_uk LIKE 'las Canteras'));
INSERT INTO servrecog_puntos_recogida(lat_nn,lon_nn,direccion,zone)
	VALUES(36.536090,-6.191984,'Calle Geminis',
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

-- VALUES DE CATEGORIAS DE MUEBLES Y ENSERES
INSERT INTO servrecog_categories(id_category_pk,name_nn)
	VALUES(1,'bathroom');
INSERT INTO servrecog_categories(id_category_pk,name_nn)
	VALUES(2,'kitchen');
INSERT INTO servrecog_categories(id_category_pk,name_nn)
	VALUES(3,'bedroom');
INSERT INTO servrecog_categories(id_category_pk,name_nn)
	VALUES(4,'outside');
INSERT INTO servrecog_categories(id_category_pk,name_nn)
	VALUES(5,'livingroom');
INSERT INTO servrecog_categories(id_category_pk,name_nn)
	VALUES(6,'general');

--- VALUES OF FURNITURES BATHROOM
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(1,'bathtub',1);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(2,'sink',1);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(3,'bidet',1);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(4,'bath_furniture',1);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(5,'shower_plate',1);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(6,'bath_self',1);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(7,'wc',1);
--- VALUES OF FURNITURES KITCHEN
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(8,'sink',2);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(9,'worktop',2);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(10,'kitchen_table',2);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(11,'fridge',2);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(12,'kitchen_self',2);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(13,'oven',2);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(14,'microwave',2);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(15,'dishwasher',2);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(16,'kitchen_closet',2);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(17,'kitchen_fire',2);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(18,'kitchen_gas',2);
--- VALUES OF FURNITURES BEDROOM
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(19,'bed',3);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(20,'doublebed',3);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(21,'bedroom_table',3);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(22,'bedside_table',3);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(23,'bedroom_chair',3);
-- VALUES OF FURNITURE OUTSIDE
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(24,'garden_table',4);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(25,'barbacue',4);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(26,'garden_chair',4);
-- VALUES OF LIVINGROOM
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(27,'sofa',5);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(28,'living_chair',5);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(29,'living_table',5);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(30,'armchair',5);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(31,'living_furniture',5);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(32,'living_shelf',5);
-- VALUES OF GENERAL
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(33,'desk',6);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(34,'door',6);
INSERT INTO servrecog_furnitures (id_furniture_pk,name_nn,id_category_uk)
	VALUES(35,'heater',6);