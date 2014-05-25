# Servicio de Recogida de Muebles y Enseres
Este proyecto es un servicio web mediante JAX-WS que permite responder a las solicitudes de una app Recicloid, y registrar/consultar peticiones en una base de datos "servrecog". Este proyecto se debe integrar con el proyecto "recicloid"

## Configuración
Para hacer funcionar el servicio web se requiere de los siguientes componentes en ejecución:

1. _Servidor Apache Tomcat 7_
2, _Base de datos Postgresql_

Se deberá adaptar la configuración de los ficheros _resources/mybatis___config.xml_ para poder conectar correctamente con la base de datos. 

## Recursos
Los recursos del servidor _FurnitureCollectionService/resources_ se muestra en la siguiente tabla:

| URI path | Resource class | HTTP methods |
| -------- | -------------- | ------------ | 
| /user | JsonResource | POST |
| /user/{id} | JsonResource | GET |
| /point/{id} | JsonResource | GET |

## Pruebas realizadas
