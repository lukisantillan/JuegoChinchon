# CHINCHON

![Image](https://d29v67onoz09dn.cloudfront.net/pro/web/images/white/faldon_chinchon_es.png)


## Introducción

Este proyecto se basa en implementar el juego "Chinchon" como parte del trabajo de la materia Programacion Orientada a Objetos de la Universidad Nacional de Lujan. El objetivo es desarrollar una aplicacion que permita a multiples usuarios conectados simultaneamente en tiempo real, poder jugar una partida de chinchon, siguiendo debidamente las reglas del mismo.
En el proyecto se utiliza patrones como MVC y Observer para una mejor organizacion del codigo, ademas de la libreria RMIMVC que provee el equipo docente para la posibilidad de jugar en red.

## Características

- Patron MVC-Observer.
- Juego en red.
- Interfaz de Consola

## Diagrama de clases

![Image]()

## Instalación

```bash
git clone https://github.com/lukisantillan/juegoChinchon.git
```

## Uso

1. Ejecutar el servidor
    1. Ejecutar la clase AppServidor.java situada en "src/ar/edu/unlu/uno/AppServidor.java"
    2. Selecciona la IP donde se escucharan las peticiones

       ![Image](https://res.cloudinary.com/dwybyiio5/image/upload/v1690935864/imcyz62cvqyejhugfbcg.png)

    3. Presione aceptar, luego seleccione el numero de puerto

       ![Image](https://res.cloudinary.com/dwybyiio5/image/upload/v1690935905/jdunzlpnodidrybhoejp.png)

2. Ejecutar el cliente
    1. Ejecutar la clase AppClienteConsola.java o AppClienteGrafica.java, segun la interfaz que prefiera
    2. Seleccionar la IP donde se escucharan las peticiones el cliente.
    ![Image](https://res.cloudinary.com/dwybyiio5/image/upload/v1690935935/vjgmtuxujufilgxf6okw.png)

    3. Presione aceptar, luego seleccione el numero de puerto en que escuchará el cliente.
    ![Image](https://res.cloudinary.com/dwybyiio5/image/upload/v1690935533/cdpn8nhjs80kfptkzt2j.png)

    4. Presione aceptar, luego seleccione la IP en la que corre el servidor.
    ![Image](https://res.cloudinary.com/dwybyiio5/image/upload/v1690935691/qnpxvcewjxe61hzrtxma.png)

    5. Presione aceptar, luego seleccione el numero de puerto que corre el servidor y presione aceptar.
    ![Image](https://res.cloudinary.com/dwybyiio5/image/upload/v1690935647/zqkmhzotkod629if2ubr.png)

3. Repita este ultimo proceso por cada cliente que desee tener en la red.

## Reglas de juego

### Objetivo del Juego

Cada jugador recibe 7 cartas. El objetivo del Chinchon es formar grupos de cartas, para sumar el menor puntaje posible, los grupos pueden ser de minimo 3 cartas que cumplan con las siguiente condiciones:
- Cartas con el mismo índice.
- Cartas de un mismo palo que forman una escalera.

### Cómo Jugar

En su turno, un jugador puede:

- Tomar una carta del descarte o del Mazo, para ir formando sus grupos.
- Descartar una carta:
  - Si cuando quiere descartar la carta, forma 2 grupos de al menos 3 cartas y la restante es < 5, puede cerrar la ronda.

### Puntaje
Si el jugador que cierra la mano lo hace agrupando sus 7 cartas, obtiene el premio de restar puntos:
Menos 10 (-10) con dos grupos de cartas (uno de tres y otro de cuatro).
Menos 25 (-25) con una escalera de siete cartas con dos comodines.
Menos 50 (-50) con una escalera de siete cartas con un comodín.
Chinchón: con una escalera de siete cartas sin comodines gana la partida directamente.

Los jugadores que tengan cartas las cuales no pertenecen a ningun grupo, se sumaran los valores de las mismas. 

## Fin de la partida 
El final de la partida se rige por las siguientes reglas:

Cuando un jugador sobrepasa por primera vez el límite de los 100 puntos, queda eliminado.
El jugador que cierra la mano consigue la victoria inmediatamente si todos los demás rivales superan simultáneamente los 100 puntos.
En particular, la opción de reenganche nunca puede utilizarse cuando solamente quedan en juego dos participantes.
La partida finaliza inmediatamente cuando un jugador gana haciendo chinchón.
