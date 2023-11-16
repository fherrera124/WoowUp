# Ejercicio - Sistema de Alertas

Seguramente conocés la funcionalidad de Notificaciones de Facebook: es esa campanita arriba en el menú donde te muestra las nuevas alertas que el sistema tiene para mostrarte sobre distintos temas: un amigo cumple años, una página que seguís compartió una publicación, un amigo publicó una foto, alguien comentó un posteo tuyo, una sugerencia de amistad, etc.

image.png

Facebook no es el único. En general todas las aplicaciones tienen un sistema de alertas o notificaciones. En este ejercicio, te vamos a pedir que hagas una versión muy simplificada.

Se pide programar un sistema para enviar alertas a usuarios que tenga la siguiente funcionalidad:

1. Se pueden registrar usuarios que recibirán alertas.
2. Se pueden registrar temas sobre los cuales se enviarán alertas.
3. Los usuarios pueden optar sobre cuales temas quieren recibir alertas.
4. Se puede enviar una alerta sobre un tema y lo reciben todos los usuarios que han optado recibir alertas de ese tema.
5. Se puede enviar una alerta sobre un tema a un usuario específico, solo lo recibe ese único usuario.
6. Una alerta puede tener una fecha y hora de expiración.
7. Hay dos tipos de alertas: Informativas y Urgentes.
8. Un usuario puede marcar una alerta como leída.
9. Se pueden obtener todas las alertas no expiradas de un usuario que aún no ha leído.
10. Se pueden obtener todas las alertas no expiradas para un tema. Se informa para cada alerta si es para todos los usuarios o para uno específico.
11. Tanto para el punto 9 como el 10, el ordenamiento de las alertas es el siguiente: las Urgentes van al inicio, siendo la última en llegar la primera en obtenerse (LIFO). Y a continuación las informativas, siendo la primera en llegar la primera en obtenerse (FIFO). Ej: Dadas las siguientes alertas Informativas y Urgentes que llegan en el siguiente orden: I1,I2,U1,I3,U2,I4 se ordenarán de la siguiente forma --> U2,U1,I1,I2,I3,I4

## Aclaraciones importantes:

- La aplicación se ejecuta desde línea de comando. En ningún caso pedimos que escribas código de front end, tampoco que hagas impresiones a la consola.
- Debe tener Tests Unitarios.
- No debés hacer ningún tipo de persistencia de datos (base de datos, archivos). Todo debe resolverse con estructuras en memoria.
- Si tenés que hacer algún supuesto sobre algo que no esté claro en el ejercicio, por favor anotalo para que lo tengamos en cuenta al revisar el código.

Al responder el ejercicio, por favor entregá código que funcione y se pueda probar. Se minuciosa en los detalles y en la claridad del código que escribas para que al revisor le sea fácil entenderlo.

Cuando revisamos el ejercicio, esto es lo que evaluamos:

1. Solución: ¿El código soluciona correctamente los requisitos?
2. Programación orientada a objetos:
   a. ¿Hay un modelo de clases pensado, que modela la realidad del enunciado?
   b. ¿Está resuelto usando polimorfismo?
   c. ¿Hay algún patrón de diseño presente en la solución?

3. Legibilidad del código: Miramos la elección de los nombres de las variables, los nombres de los métodos y de las clases. ¿Son lo suficientemente representativos como para entender su funcionamiento o propósito, si lo leyera un compañero tuyo sin que vos estés?
4. Principio de responsabilidad única: Cada clase, ¿tiene una única responsabilidad?¿Cada método hace una única cosa? (¿O tienen mucho código y se podría refactorizar en varios métodos más cortitos?).

5. Unit Test: ¿Hay tests que prueben el correcto funcionamiento de los casos de uso?

Podés usar cualquier lenguaje con el que te sientas cómodo, creemos que una buen a programadora puede ser productiva en cualquier lenguaje. Sugerencia: Podés usar https://www.github.com/ para compartirnos el código.

## Ejercicio SQL

Escribir una consulta SQL que traiga todos los clientes que han comprado en total más de 100,000$ en los últimos 12 meses usando las siguientes tablas:

`Clientes: ID, Nombre, Apellido`

`Ventas: Fecha, Sucursal, Numero_factura, Importe, Id_cliente`

## Algunas consideraciones más

Creemos que la resolución no debería tomarte más de 1 día. Pero entendemos que podés tener otros compromisos o restricciones de tiempo. Por favor, contame aproximadamente cuándo puedo esperar tus respuestas (ej: hoy a la tarde / en 2 o 3 días / el fin de semana).

¡Éxitos!

---

## Resolucion

### Ejercicio - Sistema de Alertas

- Para poder ejecutar los tests unitarios es necesario contar con [Java 17](https://www.oracle.com/java/technologies/downloads/#java17) o superior.

- Luego, en una terminal, ejecutar lo siguiente para el caso de un entorno `Linux`:
  `.\mvnw test`

- o lo siguiente para el caso de un entorno `Windows`:
  `.\mvnw.cmd test`

### Ejercicio SQL

```
SELECT c.ID, c.Nombre, c.Apellido
FROM Clientes c
INNER JOIN Ventas v ON c.ID = v.Id_cliente
WHERE v.Fecha >= DATE_SUB(NOW(), INTERVAL 12 MONTH)
GROUP BY c.ID, c.Nombre, c.Apellido
HAVING SUM(v.Importe) > 100000;
```
