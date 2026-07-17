# Mascaras de red y comprobacion de conectividad en sistemas radar

## Objetivo

Este documento explica que es una mascara de red y que condiciones deben cumplirse para poder hacer `ping` desde un puesto de control hacia otros equipos de un sistema radar, como el centro de distribucion, el jammer y el resto de dispositivos conectados a la red.

El enfoque es operativo: entender si dos equipos pueden verse en red, comprobar la configuracion basica y diagnosticar fallos habituales de conectividad.

## Conceptos basicos

### Direccion IP

Una direccion IP identifica a un equipo dentro de una red. Por ejemplo:

```text
192.168.10.25
```

En un sistema radar, cada elemento conectado a la red debe tener una IP unica:

```text
Puesto de control:       192.168.10.10
Centro de distribucion:  192.168.10.20
Jammer:                  192.168.10.30
Radar:                   192.168.10.40
Switch gestionable:      192.168.10.2
```

Dos equipos no deben compartir la misma IP. Si dos dispositivos tienen la misma direccion, pueden aparecer fallos intermitentes, perdida de paquetes o imposibilidad de comunicacion.

### Mascara de red

La mascara de red indica que parte de la IP identifica la red y que parte identifica al dispositivo.

Ejemplo:

```text
IP:       192.168.10.25
Mascara: 255.255.255.0
```

Con la mascara `255.255.255.0`, la red seria:

```text
192.168.10.0/24
```

Eso significa que normalmente los equipos validos para esa red estaran entre:

```text
192.168.10.1 - 192.168.10.254
```

Si el puesto de control tiene `192.168.10.10/24` y el centro de distribucion tiene `192.168.10.20/24`, ambos estan en la misma red y deberian poder comunicarse directamente, siempre que el cableado, switches, VLANs y firewalls lo permitan.

### Notacion CIDR

La mascara tambien puede escribirse en formato CIDR:

```text
255.255.255.0 = /24
255.255.0.0   = /16
255.0.0.0     = /8
```

En documentacion tecnica es habitual ver direcciones como:

```text
192.168.10.10/24
```

Eso significa:

```text
IP:       192.168.10.10
Mascara: 255.255.255.0
```

## Para que sirve la mascara de red

La mascara permite al equipo decidir si el destino esta en su misma red local o si debe enviar el trafico a traves de una puerta de enlace.

Ejemplo 1: misma red

```text
Puesto de control:      192.168.10.10/24
Centro de distribucion: 192.168.10.20/24
```

Ambos estan en `192.168.10.0/24`. El puesto de control puede intentar comunicarse directamente con el centro de distribucion.

Ejemplo 2: redes distintas

```text
Puesto de control:      192.168.10.10/24
Centro de distribucion: 192.168.20.20/24
```

El puesto de control esta en `192.168.10.0/24` y el centro de distribucion esta en `192.168.20.0/24`. Para comunicarse, necesitara una ruta a traves de un router, firewall o gateway configurado.

## Que es `ping`

`ping` es una herramienta de diagnostico que envia paquetes ICMP Echo Request a una direccion IP y espera una respuesta ICMP Echo Reply.

Sirve para comprobar si un equipo es alcanzable a nivel de red.

Ejemplo:

```bash
ping 192.168.10.20
```

Si responde, normalmente veremos algo parecido a:

```text
Reply from 192.168.10.20: bytes=32 time<1ms TTL=64
```

Si no responde, podemos ver mensajes como:

```text
Request timed out
Destination host unreachable
```

Importante: que un equipo no responda a `ping` no siempre significa que este apagado. Puede estar encendido pero tener ICMP bloqueado por firewall, estar en otra VLAN, tener una ruta mal configurada o tener una mascara incorrecta.

## Requisitos para poder hacer `ping`

Para hacer `ping` desde el puesto de control al centro de distribucion, jammer u otros dispositivos, deben cumplirse estas condiciones:

1. El dispositivo destino debe estar encendido.
2. El cable de red o enlace fisico debe estar conectado.
3. El puerto del switch debe estar activo.
4. El puesto de control debe tener una IP valida.
5. El dispositivo destino debe tener una IP valida.
6. Las mascaras de red deben ser coherentes.
7. Si estan en redes distintas, debe existir una puerta de enlace o ruta.
8. No debe haber conflicto de IP.
9. Las VLANs deben permitir comunicacion entre origen y destino.
10. El firewall del origen, destino o equipo intermedio debe permitir ICMP.
11. El dispositivo destino debe estar configurado para responder a ICMP, si aplica.

## Ejemplo de red para sistema radar

Una configuracion simple podria ser:

```text
Red principal radar: 192.168.10.0/24
Mascara:             255.255.255.0
Gateway:             192.168.10.1
```

| Equipo | IP | Mascara | Gateway |
| --- | --- | --- | --- |
| Puesto de control | 192.168.10.10 | 255.255.255.0 | 192.168.10.1 |
| Centro de distribucion | 192.168.10.20 | 255.255.255.0 | 192.168.10.1 |
| Jammer | 192.168.10.30 | 255.255.255.0 | 192.168.10.1 |
| Radar principal | 192.168.10.40 | 255.255.255.0 | 192.168.10.1 |
| Consola de mantenimiento | 192.168.10.50 | 255.255.255.0 | 192.168.10.1 |

Con esta configuracion, desde el puesto de control se podria probar:

```bash
ping 192.168.10.20
ping 192.168.10.30
ping 192.168.10.40
ping 192.168.10.50
```

## Caso con varias redes

En sistemas mas complejos puede haber varias redes separadas:

```text
Red de control:       192.168.10.0/24
Red de sensores:      192.168.20.0/24
Red de mantenimiento: 192.168.30.0/24
```

Ejemplo:

```text
Puesto de control:      192.168.10.10/24
Centro de distribucion: 192.168.10.20/24
Radar:                  192.168.20.40/24
Jammer:                 192.168.20.30/24
Gateway control:        192.168.10.1
Gateway sensores:       192.168.20.1
```

En este caso, el puesto de control no esta en la misma red que el radar o el jammer. Para poder hacer `ping`, debe existir enrutamiento entre la red `192.168.10.0/24` y la red `192.168.20.0/24`.

Si no hay ruta, el `ping` fallara aunque los equipos esten encendidos.

## Procedimiento de comprobacion desde el puesto de control

### 1. Comprobar la configuracion IP local

En Windows:

```cmd
ipconfig /all
```

En Linux:

```bash
ip addr
ip route
```

Se debe revisar:

```text
Direccion IP
Mascara de red
Puerta de enlace
Servidor DNS, si aplica
Interfaz activa
```

### 2. Comprobar conectividad con la propia interfaz

```bash
ping 127.0.0.1
ping <IP_DEL_PUESTO_DE_CONTROL>
```

Si falla el ping a `127.0.0.1`, hay un problema local en la pila TCP/IP del equipo.

Si falla el ping a la propia IP, puede haber un problema de configuracion de la interfaz.

### 3. Comprobar el gateway

```bash
ping 192.168.10.1
```

Si no responde el gateway, revisar:

```text
Cableado
Puerto del switch
VLAN
Configuracion IP
Mascara
Gateway configurado
Firewall local
```

### 4. Comprobar el centro de distribucion

```bash
ping 192.168.10.20
```

Si responde, hay conectividad basica entre el puesto de control y el centro de distribucion.

Si no responde, comprobar:

```text
IP del centro de distribucion
Mascara del centro de distribucion
Estado del enlace fisico
VLAN asignada
Firewall
Tabla ARP
Rutas
```

### 5. Comprobar el jammer

```bash
ping 192.168.10.30
```

Si el jammer esta en otra red, por ejemplo `192.168.20.30`, entonces el puesto de control debe tener una ruta hacia esa red:

```bash
ping 192.168.20.30
```

Si falla, revisar el router, firewall o switch de capa 3 que conecta ambas redes.

### 6. Comprobar el resto de dispositivos

Se recomienda mantener una tabla de inventario con:

```text
Nombre del equipo
Funcion
Direccion IP
Mascara
Gateway
VLAN
Puerto de switch
Ubicacion fisica
Responsable
Observaciones
```

Ejemplo:

| Equipo | Funcion | IP | VLAN | Resultado ping |
| --- | --- | --- | --- | --- |
| CD-01 | Centro de distribucion | 192.168.10.20 | 10 | OK |
| JAM-01 | Jammer | 192.168.10.30 | 10 | OK |
| RAD-01 | Radar principal | 192.168.10.40 | 10 | OK |
| SW-01 | Switch principal | 192.168.10.2 | 10 | OK |

## Interpretacion de errores habituales

### `Request timed out`

El equipo origen envia el paquete, pero no recibe respuesta.

Posibles causas:

```text
Equipo destino apagado
ICMP bloqueado
Firewall activo
VLAN incorrecta
Ruta inexistente
Cable desconectado
Dispositivo destino no configurado para responder a ping
```

### `Destination host unreachable`

El equipo origen o un router intermedio indica que no sabe llegar al destino.

Posibles causas:

```text
Mascara incorrecta
Gateway incorrecto
Ruta inexistente
Destino fuera de la red
Interfaz sin enlace
```

### Respuesta desde una IP distinta

Puede indicar que un router, firewall u otro equipo intermedio esta generando la respuesta de error.

Se debe revisar la ruta con:

```bash
tracert <IP_DESTINO>
```

En Linux:

```bash
traceroute <IP_DESTINO>
```

## Errores frecuentes de configuracion

### Mascara diferente entre equipos de la misma red

Ejemplo incorrecto:

```text
Puesto de control:      192.168.10.10/24
Centro de distribucion: 192.168.10.20/16
```

Puede funcionar en algunos casos, pero genera comportamientos confusos cuando se incorporan otras redes. Lo recomendable es que todos los equipos del mismo segmento usen la misma mascara.

### Gateway fuera de la red local

Ejemplo incorrecto:

```text
IP:       192.168.10.10
Mascara: 255.255.255.0
Gateway: 192.168.20.1
```

Con esa mascara, `192.168.20.1` no esta en la misma red que `192.168.10.10`, por lo que el gateway no es valido para ese equipo.

### IP duplicada

Si dos equipos tienen la misma IP, la comunicacion puede fallar de forma intermitente.

Sintomas:

```text
El ping responde a veces si y a veces no
La MAC asociada a la IP cambia
Un equipo pierde conexion al encender otro
Aplicaciones del sistema radar se desconectan sin causa clara
```

### VLAN incorrecta

Aunque la IP y la mascara sean correctas, si los puertos del switch estan en VLANs distintas sin enrutamiento entre ellas, los equipos no se comunicaran.

## Comandos utiles

### Windows

```cmd
ipconfig /all
ping <IP_DESTINO>
tracert <IP_DESTINO>
arp -a
route print
```

### Linux

```bash
ip addr
ip route
ping <IP_DESTINO>
traceroute <IP_DESTINO>
arp -n
```

## Checklist rapido

Antes de concluir que un equipo esta averiado, revisar:

```text
[ ] El equipo esta encendido.
[ ] El cable esta conectado.
[ ] El puerto del switch tiene enlace.
[ ] La IP del origen es correcta.
[ ] La IP del destino es correcta.
[ ] No hay IP duplicada.
[ ] La mascara es correcta.
[ ] El gateway es correcto.
[ ] La VLAN es correcta.
[ ] Existe ruta si origen y destino estan en redes distintas.
[ ] El firewall permite ICMP.
[ ] El destino esta configurado para responder a ping.
```

## Recomendaciones para sistemas radar

1. Mantener un plan de direccionamiento IP documentado.
2. Reservar rangos por funcion: control, sensores, mantenimiento, gestion y servicios auxiliares.
3. Evitar cambios manuales no registrados en IPs, mascaras o gateways.
4. Etiquetar fisicamente cables, puertos y equipos.
5. Documentar la VLAN de cada puerto de switch.
6. Registrar pruebas de `ping` durante la puesta en marcha y tras cada cambio.
7. No asumir que un fallo de `ping` implica fallo del equipo; comprobar tambien firewall, rutas y VLANs.
8. Limitar las pruebas a redes y equipos autorizados del sistema.

## Resumen

La mascara de red define que equipos pertenecen a la misma red local. Para que el puesto de control pueda hacer `ping` al centro de distribucion, al jammer y al resto de dispositivos, todos deben tener una configuracion IP coherente, conectividad fisica, VLAN correcta, rutas validas si estan en redes distintas y permisos de firewall para ICMP.

El `ping` es una primera prueba util, pero no demuestra por si solo que todos los servicios del sistema radar funcionen. Solo confirma, cuando responde, que existe conectividad basica a nivel IP entre origen y destino.
