# Maquina Simulada
Este proyecto brinda un simulador de maquinas de turing. El objetivo principal es permitir la construcción y ejecución de máquinas de Turing a partir de una descripción textual 

## Tabla de Contenidos
- [Descripción](#descripción)
- [Como funciona](#cómo-funciona)
- [Instalación](#instalación)
- [Documentación](#documentación)

## Descripción
Explicacion mas detallada del proyecto, sus características principales y el propósito.
El simulador interpreta una máquina de Turing definida en un archivo de texto y permite simular su funcionamiento sobre una cinta finita.
El proyecto está diseñado para ser flexible, permitiendo que los usuarios definan sus propias máquinas y observen cómo interactúan con la entrada y la cinta, todo siguiendo las reglas de la máquina de Turing.
Ademas se proporciona una solucion visualizacion de las transiciones, a una velocidad establecidad antes de ejecutar.

Su propósito específico es simular una máquina de Turing que calcula una sucesión Fibonacci en formato binario.


## ¿Cómo funciona?
Este proyecto funciona leyendo un archivo de texto que describe la estructura y la lógica de una máquina de Turing. El archivo debe seguir un formato específico para que la máquina se pueda construir correctamente.

## ¿Qué formato debe tener el archivo?
las maquinas deben cumplir con un formato para que sean validas
1. Nombre de la maquina: Debe comenzar con la palabra `MAQUINA` seguida del nombre de la m�quina.
2. Alfabeto: En una línea, debe listar todos los símbolos que formarán parte del alfabeto, separados por un espacio (por ejemplo: `0 1`).
3. Simbolo de espacio: El símbolo que usará la cinta de la máquina para representar espacios vacíos (generalmente se utiliza `-`).
4. Numero de estados: Un número entero positivo que indica la cantidad exacta de estados de la máquina.
5. Estado inicial: Un número entero que representa el estado inicial, que debe ser un número entre `0` y `N-1`, donde `N` es el número de estados.
6. Transiciones: Para cada estado, se debe indicar la cantidad de transiciones (un número entero). Si el número de transiciones es 0, el estado es un estado de aceptación. 
   
   Luego, por cada transición, se deben especificar los siguientes detalles en una línea (separado por espacios):
   * Símbolo que lee: El símbolo que la máquina lee de la cinta.
   * Símbolo que escribe: El símbolo que la máquina escribe en la cinta.
   * Desplazamiento: El desplazamiento de la cabeza de la máquina, que puede ser `L` (izquierda), `R` (derecha), o `N` (sin movimiento).
   * Estado destino: El estado al que la máquina saltará después de realizar la transición.
7. Reglas para líneas válidas: Solo se consideran válidas las líneas que no comiencen con `//` (comentarios) ni que estén vacías.

Ejemplo
```bash
// nombre
MAQUINA ejemplo
// alfabeto
X Y Z
// espacio
-
// cantidad
2
// inicial
0
// estado 0
3 
X Y L 0
Y X R 0
Z Z N 1
// estado 1
0
```
## Instalacion
Instrucciones para instalar y configurar el proyecto. Puede incluir pasos como:

1. **Requisitos previos** (lenguajes, versiones, dependencias).

2. **Clonación del repositorio**:  
   ```bash
   git clone https://github.com/mae2033/turing-FTI.git

## Documentación
maquina [fibonacci](./src/resources/doc/fibonacci.pdf) diseñada para resolver el problema
## Descarga
El simulador se puede usar apartir de un ejecutable jar. Se necesita java para probarlo.

Una alternativa, si quieres hacer tu propio jar y/o modificar el codigo, para esto debes clonar el repo.

**Clonación del repositorio**:  
   ```bash
   git clone https://github.com/mae2033/turing-FTI.git
