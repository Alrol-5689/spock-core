# Principios de arquitectura

## 1. Propiedad de los datos

Los datos deben pertenecer siempre al usuario.

Spock debe evitar guardar informacion critica en formatos propietarios o dependientes de una plataforma concreta.

## 2. Formatos abiertos primero

Siempre que sea posible:

- conocimiento en Markdown
- configuracion en archivos de texto legibles
- datos estructurados en PostgreSQL
- exportaciones en formatos abiertos o ampliamente soportados

## 3. Separacion entre conocimiento y metadatos

Los documentos largos viven como archivos Markdown.

La base de datos guarda metadatos, relaciones, indices y estado estructurado. No debe convertirse en un contenedor de documentos largos salvo que exista una razon tecnica especifica.

## 4. La API es la frontera de escritura

Los agentes, dashboards y automatizaciones no deben escribir directamente en la base de datos.

La logica de negocio debe vivir en el backend de Spock y exponerse mediante una API propia.

## 5. Componentes reemplazables

Los modelos de IA, agentes, dashboards, interfaces web y servicios externos deben poder cambiarse sin redisenar el nucleo del sistema.

## 6. Simplicidad antes que sofisticacion

Las decisiones deben favorecer:

- simplicidad
- mantenibilidad
- observabilidad
- capacidad de backup y restauracion
- bajo acoplamiento entre componentes

No se debe introducir una cola, motor de eventos, bus de mensajes, sistema distribuido o arquitectura compleja sin una necesidad clara.

## 7. Diseno antes que implementacion

Spock esta en fase temprana. Las decisiones importantes deben documentarse antes de implementarse.

Cuando existan dudas, se debe preferir una decision reversible y pequena frente a una gran arquitectura prematura.

## 8. Sin vendor lock-in

Spock puede integrarse con servicios externos, pero no debe depender de ellos para conservar su conocimiento o estado esencial.

Toda integracion externa debe evaluarse con una pregunta minima:

> Que ocurre con mis datos si este proveedor desaparece?
