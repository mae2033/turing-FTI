# Maquina Simulada
Este proyecto brinda un simulador de maquinas de turing. El objetivo principal es permitir la construcción y ejecución de máquinas de Turing a partir de una descripción textual 

## Tabla de Contenidos
- [Descripción](#descripción)
- [Como funciona?](#funcionamiento)
- [Documentación](#documentación)
- [Uso](#uso)

## Descripción
El simulador interpreta una máquina de Turing definida en un archivo de texto y permite simular su funcionamiento sobre una cinta finita.
El proyecto está diseñado para ser flexible, permitiendo que los usuarios definan sus propias máquinas y observen cómo interactúan con la entrada y la cinta, todo siguiendo las reglas de la máquina de Turing.
Ademas se proporciona una solucion visualizacion de las transiciones, a una velocidad establecidad antes de ejecutar.

Su diseño tenia un propósito específico, el simular una máquina de Turing que calcula una sucesión Fibonacci en formato binario.


## Funcionamiento
Este programa funciona leyendo un archivo de texto que describe la estructura y la lógica de una máquina de Turing. El archivo debe seguir un formato específico para que la máquina se pueda construir correctamente.

### ¿Qué formato debe tener el archivo?
las maquinas deben cumplir con un formato para que sean validas
1. Nombre de la maquina: Debe comenzar con la palabra `MAQUINA` seguida del nombre de la máquina.
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

## Documentación
La documentación del sistema está disponible [aquí](./doc/index.html), donde se detallan las clases y métodos clave para su funcionamiento.

Además, puedes consultar la descripción de la máquina [Fibonacci](./src/resources/doc/fibonacci.pdf), diseñada para resolver el problema especifico.
## Uso
El simulador se puede utilizar a partir del archivo JAR ejecutable. Asegúrate de tener Java instalado para ejecutarlo.

Si prefieres crear tu propio JAR o modificar el código, puedes clonar el repositorio siguiendo estos pasos:
**Clonación del repositorio**:  
   ```bash
   git clone https://github.com/mae2033/turing-FTI.git
   ```

## Guía de Uso

Una vez que el programa está en ejecución, puedes interactuar con él de la siguiente manera:

1. **Pantalla Principal**: Al abrir el simulador, verás una pantalla de bienvenida con dos opciones: **Seleccionar** y **Salir**. Al presionar la primera opción, serás dirigido a la selección de un archivo.

2. **Seleccionar Archivo**: Carga un archivo de entrada a través del *FileChooser*.

3. **Ejecutar Simulación**: Una vez que hayas cargado un archivo válido, puedes iniciar la simulación presionando el botón **Ejecutar**. La máquina de Turing procesará el archivo y mostrará los resultados en la interfaz.

4. **Opciones de Configuración**: Antes de iniciar la ejecución, puedes ajustar parámetros como la velocidad de la simulación o los estados iniciales de la cinta.

5. **Detener y Reiniciar**: Si deseas detener la simulación en cualquier momento, utiliza el botón **Detener**. También puedes reiniciar el simulador para comenzar desde el principio, restableciendo la cinta y eliminando los resultados anteriores.
