# Dashboards

## Responsabilidad

Los dashboards son interfaces de visualizacion y edicion.

No deben contener logica de negocio.

## Regla principal

Un dashboard debe:

- mostrar informacion
- permitir acciones del usuario
- llamar a la API
- representar estados de carga y error

Un dashboard no debe:

- escribir directamente en PostgreSQL
- decidir reglas de negocio importantes
- modificar documentos Markdown por su cuenta
- contener automatizaciones criticas sin pasar por el backend

## Dashboards posibles

- proyectos
- tareas
- calendario
- conocimiento
- inversiones
- salud
- automatizaciones
- actividad de agentes

## Criterios de diseno

Los dashboards de Spock deben ser utiles para trabajo diario.

Prioridades:

- densidad de informacion razonable
- navegacion rapida
- estados claros
- edicion segura
- busqueda y filtrado
- baja friccion

No deben convertirse en paginas de marketing ni interfaces decorativas.

## Cuestiones abiertas

- framework frontend
- autenticacion local
- modo responsive
- integracion con Markdown
- editor embebido o editor externo
- permisos por dashboard
