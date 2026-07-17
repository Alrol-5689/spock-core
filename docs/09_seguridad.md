# Seguridad

## Principio

Spock gestionara informacion personal. La seguridad debe tratarse como una parte central del diseno, no como un anadido posterior.

## Alcance inicial

Durante la primera fase, Spock se ejecutara localmente.

Esto reduce algunos riesgos, pero no elimina la necesidad de controlar accesos, secretos, backups y permisos de agentes.

## Areas de riesgo

- acceso no autorizado a la API
- agentes con demasiados permisos
- exposicion accidental de informacion personal
- backups sin cifrar
- secretos guardados en Git
- dashboards expuestos en red local o internet
- automatizaciones destructivas sin confirmacion

## Reglas iniciales

- no guardar secretos en el repositorio
- no exponer servicios a internet sin diseno de seguridad previo
- no permitir escrituras directas de agentes a PostgreSQL
- registrar acciones importantes realizadas por agentes
- exigir confirmacion para operaciones destructivas

## Permisos de agentes

A futuro, cada agente deberia tener permisos explicitos.

Ejemplos:

- leer conocimiento
- crear documentos
- editar documentos
- crear tareas
- modificar tareas
- ejecutar scripts
- acceder a datos sensibles

## Backups

Los backups deben protegerse igual que los datos originales.

Si contienen informacion personal, deben cifrarse antes de almacenarse fuera de la maquina principal.

## Cuestiones abiertas

- mecanismo de autenticacion local
- cifrado de backups
- gestion de claves
- control de permisos por agente
- auditoria de acciones
- separacion entre datos sensibles y no sensibles
